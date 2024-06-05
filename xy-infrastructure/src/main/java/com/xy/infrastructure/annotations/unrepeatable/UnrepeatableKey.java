package com.xy.infrastructure.annotations.unrepeatable;

public class UnrepeatableKey {
    public static final String PREFIX = "Unrepeatable:";
    public static final String TOKEN_KEY = PREFIX + "Token:";
    public static final String LOGIN_CAPTCHA_KEY = PREFIX + "Login-Captcha:";

    public static final String TEST_KEY = PREFIX + "Test:";

    private UnrepeatableKey() {
    }
}
