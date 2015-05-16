package net.rimoto.wheninroam.PagerIndicator;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.TextView;

import com.viewpagerindicator.PageIndicator;

public class SimpleTitleIndicator extends TextView implements PageIndicator {
    private ViewPager mViewPager;

    public SimpleTitleIndicator(Context context) { super(context); }

    public SimpleTitleIndicator(Context context, AttributeSet attrs) { super(context, attrs); }
    public SimpleTitleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setSimpleText(int position) {
        this.setText(this.mViewPager.getAdapter().getPageTitle(position));
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

        setSimpleText(view.getCurrentItem());
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
        this.setSimpleText(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
