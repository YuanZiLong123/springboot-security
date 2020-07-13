package com.yzl.springbootsecurity.interceptor.security;

import com.alibaba.fastjson.JSON;
import com.yzl.springbootsecurity.util.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author admin
 * @date 2020-07-13 10:47
 */
public class MyTokenFilter extends OncePerRequestFilter {


    private final RedisTemplate<String,String> redisTemplate;

    @Autowired
    public MyTokenFilter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String rememberMe = request.getParameter(" remember-me");

        System.out.println(rememberMe);

        if(StringUtils.equals("/img-code/login", request.getRequestURI())) {
            filterChain.doFilter(request,response );

        }


        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        PrintWriter printWriter = response.getWriter();


        String token = request.getHeader("token");

        if (StringUtils.isBlank(token)){
            Result result = new Result();
            result.setMessage("token不能为空");
            result.setCode(Result.RESULT_ERROR);
            printWriter.write(JSON.toJSONString(result));
            return;
        }else {
            String userId = redisTemplate.opsForValue().get(token);

            if (StringUtils.isBlank(userId)){
                Result result = new Result();
                result.setMessage("登录失效");
                result.setCode(Result.RESULT_LOGINOUT);
                printWriter.write(JSON.toJSONString(result));
                return;
            }else {
                redisTemplate.expire(token,30 , TimeUnit.MINUTES);
            }
        }

        filterChain.doFilter(request,response );
    }
}
