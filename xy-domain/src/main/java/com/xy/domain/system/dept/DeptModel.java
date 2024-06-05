package com.xy.domain.system.dept;

import com.xy.common.enums.common.StatusEnum;
import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.domain.EventQueue;
import com.xy.domain.system.dept.command.AddDeptCommand;
import com.xy.domain.system.dept.command.DeleteDeptCommand;
import com.xy.domain.system.dept.command.UpdateDeptCommand;
import com.xy.domain.system.dept.event.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author valarchie
 */
@Data
public class DeptModel {
    private Long deptId;//部门id
    private String deptName;//部门名称
    private Long parentId;//父部门id
    private Long childrenDeptCount;//子部门数量
    private Long deptIsAssignedToUsers;//部门分配给用户的数量
    private Boolean deptNameIsUnique;//部门名称是否唯一,不用存在数据库里，只是用来解耦判断
    private Integer status;//部门状态

    public Boolean handle(EventQueue eventQueue, AddDeptCommand command) {
        this.setDeptName(command.getDeptName());
        this.setStatus(command.getStatus());
        this.setParentId(command.getParentId());
        try {
            checkDeptNameUnique();//检查部门名称是否唯一
            checkParentIdExist();//检查父部门是否存在
        } catch (ApiException e) {
            eventQueue.enqueue(new DeptAddFailedEvent());
            return false;
        }
        //赋值
        this.deptIsAssignedToUsers = 0L;
        this.childrenDeptCount = 0L;
        //command和deptAddEvent的变量是一样的，这里只是为了划分一下
        DeptAddEvent deptAddEvent = new DeptAddEvent();
        BeanUtils.copyProperties(command, deptAddEvent);
        return true;
    }
    public Boolean handle(EventQueue eventQueue, UpdateDeptCommand command) {
        try {
            checkDeptExist();//检查部门是否存在
            checkDeptNameUnique();//检查部门名称是否唯一
            checkParentIdExist();//检查父部门是否存在
            checkParentIdConflict();//检查父部门是否和自己相同
            checkStatusAllowChange();//检查状态是否允许修改
        } catch (ApiException e) {
            eventQueue.enqueue(new DeptUpdateFailedEvent());
            return false;
        }
        //赋值
        //command和deptUpdateEvent的变量是一样的，这里只是为了划分一下
        DeptUpdateEvent deptUpdateEvent = new DeptUpdateEvent();
        BeanUtils.copyProperties(command, deptUpdateEvent);
        return true;
    }
    public Boolean handle(EventQueue eventQueue, DeleteDeptCommand command) {
        try {
            checkHasChildDept();//检查是否有子部门
            checkDeptAssignedToUsers();//检查部门是否分配给用户
        } catch (ApiException e) {
            eventQueue.enqueue(new DeptUpdateFailedEvent());
            return false;
        }
        DeptDeleteEvent deptDeleteEvent = new DeptDeleteEvent();
        deptDeleteEvent.setDeptId(command.getDeptId());
        return true;
    }
    public void checkDeptExist() {
        if (deptId == null) {
            throw new ApiException(ErrorCode.Business.DEPT_ID_IS_NULL);
        }
    }
    public void checkParentIdExist() {
        if (parentId == null) {
            throw new ApiException(ErrorCode.Business.DEPT_PARENT_DEPT_NO_EXIST_OR_DISABLED);
        }
    }

    public void checkDeptNameUnique() {
        if (!deptNameIsUnique) {
            throw new ApiException(ErrorCode.Business.DEPT_NAME_IS_NOT_UNIQUE);
        }
    }

    public void checkParentIdConflict() {
        if (deptId.equals(parentId)) {
            throw new ApiException(ErrorCode.Business.DEPT_PARENT_ID_IS_NOT_ALLOWED_SELF);
        }
    }
    public void checkHasChildDept() {
        if (childrenDeptCount > 0) {
            throw new ApiException(ErrorCode.Business.DEPT_EXIST_CHILD_DEPT_NOT_ALLOW_DELETE);
        }
    }
    public void checkDeptAssignedToUsers() {
        if (deptIsAssignedToUsers > 0) {
            throw new ApiException(ErrorCode.Business.DEPT_EXIST_LINK_USER_NOT_ALLOW_DELETE);
        }
    }

    /**
     * 检查状态是否允许修改,如果部门要修改的状态为停用且部门已经分配给用户,则不允许修改
     */
    public void checkStatusAllowChange() {
        if (StatusEnum.DISABLE.getValue().equals(status) &&
                deptIsAssignedToUsers > 0) {
            throw new ApiException(ErrorCode.Business.DEPT_STATUS_ID_IS_NOT_ALLOWED_CHANGE);
        }
    }

}
