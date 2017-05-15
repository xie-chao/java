package com.calix.tools.handler;

import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;


public class CharsetHander extends Handler {

    @Override
    public void handle(String paramString, HttpServletRequest request, HttpServletResponse response, boolean[] paramArrayOfBoolean) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            next.handle(paramString, request, response, paramArrayOfBoolean);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
