package net.rimoto.wheninroam.utils.RimotoCore;

import android.app.ProgressDialog;
import android.content.Context;

public class UiUtils {
    private static ProgressDialog spinner;
    public static void showSpinner(Context context) {
        spinner = new ProgressDialog(context);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        spinner.setMessage("Loading. Please wait...");
        spinner.setIndeterminate(true);
        spinner.setCanceledOnTouchOutside(false);
        spinner.show();
    }
    public static void hideSpinner() {
        spinner.dismiss();
        spinner = null;
    }
}
