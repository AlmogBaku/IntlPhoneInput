package net.rimoto.android.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.CirclePageIndicator;

import net.rimoto.android.R;
import net.rimoto.android.adapter.WizardFragmentAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_wizard)
public class WizardActivity extends FragmentActivity {
    @ViewById(R.id.pager)
    protected ViewPager mPager;

    @ViewById(R.id.indicator)
    protected CirclePageIndicator mCircleIndicator;

    PagerAdapter mPagerAdapter;

    @AfterViews
    protected void afterViews() {
        // Instantiate a ViewPager and a PagerAdapter.
        mPagerAdapter = new WizardFragmentAdapter(getSupportFragmentManager(), getContentResolver());
        mPager.setAdapter(mPagerAdapter);

        mCircleIndicator`.setViewPager(mPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO fix this
        mPagerAdapter.notifyDataSetChanged();
    }
}