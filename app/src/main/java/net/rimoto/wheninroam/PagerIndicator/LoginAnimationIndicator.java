package net.rimoto.wheninroam.PagerIndicator;

import android.os.Handler;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.PageIndicator;

import net.rimoto.wheninroam.adapter.LoginFragmentAdapter;
import net.rimoto.wheninroam.fragment.LoginFragment;

public class LoginAnimationIndicator implements PageIndicator {
    private ViewPager mViewPager;

    private void doAnimations(int position) {
        LoginFragmentAdapter loginFragmentAdapter = (LoginFragmentAdapter) mViewPager.getAdapter();
        LoginFragment loginFragment = (LoginFragment) loginFragmentAdapter.getItem(position);
//        loginFragment.startAnimations();
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        if (view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        mViewPager.setOnPageChangeListener(this);

        Handler handler = new Handler();
        handler.postDelayed(()->doAnimations(view.getCurrentItem()), 1000);
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {}

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {}

    @Override
    public void notifyDataSetChanged() {}

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        this.doAnimations(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
