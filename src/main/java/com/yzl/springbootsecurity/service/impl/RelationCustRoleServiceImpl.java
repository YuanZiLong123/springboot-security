package com.yzl.springbootsecurity.service.impl;

import com.yzl.springbootsecurity.entity.RelationCustRole;
import com.yzl.springbootsecurity.mapper.RelationCustRoleMapper;
import com.yzl.springbootsecurity.service.IRelationCustRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色的关联表 服务实现类
 * </p>
 *
 * @author yzl
 * @since 2020-06-30
 */
@Service
public class RelationCustRoleServiceImpl extends ServiceImpl<RelationCustRoleMapper, RelationCustRole> implements IRelationCustRoleService {

}
