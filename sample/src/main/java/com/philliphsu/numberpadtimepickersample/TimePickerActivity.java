package com.philliphsu.numberpadtimepickersample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.philliphsu.numberpadtimepicker.NumberPadTimePicker;
import com.philliphsu.numberpadtimepicker.NumberPadTimePicker.NumberPadTimePickerLayout;

public class TimePickerActivity extends AppCompatActivity {

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
                mView.setOkButtonCallbacks(mOkButtonCallbacks);
                mView.setCancelButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            case NumberPadTimePicker.LAYOUT_BOTTOM_SHEET:
                mView = (NumberPadTimePicker) findViewById(R.id.time_picker_view_bottom_sheet);
                mView.setOkButtonCallbacks(mOkButtonCallbacks);
                break;
            case NumberPadTimePicker.LAYOUT_STANDALONE:
            default:
                mView = (NumberPadTimePicker) findViewById(R.id.time_picker_view_standalone);
                break;
        }
        mView.setVisibility(View.VISIBLE);

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
            TimeSetToastController.get(TimePickerActivity.this).show(hourOfDay, minute);
        }
    };
}
