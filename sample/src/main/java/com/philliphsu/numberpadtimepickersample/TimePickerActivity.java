package com.philliphsu.numberpadtimepickersample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.philliphsu.numberpadtimepicker.NumberPadTimePicker;
import com.philliphsu.numberpadtimepicker.NumberPadTimePicker.NumberPadTimePickerLayout;

import java.util.Calendar;

public class TimePickerActivity extends AppCompatActivity {
    private final Calendar mCalendar = Calendar.getInstance();

    private NumberPadTimePicker mView;
    private @Nullable MenuItem mCustomOkButton;
    private @NumberPadTimePickerLayout int mTimePickerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(MainActivity.EXTRA_VIEW_ACTIVITY_THEME_RES)) {
            setTheme(getIntent().getIntExtra(MainActivity.EXTRA_VIEW_ACTIVITY_THEME_RES, 0));
        }

        setContentView(R.layout.activity_time_picker);
        mTimePickerLayout = getIntent().getIntExtra(MainActivity.EXTRA_VIEW_ACTIVITY_TIME_PICKER_LAYOUT, 0);
        switch (mTimePickerLayout) {
            case NumberPadTimePicker.LAYOUT_ALERT:
                mView = (NumberPadTimePicker) findViewById(R.id.time_picker_view_alert);
                break;
            case NumberPadTimePicker.LAYOUT_BOTTOM_SHEET:
                mView = (NumberPadTimePicker) findViewById(R.id.time_picker_view_bottom_sheet);
                break;
            case NumberPadTimePicker.LAYOUT_STANDALONE:
            default:
                mView = (NumberPadTimePicker) findViewById(R.id.time_picker_view_standalone);
                break;
        }
        mView.setVisibility(View.VISIBLE);

        if (mTimePickerLayout != NumberPadTimePicker.LAYOUT_STANDALONE) {
            mView.setOkButtonCallbacks(mOkButtonCallbacks);
        }

        if (getIntent().getBooleanExtra(MainActivity.EXTRA_VIEW_ACTIVITY_CUSTOM_THEME, false)) {
            CustomThemeController.get(this).applyCustomTheme(mView.getThemer());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mTimePickerLayout == NumberPadTimePicker.LAYOUT_STANDALONE) {
            getMenuInflater().inflate(R.menu.menu_time_picker_activity, menu);

            mCustomOkButton = menu.findItem(R.id.action_save);
            mView.setOkButtonCallbacks(mOkButtonCallbacks);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                mView.confirmTimeSelection();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private NumberPadTimePicker.OkButtonCallbacks mOkButtonCallbacks =
            new NumberPadTimePicker.OkButtonCallbacks() {
        @Override
        public void onOkButtonEnabled(boolean enabled) {
            if (mCustomOkButton != null) {
                mCustomOkButton.setEnabled(enabled);
            }
        }

        @Override
        public void onOkButtonClick(NumberPadTimePicker view, int hourOfDay, int minute) {
            Context c = TimePickerActivity.this;
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar.set(Calendar.MINUTE, minute);
            Toast.makeText(c, DateFormat.getTimeFormat(c).format(mCalendar.getTime()),
                    Toast.LENGTH_LONG).show();
        }
    };
}
