package com.bizzman.security.jwt;

public enum JwtProperties {
    ISSUER,
    EXP_TIME,
    SECRET;

    public String getName() {
        switch (this) {
            case ISSUER:
                return "issuer";
            case SECRET:
                return "secret";
            case EXP_TIME:
                return "exp_time";
            default:
                return "UNKNOWN";
        }
    }
}
