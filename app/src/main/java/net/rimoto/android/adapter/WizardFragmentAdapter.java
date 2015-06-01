package net.rimoto.android.adapter;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.rimoto.android.fragment.WizardRoamingFragment_;
import net.rimoto.android.fragment.WizardVpnFragment_;

public class WizardFragmentAdapter extends FragmentPagerAdapter {
    private ContentResolver contentResolver;

    public WizardFragmentAdapter(FragmentManager fm, ContentResolver contentResolver) {
        super(fm);
        this.contentResolver = contentResolver;
    }

    @Override
    public Fragment getItem(int position) {
        if(getCount()==1 || position==1) {
            return new WizardVpnFragment_();
        } else {
            return new WizardRoamingFragment_();
        }
    }

    @Override
    public int getCount() {
        if(isDataRoamingEnabled()) return 1;
        else return 2;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected boolean isDataRoamingEnabled() {
        try {
            return Settings.Global.getInt(contentResolver, Settings.Global.DATA_ROAMING) == 1; // return true or false if data roaming is enabled or not
        } catch (Settings.SettingNotFoundException e) {
            return false;
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }
}