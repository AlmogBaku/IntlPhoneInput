package net.rimoto.android.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.viewpagerindicator.CirclePageIndicator;

import net.rimoto.android.views.SimpleTitleIndicator;
import net.rimoto.android.R;
import net.rimoto.android.views.IndicatorAggregator;
import net.rimoto.core.API;
import net.rimoto.core.Login;
import net.rimoto.core.RimotoException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import net.rimoto.android.adapter.LoginFragmentAdapter;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

@EActivity(R.layout.activity_login)
public class LoginActivity extends FragmentActivity {
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

        mPager.setInterval(7500);
        mPager.setScrollDurationFactor(27);
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
                    getVPNConfig();
                    startWizard();
                }
            });
        } catch (RimotoException e) {
            e.printStackTrace();
        }
    }

    private void getVPNConfig() {
        API.getInstance().getOvpn(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String ovpn = new String(((TypedByteArray) response.getBody()).getBytes());
                Log.d("ovpn", ovpn);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ovpn-err", error.getMessage());
            }
        });
    }
    private void startWizard() {
        Intent intent = new Intent(this, WizardActivity_.class);
        startActivity(intent);
        finish();
    }
}
