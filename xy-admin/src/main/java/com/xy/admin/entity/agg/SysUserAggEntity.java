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
@TableName("sys_user_agg")
@Schema(name = "SysUserAggEntity", title = "用户聚合表（管理端）")
public class SysUserAggEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 6065699335875210752L;
    @Schema(description = "用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    @Schema(description = "用户账号")
    @TableField("username")
    private String username;
    @Schema(description = "用户邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "手机号码")
    @TableField("phone_number")
    private String phoneNumber;
}
