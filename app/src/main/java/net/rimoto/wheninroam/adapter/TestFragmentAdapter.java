package net.rimoto.wheninroam.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

import net.rimoto.wheninroam.fragment.LoginFragment;
import net.rimoto.wheninroam.fragment.LoginFragment_;

/**
 * Created by almog on 14/05/15.
 */
public class TestFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    protected static final String[] TITLES = new String[] { "Hey", "Yo", "Ya", "Ani", "Shoel", };

    public TestFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        LoginFragment fragment = new LoginFragment_();
        fragment.setPosition(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return TestFragmentAdapter.TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TestFragmentAdapter.TITLES[position % TITLES.length];
    }

    @Override
    public int getIconResId(int index) {
        return 0;
    }

    @Override
    public float getPageWidth(int page) {
        return (float) (super.getPageWidth(page) * 1.5);
    }
}