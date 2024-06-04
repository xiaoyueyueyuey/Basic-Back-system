package com.xy.domain.system.user.command.manager;

import com.xy.common.annotation.excel.ExcelColumn;
import com.xy.domain.system.Command;
import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class AddUserCommand implements Command {
    @ExcelColumn(name = "部门ID")
    private Long deptId;
    @ExcelColumn(name = "用户名")
    private String username;
    @ExcelColumn(name = "昵称")
    private String nickname;
    @ExcelColumn(name = "邮件")
    private String email;
    @ExcelColumn(name = "电话号码")
    private String phoneNumber;
    @ExcelColumn(name = "性别")
    private Integer sex;
    @ExcelColumn(name = "头像")
    private String avatar;
    @ExcelColumn(name = "密码")
    private String password;
    @ExcelColumn(name = "状态")
    private Integer status;
    @ExcelColumn(name = "角色ID")
    private Long roleId;
    @ExcelColumn(name = "职位ID")
    private Long postId;
    @ExcelColumn(name = "备注")
    private String remark;


}
