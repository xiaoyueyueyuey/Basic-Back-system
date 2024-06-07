package com.xy.admin.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xy.admin.customize.aop.accessLog.AccessLog;
import com.xy.admin.domain.common.CommandInvoker;
import com.xy.admin.dto.role.RoleDTO;
import com.xy.admin.dto.user.UserDTO;
import com.xy.admin.entity.SysRoleEntity;
import com.xy.admin.query.AllocatedRoleQuery;
import com.xy.admin.query.RoleQuery;
import com.xy.admin.query.UnallocatedRoleQuery;
import com.xy.admin.query.service.SysRoleService;
import com.xy.common.base.BaseResponseData;
import com.xy.common.enums.common.BusinessTypeEnum;
import com.xy.domain.system.role.command.*;
import com.xy.domain.system.role.handler.*;
import com.xy.infrastructure.base.BaseController;
import com.xy.infrastructure.page.PageDTO;
import com.xy.infrastructure.utils.poi.CustomExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息
 *
 * @author valarchie
 */
@Tag(name = "角色API", description = "角色相关的增删查改")
@RestController
@RequestMapping("/system/role")
@Validated
@RequiredArgsConstructor
public class SysRoleController extends BaseController {
    @Resource
    public SysRoleService sysRoleService;

    @Resource
    public CommandInvoker commandInvoker;

    @Resource
    public UpdateRoleCommandHandler updateRoleCommandHandler;
    @Resource
    public UpdateRoleStatusCommandHandler updateRoleStatusCommandHandler;
    @Resource
    public UpdateRoleDataScopeCommandHandler updateRoleDataScopeCommandHandler;
    @Resource
    public DeleteRoleCommandHandler deleteRoleCommandHandler;
    @Resource
    public AddRoleCommandHandler addRoleCommandHandler;


    @Operation(summary = "角色列表")
    @PreAuthorize("@permission.has('system:role:list')")
    @GetMapping("/list")
    public BaseResponseData<PageDTO<RoleDTO>> list(RoleQuery query) {
        Page<SysRoleEntity> page = sysRoleService.page(query.toPage(), query.toQueryWrapper());
        List<RoleDTO> records = page.getRecords().stream().map(RoleDTO::new).collect(Collectors.toList());
        PageDTO<RoleDTO> pageDTO = new PageDTO<>(records, page.getTotal());
        return BaseResponseData.ok(pageDTO);
    }

    @Operation(summary = "角色列表导出")
    @AccessLog(title = "角色管理", businessType = BusinessTypeEnum.EXPORT)
    @PreAuthorize("@permission.has('system:role:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, RoleQuery query) {
        Page<SysRoleEntity> page = sysRoleService.page(query.toPage(), query.toQueryWrapper());
        List<RoleDTO> records = page.getRecords().stream().map(RoleDTO::new).collect(Collectors.toList());
        PageDTO<RoleDTO> pageDTO = new PageDTO<>(records, page.getTotal());
        CustomExcelUtil.writeToResponse(pageDTO.getRows(), RoleDTO.class, response);
    }

    /**
     * 根据角色编号获取详细信息
     */
    @Operation(summary = "角色详情")
    @PreAuthorize("@permission.has('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public BaseResponseData<RoleDTO> getInfo(@PathVariable @NotNull Long roleId) {
        RoleDTO roleInfo = sysRoleService.getRoleInfo(roleId);
        return BaseResponseData.ok(roleInfo);
    }

    /**
     * 新增角色
     */
    @Operation(summary = "添加角色")
    @PreAuthorize("@permission.has('system:role:add')")
    @AccessLog(title = "角色管理", businessType = BusinessTypeEnum.ADD)
    @PostMapping
    public BaseResponseData<Void> add(@RequestBody AddRoleCommand addCommand) {
        Boolean execute = commandInvoker.execute(addRoleCommandHandler, addCommand);
        if(!execute){
            return BaseResponseData.fail();
        }
        return BaseResponseData.ok();
    }
    /**
     * 待优化，批量删除
     * 移除角色
     */
    @Operation(summary = "删除角色")
    @PreAuthorize("@permission.has('system:role:remove')")
    @AccessLog(title = "角色管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping(value = "/{roleId}")
    public BaseResponseData<Void> remove(@PathVariable("roleId") List<Long> roleIds) {
        roleIds.forEach(roleId -> {
            DeleteRoleCommand deleteRoleCommand = new DeleteRoleCommand();
            deleteRoleCommand.setRoleId(roleId);
            commandInvoker.execute(deleteRoleCommandHandler, deleteRoleCommand);
        });
        return BaseResponseData.ok();
    }

    /**
     * 修改保存角色
     */
    @Operation(summary = "修改角色")
    @PreAuthorize("@permission.has('system:role:edit')")
    @AccessLog(title = "角色管理", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping
    public BaseResponseData<Void> edit(@Validated @RequestBody UpdateRoleCommand updateCommand) {
        Boolean execute = commandInvoker.execute(updateRoleCommandHandler, updateCommand);
        if(!execute){
            return BaseResponseData.fail();
        }
        return BaseResponseData.ok();
    }
    /**
     * 修改保存数据权限
     */
    @Operation(summary = "修改角色数据权限")
    @PreAuthorize("@permission.has('system:role:edit')")
    @AccessLog(title = "角色管理", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping("/{roleId}/dataScope")
    public BaseResponseData<Void> dataScope(@PathVariable("roleId") Long roleId,
                                            @RequestBody UpdateRoleDataScopeCommand command) {
        command.setRoleId(roleId);
        Boolean execute = commandInvoker.execute(updateRoleDataScopeCommandHandler, command);
        if(!execute){
            return BaseResponseData.fail();
        }
        return BaseResponseData.ok();
    }

    /**
     * 角色状态修改
     */
    @Operation(summary = "修改角色状态")
    @PreAuthorize("@permission.has('system:role:edit')")
    @AccessLog(title = "角色管理", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping("/{roleId}/status")
    public BaseResponseData<Void> changeStatus(@PathVariable("roleId") Long roleId,
                                               @RequestBody UpdateRoleStatusCommand command) {
        command.setRoleId(roleId);
        Boolean execute = commandInvoker.execute(updateRoleStatusCommandHandler, command);
        if(!execute){
            return BaseResponseData.fail();
        }
        return BaseResponseData.ok();
    }

    /**
     * 查询已分配用户角色列表
     */
    @Operation(summary = "已关联该角色的用户列表")
    @PreAuthorize("@permission.has('system:role:list')")
    @GetMapping("/{roleId}/allocated/list")
    public BaseResponseData<PageDTO<UserDTO>> allocatedUserList(@PathVariable("roleId") Long roleId,
                                                                AllocatedRoleQuery query) {
        query.setRoleId(roleId);
        PageDTO<UserDTO> page = sysRoleService.getAllocatedUserList(query);
        return BaseResponseData.ok(page);
    }
    /**
     * 查询未分配用户角色列表
     */
    @Operation(summary = "未关联该角色的用户列表")
    @PreAuthorize("@permission.has('system:role:list')")
    @GetMapping("/{roleId}/unallocated/list")
    public BaseResponseData<PageDTO<UserDTO>> unallocatedUserList(@PathVariable("roleId") Long roleId,
                                                                  UnallocatedRoleQuery query) {
        query.setRoleId(roleId);
        PageDTO<UserDTO> page = sysRoleService.getUnallocatedUserList(query);
        return BaseResponseData.ok(page);
    }
    /**
     * 批量取消授权用户
     */
//    @Operation(summary = "批量解除角色和用户的关联")
//    @PreAuthorize("@permission.has('system:role:edit')")
//    @AccessLog(title = "角色管理", businessType = BusinessTypeEnum.GRANT)
//    @DeleteMapping("/users/{userIds}/grant/bulk")
//    public BaseResponseData<Void> deleteRoleOfUserByBulk(@PathVariable("userIds") List<Long> userIds) {
//
//        roleApplicationService.deleteRoleOfUserByBulk(userIds);
//        return BaseResponseData.ok();
//    }

    /**
     * 批量选择用户授权
     */
//    @Operation(summary = "批量添加用户和角色关联")
//    @PreAuthorize("@permission.has('system:role:edit')")
//    @AccessLog(title = "角色管理", businessType = BusinessTypeEnum.GRANT)
//    @PostMapping("/{roleId}/users/{userIds}/grant/bulk")
//    public BaseResponseData<Void> addRoleForUserByBulk(@PathVariable("roleId") Long roleId,
//                                                       @PathVariable("userIds") List<Long> userIds) {
//
//        roleApplicationService.addRoleOfUserByBulk(roleId, userIds);
//        return BaseResponseData.ok();
//    }

}
