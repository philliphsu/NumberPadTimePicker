package com.philliphsu.numberpadtimepicker;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.philliphsu.numberpadtimepicker.NumberPadTimePicker.NumberPadTimePickerComponent;

import static com.philliphsu.numberpadtimepicker.Preconditions.checkNotNull;

/**
 * Interface through which a {@link NumberPadTimePicker} contained in
 * a {@link NumberPadTimePickerDialog} or {@link BottomSheetNumberPadTimePickerDialog}
 * can have its colors customized.
 */
public class NumberPadTimePickerDialogThemer implements NumberPadTimePickerThemer {
    private final NumberPadTimePickerComponent mTimePickerComponent;

    NumberPadTimePickerDialogThemer(@NonNull NumberPadTimePickerComponent timePickerComponent) {
        mTimePickerComponent = checkNotNull(timePickerComponent);
    }

    /**
     * Set the text color for the inputted time in the header.
     *
     * @see R.attr#nptp_inputTimeTextColor
     */
    @Override
    public NumberPadTimePickerDialogThemer setInputTimeTextColor(@ColorInt int color) {
        mTimePickerComponent.setInputTimeTextColor(color);
        return this;
    }

    /**
     * Set the text color for the inputted AM/PM in the header.
     *
     * @see R.attr#nptp_inputAmPmTextColor
     */
    @Override
    public NumberPadTimePickerDialogThemer setInputAmPmTextColor(@ColorInt int color) {
        mTimePickerComponent.setInputAmPmTextColor(color);
        return this;
    }

    /**
     * Set the tint to apply to the backspace icon.
     *
     * @param colors A ColorStateList that defines colors for {@link android.R.attr#state_enabled}.
     *
     * @see R.attr#nptp_backspaceTint
     */
    @Override
    public NumberPadTimePickerDialogThemer setBackspaceTint(ColorStateList colors) {
        mTimePickerComponent.setBackspaceTint(colors);
        return this;
    }

    /**
     * Set the text color for the number keys.
     *
     * @param colors A ColorStateList that defines colors for {@link android.R.attr#state_enabled}.
     *
     * @see R.attr#nptp_numberKeysTextColor
     */
    @Override
    public NumberPadTimePickerDialogThemer setNumberKeysTextColor(ColorStateList colors) {
        mTimePickerComponent.setNumberKeysTextColor(colors);
        return this;
    }

    /**
     * Set the text color for the alt keys, i.e. "AM"/"PM" for 12-hour mode and ":00"/":30"
     * for 24-hour mode.
     *
     * @param colors A ColorStateList that defines colors for {@link android.R.attr#state_enabled}.
     *
     * @see R.attr#nptp_altKeysTextColor
     */
    @Override
    public NumberPadTimePickerDialogThemer setAltKeysTextColor(ColorStateList colors) {
        mTimePickerComponent.setAltKeysTextColor(colors);
        return this;
    }

    /**
     * Set the background for the header containing the inputted time and AM/PM.
     *
     * @see R.attr#nptp_headerBackground
     */
    @Override
    public NumberPadTimePickerDialogThemer setHeaderBackground(Drawable background) {
        mTimePickerComponent.setHeaderBackground(background);
        return this;
    }

    /**
     * Set the background for the number pad.
     *
     * @see R.attr#nptp_numberPadBackground
     */
    @Override
    public NumberPadTimePickerDialogThemer setNumberPadBackground(Drawable background) {
        mTimePickerComponent.setNumberPadBackground(background);
        return this;
    }

    /**
     * Set the header divider.
     *
     * @see R.attr#nptp_divider
     */
    @Override
    public NumberPadTimePickerDialogThemer setDivider(Drawable divider) {
        mTimePickerComponent.setDivider(divider);
        return this;
    }
}
