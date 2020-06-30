package com.yzl.springbootsecurity.service.impl;

import com.yzl.springbootsecurity.entity.Menu;
import com.yzl.springbootsecurity.mapper.MenuMapper;
import com.yzl.springbootsecurity.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yzl
 * @since 2020-06-30
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}
