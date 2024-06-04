package com.xy.admin.query.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.admin.dto.menu.MenuDTO;
import com.xy.admin.dto.menu.MenuDetailDTO;
import com.xy.admin.entity.SysMenuEntity;
import com.xy.admin.query.MenuQuery;
import com.xy.infrastructure.user.web.SystemLoginUser;

import java.util.List;

public interface SysMenuService extends IService<SysMenuEntity> {
    List<MenuDTO> getMenuList(MenuQuery menuQuery);

    MenuDetailDTO getMenuInfo(Long menuId);

    List<Tree<Long>> getDropdownList(SystemLoginUser loginUser);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenuEntity> getMenuListByUserId(Long userId);

    List<Long> getMenuIdsByRoleId(Long roleId);
}
