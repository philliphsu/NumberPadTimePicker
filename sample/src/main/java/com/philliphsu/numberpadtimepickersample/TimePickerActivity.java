package com.philliphsu.numberpadtimepickersample;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

        mTimePickerLayout = getIntent().getIntExtra(MainActivity.EXTRA_VIEW_ACTIVITY_TIME_PICKER_LAYOUT, 0);
        final int nptpLayoutRes;
        switch (mTimePickerLayout) {
            case NumberPadTimePicker.LAYOUT_ALERT:
                nptpLayoutRes = R.layout.number_pad_time_picker_alert;
                break;
            case NumberPadTimePicker.LAYOUT_BOTTOM_SHEET:
                nptpLayoutRes = R.layout.number_pad_time_picker_bottom_sheet;
                break;
            case NumberPadTimePicker.LAYOUT_STANDALONE:
            default:
                nptpLayoutRes = R.layout.number_pad_time_picker_standalone;
                break;
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Set a ScrollView as the root view
            setContentView(R.layout.activity_time_picker);
            // Inflate nptpLayoutRes using the ScrollView as root so that the NumberPadTimePicker's
            // layout params are applied.
            mView = (NumberPadTimePicker) getLayoutInflater().inflate(nptpLayoutRes,
                    (ViewGroup) findViewById(R.id.scrollView), false /* attachToRoot */);
            // attachToRoot was false so that the inflated NumberPadTimePicker is returned.
            // Add it as a child view manually.
            ((ViewGroup) findViewById(R.id.scrollView)).addView(mView);
        } else {
            // Inflate nptpLayoutRes using the window as root so that the NumberPadTimePicker's
            // layout params are applied.
            mView = (NumberPadTimePicker) getLayoutInflater().inflate(nptpLayoutRes,
                    (ViewGroup) findViewById(android.R.id.content), false /* attachToRoot */);
            // attachToRoot was false so that the inflated NumberPadTimePicker is returned.
            // Set it as the content view manually.
            setContentView(mView);
        }

        if (mTimePickerLayout != NumberPadTimePicker.LAYOUT_STANDALONE) {
            mView.setOkButtonCallbacks(mOkButtonCallbacks);
        }

        if (mTimePickerLayout == NumberPadTimePicker.LAYOUT_ALERT) {
            mView.setCancelButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
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
            TimeSetToastController.get(TimePickerActivity.this).show(hourOfDay, minute);
        }
    };
}
