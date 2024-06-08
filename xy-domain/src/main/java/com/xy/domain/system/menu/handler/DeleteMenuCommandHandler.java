package com.xy.domain.system.menu.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.menu.command.DeleteMenuCommand;
import com.xy.domain.system.menu.MenuModel;
import com.xy.domain.system.menu.MenuRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DeleteMenuCommandHandler implements CommandHandler<DeleteMenuCommand> {

    @Resource
    private MenuRepository menuRepository;

    /**
     * 删除菜单
     * @param eventQueue
     * @param command
     */
    public Boolean handle(EventQueue eventQueue, DeleteMenuCommand command) {
        MenuModel menuModel = menuRepository.findByIdOrError(command.getMenuId());

        Boolean handle = menuModel.handle(eventQueue, command);
        if (handle) {
            menuRepository.deleteBatchByIds(Collections.singletonList(command.getMenuId()));
        }
        return handle;
    }
}
