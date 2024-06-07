package com.xy.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xy.admin.entity.agg.SysRoleAggEntity;
import com.xy.admin.mapper.agg.SysRoleAggMapper;
import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
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
    public Long save(RoleModel model) {
        SysRoleAggEntity sysRoleAggEntity = new SysRoleAggEntity();
        if (model.getRoleId() != null) {
            int i = sysRoleAggMapper.updateById(sysRoleAggEntity);
            return (long) i;
        } else {
            int insert = sysRoleAggMapper.insert(sysRoleAggEntity);
            if(insert>0){
                return sysRoleAggEntity.getRoleId();
            }
        }
        throw new ApiException(ErrorCode.Internal.INTERNAL_ERROR);
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
