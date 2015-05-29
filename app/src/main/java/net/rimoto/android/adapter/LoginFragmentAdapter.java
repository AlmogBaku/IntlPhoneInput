package net.rimoto.android.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;

import net.rimoto.android.fragment.LoginFragment_;

public class LoginFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    private String packageName;
    private Resources resources;

    public LoginFragmentAdapter(FragmentManager fm, Resources resources, String package_name) {
        super(fm);
        this.resources = resources;
        packageName = package_name;
    }

    @Override
    public Fragment getItem(int position) {
        LoginFragment_ fragment = new LoginFragment_();
        fragment.setPosition(position);

        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int id = resources.getIdentifier("login_title_"+(position % getCount()), "string", packageName);
        return resources.getString(id);
    }

    @Override
    public int getIconResId(int index) {
        return 0;
    }
}