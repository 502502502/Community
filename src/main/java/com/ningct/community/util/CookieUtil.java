package com.ningct.community.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieUtil {
    public static String getValue(HttpServletRequest request, String key){
        if(key == null || request == null){
            throw new IllegalArgumentException("参数为空！");
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(key)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
