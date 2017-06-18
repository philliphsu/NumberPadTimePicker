package com.philliphsu.numberpadtimepickersample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.philliphsu.numberpadtimepicker.NumberPadTimePicker;

import java.util.Calendar;

public class TimePickerActivity extends AppCompatActivity {
    private final Calendar mCalendar = Calendar.getInstance();

    private NumberPadTimePicker mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        mView = (NumberPadTimePicker) findViewById(R.id.time_picker_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_time_picker_activity, menu);

        final MenuItem okButton = menu.findItem(R.id.action_save);
        mView.setOkButtonCallbacks(new NumberPadTimePicker.OkButtonCallbacks() {
            @Override
            public void onOkButtonEnabled(boolean enabled) {
                okButton.setEnabled(enabled);
            }

            @Override
            public void onOkButtonClick(NumberPadTimePicker view, int hourOfDay, int minute) {
                Context c = TimePickerActivity.this;
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                Toast.makeText(c, DateFormat.getTimeFormat(c).format(mCalendar.getTime()),
                        Toast.LENGTH_LONG).show();
            }
        }, DateFormat.is24HourFormat(this));

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
}
