package com.philliphsu.numberpadtimepicker;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

/**
 * Adds {@link NumberPadTimePicker#LAYOUT_BOTTOM_SHEET bottom sheet} theming APIs
 * to the {@link NumberPadTimePickerThemer base set of APIs}.
 */
public interface BottomSheetNumberPadTimePickerThemer extends NumberPadTimePickerThemer {
    BottomSheetNumberPadTimePickerThemer setFabBackgroundColor(ColorStateList colors);

    BottomSheetNumberPadTimePickerThemer setFabRippleColor(@ColorInt int color);

    BottomSheetNumberPadTimePickerThemer setFabIconTint(ColorStateList tint);

    BottomSheetNumberPadTimePickerThemer setAnimateFabBackgroundColor(boolean animate);

    BottomSheetNumberPadTimePickerThemer setShowFabPolicy(@ShowFabPolicy int policy);

    BottomSheetNumberPadTimePickerThemer setAnimateFabIn(boolean animateIn);

    BottomSheetNumberPadTimePickerThemer setBackspaceLocation(@BackspaceLocation int location);

    @Override
    BottomSheetNumberPadTimePickerThemer setInputTimeTextColor(@ColorInt int color);

    @Override
    BottomSheetNumberPadTimePickerThemer setInputAmPmTextColor(@ColorInt int color);

    @Override
    BottomSheetNumberPadTimePickerThemer setBackspaceTint(ColorStateList colors);

    @Override
    BottomSheetNumberPadTimePickerThemer setNumberKeysTextColor(ColorStateList colors);

    @Override
    BottomSheetNumberPadTimePickerThemer setAltKeysTextColor(ColorStateList colors);

    @Override
    BottomSheetNumberPadTimePickerThemer setHeaderBackground(Drawable background);

    @Override
    BottomSheetNumberPadTimePickerThemer setNumberPadBackground(Drawable background);

    @Override
    BottomSheetNumberPadTimePickerThemer setDivider(Drawable divider);
}
