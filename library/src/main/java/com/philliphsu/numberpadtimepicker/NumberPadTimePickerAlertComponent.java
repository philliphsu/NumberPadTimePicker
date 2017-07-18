package com.philliphsu.numberpadtimepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import static com.philliphsu.numberpadtimepicker.Preconditions.checkNotNull;

/**
 * Component that install {@link NumberPadTimePicker#LAYOUT_ALERT alert dialog} functionality
 * to a {@link NumberPadTimePicker}.
 */
final class NumberPadTimePickerAlertComponent extends
        NumberPadTimePicker.NumberPadTimePickerComponent {

    private final TextView mCancelButton;

    NumberPadTimePickerAlertComponent(NumberPadTimePicker timePicker, Context context,
            AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(timePicker, context, attrs, defStyleAttr, defStyleRes);
        mCancelButton = (TextView) timePicker.findViewById(R.id.nptp_button2);

        ((TextView) checkNotNull(mOkButton)).setText(android.R.string.ok);
        mCancelButton.setText(android.R.string.cancel);
    }

    @Override
    View inflate(Context context, NumberPadTimePicker root) {
        return View.inflate(context, R.layout.nptp_alert_numberpad_time_picker, root);
    }

    View getCancelButton() {
        return mCancelButton;
    }
}
