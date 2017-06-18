package com.philliphsu.numberpadtimepickersample;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.DialogMode;

import java.util.Calendar;

import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.ALERT_THEME_DARK;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.ALERT_THEME_LIGHT;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.BOTTOM_SHEET_THEME_DARK;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.BOTTOM_SHEET_THEME_LIGHT;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.MODE_ALERT;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.MODE_BOTTOM_SHEET;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_ALERT = "alert";
    private static final String TAG_BOTTOM_SHEET = "bottom_sheet";

    private CustomThemeModel customThemeModel;
    private TextView timeSetView;

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

        findViewById(R.id.button_time_picker_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TimePickerActivity.class));
            }
        });

        customThemeModel = CustomThemeModel.get(this);
        timeSetView = (TextView) findViewById(R.id.time_set);
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
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            timeSetView.setText(getString(R.string.time_set_label, DateFormat.getTimeFormat(
                    MainActivity.this).format(calendar.getTime())));
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
}
