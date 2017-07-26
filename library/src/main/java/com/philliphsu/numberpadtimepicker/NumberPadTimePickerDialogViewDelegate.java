package com.philliphsu.numberpadtimepicker;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TimePicker;

import com.philliphsu.numberpadtimepicker.INumberPadTimePicker.DialogView;

import static com.philliphsu.numberpadtimepicker.Preconditions.checkNotNull;

/**
 * Handles the {@link DialogView DialogView} responsibilities of a number pad time picker dialog.
 */
final class NumberPadTimePickerDialogViewDelegate implements DialogView {

    private final @NonNull DialogInterface mDelegator;
    private final @NonNull NumberPadTimePicker mTimePicker;
    private final @Nullable OnTimeSetListener mTimeSetListener;
    private final INumberPadTimePicker.DialogPresenter mPresenter;
    // Dummy TimePicker passed to onTimeSet() callback.
    private final TimePicker mDummy;

    NumberPadTimePickerDialogViewDelegate(@NonNull DialogInterface delegator,
            @NonNull Context context, @NonNull final NumberPadTimePicker timePicker,
            @NonNull View okButton, @Nullable View cancelButton,
            @Nullable OnTimeSetListener listener, boolean is24HourMode) {
        checkNotNull(context);
        checkNotNull(okButton);
        mDelegator = checkNotNull(delegator);
        mTimePicker = checkNotNull(timePicker);
        mTimeSetListener = listener;
        mDummy = new TimePicker(context);

        mPresenter = new NumberPadTimePickerDialogPresenter(this, timePicker.getPresenter());

        timePicker.setIs24HourMode(is24HourMode);
        timePicker.setOkButtonCallbacks(new NumberPadTimePicker.OkButtonCallbacks() {
            @Override
            public void onOkButtonEnabled(boolean enabled) {
                // Do nothing.
            }

            @Override
            public void onOkButtonClick(NumberPadTimePicker view, int hourOfDay, int minute) {
                if (mTimeSetListener != null) {
                    mTimeSetListener.onTimeSet(mDummy, hourOfDay, minute);
                }
            }
        });

        // Overrides the default behavior set in the appropriate NumberPadTimePickerComponent.
        // The default behavior just returns the time to the OnTimeSet listener.
        // This has the addition of cancelling the dialog afterward.
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onOkButtonClick();
            }
        });

        if (cancelButton != null) {
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.onCancelClick();
                }
            });
        }
    }

    @Override
    public void cancel() {
        mDelegator.cancel();
    }
}
