package net.rimoto.android.views;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.LruCache;

/**
 * Style a {@link Spannable} with a custom {@link Typeface}.
 *
 */
public class TypefaceSpan extends MetricAffectingSpan {
    /** An <code>LruCache</code> for previously loaded typefaces. */
    private static LruCache<String, Typeface> sTypefaceCache = new LruCache<>(12);

    private Typeface mTypeface;

    /**
     * Load the {@link Typeface} and apply to a {@link Spannable}.
     */
    public TypefaceSpan(Context context, String typefaceName) {
        mTypeface = sTypefaceCache.get(typefaceName);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), typefaceName);

            // Cache the loaded Typeface
            sTypefaceCache.put(typefaceName, mTypeface);
        }
    }

    @Override
    public void updateMeasureState(TextPaint tp) {
        apply(tp);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        apply(tp);
    }

    protected void apply(TextPaint tp) {
        tp.setTypeface(mTypeface);

        // Note: This flag is required for proper typeface rendering
        tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}