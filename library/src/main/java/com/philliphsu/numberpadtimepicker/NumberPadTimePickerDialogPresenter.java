package com.philliphsu.numberpadtimepicker;

import android.support.annotation.NonNull;

final class NumberPadTimePickerDialogPresenter extends NumberPadTimePickerPromptPresenter
        implements INumberPadTimePicker.DialogPresenter {

    private INumberPadTimePicker.DialogView mView;

    NumberPadTimePickerDialogPresenter(@NonNull INumberPadTimePicker.DialogView view,
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
    public void onCancelClick() {
        mView.cancel();
    }

    @Override
    public boolean onOkButtonClick() {
        if (super.onOkButtonClick()) {
            mView.cancel();
            return true;
        }
        return false;
    }

    @Override
    public void onDialogShow() {
        mView.showOkButton();
    }
}
