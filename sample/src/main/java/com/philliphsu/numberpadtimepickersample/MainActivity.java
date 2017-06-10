package com.philliphsu.numberpadtimepickersample;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.DialogMode;

import java.util.Calendar;

import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.ALERT_THEME_DARK;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.ALERT_THEME_LIGHT;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.BOTTOM_SHEET_THEME_DARK;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.BOTTOM_SHEET_THEME_LIGHT;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.MODE_ALERT;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.MODE_BOTTOM_SHEET;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG_ALERT = "alert";
    private static final String TAG_BOTTOM_SHEET = "bottom_sheet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.button1).setOnClickListener(mAlertClickListener);
        findViewById(R.id.button2).setOnClickListener(mAlertClickListener);
        findViewById(R.id.button3).setOnClickListener(mAlertClickListener);
        findViewById(R.id.button4).setOnClickListener(mBottomSheetClickListener);
        findViewById(R.id.button5).setOnClickListener(mBottomSheetClickListener);
        findViewById(R.id.button6).setOnClickListener(mBottomSheetClickListener);
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

            Context context = MainActivity.this;
            String text = "Time set: " + DateFormat.getTimeFormat(context).format(calendar.getTime());
            Log.d(TAG, text);
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    };

    private View.OnClickListener mAlertClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showTimePicker(MODE_ALERT, v.getId(), TAG_ALERT);
        }
    };

    private View.OnClickListener mBottomSheetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showTimePicker(MODE_BOTTOM_SHEET, v.getId(), TAG_BOTTOM_SHEET);
        }
    };
    
    private void showTimePicker(@DialogMode int mode, @IdRes int buttonId, String tag) {
        boolean customTheme = false;
        int theme = 0;
        switch (buttonId) {
            case R.id.button1:
                theme = ALERT_THEME_LIGHT;
                break;
            case R.id.button2:
                theme = ALERT_THEME_DARK;
                break;
            case R.id.button3:
                customTheme = true;
                break;
            case R.id.button4:
                theme = BOTTOM_SHEET_THEME_LIGHT;
                break;
            case R.id.button5:
                theme = BOTTOM_SHEET_THEME_DARK;
                break;
            case R.id.button6:
                customTheme = true;
                break;
        }
        NumberPadTimePickerDialogFragment.newInstance(mListener, mode, theme, customTheme)
                .show(getSupportFragmentManager(), tag);
    }
}
