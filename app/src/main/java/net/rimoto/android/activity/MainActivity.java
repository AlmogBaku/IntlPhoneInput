package net.rimoto.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.rimoto.android.R;
import net.rimoto.android.fragment.DebugFragment;
import net.rimoto.android.fragment.DebugFragment_;
import net.rimoto.android.fragment.MainFragment;
import net.rimoto.android.fragment.MainFragment_;
import net.rimoto.android.utils.SuspendImageManipulation;
import net.rimoto.core.API;
import net.rimoto.core.Session;
import net.rimoto.core.utils.UI.UiUtils;
import net.rimoto.core.utils.VpnUtils;
import net.rimoto.vpnlib.RimotoPolicy;
import net.rimoto.vpnlib.VpnManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ProfileManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends RimotoCompatActivity {
    private MenuItem mDisconnectMenuItem;
    private MenuItem mLogoutItem;

    @Override
    protected void onResume() {
        super.onResume();
        suspensionOnResume();

        conditionalMenuItems();
    }

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
        API.clearCache();
        Session.logout();
        VpnUtils.removeCurrentProfileUUID(this);
        Intent intent = new Intent(this, LoginActivity_.class);
        startActivity(intent);
        this.finish();
    }

    @OptionsItem
    protected void action_disconnect() {
        VpnUtils.stopVPN(this, () -> {
            startActivity(getIntent());
            this.finish();
            API.clearCache();
        });

    }

    @OptionsItem(R.id.action_help)
    protected void reportBug() {
        UiUtils.openHelp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mDisconnectMenuItem = menu.findItem(R.id.action_disconnect);
        mLogoutItem = menu.findItem(R.id.action_logout);
        conditionalMenuItems();
        return super.onCreateOptionsMenu(menu);
    }

    private void conditionalMenuItems() {
        if(mDisconnectMenuItem!=null) {
            if (VpnManager.isActive(this)) {
                mDisconnectMenuItem.setVisible(true);
                mLogoutItem.setVisible(false);
            } else {
                mDisconnectMenuItem.setVisible(false);
                mLogoutItem.setVisible(true);
            }
        }
    }

    /**
     * ### Suspension addon ###
     */

    private boolean firstTime       = true;
    private View suspendedView      = null;
    private final float BRIGHTNESS  = 15;
    private final float BLURNESS    = 20;

    private void suspensionOnResume() {
        VpnProfile profile = ProfileManager.get(this, VpnUtils.getCurrentProfileUUID(this));
        if(profile==null) {
            VpnUtils.importVPNConfig(this, new Callback<VpnProfile>() {
                @Override
                public void success(VpnProfile vpnProfile, Response response) {
                    checkSuspension(vpnProfile);
                }
                @Override
                public void failure(RetrofitError error) {
                    Toast toast = Toast.makeText(getApplicationContext(), "We have an issue with your connection.. please try again later.", Toast.LENGTH_LONG);
                    toast.show();
                    error.printStackTrace();
                    finish();
                }
            });
        } else {
            checkSuspension(profile);
        }
    }

    private void checkSuspension(VpnProfile profile) {
        boolean shouldConnect = RimotoPolicy.shouldConnect(VpnManager.getCurrentNetworkInfo(this), profile);
        if(!shouldConnect && VpnManager.isActive(this)) {
            setSuspended();
        } else {
            setActive();
        }
    }

    private ViewGroup getRootView() {
        return (ViewGroup) getWindow().getDecorView().getRootView();
    }
    public void setSuspended() {
        if(suspendedView != null) return;

        if(firstTime) {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(this::addSuspendedView, 1200);
        } else {
            addSuspendedView();
        }
    }
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void addSuspendedView() {
        if(suspendedView != null) return;

        firstTime = false;
        suspendedView = LayoutInflater.from(this).inflate(R.layout.suspended, getRootView(), false);
        suspendedView.setClickable(true);
        getRootView().addView(suspendedView);

        BitmapDrawable screenshot = new BitmapDrawable(getResources(), SuspendImageManipulation.getSuspendedImage(this, BLURNESS, BRIGHTNESS));

        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            suspendedView.setBackgroundDrawable(screenshot);
        } else {
            suspendedView.setBackground(screenshot);
        }
    }
    public void setActive() {
        if(suspendedView == null) return;

        getRootView().removeView(suspendedView);
        suspendedView = null;
    }
}