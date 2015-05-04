package net.rimoto.wheninroam.utils.RimotoCore;

public class CodeToken implements TokenInterface {
    public String code;

    public CodeToken(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}