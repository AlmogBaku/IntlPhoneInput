package net.rimoto.core.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class UI {
    private static ProgressDialog sSpinner;
    public static void showSpinner(Context context) {
        showSpinner(context, "Loading. Please wait...");
    }
    public static void showSpinner(Context context, String message) {
        sSpinner = new ProgressDialog(context);
        sSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        sSpinner.setMessage(message);
        sSpinner.setIndeterminate(true);
        sSpinner.setCanceledOnTouchOutside(false);
        sSpinner.show();
    }
    public static void hideSpinner() {
        if(sSpinner == null) return;
        sSpinner.dismiss();
        sSpinner = null;
    }
}
