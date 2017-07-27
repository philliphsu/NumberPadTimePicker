package com.philliphsu.numberpadtimepicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.philliphsu.numberpadtimepicker.Preconditions.checkNotNull;

public class NumberPadTimePicker extends LinearLayout implements INumberPadTimePicker.View {

    /** Default layout for use as a standalone view. */
    public static final int LAYOUT_STANDALONE = 0;
    /** Option to layout this view for use in an alert dialog. */
    public static final int LAYOUT_ALERT = 1;
    /** Option to layout this view for use in a bottom sheet dialog. */
    public static final int LAYOUT_BOTTOM_SHEET = 2;

    @IntDef({LAYOUT_STANDALONE, LAYOUT_ALERT, LAYOUT_BOTTOM_SHEET})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NumberPadTimePickerLayout {}

    private NumberPadTimePickerComponent mTimePickerComponent;
    private OkButtonCallbacks mCallbacks;
    private INumberPadTimePicker.Presenter mPresenter;
    private LocaleModel mLocaleModel;
    private LinearLayout mInputTimeContainer;

    private @NumberPadTimePickerLayout int mLayout;
    private boolean mIs24HourMode;

    /**
     * Listener to be notified when 12/24-hour mode changes.
     */
    interface OnTimeModeChangeListener {
        /**
         * @param is24HourMode Whether the new time mode is 24-hour mode.
         * @param newPresenter The new {@code Presenter} instance created for the new time mode.
         */
        void onTimeModeChange(boolean is24HourMode, INumberPadTimePicker.Presenter newPresenter);
    }

    /**
     * Callbacks you need to handle for your custom "ok" button (e.g. a view in
     * your layout or a MenuItem).
     */
    public interface OkButtonCallbacks {
        /**
         * @param enabled Whether your "ok" button should be set enabled.
         */
        void onOkButtonEnabled(boolean enabled);
        /**
         * Returns the time you typed after you clicked your "ok" button.
         * <p>
         * This callback is fired after you call {@link #confirmTimeSelection()}.
         * You should set an appropriate click listener for your button that calls
         * {@link #confirmTimeSelection()} when your button is clicked.
         *
         * @param view The view used to type in this time.
         * @param hourOfDay The hour that was typed.
         * @param minute The minute that was typed.
         */
        void onOkButtonClick(NumberPadTimePicker view, int hourOfDay, int minute);
    }

    public NumberPadTimePicker(Context context) {
        this(context, null);
    }

    public NumberPadTimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.nptp_numberPadTimePickerStyle);
    }

    public NumberPadTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public NumberPadTimePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setOrientation(VERTICAL);
        final TypedArray timePickerAttrs = context.obtainStyledAttributes(attrs,
                R.styleable.NPTP_NumberPadTimePicker, defStyleAttr, defStyleRes);

        mLayout = retrieveLayout(timePickerAttrs);
        switch (mLayout) {
            case LAYOUT_BOTTOM_SHEET:
                mTimePickerComponent = new NumberPadTimePickerBottomSheetComponent(
                        this, context, attrs, defStyleAttr, defStyleRes);
                break;
            case LAYOUT_ALERT:
                mTimePickerComponent = new NumberPadTimePickerAlertComponent(
                        this, context, attrs, defStyleAttr, defStyleRes);
                break;
            case LAYOUT_STANDALONE:
            default:
                mTimePickerComponent = new NumberPadTimePickerComponent(
                        this, context, attrs, defStyleAttr, defStyleRes);
                break;
        }

        mInputTimeContainer = (LinearLayout) findViewById(R.id.nptp_input_time_container);
        mLocaleModel = new LocaleModel(context);

        mIs24HourMode = timePickerAttrs.getBoolean(R.styleable.
                NPTP_NumberPadTimePicker_nptp_is24HourMode, DateFormat.is24HourFormat(context));
        mPresenter = newPresenter(mIs24HourMode);
        mPresenter.presentState(NumberPadTimePickerState.EMPTY);

        post(new Runnable() {
            @Override
            public void run() {
                mPresenter.onShow();
            }
        });

        timePickerAttrs.recycle();
    }

    @Override
    public void setNumberKeysEnabled(int start, int end) {
        mTimePickerComponent.mNumberPad.setNumberKeysEnabled(start, end);
    }

    @Override
    public void setBackspaceEnabled(boolean enabled) {
        mTimePickerComponent.mBackspace.setEnabled(enabled);
    }

    @Override
    public void updateTimeDisplay(CharSequence time) {
        mTimePickerComponent.mTimeDisplay.setText(time);
    }

    @Override
    public void updateAmPmDisplay(CharSequence ampm) {
        mTimePickerComponent.mAmPmDisplay.setText(ampm);
    }

    @Override
    public void setAmPmDisplayVisible(boolean visible) {
        mTimePickerComponent.mAmPmDisplay.setVisibility(visible ? VISIBLE : GONE);
    }

    @Override
    public void setAmPmDisplayIndex(int index) {
        if (index != 0 && index != 1) {
            throw new IllegalArgumentException("Index of AM/PM display must be 0 or 1. index == " + index);
        }
        if (index == 1) return;
        mInputTimeContainer.removeViewAt(1);
        mInputTimeContainer.addView(mTimePickerComponent.mAmPmDisplay, 0);
    }

    @Override
    public void setLeftAltKeyText(CharSequence text) {
        mTimePickerComponent.mNumberPad.setLeftAltKeyText(text);
    }

    @Override
    public void setRightAltKeyText(CharSequence text) {
        mTimePickerComponent.mNumberPad.setRightAltKeyText(text);
    }

    @Override
    public void setLeftAltKeyEnabled(boolean enabled) {
        mTimePickerComponent.mNumberPad.setLeftAltKeyEnabled(enabled);
    }

    @Override
    public void setRightAltKeyEnabled(boolean enabled) {
        mTimePickerComponent.mNumberPad.setRightAltKeyEnabled(enabled);
    }

    @Override
    public void setOkButtonEnabled(boolean enabled) {
        mTimePickerComponent.setOkButtonEnabled(enabled);

        // Fire the callback even if the client is using the bottom sheet layout's provided FAB,
        // in case they want to do something additional.
        if (mCallbacks != null) {
            mCallbacks.onOkButtonEnabled(enabled);
        }
    }

    @Override
    public void setResult(int hour, int minute) {
        if (mCallbacks != null) {
            mCallbacks.onOkButtonClick(this, hour, minute);
        }
    }

    @Override
    public void showOkButton() {
        mTimePickerComponent.showOkButton();
    }

    /**
     * Set the callbacks to be invoked for your custom "ok" button during typing the time.
     */
    public void setOkButtonCallbacks(OkButtonCallbacks callbacks) {
        mCallbacks = callbacks;
        mPresenter.onSetOkButtonCallbacks();
    }

    /**
     * Set the click listener to be invoked when this time picker's cancel button is clicked, if
     * there is one.
     * <p>
     * This is a no-op for {@link #getLayout() layouts} other than {@link #LAYOUT_ALERT}. You do not
     * need to set this if you are implementing your own "cancel" flow, perhaps through an external
     * button defined in your own layouts.
     */
    public void setCancelButtonClickListener(OnClickListener listener) {
        if (mLayout == LAYOUT_ALERT && mTimePickerComponent instanceof
                NumberPadTimePickerAlertComponent) {
            ((NumberPadTimePickerAlertComponent) mTimePickerComponent)
                    .setCancelButtonClickListener(listener);
        }
    }

    /**
     * This should be called by the click listener you set on your "ok" button.
     * This will call your {@link OkButtonCallbacks#onOkButtonClick(NumberPadTimePicker, int, int)
     * onOkButtonClick()} callback.
     *
     * <p>
     * <strong>
     *     You should only allow your button to be clickable after you received an
     *     {@link OkButtonCallbacks#onOkButtonEnabled(boolean) onOkButtonEnabled(true)} callback.
     * </strong>
     * </p>
     */
    public void confirmTimeSelection() {
        mPresenter.onOkButtonClick();  // Calls setResult() if "ok" button last enabled
    }

    /**
     * Access the theming APIs available for this time picker. The {@link #getLayout() layout} of
     * this picker will determine which APIs are available to you.
     * <p>
     * If this time picker's layout is {@link #LAYOUT_BOTTOM_SHEET},
     * the return value can safely be casted to {@link BottomSheetNumberPadTimePickerThemer}
     * to access additional bottom sheet theming APIs.
     *
     * @return An object that can be used to customize this time picker's theme.
     */
    public NumberPadTimePickerThemer getThemer() {
        return mTimePickerComponent;
    }

    /**
     * Returns the layout of this time picker. The layout determines the presence and the appearance
     * of dialog buttons, if applicable.
     *
     * @return One of
     *          <ul>
     *              <li>{@link #LAYOUT_STANDALONE}</li>
     *              <li>{@link #LAYOUT_ALERT}</li>
     *              <li>{@link #LAYOUT_BOTTOM_SHEET}</li>
     *          </ul>
     */
    @NumberPadTimePickerLayout
    public int getLayout() {
        return mLayout;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPresenter.detachView();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), mPresenter.getState());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof SavedState) {
            mPresenter.presentState(((SavedState) state).getNptpState());
        }
    }

    NumberPadTimePickerComponent getComponent() {
        return mTimePickerComponent;
    }

    INumberPadTimePicker.Presenter getPresenter() {
        return mPresenter;
    }

    void setIs24HourMode(boolean is24HourMode, OnTimeModeChangeListener listener) {
        if (is24HourMode != mIs24HourMode) {
            // Tear down the current presenter.
            final INumberPadTimePicker.State state = mPresenter.getState();
            mPresenter.detachView();

            // Set up a new presenter using the previous presenter's state.
            INumberPadTimePicker.Presenter newPresenter = newPresenter(is24HourMode);
            newPresenter.presentState(state);

            if (listener != null) {
                listener.onTimeModeChange(is24HourMode, newPresenter);
            }
            mPresenter = newPresenter;
            mIs24HourMode = is24HourMode;
        }
    }

    private INumberPadTimePicker.Presenter newPresenter(boolean is24HourMode) {
        INumberPadTimePicker.Presenter presenter = new NumberPadTimePickerPresenter(
                this, mLocaleModel, is24HourMode);
        setupClickListeners(presenter);
        return presenter;
    }

    /**
     * @param presenter The presenter to forward click events to.
     */
    private void setupClickListeners(INumberPadTimePicker.Presenter presenter) {
        OnBackspaceClickHandler backspaceClickHandler = new OnBackspaceClickHandler(presenter);
        mTimePickerComponent.mBackspace.setOnClickListener(backspaceClickHandler);
        mTimePickerComponent.mBackspace.setOnLongClickListener(backspaceClickHandler);
        mTimePickerComponent.mNumberPad.setOnNumberKeyClickListener(
                new OnNumberKeyClickListener(presenter));
        mTimePickerComponent.mNumberPad.setOnAltKeyClickListener(
                new OnAltKeyClickListener(presenter));
    }

    @NumberPadTimePickerLayout
    private static int retrieveLayout(TypedArray timePickerAttrs) {
        final int layout = timePickerAttrs.getInt(R.styleable.
                NPTP_NumberPadTimePicker_nptp_numberPadTimePickerLayout, LAYOUT_STANDALONE);
        switch (layout) {
            case LAYOUT_STANDALONE:
            case LAYOUT_ALERT:
            case LAYOUT_BOTTOM_SHEET:
                return layout;
            default:
                return LAYOUT_STANDALONE;
        }
    }

    /**
     * Component that installs the base functionality of a {@link NumberPadTimePicker}. 
     */
    static class NumberPadTimePickerComponent implements NumberPadTimePickerThemer {
        final NumberPadView mNumberPad;
        final TextView mTimeDisplay;
        final TextView mAmPmDisplay;
        final ImageButton mBackspace;
        final ImageView mDivider;
        final View mHeader;
        final @Nullable View mOkButton;

        NumberPadTimePickerComponent(final NumberPadTimePicker timePicker, Context context,
                AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            final View root = inflate(context, timePicker);
            mNumberPad = (NumberPadView) root.findViewById(R.id.nptp_numberpad_time_picker_view);
            mTimeDisplay = (TextView) root.findViewById(R.id.nptp_input_time);
            mAmPmDisplay = (TextView) root.findViewById(R.id.nptp_input_ampm);
            mBackspace = (ImageButton) root.findViewById(R.id.nptp_backspace);
            mDivider = (ImageView) root.findViewById(R.id.nptp_divider);
            mHeader = root.findViewById(R.id.nptp_header);
            mOkButton = root.findViewById(R.id.nptp_ok_button);

            final TypedArray timePickerAttrs = context.obtainStyledAttributes(attrs,
                    R.styleable.NPTP_NumberPadTimePicker, defStyleAttr, defStyleRes);
            final int inputTimeTextColor = timePickerAttrs.getColor(
                    R.styleable.NPTP_NumberPadTimePicker_nptp_inputTimeTextColor, 0);
            final int inputAmPmTextColor = timePickerAttrs.getColor(
                    R.styleable.NPTP_NumberPadTimePicker_nptp_inputAmPmTextColor, 0);
            final ColorStateList backspaceTint = timePickerAttrs.getColorStateList(
                    R.styleable.NPTP_NumberPadTimePicker_nptp_backspaceTint);
            final ColorStateList numberKeysTextColor = timePickerAttrs.getColorStateList(
                    R.styleable.NPTP_NumberPadTimePicker_nptp_numberKeysTextColor);
            final ColorStateList altKeysTextColor = timePickerAttrs.getColorStateList(
                    R.styleable.NPTP_NumberPadTimePicker_nptp_altKeysTextColor);
            final Drawable headerBackground = timePickerAttrs.getDrawable(
                    R.styleable.NPTP_NumberPadTimePicker_nptp_headerBackground);
            final Drawable divider = timePickerAttrs.getDrawable(
                    R.styleable.NPTP_NumberPadTimePicker_nptp_divider);
            final Drawable numberPadBackground = timePickerAttrs.getDrawable(
                    R.styleable.NPTP_NumberPadTimePicker_nptp_numberPadBackground);
            timePickerAttrs.recycle();

            if (inputTimeTextColor != 0) {
                setInputTimeTextColor(inputTimeTextColor);
            }
            if (inputAmPmTextColor != 0) {
                setInputAmPmTextColor(inputAmPmTextColor);
            }
            if (backspaceTint != null) {
                setBackspaceTint(backspaceTint);
            }
            if (numberKeysTextColor != null) {
                setNumberKeysTextColor(numberKeysTextColor);
            }
            if (altKeysTextColor != null) {
                setAltKeysTextColor(altKeysTextColor);
            }
            if (headerBackground != null) {
                setHeaderBackground(headerBackground);
            }
            if (divider != null) {
                setDivider(divider);
            }
            if (numberPadBackground != null) {
                setNumberPadBackground(numberPadBackground);
            }

            if (mOkButton != null) {
                mOkButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePicker.confirmTimeSelection();
                    }
                });
            }
        }

        @Override
        public NumberPadTimePickerThemer setInputTimeTextColor(@ColorInt int color) {
            mTimeDisplay.setTextColor(color);
            return this;
        }

        @Override
        public NumberPadTimePickerThemer setInputAmPmTextColor(@ColorInt int color) {
            mAmPmDisplay.setTextColor(color);
            return this;
        }

        @Override
        public NumberPadTimePickerThemer setBackspaceTint(ColorStateList colors) {
            DrawableCompat.setTintList(mBackspace.getDrawable(), colors);
            return this;
        }

        @Override
        public NumberPadTimePickerThemer setNumberKeysTextColor(ColorStateList colors) {
            mNumberPad.setNumberKeysTextColor(colors);
            return this;
        }

        @Override
        public NumberPadTimePickerThemer setAltKeysTextColor(ColorStateList colors) {
            mNumberPad.setAltKeysTextColor(colors);
            return this;
        }

        @Override
        public NumberPadTimePickerThemer setHeaderBackground(Drawable background) {
            setBackground(mHeader, background);
            return this;
        }

        @Override
        public NumberPadTimePickerThemer setNumberPadBackground(Drawable background) {
            setBackground(mNumberPad, background);
            return this;
        }

        @Override
        public NumberPadTimePickerThemer setDivider(Drawable divider) {
            mDivider.setImageDrawable(divider);
            if (Build.VERSION.SDK_INT >= 21) {
                // Clear the tint set in the header's layout resource.
                // This is not necessary for pre-21, because the tint
                // doesn't show up when the divider is changed.
                mDivider.setImageTintList(null);
            }
            return this;
        }

        View inflate(Context context, NumberPadTimePicker root) {
            return View.inflate(context, R.layout.nptp_numberpad_time_picker, root);
        }

        @Nullable
        View getOkButton() {
            return mOkButton;
        }

        void setOkButtonEnabled(boolean enabled) {
            if (mOkButton != null) {
                mOkButton.setEnabled(enabled);
            }
        }

        void showOkButton() {
            // No default implementation.
        }

        private static void setBackground(View view, Drawable background) {
            if (Build.VERSION.SDK_INT < 16) {
                view.setBackgroundDrawable(background);
            } else {
                view.setBackground(background);
            }
        }
    }

    private static class SavedState extends BaseSavedState {
        private final INumberPadTimePicker.State mNptpState;

        SavedState(Parcelable superState, @NonNull INumberPadTimePicker.State nptpState) {
            super(superState);
            mNptpState = checkNotNull(nptpState);
        }

        INumberPadTimePicker.State getNptpState() {
            // As a nested class, this member is directly accessible to the parent class.
            // Why did we bother writing a getter? If this class is ever moved to the top-level,
            // this member would no longer be directly accessible.
            return mNptpState;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeIntArray(mNptpState.getDigits());
            out.writeInt(mNptpState.getCount());
            out.writeInt(mNptpState.getAmPmState());
        }

        private SavedState(Parcel in) {
            super(in);
            mNptpState = new NumberPadTimePickerState(in.createIntArray(),
                    in.readInt(), in.readInt());
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
