package net.rimoto.android.activity;

import com.instabug.wrapper.support.activity.InstabugActivity;

import net.rimoto.android.R;
import net.rimoto.android.fragment.MainFragment;
import net.rimoto.android.utils.AppPolicies;
import net.rimoto.android.utils.InstabugRimoto;
import net.rimoto.core.API;
import net.rimoto.core.Session;
import net.rimoto.core.models.Policy;
import net.rimoto.vpnlib.VpnManager;

import org.androidannotations.annotations.EActivity;
import org.parceler.Parcels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends InstabugActivity {
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
        AppPolicies.addAppPolicy(this, (policy, error) -> {
            if (error != null) {
                if (error.getMessage().equals("NO_SIM")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "You need a sim card in order to use Rimoto", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                } else {
                    error.printStackTrace();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "We have an issue with you connection.. please try again later.",
                            Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
            }
            getPolicies();
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
