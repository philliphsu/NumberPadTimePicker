package com.philliphsu.numberpadtimepicker;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDialog;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Dialog to type in a time.
 */
public class NumberPadTimePickerDialog extends AppCompatDialog {

    private final NumberPadTimePickerDialogViewDelegate mViewDelegate;
    private final NumberPadTimePickerDialogThemer mThemer;

    public NumberPadTimePickerDialog(@NonNull Context context,
            @Nullable OnTimeSetListener listener, boolean is24HourMode) {
        this(context, 0, listener, is24HourMode);
    }

    public NumberPadTimePickerDialog(@NonNull Context context, @StyleRes int themeResId,
            @Nullable OnTimeSetListener listener, boolean is24HourMode) {
        super(context, resolveDialogTheme(context, themeResId));

        final View root = getLayoutInflater().inflate(
                R.layout.nptp_alert_numberpad_time_picker_dialog, null);
        final NumberPadTimePicker timePicker = (NumberPadTimePicker)
                root.findViewById(R.id.nptp_time_picker);
        final NumberPadTimePickerAlertComponent timePickerComponent =
                (NumberPadTimePickerAlertComponent) timePicker.getComponent();
        mViewDelegate = new NumberPadTimePickerDialogViewDelegate(this, getContext(), timePicker,
                timePickerComponent.getOkButton(), timePickerComponent.getCancelButton(), listener,
                is24HourMode);
        mThemer = new NumberPadTimePickerDialogThemer(timePickerComponent);

        // Must be requested before adding content, or get an AndroidRuntimeException!
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(root);
    }

    public NumberPadTimePickerDialogThemer getThemer() {
        return mThemer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Override the dialog's width if we're running in an eligible layout qualifier.
        try {
            getWindow().setLayout(getContext().getResources().getDimensionPixelSize(
                    R.dimen.nptp_alert_dialog_width), ViewGroup.LayoutParams.WRAP_CONTENT);
        } catch (Resources.NotFoundException nfe) {
            // Do nothing.
        }
        mViewDelegate.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Bundle onSaveInstanceState() {
        return mViewDelegate.onSaveInstanceState(super.onSaveInstanceState());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewDelegate.onStop();
    }

    static int resolveDialogTheme(Context context, int resId) {
        if (resId == 0) {
            final TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.nptp_numberPadTimePickerAlertDialogTheme,
                    outValue, true);
            return outValue.resourceId;
        } else {
            return resId;
        }
    }
}
