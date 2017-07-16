package com.philliphsu.numberpadtimepicker;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
    private static final String KEY_DIGITS = "digits";
    // TODO: Why do we need the count?
    private static final String KEY_COUNT = "count";
    private static final String KEY_AM_PM_STATE = "am_pm_state";

    private final @NonNull DialogInterface mDelegator;
    private final @NonNull NumberPadTimePicker mTimePicker;
    private final @Nullable OnTimeSetListener mTimeSetListener;
    private final INumberPadTimePicker.DialogPresenter mPresenter;
    // Dummy TimePicker passed to onTimeSet() callback.
    private final TimePicker mDummy;

    private View mOkButton;

    // TODO: Consider removing the okButton param because (1) the alert layout does not have it ready
    // at the time of construction and (2) the bottom sheet layout does not need this class anymore
    // to control its FAB. Keep the setOkButton() instead.
    NumberPadTimePickerDialogViewDelegate(@NonNull DialogInterface delegator,
            @NonNull Context context, @NonNull NumberPadTimePicker timePicker,
            @Nullable View okButton, @Nullable OnTimeSetListener listener, boolean is24HourMode) {
        mDelegator = checkNotNull(delegator);
        mTimePicker = checkNotNull(timePicker);
        mOkButton = okButton;
        mTimeSetListener = listener;
        mDummy = new TimePicker(context);

        mPresenter = new NumberPadTimePickerDialogPresenter(this, timePicker.getPresenter());

        timePicker.setIs24HourMode(is24HourMode);
        timePicker.setOkButtonCallbacks(new NumberPadTimePicker.OkButtonCallbacks() {
            @Override
            public void onOkButtonEnabled(boolean enabled) {
                // The bottom sheet dialog's FAB has been handled already. This is really only for
                // the alert dialog's ok button.
                if (mOkButton != null) {
                    mOkButton.setEnabled(enabled);
                }
            }

            @Override
            public void onOkButtonClick(NumberPadTimePicker view, int hourOfDay, int minute) {
                if (mTimeSetListener != null) {
                    mTimeSetListener.onTimeSet(mDummy, hourOfDay, minute);
                }
            }
        });
    }

    @Override
    public void cancel() {
        mDelegator.cancel();
    }

    void onCreate(@Nullable Bundle savedInstanceState) {
        mTimePicker.getPresenter().onCreate(readStateFromBundle(savedInstanceState));
    }

    @NonNull
    Bundle onSaveInstanceState(@NonNull Bundle bundle) {
        final INumberPadTimePicker.State state = mTimePicker.getPresenter().getState();
        bundle.putIntArray(KEY_DIGITS, state.getDigits());
        // TODO: Why do we need the count?
        bundle.putInt(KEY_COUNT, state.getCount());
        bundle.putInt(KEY_AM_PM_STATE, state.getAmPmState());
        return bundle;
    }

    void onStop() {
        mTimePicker.getPresenter().onStop();
    }

    INumberPadTimePicker.DialogPresenter getPresenter() {
        return mPresenter;
    }

    /**
     * Workaround for situations when the 'ok' button is not
     * guaranteed to be available at the time of construction.
     * <p>
     * e.g. {@code AlertDialog}
     */
    void setOkButton(@NonNull View okButton) {
        mOkButton = checkNotNull(okButton);
    }

    @NonNull
    private static INumberPadTimePicker.State readStateFromBundle(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            final int[] digits = savedInstanceState.getIntArray(KEY_DIGITS);
            // TODO: Why do we need the count?
            final int count = savedInstanceState.getInt(KEY_COUNT);
            final @AmPmState int amPmState = savedInstanceState.getInt(KEY_AM_PM_STATE);
            return new NumberPadTimePickerState(digits, count, amPmState);
        } else {
            return NumberPadTimePickerState.EMPTY;
        }
    }
}
