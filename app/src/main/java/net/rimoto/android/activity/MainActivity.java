package net.rimoto.android.activity;

import android.content.Intent;

import com.instabug.library.Instabug;

import net.rimoto.android.R;
import net.rimoto.android.fragment.DebugFragment;
import net.rimoto.android.fragment.DebugFragment_;
import net.rimoto.android.fragment.MainFragment;
import net.rimoto.android.fragment.MainFragment_;
import net.rimoto.core.Session;
import net.rimoto.core.utils.VpnUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends RimotoCompatActivity {

    @AfterViews
    protected void mainFragment() {
        MainFragment mainFragment = new MainFragment_();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mainFragment)
                .commit();
    }

    @OptionsItem
    protected void action_debug() {
        DebugFragment debugFragment = new DebugFragment_();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, debugFragment).addToBackStack(null)
                .commit();
    }

    @OptionsItem
    protected void action_logout() {
        VpnUtils.stopVPN(this);
        Session.logout();
        Intent intent = new Intent(this, LoginActivity_.class);
        startActivity(intent);
        this.finish();
    }

    @OptionsItem
    protected void action_disconnect() {
        VpnUtils.stopVPN(this,()-> {
            startActivity(getIntent());
            this.finish();
        });

    }

    @OptionsItem(R.id.action_reportbug)
    protected void reportBug() {
        Instabug.getInstance().invoke();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}