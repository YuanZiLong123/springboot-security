package com.yzl.springbootsecurity.interceptor.security;

import com.alibaba.fastjson.JSON;
import com.yzl.springbootsecurity.util.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author admin
 * @date 2020-06-28 15:12
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Result result = new Result();
        result.setCode(Result.RESULT_NO_AUTH);
        result.setMessage(e.getMessage()+"权限认证失败");

        out.write(JSON.toJSONString(result));
        out.flush();
        out.close();
    }
}
