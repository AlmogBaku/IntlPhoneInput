package net.rimoto.android.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
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

import de.blinkt.openvpn.core.VpnStatus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


@EActivity(R.layout.activity_wizard)
public class WizardActivity extends InstabugFragmentActivity {
    @ViewById(R.id.pager)
    protected ViewPager mPager;

    @ViewById(R.id.indicator)
    protected CirclePageIndicator mCircleIndicator;

    @ViewById
    protected Button connectBtn;

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
        String home_operator = rimotoOperatorFormat(tel.getSimOperator());
        String roaming_operator = rimotoOperatorFormat(tel.getNetworkOperator());

        API.getInstance().addAppPolicy(home_operator, roaming_operator, new Callback<Policy>() {
            @Override
            public void success(Policy policy, Response response) {
                connectBtn.setEnabled(true);
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
    private String rimotoOperatorFormat(String networkOperator) {
        int mcc = Integer.parseInt(networkOperator.substring(0, 3));
        int mnc = Integer.parseInt(networkOperator.substring(3));
        return mcc + "/" + mnc;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO fix this
        mPagerAdapter.notifyDataSetChanged();
    }


    private class RimotoStateListener implements VpnStatus.StateListener {
        @Override
        public void updateState(String state, String logmessage, int localizedResId, VpnStatus.ConnectionStatus level) {
            Log.d("vp", state);
            if(state.equals("CONNECTED")) {
                UI.hideSpinner();
                startMainActivity();
                this.finish();
            } else if(state.equals("EXITING")) {
                UI.hideSpinner();
                Toast toast = Toast.makeText(WizardActivity.this, "There was some problem with connecting you :\\", Toast.LENGTH_LONG);
                toast.show();
                this.finish();
            }
        }

        private void finish() {
            VpnStatus.removeStateListener(this);
            mStateListener=null;
        }
    }

    private RimotoStateListener mStateListener = new RimotoStateListener();

    @Click
    protected void connectBtn() {
        connectBtn.setEnabled(false);
        UI.showSpinner(this, "Connecting to rimoto's cloud..");
        VpnUtils.startVPN(this);
        VpnStatus.addStateListener(mStateListener);
    }
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity_.class);
        startActivity(intent);
        this.finish();
    }
}