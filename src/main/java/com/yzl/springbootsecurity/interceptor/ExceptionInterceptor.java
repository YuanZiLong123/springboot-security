package com.yzl.springbootsecurity.interceptor;

import com.yzl.springbootsecurity.util.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since 2019/7/25
 */
@RestControllerAdvice
public class ExceptionInterceptor {


    @ExceptionHandler(Exception.class) //拦截所有异常
    public Result throwException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        e.printStackTrace();
        Result result = new Result();
        String message = "";
        if (e instanceof BindException) {
            BindException manve = (BindException) e;
            BindingResult bindingResult = manve.getBindingResult();
            String errorMesssage = "数据校验:<br/>";
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMesssage += fieldError.getDefaultMessage() + "<br/>";
            }
            message = errorMesssage;
            result.setCode(Result.RESULT_ERROR);
            result.setMessage(message);
        } else if (e instanceof LoginException) {
            result.setCode(Result.RESULT_LOGINOUT);
            result.setMessage("登录失效");
        } else {
            message = "服务器异常<br/>" + e.getLocalizedMessage();
            result.setCode(Result.RESULT_ERROR);
            result.setMessage(message);

        }

        return result;

    }

}
