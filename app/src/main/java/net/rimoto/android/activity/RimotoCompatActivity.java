package net.rimoto.android.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.support.v8.renderscript.ScriptIntrinsicConvolve3x3;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.instabug.wrapper.support.activity.InstabugAppCompatActivity;

import net.rimoto.android.R;
import net.rimoto.android.views.TypefaceSpan;
import net.rimoto.core.utils.VpnUtils;
import net.rimoto.vpnlib.RimotoPolicy;
import net.rimoto.vpnlib.VpnManager;

import de.blinkt.openvpn.VpnProfile;
import de.blinkt.openvpn.core.ProfileManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * AppCompactActivity with rimoto enhancements
 */
public class RimotoCompatActivity extends InstabugAppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(this.getTitle());
    }

    @Override
    public void setTitle(CharSequence title) {
        SpannableString spannableString = new SpannableString(title+" ");
        spannableString.setSpan(new TypefaceSpan(this, "fonts/Pacifico-Regular.ttf"), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        super.setTitle(spannableString);
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    /**
     * ### Suspension addon ###
     */

    /** Manipulating image **/

    /**
     * Manipulate image with blur and brightness
     * @param bitmap image
     * @return Bitmap
     */
    private Bitmap getManipulatedBitmap(Bitmap bitmap, float blurness, float brightness) {
        RenderScript rs = RenderScript.create(this);

        // this will blur the bitmapOriginal with a radius of 8 and save it in bitmapOriginal
        Allocation input = Allocation.createFromBitmap(rs, bitmap); //use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
        Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        blurScript.setRadius(blurness);
        blurScript.setInput(input);
        blurScript.forEach(output);

        input = output;
        output = Allocation.createTyped(rs, input.getType());

        final ScriptIntrinsicConvolve3x3 script = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));
        script.setCoefficients(createBrightnessKernel(brightness));
        script.setInput(input);
        script.forEach(output);

        output.copyTo(bitmap);
        return bitmap;
    }

    /**
     * Brighting
     * @param brightness level
     * @return float[]
     */
    private float[] createBrightnessKernel(float brightness) {
        float kernelElement = 1.f / 9.f; //get average
        kernelElement += kernelElement * (brightness / 100.f); //add or subtract from the average to brighten or darken
        kernelElement = Math.max(Math.min(1,kernelElement),0); // normalize to max/min values

        float [] brightnessKernel = new float[9];

        for (int i = 0; i < 9; i++) {
            brightnessKernel[i] = kernelElement;
        }

        return brightnessKernel;
    }

    private boolean firstTime       = true;
    private View suspendedView      = null;
    private final float BRIGHTNESS  = 15;
    private final float BLURNESS    = 20;

    private ViewGroup getRootView() {
        return (ViewGroup) getWindow().getDecorView().getRootView();
    }
    private Bitmap getActivityBitmap() {
        View root = getRootView();
        root.setDrawingCacheEnabled(true);
        root.buildDrawingCache();
        return root.getDrawingCache();
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

        BitmapDrawable screenshot = new BitmapDrawable(getResources(), getManipulatedBitmap(getActivityBitmap(), BLURNESS, BRIGHTNESS));

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
