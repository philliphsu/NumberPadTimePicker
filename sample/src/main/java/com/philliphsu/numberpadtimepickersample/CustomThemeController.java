package com.philliphsu.numberpadtimepickersample;

import android.content.Context;

import com.philliphsu.numberpadtimepicker.BottomSheetNumberPadTimePickerThemer;
import com.philliphsu.numberpadtimepicker.NumberPadTimePickerThemer;

public class CustomThemeController {
    private static CustomThemeController instance;

    private final CustomThemeModel model;

    private CustomThemeController(Context context) {
        model = CustomThemeModel.get(context);
    }

    public static CustomThemeController get(Context context) {
        if (instance == null) {
            instance = new CustomThemeController(context);
        }
        return instance;
    }

    public void applyCustomTheme(NumberPadTimePickerThemer themer) {
        themer.setHeaderBackground(model.getHeaderBackground())
                .setInputTimeTextColor(model.getInputTimeTextColor())
                .setInputAmPmTextColor(model.getInputAmPmTextColor())
                .setNumberPadBackground(model.getNumberPadBackground())
                .setNumberKeysTextColor(model.getNumberKeysTextColor())
                .setAltKeysTextColor(model.getAltKeysTextColor())
                .setDivider(model.getDivider())
                .setBackspaceTint(model.getBackspaceTint());
        if (themer instanceof BottomSheetNumberPadTimePickerThemer) {
            ((BottomSheetNumberPadTimePickerThemer) themer)
                    .setShowFabPolicy(model.getShowFabPolicy())
                    .setAnimateFabIn(model.getAnimateFabEntry())
                    .setAnimateFabBackgroundColor(model.getAnimateFabColor())
                    .setFabBackgroundColor(model.getFabBackgroundColor())
                    .setFabIconTint(model.getFabIconTint())
                    .setFabRippleColor(model.getFabRippleColor())
                    .setBackspaceLocation(model.getBackspaceLocation());
        }
    }
}
