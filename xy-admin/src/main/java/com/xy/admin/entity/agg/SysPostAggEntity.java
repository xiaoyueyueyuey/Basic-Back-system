package com.xy.admin.entity.agg;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@TableName("sys_post_agg")
@Data
@Schema(description = "岗位聚合对象 岗位聚合表")
public class SysPostAggEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -6491453998480597694L;

    @Schema(description = "岗位ID")
    @TableId(value = "post_id", type = IdType.AUTO)
    private Long postId;

    @Schema(description = "岗位编码")
    @TableField("post_code")
    private String postCode;

    @Schema(description = "岗位名称")
    @TableField("post_name")
    private String postName;


    @Schema(description = "岗位关联的用户数量")
    @TableField("post_is_assigned_count")
    private Integer postIsAssignedCount;


}
