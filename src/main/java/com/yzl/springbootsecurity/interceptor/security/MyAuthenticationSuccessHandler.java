package com.yzl.springbootsecurity.interceptor.security;

import com.alibaba.fastjson.JSON;
import com.yzl.springbootsecurity.util.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 权限验证成功
 *
 * @author admin
 * @date 2020-06-28 15:13
 */

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        Result result = new Result();

        result.setCode(Result.RESULT_SUCCESS);
        result.setMessage("权限认证成功");

        PrintWriter printWriter = response.getWriter();

        printWriter.write(JSON.toJSONString(request));

        printWriter.flush();
        printWriter.close();
    }
}
