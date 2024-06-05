package com.xy.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xy.admin.entity.agg.SysMenuAggEntity;
import com.xy.admin.mapper.agg.SysMenuAggMapper;
import com.xy.domain.system.menu.MenuModel;
import com.xy.domain.system.menu.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor

@Component
public class MenuRepositoryImpl implements MenuRepository {

    private final SysMenuAggMapper sysMenuAggMapper;

    /**
     * 根据id查询菜单聚合
     * @param id
     * @return
     */
    @Override
    public MenuModel findByIdOrError(Long id) {
        SysMenuAggEntity sysMenuAggEntity = sysMenuAggMapper.selectById(id);
        if (sysMenuAggEntity == null) {
            // 如果查询不到数据，返回一个空的MenuModel, 用于后续的判断
            return new MenuModel();
        }
        MenuModel menuModel = new MenuModel();
        BeanUtils.copyProperties(sysMenuAggEntity, menuModel);
//        menuModel.setMenuNameIsUnique(true);
        return menuModel;

    }

    /**
     * 查看菜单名称是否唯一
     * @param menuName
     * @return
     */
    @Override
    public Boolean findByMenuNameOrError(String menuName) {
       return sysMenuAggMapper.exists(new LambdaQueryWrapper<SysMenuAggEntity>().eq(SysMenuAggEntity::getMenuName, menuName));
    }

    @Override
    public Boolean findByMenuNameOrError(String menuName, Long menuId) {
        return sysMenuAggMapper.exists(new LambdaQueryWrapper<SysMenuAggEntity>().eq(SysMenuAggEntity::getMenuName, menuName).ne(SysMenuAggEntity::getMenuId, menuId));
    }

    @Override
    public Boolean save(MenuModel menu) {
        SysMenuAggEntity sysMenuAggEntity = new SysMenuAggEntity();
        BeanUtils.copyProperties(menu, sysMenuAggEntity);
        if(menu.getMenuId() == null){
            return sysMenuAggMapper.insert(sysMenuAggEntity) > 0;
        }
        else {
            return sysMenuAggMapper.updateById(sysMenuAggEntity) > 0;
        }
    }

    @Override
    public Boolean deleteBatchByIds(List<Long> ids) {
        return sysMenuAggMapper.deleteBatchIds(ids) > 0;
    }
}
