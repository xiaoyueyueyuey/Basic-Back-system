package com.xy.infrastructure.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * Entity基类
 *
 * @author valarchie
 */
@Data
public class BaseEntity<T>  {

    @Schema(description = "创建者ID")
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private Long creatorId;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "更新者ID")
    @TableField(value = "updater_id", fill = FieldFill.UPDATE, updateStrategy = FieldStrategy.NOT_NULL)
    private Long updaterId;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;
    /**
     * deleted字段请在数据库中 设置为tinyInt   并且非null   默认值为0
     */
    @Schema(description = "删除标志（0代表存在 1代表删除）")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

}
