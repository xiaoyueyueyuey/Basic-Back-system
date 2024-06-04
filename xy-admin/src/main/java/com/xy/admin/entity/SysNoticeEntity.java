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
 * 通知公告表
 * </p>
 *
 * @author valarchie
 * @since 2022-10-02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_notice")
@Schema(description = "SysNoticeEntity对象,通知公告表")
public class SysNoticeEntity extends BaseEntity<SysNoticeEntity> implements Serializable{


    @Serial
    private static final long serialVersionUID = -5469620761307874263L;
    @Schema(description = "公告ID")
    @TableId(value = "notice_id", type = IdType.AUTO)
    private Integer noticeId;
    @Schema(description = "公告标题")
    @TableField("notice_title")
    private String noticeTitle;

    @Schema(description = "公告类型（1通知 2公告）")
    @TableField("notice_type")
    private Integer noticeType;

    @Schema(description = "公告内容")
    @TableField("notice_content")
    private String noticeContent;

    @Schema(description = "公告状态（1正常 0关闭）")
    @TableField("`status`")
    private Integer status;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

}
