package com.philliphsu.numberpadtimepicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
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

public class NumberPadTimePicker extends LinearLayout implements INumberPadTimePicker.View {

    /** Default layout for use as a standalone view. */
    static final int LAYOUT_STANDALONE = 0;
    /** Option to layout this view for use in an alert dialog. */
    static final int LAYOUT_ALERT = 1;
    /** Option to layout this view for use in a bottom sheet dialog. */
    static final int LAYOUT_BOTTOM_SHEET = 2;

    @IntDef({LAYOUT_STANDALONE, LAYOUT_ALERT, LAYOUT_BOTTOM_SHEET})
    @Retention(RetentionPolicy.SOURCE)
    @interface NumberPadTimePickerLayout {}

    private NumberPadTimePickerComponent mTimePickerComponent;
    private OkButtonCallbacks mCallbacks;
    private INumberPadTimePicker.Presenter mPresenter;
    private LocaleModel mLocaleModel;
    private LinearLayout mInputTimeContainer;

    private @NumberPadTimePickerLayout int mLayout;

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
        timePickerAttrs.recycle();

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

        // TODO: Create and retrieve an attribute 'nptp_is24HourMode'
        setIs24HourMode(DateFormat.is24HourFormat(context));
        setupClickListeners(mPresenter);

        post(new Runnable() {
            @Override
            public void run() {
                mPresenter.onShow();
            }
        });
    }

    void setIs24HourMode(boolean is24HourMode) {
        // TODO: Consider writing a setIs24HourMode() method in the presenter, and using that
        // instead of creating a new Presenter instance every time we want to change the mode.
        mPresenter = new NumberPadTimePickerPresenter(this, mLocaleModel, is24HourMode);
        // TODO: We can move the contents of this method to the constructor? Then delete this.
        mPresenter.onCreate(NumberPadTimePickerState.EMPTY);
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

    @Deprecated
    @Override
    public void setHeaderDisplayFocused(boolean focused) {
        // Do nothing.
    }

    /**
     * Set the callbacks to be invoked for your custom "ok" button during typing the time.
     */
    public void setOkButtonCallbacks(OkButtonCallbacks callbacks) {
        mCallbacks = callbacks;
        mPresenter.onSetOkButtonCallbacks();
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

    @NumberPadTimePickerLayout
    int getLayout() {
        return mLayout;
    }

    NumberPadTimePickerComponent getComponent() {
        return mTimePickerComponent;
    }

    INumberPadTimePicker.Presenter getPresenter() {
        return mPresenter;
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
        public final NumberPadTimePickerThemer setInputTimeTextColor(@ColorInt int color) {
            mTimeDisplay.setTextColor(color);
            return this;
        }

        @Override
        public final NumberPadTimePickerThemer setInputAmPmTextColor(@ColorInt int color) {
            mAmPmDisplay.setTextColor(color);
            return this;
        }

        @Override
        public final NumberPadTimePickerThemer setBackspaceTint(ColorStateList colors) {
            DrawableCompat.setTintList(mBackspace.getDrawable(), colors);
            return this;
        }

        @Override
        public final NumberPadTimePickerThemer setNumberKeysTextColor(ColorStateList colors) {
            mNumberPad.setNumberKeysTextColor(colors);
            return this;
        }

        @Override
        public final NumberPadTimePickerThemer setAltKeysTextColor(ColorStateList colors) {
            mNumberPad.setAltKeysTextColor(colors);
            return this;
        }

        @Override
        public final NumberPadTimePickerThemer setHeaderBackground(Drawable background) {
            setBackground(mHeader, background);
            return this;
        }

        @Override
        public final NumberPadTimePickerThemer setNumberPadBackground(Drawable background) {
            setBackground(mNumberPad, background);
            return this;
        }

        @Override
        public final NumberPadTimePickerThemer setDivider(Drawable divider) {
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
}
