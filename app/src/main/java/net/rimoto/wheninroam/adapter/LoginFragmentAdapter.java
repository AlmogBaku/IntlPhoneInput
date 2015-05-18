package net.rimoto.wheninroam.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;
import net.rimoto.wheninroam.fragment.LoginFragment_;

public class LoginFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    protected static final String[] TITLES = new String[] { "Hey", "Yo", "Ya", "Ani", "Shoel", };
    protected LoginFragment_[] mFragments;

    public LoginFragmentAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new LoginFragment_[getCount()];
    }

    @Override
    public Fragment getItem(int position) {
        LoginFragment_ fragment = mFragments[position];
        if(fragment==null) {
            fragment = new LoginFragment_();
            fragment.setPosition(position);
            mFragments[position] = fragment;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return LoginFragmentAdapter.TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return LoginFragmentAdapter.TITLES[position % TITLES.length];
    }

    @Override
    public int getIconResId(int index) {
        return 0;
    }

//    @Override
//    public float getPageWidth(int page) {
//        return (float) (super.getPageWidth(page) * 1.5);
//    }
}