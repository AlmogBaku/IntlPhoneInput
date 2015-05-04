package net.rimoto.wheninroam.utils.RimotoCore;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

/**
 * Managing the current session
 */
public class Session {

    /**
     * Current AccessToken
     */
    private static AccessToken currentAccessToken = null;

    /**
     * Save AccessToken as current user
     * @param context Context
     * @param token AccessToken
     */
    public static void saveAccessToken(Context context, AccessToken token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putString("RimotoToken[access_token]", token.getAccess_token());
        prefsEditor.putLong("RimotoToken[expires_in]", token.getExpires_in());
        prefsEditor.putLong("RimotoToken[expires_at]", token.getExpires_at().getTime());
        prefsEditor.putString("RimotoToken[token_type]", token.getToken_type());

        prefsEditor.apply();

        currentAccessToken = token;
    }

    /**
     * Get current AccessToken if valid
     * @param context Context
     * @return AccessToken | null
     */
    public static AccessToken getCurrentAccessToken(Context context) {
        //Stored static token
        if (currentAccessToken != null) {
            if (currentAccessToken.isValid()) {
                return currentAccessToken;
            } else {
                currentAccessToken = null;
                return null;
            }
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        long expires_at_pref = prefs.getLong("RimotoToken[expires_at]", -1);
        if(expires_at_pref==-1) {
            return null;
        }

        Date expires_at = new Date(expires_at_pref);
        String access_token = prefs.getString("RimotoToken[access_token]", null);

        AccessToken token = new AccessToken(
                access_token,
                prefs.getLong("RimotoToken[expires_in]", -1),
                prefs.getString("RimotoToken[token_type]", null)
        );
        token.setExpires_at(expires_at);

        if(token.isValid()) {
            currentAccessToken = token;
            return token;
        } else {
            return null;
        }
    }
}
