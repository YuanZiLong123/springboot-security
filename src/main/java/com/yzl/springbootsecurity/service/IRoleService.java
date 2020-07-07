package com.yzl.springbootsecurity.service;

import com.yzl.springbootsecurity.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色信息 服务类
 * </p>
 *
 * @author yzl
 * @since 2020-06-30
 */
public interface IRoleService extends IService<Role> {

    List<String> selectRoleNameByMenuId(Long menuId);

    List<String> selectRoleNameByUserId(Long userId);

}
