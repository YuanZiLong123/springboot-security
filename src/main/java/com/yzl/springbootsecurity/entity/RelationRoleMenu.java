package com.yzl.springbootsecurity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户和角色的关联表
 * </p>
 *
 * @author yzl
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RelationRoleMenu对象", description="用户和角色的关联表")
public class RelationRoleMenu extends Model<RelationRoleMenu> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户和角色的关联表id")
    @TableId(value = "relation_role_menu_id", type = IdType.AUTO)
    private Long relationRoleMenuId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "用户id")
    private Long menuId;


    @Override
    protected Serializable pkVal() {
        return this.relationRoleMenuId;
    }

}
