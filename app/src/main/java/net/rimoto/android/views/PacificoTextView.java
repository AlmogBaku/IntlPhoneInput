package net.rimoto.android.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
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
        this.setTypeface(Typeface.createFromAsset(this.getContext().getAssets(), "fonts/Pacifico.ttf"));
    }
}
