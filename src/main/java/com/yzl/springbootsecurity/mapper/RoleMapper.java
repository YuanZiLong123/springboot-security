package com.yzl.springbootsecurity.mapper;

import com.yzl.springbootsecurity.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色信息 Mapper 接口
 * </p>
 *
 * @author yzl
 * @since 2020-06-30
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleNameByMenuId(Long menuId);

    List<String> selectRoleNameByUserId(Long userId);
}
