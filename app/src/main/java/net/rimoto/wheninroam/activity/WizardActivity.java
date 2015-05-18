package net.rimoto.wheninroam.activity;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.widget.TextView;

import net.rimoto.wheninroam.R;

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
        onClickRoamingDataBtn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(roamingSwitcherStatusView !=null) {
            onClickRoamingDataBtn();
        }
    }

    @Click(R.id.roamingDataBtn)
    protected void onClickRoamingDataBtn() {
        roamingSwitcherStatusView.setText(Boolean.toString(isDataRoamingEnabled()));
    }
    @Click(R.id.openSettingsBtn)
    protected void onClickOpenSettingsBtn() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
        startActivity(intent);
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