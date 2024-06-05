package com.xy.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xy.admin.entity.agg.SysRoleAggEntity;
import com.xy.admin.mapper.agg.SysRoleAggMapper;
import com.xy.domain.system.role.RoleModel;
import com.xy.domain.system.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor

@Component
public class RoleRepositoryImpl implements RoleRepository {
    final SysRoleAggMapper sysRoleAggMapper;

    @Override
    public RoleModel findByIdOrError(Long id) {
        SysRoleAggEntity sysRoleAggEntity = sysRoleAggMapper.selectById(id);
        if (sysRoleAggEntity == null) {
            return new RoleModel();
        }
        RoleModel model = new RoleModel();
        BeanUtils.copyProperties(sysRoleAggEntity, model);
        return model;
    }

    @Override
    public Boolean save(RoleModel model) {
        SysRoleAggEntity sysRoleAggEntity = new SysRoleAggEntity();
        if (model.getRoleId() != null) {
            return sysRoleAggMapper.updateById(sysRoleAggEntity) > 0;
        } else {
            return sysRoleAggMapper.insert(sysRoleAggEntity) > 0;
        }
    }

    @Override
    public Boolean deleteBatchByIds(List<Long> ids) {
        return sysRoleAggMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean checkRoleNameIsUnique(String roleName) {
        return sysRoleAggMapper.exists(new LambdaQueryWrapper<SysRoleAggEntity>().eq(SysRoleAggEntity::getRoleName, roleName));
    }

    @Override
    public Boolean checkRoleKeyIsUnique(String roleKey) {
        return sysRoleAggMapper.exists(new LambdaQueryWrapper<SysRoleAggEntity>().eq(SysRoleAggEntity::getRoleKey, roleKey));
    }

    @Override
    public Boolean checkRoleNameIsUnique(String roleName, Long excludeRoleId) {
        return sysRoleAggMapper.exists(new LambdaQueryWrapper<SysRoleAggEntity>().eq(SysRoleAggEntity::getRoleName, roleName).ne(SysRoleAggEntity::getRoleId, excludeRoleId));
    }

    @Override
    public Boolean checkRoleKeyIsUnique(String roleKey, Long excludeRoleId) {
        return sysRoleAggMapper.exists(new LambdaQueryWrapper<SysRoleAggEntity>().eq(SysRoleAggEntity::getRoleKey, roleKey).ne(SysRoleAggEntity::getRoleId, excludeRoleId));
    }
}
