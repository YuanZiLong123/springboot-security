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
@ApiModel(value="RelationCustRole对象", description="用户和角色的关联表")
public class RelationCustRole extends Model<RelationCustRole> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户和角色的关联表id")
    @TableId(value = "relation_cust_role_id", type = IdType.AUTO)
    private Long relationCustRoleId;

    @ApiModelProperty(value = "用户id")
    private Long custId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;


    @Override
    protected Serializable pkVal() {
        return this.relationCustRoleId;
    }

}
