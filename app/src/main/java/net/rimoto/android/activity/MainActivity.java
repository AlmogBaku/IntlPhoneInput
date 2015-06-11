package net.rimoto.android.activity;

import android.content.Intent;

import net.rimoto.android.R;
import net.rimoto.core.Session;
import net.rimoto.core.utils.VpnUtils;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends RimotoCompatActivity {
    @OptionsItem(R.id.action_logout)
    protected void action_logout() {
        VpnUtils.stopVPN(this);
        Session.logout();
        Intent intent = new Intent(this, LoginActivity_.class);
        startActivity(intent);
        this.finish();
    }

    @OptionsItem(R.id.action_disconnect)
    protected void disconnect() {
        VpnUtils.stopVPN(this);
        Intent intent = new Intent(this, WizardActivity_.class);
        startActivity(intent);
        this.finish();
    }

    @OptionsItem(R.id.action_logs)
    protected void sendLogs() {
        VpnUtils.sendLogs(this);
    }
}