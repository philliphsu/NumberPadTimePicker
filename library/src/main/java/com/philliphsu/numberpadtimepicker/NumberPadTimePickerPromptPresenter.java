package com.philliphsu.numberpadtimepicker;

import android.support.annotation.NonNull;

/**
 * Processes clicks on and presents a confirmation button (e.g. "OK") in the
 * {@link INumberPadTimePicker.PromptView PromptView}.
 */
class NumberPadTimePickerPromptPresenter extends NumberPadTimePickerPresenter
        implements INumberPadTimePicker.PromptPresenter {
    private final DigitwiseTimeParser mTimeParser = new DigitwiseTimeParser(mTimeModel);

    private INumberPadTimePicker.PromptView mView;

    private boolean mOkButtonEnabled;

    NumberPadTimePickerPromptPresenter(@NonNull INumberPadTimePicker.PromptView view,
                                       @NonNull LocaleModel localeModel, boolean is24HourMode) {
        super(view, localeModel, is24HourMode);
        mView = view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mView = null;
    }

    @Override
    public boolean onOkButtonClick() {
        if (mOkButtonEnabled) {
            mView.setResult(mTimeParser.getHour(mAmPmState), mTimeParser.getMinute(mAmPmState));
            return true;
        }
        return false;
    }

    @Override
    void updateViewEnabledStates() {
        super.updateViewEnabledStates();
        updateOkButtonState();
    }

    private void updateOkButtonState() {
        mOkButtonEnabled = mTimeParser.checkTimeValid(mAmPmState);
        mView.setOkButtonEnabled(mOkButtonEnabled);
    }
}
