package net.rimoto.core;

import net.rimoto.core.models.TokenInterface;

public interface RimotoCallback {
    /**
     * Will called as callback
     * @param token TokenInterface
     * @param error RimotoException
     */
    void done(TokenInterface token, RimotoException error);
}
