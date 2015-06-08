package net.rimoto.android.activity;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;

import com.instabug.wrapper.support.activity.InstabugAppCompatActivity;

import net.rimoto.android.views.TypefaceSpan;

/**
 * AppCompactActivity with rimoto enhancements
 */
public class RimotoCompatActivity extends InstabugAppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SpannableString spannableString = new SpannableString(this.getTitle());
        spannableString.setSpan(new TypefaceSpan(this, "fonts/Pacifico-Old.ttf"), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        setTitle(spannableString);
    }
}
