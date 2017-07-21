package com.philliphsu.numberpadtimepickersample;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.Calendar;

public class TimeSetToastController {

    private static TimeSetToastController instance;

    private final Calendar calendar = Calendar.getInstance();

    private final Context appContext;
    private final java.text.DateFormat timeFormatter;

    private TimeSetToastController(Context appContext) {
        this.appContext = appContext;
        timeFormatter = DateFormat.getTimeFormat(appContext);
    }

    public static TimeSetToastController get(Context context) {
        if (instance == null) {
            instance = new TimeSetToastController(context.getApplicationContext());
        }
        return instance;
    }

    public void show(int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        Toast.makeText(appContext, timeFormatter.format(calendar.getTime()),
                Toast.LENGTH_LONG).show();
    }
}
