package net.rimoto.android.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import net.rimoto.android.R;

/**
 * CircleProgressView
 * License: MIT
 * Author:
 *  Almog Baku - http://www.AlmogBaku.com (On behalf of Rimoto - www.rimoto.net)
 */
public class CircleProgressView extends TextView {
    private Paint   mPaintBase;
    private Paint   mPaintBold;
    private float   mPercentage, mStrokeBase, mStrokeBold;
    private int     mColorBase, mColorBold;

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleProgressView,
                0,0);

        mColorBase = attrsArray.getColor(R.styleable.CircleProgressView_colorBase, Color.BLACK);
        mColorBold = attrsArray.getColor(R.styleable.CircleProgressView_colorBold, Color.BLACK);
        mPercentage= attrsArray.getFloat(R.styleable.CircleProgressView_percentage, 25);
        mStrokeBase= attrsArray.getFloat(R.styleable.CircleProgressView_strokeBase, 5);
        mStrokeBold= attrsArray.getFloat(R.styleable.CircleProgressView_strokeBold, 10);

        init();
    }
    public void setPercentage(float percentage) {
        this.mPercentage = percentage;
        invalidate();
    }
    public void setStrokeBase(float stroke) {
        this.mStrokeBase = stroke;
        invalidate();
    }
    public void setStrokeBold(float stroke) {
        this.mStrokeBold = stroke;
        invalidate();
    }
    public void setColorBase(int color) {
        this.mColorBase = color;
        invalidate();
    }
    public void setColorBold(int color) {
        this.mColorBold = color;
        invalidate();
    }

    @Override
    public void invalidate() {
        if(this.getText().toString().isEmpty()) {
            this.setText(Integer.valueOf(Math.round(mPercentage)).toString());
        }
        super.invalidate();
    }

    private void init() {
        this.setGravity(Gravity.CENTER);

        mPaintBase = new Paint();
        mPaintBase.setColor(mColorBase);
        mPaintBase.setStrokeWidth(mStrokeBase);
        mPaintBase.setStyle(Paint.Style.STROKE);
        mPaintBase.setAntiAlias(true);

        mPaintBold = new Paint();
        mPaintBold.setColor(mColorBold);
        mPaintBold.setStrokeWidth(mStrokeBold);
        mPaintBold.setStyle(Paint.Style.STROKE);
        mPaintBold.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final RectF oval = new RectF();

        oval.set(mStrokeBold, mStrokeBold, getWidth() - mStrokeBold, getHeight() - mStrokeBold);

        Path path = new Path();
        path.addArc(oval, -90, 360);
        canvas.drawPath(path, mPaintBase);

        float percentageSweep = (360* mPercentage *0.01f);
        if(percentageSweep!=0) {
            path = new Path();
            path.addArc(oval, -90, percentageSweep);
            canvas.drawPath(path, mPaintBold);
        }
    }
}
