package com.philliphsu.numberpadtimepicker;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * View to show a 4 x 3 grid of text buttons.
 */
public class GridPickerView extends GridLayout {

    private static final @IdRes int[] TEXTVIEW_IDS = {
            R.id.nptp_text0,  R.id.nptp_text1,   R.id.nptp_text2,
            R.id.nptp_text3,  R.id.nptp_text4,   R.id.nptp_text5,
            R.id.nptp_text6,  R.id.nptp_text7,   R.id.nptp_text8,
            R.id.nptp_text9,  R.id.nptp_text10,  R.id.nptp_text11,
    };

    private final TextView[] TEXTVIEWS = new TextView[12];

    public GridPickerView(Context context) {
        this(context, null);
    }

    public GridPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setColumnCount(context.getResources().getInteger(R.integer.nptp_gridpicker_column_count));
        inflate(context, R.layout.nptp_gridpicker_text_buttons, this);

        for (int i = 0; i < 12; i++) {
            TEXTVIEWS[i] = (TextView) findViewById(TEXTVIEW_IDS[i]);
        }
    }

    protected final void setOnButtonClickListener(OnClickListener l) {
        for (int i = 0; i < 12; i++) {
            TEXTVIEWS[i].setOnClickListener(l);
        }
    }

    /**
     * @param i     A position from {@code 0 <= i < 12}.
     * @param text  The text to be displayed at position i.
     */
    protected final void setTextForPosition(int i, @Nullable CharSequence text) {
        TEXTVIEWS[i].setText(text);
    }

    /**
     * @param i  A position from {@code 0 <= i < 12}.
     */
    protected final TextView getButton(int i) {
        return TEXTVIEWS[i];
    }
}