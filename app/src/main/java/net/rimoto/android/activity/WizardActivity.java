package net.rimoto.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.Toast;

import com.instabug.wrapper.support.activity.InstabugFragmentActivity;
import com.viewpagerindicator.CirclePageIndicator;

import net.rimoto.android.R;
import net.rimoto.android.adapter.WizardFragmentAdapter;
import net.rimoto.core.API;
import net.rimoto.core.models.Policy;
import net.rimoto.core.utils.UI;
import net.rimoto.core.utils.VpnUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


@EActivity(R.layout.activity_wizard)
public class WizardActivity extends InstabugFragmentActivity {
    @ViewById(R.id.pager)
    protected ViewPager mPager;

    @ViewById(R.id.indicator)
    protected CirclePageIndicator mCircleIndicator;

    @ViewById(R.id.connectBtn)
    protected Button mConnectBtn;

    PagerAdapter mPagerAdapter;


    @AfterViews
    protected void afterViews() {
        // Instantiate a ViewPager and a PagerAdapter.
        mPagerAdapter = new WizardFragmentAdapter(getSupportFragmentManager(), getContentResolver());
        mPager.setAdapter(mPagerAdapter);

        mCircleIndicator.setViewPager(mPager);

        addAppPolicy();
    }

    private void addAppPolicy() {
        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String home_operator = API.rimotoOperatorFormat(tel.getSimOperator());
        String roaming_operator = API.rimotoOperatorFormat(tel.getNetworkOperator());

        if(home_operator.equals("N/A") || roaming_operator.equals("N/A")) {
            Toast toast = Toast.makeText(WizardActivity.this, "You need a sim card in order to use rimoto", Toast.LENGTH_LONG);
            toast.show();
        }

        API.getInstance().addAppPolicy(home_operator, roaming_operator, new Callback<Policy>() {
            @Override
            public void success(Policy policy, Response response) {
                if(mConnectBtn != null) {
                    mConnectBtn.setEnabled(true);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPagerAdapter.notifyDataSetChanged();
    }

    @Click
    protected void connectBtn() {
        mConnectBtn.setEnabled(false);

        int count_pages = mPagerAdapter.getCount();
        int currPage = mPager.getCurrentItem();
        if((currPage+1)==count_pages) {
            connect();
        } else {
            mPager.setCurrentItem(currPage+1, true);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(this::connect, 2000);
        }
    }
    protected void connect() {
        VpnUtils.VpnConnectionSpinner(this, new VpnUtils.RimotoConnectStateCallback() {
            @Override
            public void connected() {
                startMainActivity();
            }
            @Override
            public void exiting() {
                Toast toast = Toast.makeText(WizardActivity.this, "There was some problem with connecting you :\\", Toast.LENGTH_LONG);
                toast.show();
                mConnectBtn.setEnabled(true);
            }

            @Override
            public void shouldntConnect() {
                Toast toast = Toast.makeText(WizardActivity.this, "Rimoto will connect to the cloud as soon you'll be abroad on wifi.", Toast.LENGTH_LONG);
                toast.show();
                startMainActivity();
            }
        });
        VpnUtils.startVPN(this);
    }
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity_.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPagerAdapter   = null;
        mPager          = null;
        mCircleIndicator= null;
        mConnectBtn     = null;
        UI.hideSpinner();
        System.gc();
    }
}