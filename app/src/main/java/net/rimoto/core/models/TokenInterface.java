package net.rimoto.core.models;

public interface TokenInterface {
    String token = null;
    String type = null;

    /**
     * Get token
     * @return String
     */
    String getToken();

    /**
     * Get token type
     * @return String
     */
    String getType();
}
