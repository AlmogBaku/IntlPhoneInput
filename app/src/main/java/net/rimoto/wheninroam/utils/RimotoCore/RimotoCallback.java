package net.rimoto.wheninroam.utils.RimotoCore;

import android.content.Context;

public interface RimotoCallback {
    void done(TokenInterface token, RimotoException error);
}
