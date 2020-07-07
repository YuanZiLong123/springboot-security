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
 * 登录成功后的处理拦截器
 *  服务器直接跳转则使用HttpServletRequest 做重定向要跳转
 * 前后端分离  直接返回json
 * @author admin
 * @date 2020-06-28 15:13
 */

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

       // request.getRequestDispatcher("").forward(request, response);
        //response.sendRedirect("");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        Result result = new Result();

        result.setCode(Result.RESULT_SUCCESS);
        result.setMessage("登录成功");
        result.setData(authentication.getPrincipal());
        PrintWriter printWriter = response.getWriter();

        printWriter.write(JSON.toJSONString(result));

        printWriter.flush();
        printWriter.close();
    }
}
