package com.philliphsu.numberpadtimepickersample;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.StyleRes;
import android.support.v7.preference.PreferenceManager;

import com.philliphsu.numberpadtimepicker.BackspaceLocation;
import com.philliphsu.numberpadtimepicker.ShowFabPolicy;

public final class CustomThemeModel {
    private static final String PREF_BASE_THEME = "pref_base_theme";
    private static final String PREF_HEADER_BACKGROUND = "pref_header_background";
    private static final String PREF_INPUT_TIME_TEXT_COLOR = "pref_input_time_text_color";
    private static final String PREF_INPUT_AMPM_TEXT_COLOR = "pref_input_ampm_text_color";
    private static final String PREF_NUMBER_PAD_BACKGROUND = "pref_number_pad_background";
    private static final String PREF_NUMBER_KEYS_TEXT_COLOR_ENABLED = "pref_number_keys_text_color_enabled";
    private static final String PREF_NUMBER_KEYS_TEXT_COLOR_DISABLED = "pref_number_keys_text_color_disabled";
    private static final String PREF_ALT_KEYS_TEXT_COLOR_ENABLED = "pref_alt_keys_text_color_enabled";
    private static final String PREF_ALT_KEYS_TEXT_COLOR_DISABLED = "pref_alt_keys_text_color_disabled";
    private static final String PREF_DIVIDER = "pref_divider";
    private static final String PREF_BACKSPACE_TINT_ENABLED = "pref_backspace_tint_enabled";
    private static final String PREF_BACKSPACE_TINT_DISABLED = "pref_backspace_tint_disabled";
    private static final String PREF_SHOW_FAB_POLICY = "pref_show_fab_policy";
    private static final String PREF_ANIMATE_FAB_ENTRY = "pref_animate_fab_entry";
    private static final String PREF_ANIMATE_FAB_COLOR = "pref_animate_fab_color";
    private static final String PREF_FAB_ENABLED_COLOR = "pref_fab_enabled_color";
    private static final String PREF_FAB_DISABLED_COLOR = "pref_fab_disabled_color";
    private static final String PREF_FAB_ICON_ENABLED_TINT = "pref_fab_icon_enabled_tint";
    private static final String PREF_FAB_ICON_DISABLED_TINT = "pref_fab_icon_disabled_tint";
    private static final String PREF_FAB_RIPPLE_COLOR = "pref_fab_ripple_color";
    private static final String PREF_BACKSPACE_LOCATION = "pref_backspace_location";

    private static CustomThemeModel instance;

    private final SharedPreferences sharedPrefs;

    private CustomThemeModel(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static CustomThemeModel get(Context context) {
        if (instance == null) {
            instance = new CustomThemeModel(context);
        }
        return instance;
    }

    @StyleRes
    public int getBaseAlertTheme() {
        switch (getBaseThemeValue(sharedPrefs)) {
            case 0:
            default:
                return R.style.Theme_AppCompat_Light_Dialog_Alert;
            case 1:
                return R.style.Theme_AppCompat_Dialog_Alert;
        }
    }

    @StyleRes
    public int getBaseBottomSheetTheme() {
        switch (getBaseThemeValue(sharedPrefs)) {
            case 0:
            default:
                return R.style.Theme_Design_Light_BottomSheetDialog;
            case 1:
                return R.style.Theme_Design_BottomSheetDialog;
        }
    }

    @StyleRes
    public int getBaseViewActivityTheme() {
        switch (getBaseThemeValue(sharedPrefs)) {
            case 0:
            default:
                return R.style.Theme_Design_Light;  // Inherits from Theme.AppCompat.Light
            case 1:
                return R.style.Theme_Design;        // Inherits from Theme.AppCompat
        }
    }

    public Drawable getHeaderBackground() {
        return makeDrawable(PREF_HEADER_BACKGROUND);
    }

    @ColorInt
    public int getInputTimeTextColor() {
        return sharedPrefs.getInt(PREF_INPUT_TIME_TEXT_COLOR, 0);
    }

    @ColorInt
    public int getInputAmPmTextColor() {
        return sharedPrefs.getInt(PREF_INPUT_AMPM_TEXT_COLOR, 0);
    }

    public Drawable getNumberPadBackground() {
        return makeDrawable(PREF_NUMBER_PAD_BACKGROUND);
    }

    public ColorStateList getNumberKeysTextColor() {
        return makeColorStateList(PREF_NUMBER_KEYS_TEXT_COLOR_DISABLED,
                PREF_NUMBER_KEYS_TEXT_COLOR_ENABLED);
    }

    public ColorStateList getAltKeysTextColor() {
        return makeColorStateList(PREF_ALT_KEYS_TEXT_COLOR_DISABLED,
                PREF_ALT_KEYS_TEXT_COLOR_ENABLED);
    }

    public Drawable getDivider() {
        return makeDrawable(PREF_DIVIDER);
    }

    public ColorStateList getBackspaceTint() {
        return makeColorStateList(PREF_BACKSPACE_TINT_DISABLED, PREF_BACKSPACE_TINT_ENABLED);
    }

    @ShowFabPolicy
    public int getShowFabPolicy() {
        return parseEnum(sharedPrefs, PREF_SHOW_FAB_POLICY);
    }

    public boolean getAnimateFabEntry() {
        return sharedPrefs.getBoolean(PREF_ANIMATE_FAB_ENTRY, false);
    }

    public boolean getAnimateFabColor() {
        return sharedPrefs.getBoolean(PREF_ANIMATE_FAB_COLOR, true);
    }

    public ColorStateList getFabBackgroundColor() {
        return makeColorStateList(PREF_FAB_DISABLED_COLOR, PREF_FAB_ENABLED_COLOR);
    }

    public ColorStateList getFabIconTint() {
        return makeColorStateList(PREF_FAB_ICON_DISABLED_TINT, PREF_FAB_ICON_ENABLED_TINT);
    }

    @ColorInt
    public int getFabRippleColor() {
        return sharedPrefs.getInt(PREF_FAB_RIPPLE_COLOR, 0);
    }

    @BackspaceLocation
    public int getBackspaceLocation() {
        return parseEnum(sharedPrefs, PREF_BACKSPACE_LOCATION);
    }

    public void resetToDefaults(Context context) {
        sharedPrefs.edit().clear().apply();
        PreferenceManager.setDefaultValues(context, R.xml.preferences, true);
    }

    private Drawable makeDrawable(String key) {
        return new ColorDrawable(sharedPrefs.getInt(key, 0));
    }

    private ColorStateList makeColorStateList(String keyDisabled, String keyEnabled) {
        return makeColorStateList(sharedPrefs.getInt(keyDisabled, 0),
                sharedPrefs.getInt(keyEnabled, 0));
    }

    @StyleRes
    private static int getBaseThemeValue(SharedPreferences prefs) {
        return parseEnum(prefs, PREF_BASE_THEME);
    }

    private static int parseEnum(SharedPreferences prefs, String key) {
        return Integer.parseInt(prefs.getString(key, "0"));
    }

    private static final int[][] stateSet = {{-android.R.attr.state_enabled}, {}};

    private static ColorStateList makeColorStateList(int... colors) {
        return new ColorStateList(stateSet, colors);
    }
}
