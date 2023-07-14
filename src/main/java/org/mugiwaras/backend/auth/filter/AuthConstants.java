package org.mugiwaras.backend.auth.filter;

public final class AuthConstants {
    public static final long EXPIRATION_TIME = (5 * 60 * 1000); // 60 son 60minutos, con 5 son 5min
    public static final String SECRET = "MyVerySecretKey";

    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTH_PARAM_NAME = "authtoken";
    public static final String TOKEN_PREFIX = "Bearer ";
}
