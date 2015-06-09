package net.rimoto.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import net.rimoto.core.Session;
import net.rimoto.android.R;
import net.rimoto.vpnlib.VpnLog;
import net.rimoto.vpnlib.VpnManager;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {
    private static final int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VpnLog.getInstance().registerLogcat();

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            if(Session.getCurrentAccessToken()==null) {
                intent = new Intent(this, LoginActivity_.class);
            } else {
                if(VpnManager.isActive(this)) {
                    intent = new Intent(this, MainActivity_.class);
                } else {
                    intent = new Intent(this, WizardActivity_.class);
                }
            }

            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);
    }
}
