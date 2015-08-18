package net.rimoto.android.views;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.LruCache;

/**
 * Style a {@link Spannable} with a Pacifico {@link Typeface}.
 **/
public class PacificoTypefaceSpan extends TypefaceSpan {
    /**
     * Load the {@link Typeface} and apply to a {@link Spannable}.
     */
    public PacificoTypefaceSpan(Context context) {
        super(context, "fonts/Pacifico-Regular.ttf");
    }

    @Override
    public void apply(TextPaint tp) {
        super.apply(tp);
        tp.baselineShift -= (int) (tp.getTextSize()*0.45);
    }
}