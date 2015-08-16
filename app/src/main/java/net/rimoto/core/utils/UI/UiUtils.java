package net.rimoto.core.utils.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import net.rimoto.core.utils.UI.fragment.HelpDialogFaq;

public class UiUtils {
    private static ProgressDialog sSpinner;
    public static void showSpinner(Context context) {
        showSpinner(context, "Loading. Please wait...");
    }
    public static void showSpinner(Context context, String message) {
        if(context==null) {
            return;
        }
        sSpinner = new ProgressDialog(context);
        sSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        sSpinner.setMessage(message);
        sSpinner.setIndeterminate(true);
        sSpinner.setCanceledOnTouchOutside(false);
        sSpinner.show();
    }
    public static void setNonCanaclableSpinner() {
        sSpinner.setCancelable(false);
    }
    public static void hideSpinner() {
        if(sSpinner == null) return;
        sSpinner.dismiss();
        sSpinner = null;
    }

    public static void openHelp(FragmentActivity activityContext) {
        FragmentTransaction ft = activityContext.getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);

        // Create and show the dialog.
        HelpDialogFaq helpDialog = new HelpDialogFaq();
        helpDialog.show(ft, "dialog");
    }
}
