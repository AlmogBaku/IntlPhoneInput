package net.rimoto.core;

import net.rimoto.core.models.TokenInterface;

public interface RimotoCallback {
    void done(TokenInterface token, RimotoException error);
}
