package com.xy.domain.system.menu;

import com.xy.domain.common.Repository;

public interface MenuRepository extends Repository<MenuModel>{
    Boolean findByMenuNameOrError(String menuName);
    Boolean findByMenuNameOrError(String menuName, Long menuId);

}
