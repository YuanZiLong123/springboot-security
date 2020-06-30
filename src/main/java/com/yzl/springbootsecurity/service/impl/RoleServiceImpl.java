package com.yzl.springbootsecurity.service.impl;

import com.yzl.springbootsecurity.entity.Role;
import com.yzl.springbootsecurity.mapper.RoleMapper;
import com.yzl.springbootsecurity.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色信息 服务实现类
 * </p>
 *
 * @author yzl
 * @since 2020-06-30
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Override
    public List<String> selectRoleNameByMenuId(Long menuId) {
        return baseMapper.selectRoleNameByMenuId(menuId);
    }
}
