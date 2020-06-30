package com.yzl.springbootsecurity.interceptor;

import com.yzl.springbootsecurity.exception.SystemException;
import com.yzl.springbootsecurity.service.IAuthService;
import com.yzl.springbootsecurity.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author admin
 * @date 2020-06-24 16:51
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private IAuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = SessionUtils.getInstance().getToken(request);

        String url = request.getRequestURI();

        if (authService.checkRole(token,url )){
            return  true;
        }
        throw  new SystemException(4,"没有权限");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
