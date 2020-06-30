package com.yzl.springbootsecurity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzl.springbootsecurity.entity.RelationCustRole;
import com.yzl.springbootsecurity.mapper.RelationCustRoleMapper;
import com.yzl.springbootsecurity.mapper.RoleMapper;
import com.yzl.springbootsecurity.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author admin
 * @date 2020-06-23 16:55
 */
@Service
public class AuthServiceImpl implements IAuthService {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Autowired
    private RelationCustRoleMapper relationCustRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 判断是否有权限
     *
     * @param token
     * @param role
     */
    @Override
    public boolean checkRole(String token, String role) {
        String id = redisTemplate.opsForValue().get(token);

        QueryWrapper<RelationCustRole> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda()
                .select(RelationCustRole::getRoleId)
                .eq(RelationCustRole::getCustId, Integer.valueOf(id));
        List<Object> roleIds = relationCustRoleMapper.selectObjs(queryWrapper);


        return true;
    }

}
