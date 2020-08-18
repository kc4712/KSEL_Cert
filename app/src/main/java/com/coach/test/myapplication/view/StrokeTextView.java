package com.coach.test.myapplication.view;

import com.coach.test.myapplication.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by Administrator on 2017-02-17.
 */

public class StrokeTextView extends TextView {
    private int strokeColor = 0;
    private float strokeWidth = 0.0f;
    private boolean stroke = false;

    public StrokeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }
    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }
    public StrokeTextView(Context context) {
        super(context);
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = Color.parseColor(strokeColor);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
        stroke = a.getBoolean(R.styleable.StrokeTextView_textStroke, false);
        strokeWidth = a.getFloat(R.styleable.StrokeTextView_textStrokeWidth, 0.0f);
        strokeColor = a.getColor(R.styleable.StrokeTextView_textStrokeColor, 0xffffffff);
        a.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ColorStateList states = getTextColors(); // text color 값 저장

        // stroke 그리기
        if(stroke) {
            getPaint().setStyle(Style.STROKE);
            getPaint().setStrokeWidth(strokeWidth);
            setTextColor(strokeColor);
            super.onDraw(canvas);

            // stroke 위에 그리기
            getPaint().setStyle(Style.FILL);
            setTextColor(states);
        }
        super.onDraw(canvas);
    }
}