package com.xy.admin.controller;

import cn.hutool.core.lang.tree.Tree;
import com.xy.admin.customize.aop.accessLog.AccessLog;
import com.xy.admin.domain.common.CommandInvoker;
import com.xy.admin.dto.dept.DeptDTO;
import com.xy.admin.entity.SysDeptEntity;
import com.xy.admin.query.DeptQuery;
import com.xy.admin.query.service.SysDeptService;
import com.xy.common.base.BaseResponseData;
import com.xy.common.enums.common.BusinessTypeEnum;
import com.xy.domain.system.dept.command.AddDeptCommand;
import com.xy.domain.system.dept.command.DeleteDeptCommand;
import com.xy.domain.system.dept.command.UpdateDeptCommand;
import com.xy.domain.system.dept.handler.AddDeptCommandHandler;
import com.xy.domain.system.dept.handler.DeleteDeptCommandHandler;
import com.xy.domain.system.dept.handler.UpdateDeptCommandHandler;
import com.xy.infrastructure.base.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门信息
 *
 * @author valarchie
 */
@RestController
@RequestMapping("/system")
@Validated
@RequiredArgsConstructor
@Tag(name = "部门API", description = "部门相关的增删查改")
public class SysDeptController extends BaseController {


    @Resource
    private SysDeptService deptService;

    @Resource
    private CommandInvoker commandInvoker;
    @Resource
    private AddDeptCommandHandler addDeptCommandHandler;

    @Resource
    private UpdateDeptCommandHandler updateDeptCommandHandler;
    @Resource
    private DeleteDeptCommandHandler deleteDeptCommandHandler;

    /**
     * 获取部门列表
     */
    @Operation(summary = "部门列表")
    @PreAuthorize("@permission.has('system:dept:list')")
    @GetMapping("/depts")
    public BaseResponseData<List<DeptDTO>> list(DeptQuery query) {
        List<SysDeptEntity> list = deptService.list(query.toQueryWrapper());
        List<DeptDTO> deptList = list.stream().map(DeptDTO::new).collect(Collectors.toList());
        return BaseResponseData.ok(deptList);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @Operation(summary = "部门详情")
    @PreAuthorize("@permission.has('system:dept:query')")
    @GetMapping(value = "/dept/{deptId}")
    public BaseResponseData<DeptDTO> getInfo(@PathVariable Long deptId) {
        DeptDTO dept = deptService.getDeptInfo(deptId);
        return BaseResponseData.ok(dept);
    }

    /**
     * 获取部门下拉树列表
     */
    @Operation(summary = "获取部门树级结构")
    @GetMapping("/depts/dropdown")
    public BaseResponseData<List<Tree<Long>>> dropdownList() {
        List<Tree<Long>> deptTree = deptService.getDeptTree();
        return BaseResponseData.ok(deptTree);
    }

    /**
     * 新增部门
     */
    @Operation(summary = "新增部门")
    @PreAuthorize("@permission.has('system:dept:add')")
    @AccessLog(title = "部门管理", businessType = BusinessTypeEnum.ADD)
    @PostMapping("/dept")
    public BaseResponseData<Void> add(@RequestBody AddDeptCommand addCommand) {
        Boolean execute = commandInvoker.execute(addDeptCommandHandler, addCommand);
        if(!execute){
            return BaseResponseData.fail();
        }
        return BaseResponseData.ok();
    }

    /**
     * 修改部门
     */
    @Operation(summary = "修改部门")
    @PreAuthorize("@permission.has('system:dept:edit') AND @dataScope.checkDeptId(#updateCommand.deptId)")
    @AccessLog(title = "部门管理", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping("/dept/{deptId}")
    public BaseResponseData<Void> edit(@PathVariable("deptId")Long deptId, @RequestBody UpdateDeptCommand updateCommand) {
        updateCommand.setDeptId(deptId);
        Boolean execute = commandInvoker.execute(updateDeptCommandHandler, updateCommand);
        if(!execute){
            return BaseResponseData.fail();
        }
        return BaseResponseData.ok();
    }

    /**
     * 删除部门
     */
    @Operation(summary = "删除部门")
    @PreAuthorize("@permission.has('system:dept:remove') AND @dataScope.checkDeptId(#deptId)")
    @AccessLog(title = "部门管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/dept/{deptId}")
    public BaseResponseData<Void> remove(@PathVariable @NotNull Long deptId) {
        DeleteDeptCommand deleteDeptCommand = new DeleteDeptCommand();
        deleteDeptCommand.setDeptId(deptId);
        Boolean execute = commandInvoker.execute(deleteDeptCommandHandler, deleteDeptCommand);
        if(!execute){
            return BaseResponseData.fail();
        }
        return BaseResponseData.ok();
    }
}
