package net.rimoto.android.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import net.rimoto.android.R;
import net.rimoto.android.activity.LoginActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;


@EFragment(R.layout.fragment_wizard_roaming)
public class WizardRoamingFragment extends Fragment {

    protected void openRoamingSettings() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
        startActivity(intent);
    }
}
