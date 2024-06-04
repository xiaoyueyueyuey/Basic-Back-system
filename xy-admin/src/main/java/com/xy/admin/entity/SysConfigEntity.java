package com.xy.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xy.infrastructure.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 参数配置表
 * </p>
 *
 * @author valarchie
 * @since 2022-11-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_config")
@Schema(description = "SysConfigEntity对象参数配置表")
public class SysConfigEntity extends BaseEntity<SysConfigEntity> implements Serializable {


    @Serial
    private static final long serialVersionUID = -7772303262762912670L;
    @Schema(description = "参数主键")
    @TableId(value = "config_id", type = IdType.AUTO)
    private Integer configId;

    @Schema(description = "配置名称")
    @TableField("config_name")
    private String configName;

    @Schema(description = "配置键名")
    @TableField("config_key")
    private String configKey;

    @Schema(description = "可选的选项")
    @TableField("config_options")
    private String configOptions;

    @Schema(description = "配置值")
    @TableField("config_value")
    private String configValue;

    @Schema(description = "是否允许修改")
    @TableField("is_allow_change")
    private Boolean isAllowChange;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;


}
