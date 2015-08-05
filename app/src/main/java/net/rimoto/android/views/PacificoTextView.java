package net.rimoto.android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * TextView with the Pacifico font
 */
public class PacificoTextView extends TextView {
    public PacificoTextView(Context context) {
        super(context);
        init();
    }
    public PacificoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setTypeface(Typeface.createFromAsset(this.getContext().getAssets(), "fonts/Pacifico-Regular.ttf"));
        setIncludeFontPadding(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.setMeasuredDimension((int)(getMeasuredWidth()*1.025), (int)(getBaseline()*1.3));
    }

    @Override
    protected void onDraw(Canvas canvas){
        float offset = (getBaseline() - getLineHeight())*0.5f;

        canvas.translate(0, offset);
        super.onDraw(canvas);
    }
}
