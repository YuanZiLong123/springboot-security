package com.yzl.springbootsecurity.interceptor.security;

import com.yzl.springbootsecurity.exception.MyException;
import com.yzl.springbootsecurity.vo.ImageCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.Objects;

import static com.yzl.springbootsecurity.config.constants.MyConstants.SESSION_KEY;

/**
 * 自定义的认证  只在登录时使用   避免拦截器每次都进行拦截
 * @author admin
 *
 * @date 2020-07-07 17:13
 */
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private SessionStrategy sessionStrategy;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        RequestAttributes requestAttributes =RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = ((ServletRequestAttributes) requestAttributes).getRequest();
        String code = req.getParameter("code");




        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(requestAttributes, SESSION_KEY);
        // 2. 校验空值情况
        if(StringUtils.isEmpty(code)) {
            throw new MyException("验证码不能为空");
        }

        // 3. 获取服务器session池中的验证码
        if(Objects.isNull(codeInSession)) {
            throw new MyException("验证码不存在");
        }

        // 4. 校验服务器session池中的验证码是否过期
        if(codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(requestAttributes, SESSION_KEY);
            throw new MyException("验证码过期了");
        }

        // 5. 请求验证码校验
        if(!StringUtils.equals(codeInSession.getCode(), code)) {
            throw new MyException("验证码不匹配");
        }

        // 6. 移除已完成校验的验证码
        sessionStrategy.removeAttribute(requestAttributes, SESSION_KEY);


        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
