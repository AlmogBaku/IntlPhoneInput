package net.rimoto.core.models;

public class CodeToken implements TokenInterface {
    private String token;
    private String type = "code";

    /**
     * Code Token
     * @param code String
     */
    public CodeToken(String code) {
        this.token  = code;
    }

    /**
     * To string
     * @return String Token
     */
    @Override
    public String toString() {
        return token;
    }

    /**
     * Get token code
     * @return String tonken code
     */
    @Override
    public String getToken() {
        return null;
    }

    /**
     * Get token type
     * @return String
     */
    @Override
    public String getType() {
        return type;
    }
}