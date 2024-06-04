package com.xy.admin.entity.agg;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(name = "SysRoleAggEntity", title = "角色聚合实体类")
@TableName("sys_role_agg")
public class SysRoleAggEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -1468335406916776669L;
    @Schema(description = "角色ID")
    @TableId(value = "role_id",type = IdType.AUTO)
    private Long roleId;
    @Schema(description = "角色名称")
    @TableId(value = "role_name")
    private String roleName;
    @Schema(description = "角色权限字符串")
    @TableId(value = "role_key")
    private String roleKey;
    @Schema(description = "角色分配给用户数")
    @TableId(value = "role_is_assign_to_user_count")
    private Long roleIsAssignToUserCount;
    @Schema(description = "角色状态（1正常 0停用）")
    private Integer status;
}
