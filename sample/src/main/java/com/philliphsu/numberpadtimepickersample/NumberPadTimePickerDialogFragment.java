package com.philliphsu.numberpadtimepickersample;

import android.app.Dialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import com.philliphsu.numberpadtimepicker.BottomSheetNumberPadTimePickerDialog;
import com.philliphsu.numberpadtimepicker.BottomSheetNumberPadTimePickerDialogThemer;
import com.philliphsu.numberpadtimepicker.NumberPadTimePickerDialog;
import com.philliphsu.numberpadtimepicker.NumberPadTimePickerDialogThemer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class NumberPadTimePickerDialogFragment extends DialogFragment {
    private static final String KEY_THEME_RES_ID = "theme_res_id";
    private static final String KEY_DIALOG_MODE = "dialog_mode";
    private static final String KEY_CUSTOM_THEME = "custom_theme";

    @StyleRes
    public static final int ALERT_THEME_LIGHT = R.style.Theme_AppCompat_Light_Dialog_Alert;
    @StyleRes
    public static final int ALERT_THEME_DARK = R.style.Theme_AppCompat_Dialog_Alert;
    @StyleRes
    public static final int BOTTOM_SHEET_THEME_LIGHT = R.style.Theme_Design_Light_BottomSheetDialog;
    @StyleRes
    public static final int BOTTOM_SHEET_THEME_DARK = R.style.Theme_Design_BottomSheetDialog;

    public static final int MODE_ALERT = 1;
    public static final int MODE_BOTTOM_SHEET = 2;

    @IntDef({MODE_ALERT, MODE_BOTTOM_SHEET})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DialogMode {}

    @DialogMode
    private int dialogMode;

    @StyleRes
    private int themeResId;

    private boolean customTheme;

    private OnTimeSetListener listener;

    public static NumberPadTimePickerDialogFragment newInstance(OnTimeSetListener listener,
            @DialogMode int dialogMode, @StyleRes int themeResId, boolean customTheme) {
        NumberPadTimePickerDialogFragment f = new NumberPadTimePickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_THEME_RES_ID, themeResId);
        args.putInt(KEY_DIALOG_MODE, dialogMode);
        args.putBoolean(KEY_CUSTOM_THEME, customTheme);
        f.setArguments(args);
        f.listener = listener;
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            themeResId = args.getInt(KEY_THEME_RES_ID, 0);
            dialogMode = args.getInt(KEY_DIALOG_MODE, MODE_ALERT);
            customTheme = args.getBoolean(KEY_CUSTOM_THEME, false);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog;
        NumberPadTimePickerDialogThemer themer;
        boolean is24HourMode = DateFormat.is24HourFormat(getContext());
        boolean isBottomSheetThemer = false;

        switch (dialogMode) {
            case MODE_BOTTOM_SHEET:
                BottomSheetNumberPadTimePickerDialog bottomSheetPicker = new
                        BottomSheetNumberPadTimePickerDialog(getContext(),
                        themeResId, listener, is24HourMode);
                themer = bottomSheetPicker.getThemer();
                dialog = bottomSheetPicker;
                isBottomSheetThemer = true;
                break;
            case MODE_ALERT:
            default:
                NumberPadTimePickerDialog alertPicker = new NumberPadTimePickerDialog(
                        getContext(), themeResId, listener, is24HourMode);
                themer = alertPicker.getThemer();
                dialog = alertPicker;
                break;
        }

        if (customTheme) {
            CustomThemeModel model = CustomThemeModel.get(getContext());
            themer.setHeaderBackground(model.getHeaderBackground())
                    .setInputTimeTextColor(model.getInputTimeTextColor())
                    .setInputAmPmTextColor(model.getInputAmPmTextColor())
                    .setNumberPadBackground(model.getNumberPadBackground())
                    .setNumberKeysTextColor(model.getNumberKeysTextColor())
                    .setAltKeysTextColor(model.getAltKeysTextColor())
                    .setDivider(model.getDivider())
                    .setBackspaceTint(model.getBackspaceTint());
            if (isBottomSheetThemer) {
                ((BottomSheetNumberPadTimePickerDialogThemer) themer)
                        .setShowFabPolicy(model.getShowFabPolicy())
                        .setAnimateFabIn(model.getAnimateFabEntry())
                        .setAnimateFabBackgroundColor(model.getAnimateFabColor())
                        .setFabBackgroundColor(model.getFabBackgroundColor())
                        .setFabIconTint(model.getFabIconTint())
                        .setFabRippleColor(model.getFabRippleColor())
                        .setBackspaceLocation(model.getBackspaceLocation());
            }
        }

        return dialog;
    }
}
