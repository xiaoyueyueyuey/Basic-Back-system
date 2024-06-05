package com.xy.admin.query;

import com.xy.admin.entity.SysDeptEntity;
import com.xy.admin.mapper.SysDeptMapper;
import com.xy.common.enums.BasicEnumUtil;
import com.xy.common.enums.common.StatusEnum;
import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.domain.DomainEvent;
import com.xy.domain.DomainEventListener;
import com.xy.domain.system.dept.event.DeptAddEvent;
import com.xy.domain.system.dept.event.DeptDeleteEvent;
import com.xy.domain.system.dept.event.DeptUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@Component
public class SysDeptMaterialize implements DomainEventListener {
    private final SysDeptMapper mapper;

    @Override
    public void onEvent(DomainEvent event) {
        if (event instanceof DeptAddEvent) addSysDept((DeptAddEvent) event);
        else if (event instanceof DeptUpdateEvent) updateSysDept((DeptUpdateEvent) event);
        else if (event instanceof DeptDeleteEvent) deleteSysDept((DeptDeleteEvent) event);
    }

    private void deleteSysDept(DeptDeleteEvent event) {
        mapper.deleteById(event.getDeptId());
    }

    private void updateSysDept(DeptUpdateEvent event) {
        SysDeptEntity sysDeptEntity = new SysDeptEntity();
        //TODO生成祖先列表
        BeanUtils.copyProperties(event, sysDeptEntity);
        sysDeptEntity.setAncestors(generateAncestors(event.getParentId()));
        mapper.updateById(sysDeptEntity);
    }

    private void addSysDept(DeptAddEvent event) {
        SysDeptEntity sysDeptEntity = new SysDeptEntity();
        //TODO生成祖先列表
        BeanUtils.copyProperties(event, sysDeptEntity);
        sysDeptEntity.setAncestors(generateAncestors(event.getParentId()));
        mapper.insert(sysDeptEntity);
    }
    public String generateAncestors(Long parentId) {
        if (parentId == 0) {
            return "0"; // 根部门没有祖先
        }
        SysDeptEntity parentDept = mapper.selectById(parentId);

        if (parentDept == null || StatusEnum.DISABLE.equals(
                BasicEnumUtil.fromValue(StatusEnum.class, parentDept.getStatus()))) {
            throw new ApiException(ErrorCode.Business.DEPT_PARENT_DEPT_NO_EXIST_OR_DISABLED);
        }

        // 获取父级的祖先列表并添加父级的 ID
        return parentDept.getAncestors() + "," + parentId;
    }
}
