package com.philliphsu.numberpadtimepicker;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Interface through which a {@link NumberPadTimePicker} contained in
 * {@link BottomSheetNumberPadTimePickerDialog} can have its colors
 * and other attributes customized.
 */
public class BottomSheetNumberPadTimePickerDialogThemer extends NumberPadTimePickerDialogThemer
        implements BottomSheetNumberPadTimePickerThemer {
    private final NumberPadTimePickerBottomSheetComponent mTimePickerComponent;

    BottomSheetNumberPadTimePickerDialogThemer(@NonNull NumberPadTimePickerBottomSheetComponent timePickerComponent) {
        super(timePickerComponent);
        mTimePickerComponent = timePickerComponent;
    }

    /**
     * Set the background color of the FloatingActionButton.
     *
     * @param colors A ColorStateList that defines colors for {@link android.R.attr#state_enabled}.
     *
     * @see R.attr#nptp_fabBackgroundColor
     */
    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setFabBackgroundColor(ColorStateList colors) {
        mTimePickerComponent.setFabBackgroundColor(colors);
        return this;
    }

    /**
     * Set the ripple color of the FloatingActionButton.
     *
     * @see R.attr#nptp_fabRippleColor
     */
    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setFabRippleColor(@ColorInt int color) {
        mTimePickerComponent.setFabRippleColor(color);
        return this;
    }

    /**
     * Set the tint to apply to the FloatingActionButton's icon.
     *
     * @param tint A ColorStateList that defines colors for {@link android.R.attr#state_enabled}.
     *
     * @see R.attr#nptp_fabIconTint
     */
    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setFabIconTint(ColorStateList tint) {
        mTimePickerComponent.setFabIconTint(tint);
        return this;
    }

    /**
     * Set whether the FloatingActionButton should transition between its enabled and disabled colors.
     *
     * <p> If the {@link ShowFabPolicy} was set to {@link ShowFabPolicy#SHOW_FAB_VALID_TIME
     * SHOW_FAB_VALID_TIME}, then setting this to {@code true} does nothing.
     *
     * @see #setFabBackgroundColor(ColorStateList)
     * @see #setShowFabPolicy(int)
     * @see R.attr#nptp_animateFabBackgroundColor
     */
    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setAnimateFabBackgroundColor(boolean animate) {
        mTimePickerComponent.setAnimateFabBackgroundColor(animate);
        return this;
    }

    /**
     * Set the policy for when the FloatingActionButton should be shown.
     *
     * @param policy {@link ShowFabPolicy#SHOW_FAB_ALWAYS SHOW_FAB_ALWAYS} or
     *               {@link ShowFabPolicy#SHOW_FAB_VALID_TIME SHOW_FAB_VALID_TIME}.
     *
     * @see R.attr#nptp_showFab
     */
    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setShowFabPolicy(@ShowFabPolicy int policy) {
        mTimePickerComponent.setShowFabPolicy(policy);
        return this;
    }

    /**
     * Set whether the FloatingActionButton should animate onto the screen when the dialog is shown.
     *
     * <p> If the {@link ShowFabPolicy} was set to {@link ShowFabPolicy#SHOW_FAB_VALID_TIME
     * SHOW_FAB_VALID_TIME}, then setting this to {@code true} does nothing.
     *
     * @see #setShowFabPolicy(int)
     * @see R.attr#nptp_animateFabIn
     */
    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setAnimateFabIn(boolean animateIn) {
        mTimePickerComponent.setAnimateFabIn(animateIn);
        return this;
    }

    /**
     * Set the location of the backspace button.
     *
     * @param location {@link BackspaceLocation#LOCATION_HEADER LOCATION_HEADER} or
     *                 {@link BackspaceLocation#LOCATION_FOOTER LOCATION_FOOTER}
     *
     * @see R.attr#nptp_backspaceLocation
     */
    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setBackspaceLocation(@BackspaceLocation int location) {
        mTimePickerComponent.setBackspaceLocation(location);
        return this;
    }

    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setInputTimeTextColor(@ColorInt int color) {
        return (BottomSheetNumberPadTimePickerDialogThemer) super.setInputTimeTextColor(color);
    }

    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setInputAmPmTextColor(@ColorInt int color) {
        return (BottomSheetNumberPadTimePickerDialogThemer) super.setInputAmPmTextColor(color);
    }

    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setBackspaceTint(ColorStateList colors) {
        return (BottomSheetNumberPadTimePickerDialogThemer) super.setBackspaceTint(colors);
    }

    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setNumberKeysTextColor(ColorStateList colors) {
        return (BottomSheetNumberPadTimePickerDialogThemer) super.setNumberKeysTextColor(colors);
    }

    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setAltKeysTextColor(ColorStateList colors) {
        return (BottomSheetNumberPadTimePickerDialogThemer) super.setAltKeysTextColor(colors);
    }

    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setHeaderBackground(Drawable background) {
        return (BottomSheetNumberPadTimePickerDialogThemer) super.setHeaderBackground(background);
    }

    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setNumberPadBackground(Drawable background) {
        return (BottomSheetNumberPadTimePickerDialogThemer) super.setNumberPadBackground(background);
    }

    @Override
    public BottomSheetNumberPadTimePickerDialogThemer setDivider(Drawable divider) {
        return (BottomSheetNumberPadTimePickerDialogThemer) super.setDivider(divider);
    }
}
