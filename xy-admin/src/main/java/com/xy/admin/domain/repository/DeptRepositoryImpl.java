package com.xy.admin.domain.repository;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xy.admin.entity.agg.SysDeptAggEntity;
import com.xy.admin.mapper.agg.SysDeptAggMapper;
import com.xy.domain.system.dept.DeptModel;
import com.xy.domain.system.dept.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
public class DeptRepositoryImpl implements DeptRepository {

    private final SysDeptAggMapper mapper;
    @Override
    public DeptModel findByIdOrError(Long id) {
        SysDeptAggEntity sysDeptAggEntity = mapper.selectById(id);
        if (sysDeptAggEntity == null) {
            return new DeptModel();
        }
        DeptModel deptModel = new DeptModel();
        BeanUtils.copyProperties(sysDeptAggEntity, deptModel);
        return deptModel;
    }

    @Override
    public Boolean save(DeptModel model) {
        SysDeptAggEntity sysDeptAggEntity = new SysDeptAggEntity();
        if(model.getDeptId() == null){
            return mapper.insert(sysDeptAggEntity) > 0;
        }
        else {
            return mapper.updateById(sysDeptAggEntity) > 0;
        }
    }

    @Override
    public Boolean deleteBatchByIds(List<Long> ids) {
        return mapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean checkDeptNameIsUnique(String deptName) {
        return mapper.exists(new LambdaQueryWrapper<SysDeptAggEntity>().eq(SysDeptAggEntity::getDeptName, deptName));
    }

    @Override
    public Boolean checkDeptNameIsUnique(String deptName, Long deptId) {
        return mapper.exists(new LambdaQueryWrapper<SysDeptAggEntity>().eq(SysDeptAggEntity::getDeptName, deptName).ne(SysDeptAggEntity::getDeptId, deptId));
    }

    @Override
    public Boolean checkParentDeptIsExist(Long parentId) {
        if(parentId==0){
            return true;
        }
        return mapper.exists(new LambdaQueryWrapper<SysDeptAggEntity>().eq(SysDeptAggEntity::getDeptId, parentId));
    }

//    @Override
//    public Integer getChildDeptCount(Long deptId) {
//
//        return mapper);
//    }
//
//    @Override
//    public Integer getUserIsAssignedCount(Long deptId) {
//        return null;
//    }
}
