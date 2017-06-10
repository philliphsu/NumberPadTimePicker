package com.philliphsu.numberpadtimepickersample;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StyleRes;
import android.support.v7.preference.PreferenceManager;

import com.philliphsu.numberpadtimepicker.BackspaceLocation;
import com.philliphsu.numberpadtimepicker.ShowFabPolicy;

public final class CustomThemeModel {
    private static final String PREF_BASE_THEME = "pref_base_theme";
    private static final String PREF_SHOW_FAB_POLICY = "pref_show_fab_policy";
    private static final String PREF_ANIMATE_FAB_ENTRY = "pref_animate_fab_entry";
    private static final String PREF_ANIMATE_FAB_COLOR = "pref_animate_fab_color";
    private static final String PREF_BACKSPACE_LOCATION = "pref_backspace_location";

    private static CustomThemeModel instance;

    private final SharedPreferences sharedPrefs;

    private CustomThemeModel(Context context) {
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

    @BackspaceLocation
    public int getBackspaceLocation() {
        return parseEnum(sharedPrefs, PREF_BACKSPACE_LOCATION);
    }

    @StyleRes
    private static int getBaseThemeValue(SharedPreferences prefs) {
        return parseEnum(prefs, PREF_BASE_THEME);
    }

    private static int parseEnum(SharedPreferences prefs, String key) {
        return Integer.parseInt(prefs.getString(key, "0"));
    }
}
