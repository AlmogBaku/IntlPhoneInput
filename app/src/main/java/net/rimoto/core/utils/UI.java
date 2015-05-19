package net.rimoto.core.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class UI {
    private static ProgressDialog sSpinner;
    public static void showSpinner(Context context) {
        sSpinner = new ProgressDialog(context);
        sSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        sSpinner.setMessage("Loading. Please wait...");
        sSpinner.setIndeterminate(true);
        sSpinner.setCanceledOnTouchOutside(false);
        sSpinner.show();
    }
    public static void hideSpinner() {
        sSpinner.dismiss();
        sSpinner = null;
    }
}
