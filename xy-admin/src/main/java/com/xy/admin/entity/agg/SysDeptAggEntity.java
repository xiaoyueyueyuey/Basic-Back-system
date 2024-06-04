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
@TableName("sys_dept_agg")
@Schema(description = "系统部门聚合对象")
public class SysDeptAggEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -169878337512741685L;
    @Schema(description = "部门id")
    @TableId(value = "dept_id",type = IdType.AUTO)
    private Long deptId;//部门id
    @Schema(description = "部门名称")
    @TableField("dept_name")
    private String deptName;//部门名称
    @Schema(description = "父部门id")
    @TableField("parent_id")
    private Long parentId;//父部门id
    @Schema(description = "子部门数量")
    @TableField("children_dept_count")
    private Long childrenDeptCount;//子部门数量
    @Schema(description = "部门人数")
    @TableField("dept_user_count")
    private Long deptUserCount;//部门分配给用户的数量
    @Schema(description = "部门状态")
    @TableField("status")
    private Integer status;//部门状态
}
