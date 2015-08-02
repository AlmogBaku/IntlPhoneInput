package net.rimoto.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import net.rimoto.android.fragment.MainFragment;
import net.rimoto.android.utils.InstabugRimoto;
import net.rimoto.core.API;
import net.rimoto.core.Session;
import net.rimoto.android.R;
import net.rimoto.core.models.Policy;
import net.rimoto.vpnlib.VpnLog;
import net.rimoto.vpnlib.VpnManager;

import org.androidannotations.annotations.EActivity;
import org.parceler.Parcels;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {
    private static final int SPLASH_TIME_OUT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Session.getCurrentAccessToken()==null) {
            startLoginActivity();
        } else {
            InstabugRimoto.attachUser(this);

            if(VpnManager.isActive(this)) {
                getPolicies();
            } else {
                addAppPolicy();
            }
        }
    }

    /**
     * Add app policy, then `getPolicies()`
     */
    private void addAppPolicy() {
        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String home_operator = API.rimotoOperatorFormat(tel.getSimOperator());
        String visited_operator = API.rimotoOperatorFormat(tel.getNetworkOperator());

        if(home_operator.equals("N/A") || visited_operator.equals("N/A")) {
            Toast toast = Toast.makeText(getApplicationContext(), "You need a sim card in order to use Rimoto", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

        API.getInstance().addAppPolicy(home_operator, visited_operator, new Callback<Policy>() {
            @Override
            public void success(Policy policy, Response response) {
                getPolicies();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(), "We have an issue with you connection.. please try again later.", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
        });
    }

    private void getPolicies() {
        API.getInstance().getPolicies(new Callback<List<Policy>>() {
            public void success(List<Policy> policies, Response response) {
                Intent intent = new Intent(SplashActivity.this, MainActivity_.class);
                intent.putExtra(MainFragment.EXTRA_POLICIES, Parcels.wrap(policies));
                startActivity(intent);
                finish();
            }

            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(), "We have an issue with you connection.. please try again later.", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
        });
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity_.class);
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);
    }
}
