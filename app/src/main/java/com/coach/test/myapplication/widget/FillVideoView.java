package com.coach.test.myapplication.widget;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * Created by Administrator on 2017-02-20.
 */

public class FillVideoView extends VideoView {

    public FillVideoView(Context context) {
        super(context);
    }

    public FillVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FillVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Display dis = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        dis.getSize(size);
        setMeasuredDimension(size.x, size.y);
    }
}
