package com.example.metricmind.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class CookieUtils {

    public String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;

        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public void clearCookie(HttpServletResponse response,
            String cookieName,
            boolean secure) {

        Cookie cookie = new Cookie(cookieName, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    public void setSessionCookie(HttpServletResponse response,
            String cookieName,
            String value,
            int maxAgeSeconds,
            boolean secure,
            String sameSite) {

        String cookieValue = String.format(
                "%s=%s; Path=/; Max-Age=%d; HttpOnly; %s; SameSite=%s",
                cookieName,
                value,
                maxAgeSeconds,
                secure ? "Secure" : "",
                sameSite);

        response.addHeader("Set-Cookie", cookieValue);
    }
}
