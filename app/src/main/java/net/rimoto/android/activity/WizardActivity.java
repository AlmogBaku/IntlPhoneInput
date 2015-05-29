package net.rimoto.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.widget.TextView;

import net.rimoto.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_wizard)
public class WizardActivity extends Activity {
    @ViewById
    protected TextView roamingSwitcherStatusView;

    @AfterViews
    protected void afterViews() {
        roamingDataBtn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(roamingSwitcherStatusView !=null) {
            roamingDataBtn();
        }
    }

    @Click
    protected void roamingDataBtn() {
        roamingSwitcherStatusView.setText(Boolean.toString(isDataRoamingEnabled()));
    }
    @Click
    protected void openSettingsBtn() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
        startActivity(intent);
    }

    @Click
    protected void gotoLoginBtn() {
        Intent intent = new Intent(this, LoginActivity_.class);
        startActivity(intent);
        finish();
    }

    protected boolean isDataRoamingEnabled() {
        try {
            // return true or false if data roaming is enabled or not
            return Settings.Global.getInt(this.getContentResolver(), Settings.Global.DATA_ROAMING) == 1;
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }
    }
}