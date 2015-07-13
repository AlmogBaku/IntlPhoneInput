package net.rimoto.android.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;

import com.instabug.wrapper.support.activity.InstabugAppCompatActivity;

import net.rimoto.android.R;
import net.rimoto.android.views.TypefaceSpan;
import net.rimoto.core.utils.VpnUtils;
import net.rimoto.vpnlib.RimotoPolicy;
import net.rimoto.vpnlib.VpnManager;

import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ProfileManager;

/**
 * AppCompactActivity with rimoto enhancements
 */
public class RimotoCompatActivity extends InstabugAppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRimotoTitle(this.getTitle());
    }

    protected void setRimotoTitle(CharSequence title) {
        SpannableString spannableString = new SpannableString(title);
        spannableString.setSpan(new TypefaceSpan(this, "fonts/Pacifico-Old.ttf"), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        setTitle(spannableString);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Resources resources= getResources();

        VpnProfile profile = ProfileManager.get(this, VpnUtils.getCurrentProfileUUID(this));
        if(RimotoPolicy.shouldConnect(VpnManager.getCurrentNetworkInfo(this), profile)) {
            setRimotoTitle(resources.getString(R.string.main_activity_title_on));
        } else {
            setRimotoTitle(resources.getString(R.string.main_activity_title_off));
        }
    }
}
