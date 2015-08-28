package net.rimoto.android.activity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;

import com.instabug.wrapper.support.activity.InstabugAppCompatActivity;
import net.rimoto.android.views.PacificoTypefaceSpan;

/**
 * AppCompactActivity with rimoto enhancements
 */
public class RimotoCompatActivity extends InstabugAppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(this.getTitle());
    }

    @Override
    public void setTitle(CharSequence title) {
        SpannableString spannableString = new SpannableString(title+" ");
        spannableString.setSpan(new PacificoTypefaceSpan(this), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        super.setTitle(spannableString);
    }
}
