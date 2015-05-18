package net.rimoto.wheninroam.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.widget.TextView;

import net.rimoto.wheninroam.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_wizard)
public class WizardActivity extends Activity {

    @ViewById
    protected TextView textView2;

    @Click(R.id.button2)
    protected void onClickBtn() {
        textView2.setText(Boolean.toString(isDataRoamingEnabled()));
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