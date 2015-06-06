package net.rimoto.core;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.rimoto.core.models.AccessToken;

import java.util.Date;

/**
 * Managing the current session
 */
public class Session {

    /**
     * Current AccessToken
     */
    private static AccessToken sCurrentAccessToken = null;

    /**
     * Save AccessToken as current user
     * @param token AccessToken
     */
    public static void saveAccessToken(AccessToken token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RimotoCore.getsApplicationContext());
        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putString("RimotoToken[access_token]", token.getToken());
        prefsEditor.putLong("RimotoToken[expires_in]", token.getExpiresIn());
        prefsEditor.putLong("RimotoToken[expires_at]", token.getExpiresAt().getTime());
        prefsEditor.putString("RimotoToken[token_type]", token.getType());

        prefsEditor.apply();

        sCurrentAccessToken = token;
    }

    /**
     * Get current AccessToken if valid
     * @return AccessToken | null
     */
    public static AccessToken getCurrentAccessToken() {
        //Stored static token
        if (sCurrentAccessToken != null) {
            if (sCurrentAccessToken.isValid()) {
                return sCurrentAccessToken;
            } else {
                sCurrentAccessToken = null;
                return null;
            }
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RimotoCore.getsApplicationContext());

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
        token.setExpiresAt(expires_at);

        if(token.isValid()) {
            sCurrentAccessToken = token;
            return token;
        } else {
            return null;
        }
    }

    public static void logout() {
        clear();
    }

    public static void clear() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RimotoCore.getsApplicationContext());
        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.remove("RimotoToken[access_token]");
        prefsEditor.remove("RimotoToken[expires_in]");
        prefsEditor.remove("RimotoToken[expires_at]");
        prefsEditor.remove("RimotoToken[token_type]");

        prefsEditor.apply();
    }
}
