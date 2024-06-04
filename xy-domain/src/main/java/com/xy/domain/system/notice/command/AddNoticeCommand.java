package com.xy.domain.system.notice.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.xy.domain.system.Command;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;



/**
 * @author valarchie
 */
@Data
public class AddNoticeCommand implements Command {

    @NotBlank(message = "公告标题不能为空")
    @Size(max = 50, message = "公告标题不能超过50个字符")
    private String noticeTitle;

    private Integer noticeType;

    /**
     * 想要支持富文本的话, 避免Xss过滤的话， 请加上@JsonDeserialize(using = StringDeserializer.class) 注解
     */
    @NotBlank
    @JsonDeserialize(using = StringDeserializer.class)
    private String noticeContent;
    private Integer status;
    private String remark;

}
