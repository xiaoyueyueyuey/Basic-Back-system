package com.xy.domain.system.menu.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.menu.command.AddMenuCommand;
import com.xy.domain.system.menu.MenuModel;
import com.xy.domain.system.menu.MenuRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class AddMenuCommandHandler implements CommandHandler<AddMenuCommand> {
    @Resource
    private MenuRepository menuRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, AddMenuCommand command) {
        Boolean hasMenuName = menuRepository.findByMenuNameOrError(command.getMenuName());
        // 加载菜单聚合
        MenuModel menuModel = new MenuModel();
        // 查询父菜单
        MenuModel parentMenu = menuRepository.findByIdOrError(command.getParentId());
        // 给菜单聚合赋命令没有的属性
        menuModel.setParentMenuType(parentMenu.getMenuType());
        menuModel.setMenuNameIsUnique(hasMenuName);
        // 处理命令
        Boolean handle = menuModel.handle(eventQueue, command);
        if (handle) {
            menuRepository.save(menuModel);
        }
        return handle;
    }


}