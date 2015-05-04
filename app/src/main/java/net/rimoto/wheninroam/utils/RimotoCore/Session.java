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

        prefsEditor.putString("RimotoToken[access_token]", token.access_token);
        prefsEditor.putLong("RimotoToken[expires_in]", token.expires_in);
        prefsEditor.putLong("RimotoToken[expires_at]", token.expires_at.getTime());
        prefsEditor.putString("RimotoToken[token_type]", token.token_type);

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
            if (new Date().before(currentAccessToken.expires_at)) {
                return currentAccessToken;
            } else {
                currentAccessToken = null;
                return null;
            }
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        long expires_at_pref = prefs.getLong("RimotoToken[expires_at]", -1);
        Date expires_at = (expires_at_pref == -1) ? null : new Date(expires_at_pref);
        String access_token = prefs.getString("RimotoToken[access_token]", null);

        if (expires_at == null || new Date().after(expires_at) || access_token == null) {
            return null;
        }

        AccessToken token = new AccessToken(
                access_token,
                prefs.getLong("RimotoToken[expires_in]", -1),
                prefs.getString("RimotoToken[token_type]", null)
        );
        token.expires_at = expires_at;
        currentAccessToken = token;

        return token;
    }
}
