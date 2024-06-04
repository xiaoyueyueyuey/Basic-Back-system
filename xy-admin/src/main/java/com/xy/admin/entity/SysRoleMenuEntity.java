package com.xy.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 角色和菜单关联表
 * </p>
 *
 * @author valarchie
 * @since 2022-10-02
 */
@Data
@TableName("sys_role_menu")
@Schema(description = "SysRoleMenuXEntity对象角色和菜单关联表")
public class SysRoleMenuEntity implements Serializable {


    @Serial
    private static final long serialVersionUID = -4971398604666169582L;
    @Schema(description = "角色ID")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    @Schema(description = "菜单ID")
    @TableField("menu_id")
    private Long menuId;




}
