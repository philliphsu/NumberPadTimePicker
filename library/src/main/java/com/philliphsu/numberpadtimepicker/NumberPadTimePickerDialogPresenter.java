package com.philliphsu.numberpadtimepicker;

import android.support.annotation.NonNull;

import static com.philliphsu.numberpadtimepicker.Preconditions.checkNotNull;

final class NumberPadTimePickerDialogPresenter implements INumberPadTimePicker.DialogPresenter {

    private final INumberPadTimePicker.DialogView mView;
    private INumberPadTimePicker.Presenter mBasePresenter;

    NumberPadTimePickerDialogPresenter(@NonNull INumberPadTimePicker.DialogView view,
            @NonNull INumberPadTimePicker.Presenter basePresenter) {
        mView = checkNotNull(view);
        mBasePresenter = checkNotNull(basePresenter);
    }

    @Override
    public void onCancelClick() {
        mView.cancel();
    }

    @Override
    public boolean onOkButtonClick() {
        if (mBasePresenter.onOkButtonClick()) {
            mView.cancel();
            return true;
        }
        return false;
    }

    @Override
    public void setBasePresenter(INumberPadTimePicker.Presenter presenter) {
        mBasePresenter = presenter;
    }
}
