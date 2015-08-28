package net.rimoto.android.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.support.v8.renderscript.ScriptIntrinsicConvolve3x3;
import android.view.View;
import android.view.ViewGroup;

public class SuspendImageManipulation {

    /** Manipulating image **/

    /**
     * Manipulate image with blur and brightness
     * @param bitmap image
     * @return Bitmap
     */
    public static Bitmap getManipulatedBitmap(Context context, Bitmap bitmap, float blurness, float brightness) {
        RenderScript rs = RenderScript.create(context);

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
    private static float[] createBrightnessKernel(float brightness) {
        float kernelElement = 1.f / 9.f; //get average
        kernelElement += kernelElement * (brightness / 100.f); //add or subtract from the average to brighten or darken
        kernelElement = Math.max(Math.min(1,kernelElement),0); // normalize to max/min values

        float [] brightnessKernel = new float[9];

        for (int i = 0; i < 9; i++) {
            brightnessKernel[i] = kernelElement;
        }

        return brightnessKernel;
    }

    public static Bitmap getActivityBitmap(Activity activity) {
        View root = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        root.setDrawingCacheEnabled(true);
        root.buildDrawingCache();
        return root.getDrawingCache();
    }

    public static Bitmap getSuspendedImage(Activity activity, float blurness, float brightness) {
        return getManipulatedBitmap(activity, getActivityBitmap(activity), blurness, brightness);
    }
}
