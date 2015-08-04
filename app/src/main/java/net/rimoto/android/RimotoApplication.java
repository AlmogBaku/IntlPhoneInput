package net.rimoto.android;

import android.app.Application;
import android.content.Context;

import com.instabug.library.Instabug;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import net.rimoto.android.utils.LeakSlackUploadService;
import net.rimoto.core.RimotoCore;
import net.rimoto.vpnlib.VpnFileLog;
import net.rimoto.vpnlib.VpnLog;

import org.androidannotations.annotations.EApplication;

import de.blinkt.openvpn.core.PRNGFixes;

@EApplication
public class RimotoApplication extends Application {
    private final String rimotoClientID = "1_56hxwnph188wkosoc8c8ccswskkwcwggc4k8o4k0gs4ksgsk08";
    private final String rimoto_authURI = "http://localhost/";
    private final String instabug_token = "cfad51965edc86000b61bd545c4d84d2";

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        PRNGFixes.apply();

        refWatcher = installLeakCanary();
        InitializeInstabug();

        RimotoCore.init(this, rimotoClientID, rimoto_authURI);
        RimotoCore.setAuthType("token");
//        VpnLog.getInstance().registerLogcat();
    }

    private void InitializeInstabug() {
        Instabug.initialize(this, instabug_token);
        Instabug.getInstance().attachFileAtLocation(VpnFileLog.logFile.getAbsolutePath());
        Instabug.getInstance().setEmailIsRequired(true);
    }


    public static RefWatcher getRefWatcher(Context context) {
        RimotoApplication application = (RimotoApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    protected RefWatcher installLeakCanary() {
        return RefWatcher.DISABLED;
//        return LeakCanary.install(this, LeakSlackUploadService.class, AndroidExcludedRefs.createAppDefaults().build());
    }
}
