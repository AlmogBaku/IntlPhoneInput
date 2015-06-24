package net.rimoto.android.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;

import com.instabug.wrapper.support.activity.InstabugFragmentActivity;
import com.viewpagerindicator.CirclePageIndicator;

import net.rimoto.android.utils.InstabugRimoto;
import net.rimoto.android.views.SimpleTitleIndicator;
import net.rimoto.android.R;
import net.rimoto.android.views.IndicatorAggregator;

import net.rimoto.core.Login;
import net.rimoto.core.RimotoException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import net.rimoto.android.adapter.LoginFragmentAdapter;
import net.rimoto.core.utils.VpnUtils;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

@EActivity(R.layout.activity_login)
public class LoginActivity extends InstabugFragmentActivity {
    @ViewById(R.id.pager)
    protected AutoScrollViewPager mPager;

    @ViewById(R.id.titles)
    protected SimpleTitleIndicator mTitleIndicator;

    @ViewById(R.id.indicator)
    protected CirclePageIndicator mCircleIndicator;

    @AfterViews
    protected void pagerAdapter() {
        // Instantiate a ViewPager and a PagerAdapter.
        PagerAdapter mPagerAdapter = new LoginFragmentAdapter(getSupportFragmentManager(), getResources(), getPackageName());
        mPager.setAdapter(mPagerAdapter);

        IndicatorAggregator indicatorAggregator = new IndicatorAggregator();
        indicatorAggregator.addIndicator(mTitleIndicator);
        indicatorAggregator.addIndicator(mCircleIndicator);
        indicatorAggregator.setViewPager(mPager);

        mPager.setInterval(6500);
        mPager.setScrollDurationFactor(10);
        mPager.setBorderAnimation(false);
        mPager.startAutoScroll();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Click(R.id.login_btn)
    protected void loginClick() {
        try {
            Login.getInstance().auth(this, (token, error) -> {
                if(error==null) {
                    VpnUtils.importVPNConfig(this);
                    InstabugRimoto.attachUser();
                    startWizard();
                }
            });
        } catch (RimotoException e) {
            e.printStackTrace();
        }
    }
    private void startWizard() {
        Intent intent = new Intent(this, WizardActivity_.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPager = null;
        mTitleIndicator = null;
        mCircleIndicator = null;
        System.gc();
    }
}
