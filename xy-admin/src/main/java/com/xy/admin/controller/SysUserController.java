package com.xy.admin.controller;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xy.admin.customize.aop.accessLog.AccessLog;
import com.xy.admin.domain.common.CommandInvoker;
import com.xy.admin.dto.user.SearchUserDO;
import com.xy.admin.dto.user.UserDTO;
import com.xy.admin.dto.user.UserDetailDTO;
import com.xy.admin.query.SearchUserQuery;
import com.xy.admin.query.service.SysUserService;
import com.xy.common.base.BaseResponseData;
import com.xy.common.enums.common.BusinessTypeEnum;
import com.xy.domain.system.user.command.manager.*;
import com.xy.domain.system.user.handler.manager.*;
import com.xy.infrastructure.base.BaseController;
import com.xy.infrastructure.page.PageDTO;
import com.xy.infrastructure.utils.poi.CustomExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息,要使用缓存中心，代办
 * @author valarchie
 */
@Tag(name = "用户API", description = "用户相关的增删查改")
@RestController
@RequestMapping("/system/users")
@RequiredArgsConstructor
public class SysUserController extends BaseController {


    @Resource
    private SysUserService sysUserService;

    @Resource
    private CommandInvoker commandInvoker;

    @Resource
    private DeleteUserCommandHandler deleteUserCommandHandler;
    @Resource
    private UpdateUserCommandHandler updateUserCommandHandler;
    @Resource
    private AddUserCommandHandler addUserCommandHandler;

    @Resource
    private ChangeStatusCommandHandler changeStatusCommandHandler;
    @Resource
    private ResetPasswordCommandHandler resetPasswordCommandHandler;
    /**
     * 获取用户列表
     */
    @Operation(summary = "用户列表")
    @PreAuthorize("@permission.has('system:user:list') AND @dataScope.checkDeptId(#query.deptId)")
    @GetMapping
    public BaseResponseData<PageDTO<UserDTO>> userList(SearchUserQuery<SearchUserDO> query) {
        Page<SearchUserDO> userPage = sysUserService.getUserList(query);
        List<UserDTO> userDTOList = userPage.getRecords().stream().map(UserDTO::new).collect(Collectors.toList());
        PageDTO<UserDTO> userDTOPageDTO = new PageDTO<>(userDTOList, userPage.getTotal());
        return BaseResponseData.ok(userDTOPageDTO);
    }
    @Operation(summary = "用户列表导出")
    @AccessLog(title = "用户管理", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@permission.has('system:user:export')")
    @GetMapping("/excel")
    public void exportUserByExcel(HttpServletResponse response, SearchUserQuery<SearchUserDO> query) {
        Page<SearchUserDO> userPage = sysUserService.getUserList(query);
        List<UserDTO> userDTOList = userPage.getRecords().stream().map(UserDTO::new).collect(Collectors.toList());
        PageDTO<UserDTO> userList = new PageDTO<>(userDTOList, userPage.getTotal());
        CustomExcelUtil.writeToResponse(userList.getRows(), UserDTO.class, response);
    }
    @Operation(summary = "用户列表导入")
    @AccessLog(title = "用户管理", businessType = BusinessTypeEnum.IMPORT)
    @PreAuthorize("@permission.has('system:user:import')")
    @PostMapping("/excel")
    public BaseResponseData<Void> importUserByExcel(MultipartFile file) {
        List<AddUserCommand> commands = CustomExcelUtil.readFromRequest(AddUserCommand.class, file);
        //TODO 优化，批量导入
        for (AddUserCommand command : commands) {
            commandInvoker.execute(addUserCommandHandler, command);
        }
        return BaseResponseData.ok();
    }
    /**
     * 下载批量导入模板
     */
    @Operation(summary = "用户导入excel下载")
    @GetMapping("/excelTemplate")
    public void downloadExcelTemplate(HttpServletResponse response) {
        CustomExcelUtil.writeToResponse(ListUtil.toList(new AddUserCommand()), AddUserCommand.class, response);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @Operation(summary = "用户详情")
    @PreAuthorize("@permission.has('system:user:query')")
    @GetMapping("/{userId}")
    public BaseResponseData<UserDetailDTO> getUserDetailInfo(@PathVariable(value = "userId", required = false) Long userId) {
        UserDetailDTO userDetailInfo =sysUserService.getUserDetailInfo(userId);
        return BaseResponseData.ok(userDetailInfo);
    }

    /**
     * 新增用户
     */
    @Operation(summary = "新增用户")
    @PreAuthorize("@permission.has('system:user:add') AND @dataScope.checkDeptId(#command.deptId)")
    @AccessLog(title = "用户管理", businessType = BusinessTypeEnum.ADD)
    @PostMapping
    public BaseResponseData<Void> add(@Validated @RequestBody AddUserCommand command) {
        Boolean execute = commandInvoker.execute(addUserCommandHandler, command);
        if(!execute){
            return BaseResponseData.fail();
        }
        return BaseResponseData.ok();
    }

    /**
     * 修改用户
     */
    @Operation(summary = "修改用户")
    @PreAuthorize("@permission.has('system:user:edit') AND @dataScope.checkUserId(#command.userId)")
    @AccessLog(title = "用户管理", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping("/{userId}")
    public BaseResponseData<Void> edit(@Validated @RequestBody UpdateUserCommand command) {
        Boolean execute = commandInvoker.execute(updateUserCommandHandler, command);
        if(!execute){
            return BaseResponseData.fail();
        }
        return BaseResponseData.ok();
    }

    /**
     * 删除用户,支持批量删除
     */
    @Operation(summary = "删除用户")
    @PreAuthorize("@permission.has('system:user:remove') AND @dataScope.checkUserIds(#userIds)")
    @AccessLog(title = "用户管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{userIds}")
    public BaseResponseData<Void> remove(@PathVariable List<Long> userIds) {
//        BulkOperationCommand<Long> bulkDeleteCommand = new BulkOperationCommand<>(userIds);
        DeleteUserCommand deleteUserCommand = new DeleteUserCommand();
        for (Long userId : userIds) {
            //TODO待优化，批量删除
            deleteUserCommand.setUserId(userId);
            commandInvoker.execute(deleteUserCommandHandler,deleteUserCommand);
        }
        return BaseResponseData.ok();
    }
    /**
     * TODO重置密码,待区分用户端和管理端
     */
    @Operation(summary = "重置用户密码")
    @PreAuthorize("@permission.has('system:user:resetPwd') AND @dataScope.checkUserId(#userId)")
    @AccessLog(title = "用户管理", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping("/{userId}/password")
    public BaseResponseData<Void> resetPassword(@PathVariable Long userId, @RequestBody ResetPasswordCommand command) {
        ResetPasswordCommand resetPasswordCommand = new ResetPasswordCommand();
        resetPasswordCommand.setUserId(userId);

        Boolean execute = commandInvoker.execute(resetPasswordCommandHandler, resetPasswordCommand);
        if(!execute){
            return BaseResponseData.fail();
        }
        return BaseResponseData.ok();
    }
    /**
     * 状态修改
     */
    @Operation(summary = "修改用户状态")
    @PreAuthorize("@permission.has('system:user:edit') AND @dataScope.checkUserId(#command.userId)")
    @AccessLog(title = "用户管理", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping("/{userId}/status")
    public BaseResponseData<Void> changeStatus(@PathVariable Long userId, @RequestBody ChangeStatusCommand command) {
        command.setUserId(userId);
        Boolean execute = commandInvoker.execute(changeStatusCommandHandler, command);
        if(!execute){
            return BaseResponseData.fail();
        }
        return BaseResponseData.ok();
    }


}
