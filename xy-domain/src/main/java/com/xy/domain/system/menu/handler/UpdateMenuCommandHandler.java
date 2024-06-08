package com.xy.domain.system.menu.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.menu.MenuModel;
import com.xy.domain.system.menu.MenuRepository;
import com.xy.domain.system.menu.command.UpdateMenuCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class UpdateMenuCommandHandler implements CommandHandler<UpdateMenuCommand> {
    @Resource
    private MenuRepository menuRepository;
    public Boolean handle(EventQueue eventQueue, UpdateMenuCommand command) {
        //加载菜单聚合
        MenuModel menuModel = menuRepository.findByIdOrError(command.getMenuId());
        if (menuModel.getMenuId() == null) {
            return menuModel.handle(eventQueue, command);
        }
        //判断名字是否修改了
        if (!menuModel.getMenuName().equals(command.getMenuName())) {
            Boolean hasMenuName = menuRepository.findByMenuNameOrError(command.getMenuName(), command.getMenuId());
            menuModel.setMenuNameIsUnique(!hasMenuName);
        }
        //处理命令
        Boolean handle = menuModel.handle(eventQueue, command);
        //如果处理成功，保存
        if (handle) {
            menuRepository.save(menuModel);
        }
        return handle;
    }
}
