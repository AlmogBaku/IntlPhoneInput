package net.rimoto.android.adapter;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.rimoto.android.fragment.WizardRoamingFragment_;
import net.rimoto.android.fragment.WizardVpnFragment_;

public class WizardFragmentAdapter extends FragmentStatePagerAdapter {
    private ContentResolver contentResolver;

    public WizardFragmentAdapter(FragmentManager fm, ContentResolver contentResolver) {
        super(fm);
        this.contentResolver = contentResolver;
    }

    @Override
    public Fragment getItem(int position) {
        if(getCount()==1) {
            return new WizardVpnFragment_();
        } else if (position==0) {
            return new WizardRoamingFragment_();
        } else {
            return new WizardVpnFragment_();
        }
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        if(isDataRoamingEnabled()) return 1;
        else return 2;
    }

    public boolean isDataRoamingEnabled() {
        return dataRoamingSwitcherStatus()==1;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected int dataRoamingSwitcherStatus() {
        if(Build.BRAND.toLowerCase().trim().equals("oneplus")) {
            return -1;
        }
        try {
            return Settings.Global.getInt(contentResolver, Settings.Global.DATA_ROAMING, 0);
        } catch (NoClassDefFoundError e) {
            return -1;
        }
    }
}