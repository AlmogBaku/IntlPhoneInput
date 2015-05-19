package net.rimoto.wheninroam.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import net.rimoto.core.Session;
import net.rimoto.wheninroam.R;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;
        if(Session.getCurrentAccessToken()==null) {
            intent = new Intent(this, LoginActivity_.class);
        } else {
            intent = new Intent(this, WizardActivity_.class);
        }
        startActivity(intent);
        finish();
    }
}
