package com.xy.domain.system.dept.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.dept.DeptModel;
import com.xy.domain.system.dept.DeptRepository;
import com.xy.domain.system.dept.command.AddDeptCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 添加部门命令处理器，处理器统一只给聚合赋聚合里面赋不到的值，不做业务逻辑处理
 */
@Component

public class AddDeptCommandHandler implements CommandHandler<AddDeptCommand> {
    @Resource
    private DeptRepository deptRepository;

    @Override
    public Boolean handle(EventQueue eventQueue, AddDeptCommand command) {
        // 加载部门聚合
        DeptModel deptModel = new DeptModel();
        // 给部门聚合赋命令没有的属性
        // 查询父部门
        Boolean parentDeptIsExist = deptRepository.checkParentDeptIsExist(command.getParentId());
        if (parentDeptIsExist) {
            deptModel.setParentId(command.getParentId());
        }
        Boolean b = deptRepository.checkDeptNameIsUnique(command.getDeptName());
        deptModel.setDeptNameIsUnique(b);
        // 处理命令
        Boolean handle = deptModel.handle(eventQueue, command);
        if (handle) {
            return deptRepository.save(deptModel);
        }
        return false;
    }
}
