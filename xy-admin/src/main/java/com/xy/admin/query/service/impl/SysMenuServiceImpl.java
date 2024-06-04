package com.xy.admin.query.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.admin.dto.menu.MenuDTO;
import com.xy.admin.dto.menu.MenuDetailDTO;
import com.xy.admin.entity.SysMenuEntity;
import com.xy.admin.mapper.SysMenuMapper;
import com.xy.admin.query.MenuQuery;
import com.xy.admin.query.service.SysMenuService;
import com.xy.infrastructure.user.web.SystemLoginUser;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuEntity> implements SysMenuService {

    @Override
    public List<MenuDTO> getMenuList(MenuQuery query) {
        List<SysMenuEntity> list = this.list(query.toQueryWrapper());
        return list.stream().map(MenuDTO::new)
                .sorted(Comparator.comparing(MenuDTO::getRank, Comparator.nullsLast(Integer::compareTo)))
                .collect(Collectors.toList());
    }

    @Override
    public MenuDetailDTO getMenuInfo(Long menuId) {
        SysMenuEntity byId = this.getById(menuId);
        return new MenuDetailDTO(byId);
    }

    @Override
    public List<Tree<Long>> getDropdownList(SystemLoginUser loginUser) {
        List<SysMenuEntity> menuEntityList =
                loginUser.isAdmin() ? this.list() : this.getMenuListByUserId(loginUser.getUserId());

        return buildMenuTreeSelect(menuEntityList);
    }

    @Override
    public List<SysMenuEntity> getMenuListByUserId(Long userId) {
        return baseMapper.selectMenuListByUserId(userId);

    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return this.baseMapper.selectMenuIdsByRoleId(roleId);
    }


    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<Tree<Long>> buildMenuTreeSelect(List<SysMenuEntity> menus) {
        TreeNodeConfig config = new TreeNodeConfig();
        //默认为id可以不设置
        config.setIdKey("menuId");
        return TreeUtil.build(menus, 0L, config, (menu, tree) -> {
            // 也可以使用 tree.setId(dept.getId());等一些默认值
            tree.setId(menu.getMenuId());
            tree.setParentId(menu.getParentId());
            tree.putExtra("label", menu.getMenuName());
        });
    }
}
