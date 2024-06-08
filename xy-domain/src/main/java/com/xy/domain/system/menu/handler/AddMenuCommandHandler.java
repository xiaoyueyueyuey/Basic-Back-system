package com.xy.domain.system.menu.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.DomainEvent;
import com.xy.domain.EventQueue;
import com.xy.domain.system.menu.command.AddMenuCommand;
import com.xy.domain.system.menu.MenuModel;
import com.xy.domain.system.menu.MenuRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddMenuCommandHandler implements CommandHandler<AddMenuCommand> {
    @Resource
    private MenuRepository menuRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, AddMenuCommand command) {
        Boolean hasMenuName = menuRepository.findByMenuNameOrError(command.getMenuName());
        System.out.println("hasMenuName = " + hasMenuName);
        // 加载菜单聚合
        MenuModel menuModel = new MenuModel();
        // 查询父菜单
        MenuModel parentMenu = menuRepository.findByIdOrError(command.getParentId());
        System.out.println("parentMenu = " + parentMenu);
        // 给菜单聚合赋命令没有的属性
        menuModel.setParentMenuType(parentMenu.getMenuType());
        menuModel.setMenuNameIsUnique(!hasMenuName);
        // 处理命令
        Boolean handle = menuModel.handle(eventQueue, command);
        if (handle) {
            Long menuId = menuRepository.save(menuModel);
            List<DomainEvent> queue = eventQueue.queue();
            for (DomainEvent domainEvent : queue) {//设置聚合id
                domainEvent.setAggregateId(menuId);
            }
            return menuId > 0;
        }
        return handle;
    }


}
