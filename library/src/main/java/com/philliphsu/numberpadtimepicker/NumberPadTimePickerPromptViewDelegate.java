package com.philliphsu.numberpadtimepicker;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philliphsu.numberpadtimepicker.INumberPadTimePicker.PromptView;
import com.philliphsu.numberpadtimepicker.NumberPadTimePicker.OkButtonCallbacks;

import static com.philliphsu.numberpadtimepicker.Preconditions.checkNotNull;

/**
 * Handles the {@link PromptView PromptView} responsibilities of a {@link NumberPadTimePicker} when
 * used as a prompt.
 */
// TODO: Does not handle state restoration yet! Perhaps the number pad time picker view can
// save its own state instead of leaving that up to the dialogs and delegates.
class NumberPadTimePickerPromptViewDelegate implements PromptView {

    private final @NonNull NumberPadTimePicker mTimePicker;
    private final @NonNull OkButtonCallbacks mCallbacks;
    private final INumberPadTimePicker.PromptPresenter mPresenter;

    public NumberPadTimePickerPromptViewDelegate(@NonNull NumberPadTimePicker timePicker,
            @NonNull Context context, @NonNull OkButtonCallbacks callbacks, boolean is24HourMode) {
        mTimePicker = checkNotNull(timePicker);
        mCallbacks = checkNotNull(callbacks);
        checkNotNull(context);

        mPresenter = new NumberPadTimePickerPromptPresenter(this,
                new LocaleModel(context), is24HourMode);
        NumberPadTimePicker.injectClickListeners(timePicker, mPresenter);

        mPresenter.onCreate(NumberPadTimePickerState.EMPTY);
    }

    @Override
    public void setNumberKeysEnabled(int start, int end) {
        mTimePicker.setNumberKeysEnabled(start, end);
    }

    @Override
    public void setBackspaceEnabled(boolean enabled) {
        mTimePicker.setBackspaceEnabled(enabled);
    }

    @Override
    public void updateTimeDisplay(CharSequence time) {
        mTimePicker.updateTimeDisplay(time);
    }

    @Override
    public void updateAmPmDisplay(CharSequence ampm) {
        mTimePicker.updateAmPmDisplay(ampm);
    }

    @Override
    public void setAmPmDisplayVisible(boolean visible) {
        mTimePicker.setAmPmDisplayVisible(visible);
    }

    @Override
    public void setAmPmDisplayIndex(int index) {
        mTimePicker.setAmPmDisplayIndex(index);
    }

    @Override
    public void setLeftAltKeyText(CharSequence text) {
        mTimePicker.setLeftAltKeyText(text);
    }

    @Override
    public void setRightAltKeyText(CharSequence text) {
        mTimePicker.setRightAltKeyText(text);
    }

    @Override
    public void setLeftAltKeyEnabled(boolean enabled) {
        mTimePicker.setLeftAltKeyEnabled(enabled);
    }

    @Override
    public void setRightAltKeyEnabled(boolean enabled) {
        mTimePicker.setRightAltKeyEnabled(enabled);
    }

    @Override
    public void setHeaderDisplayFocused(boolean focused) {
        mTimePicker.setHeaderDisplayFocused(focused);
    }

    @Override
    public void setOkButtonEnabled(boolean enabled) {
        mCallbacks.onOkButtonEnabled(enabled);
    }

    @Override
    public void setResult(int hour, int minute) {
        mCallbacks.onOkButtonClick(mTimePicker, hour, minute);
    }

    void confirmTimeSelection() {
        mPresenter.onOkButtonClick();  // Calls setResult() if "ok" button last enabled
    }
}
