package com.xy.domain.system.menu;

import com.xy.common.enums.common.MenuTypeEnum;
import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.domain.EventQueue;
import com.xy.domain.system.menu.command.AddMenuCommand;
import com.xy.domain.system.menu.command.DeleteMenuCommand;
import com.xy.domain.system.menu.command.UpdateMenuCommand;
import com.xy.domain.system.menu.event.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

@Data
public class MenuModel {
    private Long menuId;//菜单id
    private String menuName;//菜单名称
    private Boolean menuNameIsUnique;//菜单名称是否唯一
    private Integer menuType;//菜单类型
    private Boolean isButton;//是否是按钮
    private Integer childrenMenuCount;//子菜单数量
    private Integer menuAssignToRoleCount;//菜单分配给角色的数量
    private Long parentId;//父菜单id
    private Integer parentMenuType;//父菜单类型
    //    private String path;//菜单路径
    /**
     * 处理新增菜单命令
     *
     * @param eventQueue
     * @param command
     * @return
     */
    public Boolean handle(EventQueue eventQueue, AddMenuCommand command) {
        if (!menuNameIsUnique) {
            eventQueue.enqueue(new MenuAddFailedEvent());
            return false;
        }
        //赋值
        BeanUtils.copyProperties(command, this);
        this.menuAssignToRoleCount = 0;
        this.childrenMenuCount = 0;
        try {
//            this.metaDTO = command.getMeta();
            // TODO 只允许在页面类型上添加按钮
            // 目前前端不支持嵌套的外链跳转
            checkAddButtonInIframeOrOutLink();
            checkAddMenuNotInCatalog();
            checkParentIdConflict();
        } catch (ApiException e) {
            eventQueue.enqueue(new MenuAddFailedEvent());
            return false;
        }
        //command和menuAddEvent的变量是一样的，这里只是为了划分一下
        MenuAddEvent menuAddEvent = new MenuAddEvent();
        BeanUtils.copyProperties(command, menuAddEvent);
        eventQueue.enqueue(menuAddEvent);
        return true;
    }

    public Boolean handle(EventQueue eventQueue, UpdateMenuCommand command) {
        if (menuId == null) {
            eventQueue.enqueue(new MenuUpdateFailedEvent());
            return false;
        }
        if (!menuNameIsUnique) {
            eventQueue.enqueue(new MenuUpdateFailedEvent());
            return false;
        }
        //赋值
        BeanUtils.copyProperties(command, this);
        try {
            checkAddButtonInIframeOrOutLink();
            checkAddMenuNotInCatalog();
//          checkExternalLink();
            checkParentIdConflict();
        } catch (ApiException e) {
            eventQueue.enqueue(new MenuUpdateFailedEvent());
            return false;
        }
        MenuUpdateEvent menuUpdateEvent = new MenuUpdateEvent();
        BeanUtils.copyProperties(command, menuUpdateEvent);
        eventQueue.enqueue(menuUpdateEvent);
        return true;
    }

    public Boolean handle(EventQueue eventQueue, DeleteMenuCommand command) {
        if (menuId == null) {
            eventQueue.enqueue(new MenuDeletedFailedEvent());
            return false;
        }
        try {
            //检查是否有菜单分配给角色
            checkMenuAlreadyAssignToRole();
        } catch (ApiException e) {
            eventQueue.enqueue(new MenuDeleteEvent(command.getMenuId()));
            return false;
        }
        eventQueue.enqueue(new MenuDeletedFailedEvent());
        return true;
    }

    /**
     * Iframe和外链跳转类型  不允许添加按钮
     */
    public void checkAddButtonInIframeOrOutLink() {
        if (parentId != 0L && getIsButton() && (
                Objects.equals(parentMenuType, MenuTypeEnum.IFRAME.getValue())
                        || Objects.equals(parentMenuType, MenuTypeEnum.OUTSIDE_LINK_REDIRECT.getValue())
        )) {
            throw new ApiException(ErrorCode.Business.MENU_NOT_ALLOWED_TO_CREATE_BUTTON_ON_IFRAME_OR_OUT_LINK);
        }
    }

    /**
     * 只允许在目录菜单类型底下 添加子菜单
     */
    public void checkAddMenuNotInCatalog() {
        if (parentId != 0L && !getIsButton() && (
                !Objects.equals(parentMenuType, MenuTypeEnum.CATALOG.getValue())
        )) {
            throw new ApiException(ErrorCode.Business.MENU_ONLY_ALLOWED_TO_CREATE_SUB_MENU_IN_CATALOG);
        }
    }
//    /**
//     * 检查菜单外链是否为http或https
//     */

//    public void checkExternalLink() {
//        if (getIsExternal() && !HttpUtil.isHttp(getPath()) && !HttpUtil.isHttps(getPath())) {
//            throw new ApiException(ErrorCode.Business.MENU_EXTERNAL_LINK_MUST_BE_HTTP);
//        }
//    }

    /**
     * 检查菜单父id是否冲突,修改时不允许将自己设置为自己的父id
     */
    public void checkParentIdConflict() {
        if (getMenuId().equals(getParentId())) {
            throw new ApiException(ErrorCode.Business.MENU_PARENT_ID_NOT_ALLOW_SELF);
        }
    }

    /**
     * 检查菜单是否存在子菜单,用于删除菜单时的检查
     */
    public void checkHasChildMenus() {
        if (childrenMenuCount > 0) {
            throw new ApiException(ErrorCode.Business.MENU_EXIST_CHILD_MENU_NOT_ALLOW_DELETE);
        }
    }

    /**
     * 检查菜单是否已经分配给角色,用于删除菜单时的检查
     */
    public void checkMenuAlreadyAssignToRole() {
        if (menuAssignToRoleCount != null && menuAssignToRoleCount > 0) {
            throw new ApiException(ErrorCode.Business.MENU_ALREADY_ASSIGN_TO_ROLE_NOT_ALLOW_DELETE);
        }
    }

    //TODO 停用时检查是否有子菜单，如果有子菜单则不允许停用,或者停用时将子菜单一并停用也行


}
