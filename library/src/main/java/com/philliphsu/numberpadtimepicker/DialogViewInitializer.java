package com.philliphsu.numberpadtimepicker;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TimePicker;

import com.philliphsu.numberpadtimepicker.INumberPadTimePicker.DialogPresenter;
import com.philliphsu.numberpadtimepicker.INumberPadTimePicker.DialogView;
import com.philliphsu.numberpadtimepicker.INumberPadTimePicker.Presenter;

import static com.philliphsu.numberpadtimepicker.Preconditions.checkNotNull;

/**
 * Provides static setup methods for a {@link DialogView}.
 */
final class DialogViewInitializer {

    private DialogViewInitializer() {}

    static void setupDialogView(@NonNull DialogView dialogView, @NonNull final Context context,
            @NonNull final NumberPadTimePicker timePicker, @NonNull View okButton,
            @Nullable final OnTimeSetListener listener, boolean is24HourMode) {
        DialogPresenter presenter = new NumberPadTimePickerDialogPresenter(dialogView,
                timePicker.getPresenter());
        setupDialogView(dialogView, presenter, context, timePicker, okButton, listener,
                is24HourMode);
    }

    static void setupDialogView(@NonNull DialogView dialogView,
            @NonNull final DialogPresenter dialogPresenter, @NonNull final Context context,
            @NonNull final NumberPadTimePicker timePicker, @NonNull View okButton,
            @Nullable final OnTimeSetListener listener, boolean is24HourMode) {
        checkNotNull(dialogView);
        checkNotNull(dialogPresenter);
        checkNotNull(context);
        checkNotNull(timePicker);
        checkNotNull(okButton);

        timePicker.setIs24HourMode(is24HourMode, new NumberPadTimePicker.OnTimeModeChangeListener() {
            @Override
            public void onTimeModeChange(boolean is24HourMode, Presenter newPresenter) {
                dialogPresenter.setBasePresenter(newPresenter);
            }
        });
        timePicker.setOkButtonCallbacks(new NumberPadTimePicker.OkButtonCallbacks() {
            @Override
            public void onOkButtonEnabled(boolean enabled) {
                // Do nothing.
            }

            @Override
            public void onOkButtonClick(NumberPadTimePicker view, int hourOfDay, int minute) {
                if (listener != null) {
                    listener.onTimeSet(new TimePicker(context), hourOfDay, minute);
                }
            }
        });

        // Overrides the default behavior set in the appropriate NumberPadTimePickerComponent.
        // The default behavior just returns the time to the OnTimeSet listener.
        // This has the addition of cancelling the dialog afterward.
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPresenter.onOkButtonClick();
            }
        });
    }
}
