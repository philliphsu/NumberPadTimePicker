package com.philliphsu.numberpadtimepickersample;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.ALERT_THEME_DARK;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.ALERT_THEME_LIGHT;
import static com.philliphsu.numberpadtimepickersample.NumberPadTimePickerDialogFragment.MODE_ALERT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG_ALERT = "alert";
    private static final String TAG_BOTTOM_SHEET = "bottom_sheet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPadTimePickerDialogFragment.newInstance(mListener, MODE_ALERT, ALERT_THEME_LIGHT)
                        .show(getSupportFragmentManager(), TAG_ALERT);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPadTimePickerDialogFragment.newInstance(mListener, MODE_ALERT, ALERT_THEME_DARK)
                        .show(getSupportFragmentManager(), TAG_ALERT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // We only have one MenuItem, so we don't need to check which it is.
        startActivity(new Intent(this, SettingsActivity.class));
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
}
