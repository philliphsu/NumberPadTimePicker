package com.philliphsu.numberpadtimepickersample;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

import com.philliphsu.numberpadtimepicker.NumberPadTimePicker;
import com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.DialogMode;

import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.ALERT_THEME_DARK;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.ALERT_THEME_LIGHT;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.BOTTOM_SHEET_THEME_DARK;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.BOTTOM_SHEET_THEME_LIGHT;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.MODE_ALERT;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.MODE_BOTTOM_SHEET;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_ALERT = "alert";
    private static final String TAG_BOTTOM_SHEET = "bottom_sheet";

    public static final String EXTRA_VIEW_ACTIVITY_THEME_RES = "view_activity_theme_res";
    public static final String EXTRA_VIEW_ACTIVITY_TIME_PICKER_LAYOUT = "view_activity_time_picker_layout";
    public static final String EXTRA_VIEW_ACTIVITY_CUSTOM_THEME = "view_activity_custom_theme";

    private CustomThemeModel customThemeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.button1).setOnClickListener(mAlertClickListener);
        findViewById(R.id.button2).setOnClickListener(mAlertClickListener);
        findViewById(R.id.button3).setOnClickListener(mAlertClickListener);
        findViewById(R.id.button_xml_custom_theme_alert)
                .setOnClickListener(mAlertClickListener);

        findViewById(R.id.button4).setOnClickListener(mBottomSheetClickListener);
        findViewById(R.id.button5).setOnClickListener(mBottomSheetClickListener);
        findViewById(R.id.button6).setOnClickListener(mBottomSheetClickListener);
        findViewById(R.id.button_xml_custom_theme_bottom_sheet)
                .setOnClickListener(mBottomSheetClickListener);

        findViewById(R.id.button7).setOnClickListener(mViewActivityClickListener);
        findViewById(R.id.button8).setOnClickListener(mViewActivityClickListener);
        findViewById(R.id.button9).setOnClickListener(mViewActivityClickListener);
        findViewById(R.id.button_xml_custom_theme_view_standalone)
                .setOnClickListener(mViewActivityClickListener);

        findViewById(R.id.button10).setOnClickListener(mViewActivityClickListener);
        findViewById(R.id.button11).setOnClickListener(mViewActivityClickListener);
        findViewById(R.id.button12).setOnClickListener(mViewActivityClickListener);
        findViewById(R.id.button_xml_custom_theme_view_alert)
                .setOnClickListener(mViewActivityClickListener);

        findViewById(R.id.button13).setOnClickListener(mViewActivityClickListener);
        findViewById(R.id.button14).setOnClickListener(mViewActivityClickListener);
        findViewById(R.id.button15).setOnClickListener(mViewActivityClickListener);
        findViewById(R.id.button_xml_custom_theme_view_bottom_sheet)
                .setOnClickListener(mViewActivityClickListener);

        customThemeModel = CustomThemeModel.get(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // We only have one MenuItem, so we don't need to check which it is.
        startActivity(new Intent(this, EditCustomThemeActivity.class));
        return super.onOptionsItemSelected(item);
    }

    private TimePickerDialog.OnTimeSetListener mListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            TimeSetToastController.get(MainActivity.this).show(hourOfDay, minute);
        }
    };

    private View.OnClickListener mAlertClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int theme = v.getId() == R.id.button_xml_custom_theme_alert ?
                    R.style.NumberPadTimePickerAlertDialogTheme : 0;
            showTimePicker(MODE_ALERT, v.getId(), theme, TAG_ALERT);
        }
    };

    private View.OnClickListener mBottomSheetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int theme = v.getId() == R.id.button_xml_custom_theme_bottom_sheet ?
                    R.style.NumberPadTimePickerBottomSheetDialogTheme : 0;
            showTimePicker(MODE_BOTTOM_SHEET, v.getId(), theme, TAG_BOTTOM_SHEET);
        }
    };

    private View.OnClickListener mViewActivityClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int theme = 0;
            switch (v.getId()) {
                case R.id.button_xml_custom_theme_view_standalone:
                case R.id.button_xml_custom_theme_view_alert:
                case R.id.button_xml_custom_theme_view_bottom_sheet:
                    theme = R.style.AppTheme_TimePickerActivity;
                    break;
            }
            showTimePickerActivity(v.getId(), theme);
        }
    };
    
    private void showTimePicker(@DialogMode int mode, @IdRes int buttonId,
            @StyleRes int theme, String tag) {
        boolean customTheme = false;
        if (theme == 0) {
            switch (buttonId) {
                case R.id.button1:
                    theme = ALERT_THEME_LIGHT;
                    break;
                case R.id.button2:
                    theme = ALERT_THEME_DARK;
                    break;
                case R.id.button3:
                    theme = customThemeModel.getBaseAlertTheme();
                    customTheme = true;
                    break;
                case R.id.button4:
                    theme = BOTTOM_SHEET_THEME_LIGHT;
                    break;
                case R.id.button5:
                    theme = BOTTOM_SHEET_THEME_DARK;
                    break;
                case R.id.button6:
                    theme = customThemeModel.getBaseBottomSheetTheme();
                    customTheme = true;
                    break;
            }
        }
        NumberPadTimePickerDialogFragment.newInstance(mListener, mode, theme, customTheme)
                .show(getSupportFragmentManager(), tag);
    }

    private void showTimePickerActivity(@IdRes int buttonId, @StyleRes int theme) {
        boolean customTheme = false;
        if (theme == 0) {
            switch (buttonId) {
                case R.id.button7:
                case R.id.button10:
                case R.id.button13:
                    theme = R.style.Theme_Design_Light;
                    break;
                case R.id.button8:
                case R.id.button11:
                case R.id.button14:
                    theme = R.style.Theme_Design;
                    break;
                case R.id.button9:
                case R.id.button12:
                case R.id.button15:
                    theme = customThemeModel.getBaseViewActivityTheme();
                    customTheme = true;
                    break;
            }
        }
        
        int layout = 0;
        switch (buttonId) {
            case R.id.button7:
            case R.id.button8:
            case R.id.button9:
            case R.id.button_xml_custom_theme_view_standalone:
                layout = NumberPadTimePicker.LAYOUT_STANDALONE;
                break;
            case R.id.button10:
            case R.id.button11:
            case R.id.button12:
            case R.id.button_xml_custom_theme_view_alert:
                layout = NumberPadTimePicker.LAYOUT_ALERT;
                break;
            case R.id.button13:
            case R.id.button14:
            case R.id.button15:
            case R.id.button_xml_custom_theme_view_bottom_sheet:
                layout = NumberPadTimePicker.LAYOUT_BOTTOM_SHEET;
                break;
        }
        
        Intent intent = new Intent(this, TimePickerActivity.class);
        intent.putExtra(EXTRA_VIEW_ACTIVITY_THEME_RES, theme);
        intent.putExtra(EXTRA_VIEW_ACTIVITY_TIME_PICKER_LAYOUT, layout);
        intent.putExtra(EXTRA_VIEW_ACTIVITY_CUSTOM_THEME, customTheme);
        startActivity(intent);
    }
}
