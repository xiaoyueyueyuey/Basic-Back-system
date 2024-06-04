package com.xy.admin.entity.agg;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@TableName("sys_menu_agg")
@Schema(description = "系统菜单聚合表")
public class SysMenuAggEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 4419763698361812235L;
    @Schema(description = "菜单id")
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;//菜单id
    @Schema(description = "菜单名称")
    @TableField("menu_name")
    private String menuName;//菜单名称
    @Schema(description = "菜单类型")
    @TableField("menu_type")
    private Integer menuType;//菜单类型

    @Schema(description = "是否是按钮")
    @TableField("is_button")
    private Boolean isButton;//是否是按钮
    @Schema(description = "子菜单数量")
    @TableField("children_menu_count")
    private Integer childrenMenuCount;//子菜单数量
    @Schema(description = "菜单分配给角色的数量")
    @TableField("menu_assign_to_role_count")
    private Integer menuAssignToRoleCount;//菜单分配给角色的数量
    @Schema(description = "父菜单id")
    @TableField("parent_id")
    private Long parentId;//父菜单id
    @Schema(description = "父菜单类型")
    @TableField("parent_menu_type")
    private Integer parentMenuType;//父菜单类型
}
