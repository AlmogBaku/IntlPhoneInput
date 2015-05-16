package net.rimoto.wheninroam.PagerIndicator;

import android.support.v4.view.ViewPager;
import com.viewpagerindicator.PageIndicator;
import java.util.ArrayList;

/**
 * Indicator Aggregator
 */
public class IndicatorAggregator implements PageIndicator {
    private ArrayList<PageIndicator> indicators = new ArrayList<>();

    /**
     * Add indicator
     * @param indicator PageIndicator
     */
    public void addIndicator(PageIndicator indicator) {
        indicators.add(indicator);
    }

    /**
     * Remove indicator
     * @param indicator PageIndicator
     */
    public void removeIndicator(PageIndicator indicator) {
        indicators.remove(indicator);
    }

    private ViewPager mViewPager;

    /**
     * Set ViewPager
     * @param view ViewPager
     */
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

        for(PageIndicator indicator: indicators) {
            indicator.setViewPager(view);
        }

        mViewPager.setOnPageChangeListener(this);
    }

    /**
     * Set ViewPager
     * @param view ViewPager
     * @param initialPosition int
     */
    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    private int mCurrentPage;

    /**
     * Set current item
     * @param item int of position
     */
    @Override
    public void setCurrentItem(int item) {
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;

        for(PageIndicator indicator: indicators) {
            indicator.setCurrentItem(item);
        }
    }

    /**
     * Get current item
     * @return int of position
     */
    public int getCurrentPage() {
        return mCurrentPage;
    }

    /**
     * Call the indicators
     */

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        for(PageIndicator indicator: indicators) {
            indicator.setOnPageChangeListener(listener);
        }
    }
    @Override
    public void notifyDataSetChanged() {
        for(PageIndicator indicator: indicators) {
            indicator.notifyDataSetChanged();
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for(PageIndicator indicator: indicators) {
            indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }
    @Override
    public void onPageSelected(int position) {
        for(PageIndicator indicator: indicators) {
            indicator.onPageSelected(position);
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {
        for(PageIndicator indicator: indicators) {
            indicator.onPageScrollStateChanged(state);
        }
    }
}
