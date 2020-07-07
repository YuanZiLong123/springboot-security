package com.yzl.springbootsecurity.interceptor.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzl.springbootsecurity.entity.Menu;
import com.yzl.springbootsecurity.enums.StatusEnum;
import com.yzl.springbootsecurity.service.IMenuService;
import com.yzl.springbootsecurity.service.IRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.swing.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 权限认证
 * @author admin
 * @date 2020-06-28 16:52
 */

@Component
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IRoleService roleService;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private static final Logger log = LoggerFactory.getLogger(MyFilterInvocationSecurityMetadataSource.class);

/**
     * @Author: Galen
     * @Description: 返回本次访问需要的权限，可以有多个权限
     * @Date: 2019/3/27-17:11
     * @Param: [o]
     * @return: java.util.Collection<org.springframework.security.access.ConfigAttribute>
     **/

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) {
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        //去数据库查询资源
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda()
                .eq(Menu::getMenuUrl,requestUrl )
        .eq(Menu::getMenuStatus, StatusEnum.EXIST.getId());

        Menu menu = menuService.getBaseMapper().selectOne(queryWrapper);
        if (Objects.nonNull(menu)){
            List<String> roleName = roleService.selectRoleNameByMenuId(menu.getMenuId());

            String[] values = (String[]) roleName.toArray();
            log.info("当前访问路径是{},这个url所需要的访问权限是{}", requestUrl, values);
            return SecurityConfig.createList(values);
        }
/*
*
         * @Author: Galen
         * @Description: 如果本方法返回null的话，意味着当前这个请求不需要任何角色就能访问
         * 此处做逻辑控制，如果没有匹配上的，返回一个默认具体权限，防止漏缺资源配置
         **/

        log.info("当前访问路径是{},这个url所需要的访问权限是{}", requestUrl, "ROLE_LOGIN");
        return SecurityConfig.createList("ROLE_LOGIN");
    }

/**
     * @Author: Galen
     * @Description: 此处方法如果做了实现，返回了定义的权限资源列表，
     * Spring Security会在启动时校验每个ConfigAttribute是否配置正确，
     * 如果不需要校验，这里实现方法，方法体直接返回null即可。
     * @Date: 2019/3/27-17:12
     * @Param: []
     * @return: java.util.Collection<org.springframework.security.access.ConfigAttribute>
     **/

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

/*
*
     * @Author: Galen
     * @Description: 方法返回类对象是否支持校验，
     * web项目一般使用FilterInvocation来判断，或者直接返回true
     * @Date: 2019/3/27-17:14
     * @Param: [aClass]
     * @return: boolean
     *
*/

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
