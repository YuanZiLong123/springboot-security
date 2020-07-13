# Spring Security的接口



## 基本配置

```java
/**
 * @author admin
 * @date 2020-07-08 10:43
 */
@EnableWebSecurity //开启security
@EnableAutoConfiguration
/**
 * 开启Controller方法上的注解
 * @EnableGlobalMethodSecurity(securedEnabled=true) 开启@Secured 注解过滤权限
 *
 * @EnableGlobalMethodSecurity(jsr250Enabled=true)开启@RolesAllowed 注解过滤权限 
 *
 * @EnableGlobalMethodSecurity(prePostEnabled=true) 使用表达式时间方法级别的安全性         
 * 以下4个注解可用
 *
 *  @PreAuthorize 在方法调用之前, 基于表达式的计算结果来限制对方法的访问 @PreAuthorize("hasRole('admin')")
 *
 *  @PostAuthorize 允许方法调用, 但是如果表达式计算结果为false, 将抛出一个安全性异常
 *
 *  @PostFilter 允许方法调用, 但必须按照表达式来过滤方法的结果
 *
 *  @PreFilter 允许方法调用, 但必须在进入方法之前过滤输入值
 *
 */
@EnableGlobalMethodSecurity(prePostEnabled =true)
public class BaseSecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 认证配置  内存中配置
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        super.configure(auth);
    }


    /**
     *web 忽略某些请求配置
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * security配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }
}
```





## 流程拦截

  ![img](https://img-blog.csdn.net/20180318211512445?watermark/2/text/Ly9ibG9nLmNzZG4ubmV0L3UwMTM0MzU4OTM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70) 

我们可以看到在spring security内部其实是通过一个过滤器链来实现认证流程的UsernamePasswordAuthenticationFilter就是拦截我们通过表单提交接口提交的用户名和密码

如果是Basic提交的话，就会被BasicAuthenticationFilter拦截

最后的橙色FilterSecurityInterceptor是首先判断我们当前请求的url是否需要认证，如果需要认证，那么就看当前请求是否已经认证，是的话就放行到我们要访问的接口，否则重定向到认证页面。



自定义拦截器   添加验证码登录拦截

```java
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter{
    /**
     * 验证码校验失败处理器
     */
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private SessionStrategy sessionStrategy;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 必须是登录的post请求才能进行验证，其他的直接放行
        if(StringUtils.equals("/img-code/login", request.getRequestURI()) && StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
            log.info("request : {}", request.getRequestURI());
            try {
                // 1. 进行验证码的校验
                validate(new ServletWebRequest(request));
            } catch (AuthenticationException e) {
                // 2. 捕获步骤1中校验出现异常，交给失败处理类进行进行处理
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        // 3. 校验通过，就放行
        filterChain.doFilter(request, response);

    }

    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        // 1. 获取请求中的验证码
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "code");
        // 2. 校验空值情况
        if(StringUtils.isEmpty(codeInRequest)) {
            throw new MyException("验证码不能为空");
        }

        // 3. 获取服务器session池中的验证码
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request, SESSION_KEY);
        if(Objects.isNull(codeInSession)) {
            throw new MyException("验证码不存在");
        }

        // 4. 校验服务器session池中的验证码是否过期
        if(codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(request, SESSION_KEY);
            throw new MyException("验证码过期了");
        }

        // 5. 请求验证码校验
        if(!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new MyException("验证码不匹配");
        }

        // 6. 移除已完成校验的验证码
        sessionStrategy.removeAttribute(request, SESSION_KEY);
    }

}
```

向拦截器中添加自定义拦截

```java
@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /**
                *自定义验证码登录拦截器 
                * 在UsernamePasswordAuthenticationFilter 拦截器前添加
                * 添加自定义配置
                */
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
    }

```







## 认证





 ![img](https://mmbiz.qpic.cn/mmbiz_png/GvtDGKK4uYmzN0TbGT6GkzxdymHf6sdhIAalACXHe36EicFzNTVOpquAVvPLLnkHSgx8FFYib7yKc5ns5IDT2vgA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1) 



### AuthenticationProvider   

​	   

```java
public interface AuthenticationProvider {
	
    //authenticate 方法用来做验证，就是验证用户身份。
	Authentication authenticate(Authentication authentication)
			throws AuthenticationException;

	//判断当前的 AuthenticationProvider 是否支持对应的 Authentication。
	boolean supports(Class<?> authentication);
}
```

### Authentication

```java
public interface Authentication extends Principal, Serializable {
	
    //获取用户的权限
	Collection<? extends GrantedAuthority> getAuthorities();

	//获取用户凭证，一般来说就是密码。
	Object getCredentials();

	//获取用户携带的详细信息，可能是当前请求之类的东西
	Object getDetails();
	 
     //获取当前用户，可能是一个用户名，也可能是一个用户对象 
	Object getPrincipal();

	//用户是否认证成功。
	boolean isAuthenticated();

//设置用户是否认证成功。
	void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;
}
```

### AuthenticationManager

权限管理  添加权限认证

![img](C:\Users\admin\AppData\Local\YNote\data\qqB3BE1561F5F61E6DB6316DAA1409BB89\3bbbb420b0ab45d3968634a24c0aa9b5\clipboard.png)



自定义的认证  继承 DaoAuthenticationProvider

```java
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
```

添加配置

```java
 @Bean
    MyAuthenticationProvider myAuthenticationProvider() {
        MyAuthenticationProvider myAuthenticationProvider = new MyAuthenticationProvider();
        myAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        myAuthenticationProvider.setUserDetailsService(mySecurityService);
        return myAuthenticationProvider;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        ProviderManager manager = new ProviderManager(Arrays.asList(myAuthenticationProvider()));
        return manager;
    }
```



## 鉴权

授权管理，访问url时，会通过AbstractSecurityInterceptor拦截器拦截，其中会调用FilterInvocationSecurityMetadataSource的方法来获取被拦截url所需的全部权限，在调用授权管理器AccessDecisionManager，这个授权管理器会通过spring的全局缓存SecurityContextHolder获取用户的权限信息，还会获取被拦截的url和被拦截url所需的全部权限，然后根据所配的策略（有：一票决定，一票否定，少数服从多数等），如果权限足够，则返回，权限不够则报错并调用权限不足页面。




### AccessDecisionManager  权限策略管理 

```java
public interface AccessDecisionManager {
	
    //鉴权判断
	void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes) throws AccessDeniedException,
			InsufficientAuthenticationException;

	
	boolean supports(ConfigAttribute attribute);

	
	boolean supports(Class<?> clazz);
}
```



自定义实现接口，实现decide()方法可做鉴权控制

```java
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {
/**
     * @Author: Galen
     * @Description: 取当前用户的权限与这次请求的这个url需要的权限作对比，决定是否放行
     * auth 包含了当前的用户信息，包括拥有的权限,即之前UserDetailsService登录时候存储的用户对象
     * object 就是FilterInvocation对象，可以得到request等web资源。
     * configAttributes 是本次访问需要的权限。即上一步的 MyFilterInvocationSecurityMetadataSource 中查询核对得到的权限列表
     * @Date: 2019/3/27-17:18
     * @Param: [auth, object, cas]
     * @return: void
     **/

    @Override
    public void decide(Authentication auth, Object object, Collection<ConfigAttribute> cas) {
        Iterator<ConfigAttribute> iterator = cas.iterator();
        while (iterator.hasNext()) {
            if (auth == null) {
                throw new AccessDeniedException("当前访问没有权限");
            }
            ConfigAttribute ca = iterator.next();
            //当前请求需要的权限
            String needRole = ca.getAttribute();
            if ("ROLE_LOGIN".equals(needRole)) {
                if (auth instanceof AnonymousAuthenticationToken) {
                    throw new BadCredentialsException("未登录");
                } else{
                    return;
                }
            }
            //当前用户所具有的权限
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
```

配置项添加

