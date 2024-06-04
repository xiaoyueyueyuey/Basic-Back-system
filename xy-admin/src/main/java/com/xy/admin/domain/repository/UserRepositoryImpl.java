package com.xy.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xy.admin.entity.SysRoleEntity;
import com.xy.admin.entity.agg.SysDeptAggEntity;
import com.xy.admin.entity.agg.SysPostAggEntity;
import com.xy.admin.entity.agg.SysUserAggEntity;
import com.xy.admin.mapper.SysRoleMapper;
import com.xy.admin.mapper.agg.SysDeptAggMapper;
import com.xy.admin.mapper.agg.SysPostAggMapper;
import com.xy.admin.mapper.agg.SysUserAggMapper;
import com.xy.domain.system.user.UserModel;
import com.xy.domain.system.user.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryImpl implements UserRepository {
    @Resource
    private SysUserAggMapper sysUserAggMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysDeptAggMapper sysDeptAggMapper;
    @Resource
    private SysPostAggMapper sysPostAggMapper;
    @Override
    public UserModel findByIdOrError(Long id) {
        SysUserAggEntity sysUserAggEntity = sysUserAggMapper.selectById(id);
        if(sysUserAggEntity == null){
           return new UserModel();
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(sysUserAggEntity, userModel);
        return userModel;
    }

    @Override
    public Boolean save(UserModel model) {
        SysUserAggEntity sysUserAggEntity = new SysUserAggEntity();
        BeanUtils.copyProperties(model, sysUserAggEntity);
        if(model.getUserId() == null){
            return sysUserAggMapper.insert(sysUserAggEntity) > 0;
        }
        else {
            return sysUserAggMapper.updateById(sysUserAggEntity) > 0;
        }
    }

    @Override
    public Boolean deleteById(Long id) {

        return sysUserAggMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean checkUsernameIsUnique(String username) {

        return sysUserAggMapper.exists(new LambdaQueryWrapper<SysUserAggEntity>().eq(SysUserAggEntity::getUsername, username));
    }

    @Override
    public Boolean checkUsernameIsUnique(String username, Long excludeUserId) {
        return sysUserAggMapper.exists(new LambdaQueryWrapper<SysUserAggEntity>().eq(SysUserAggEntity::getUsername, username).ne(SysUserAggEntity::getUserId, excludeUserId));
    }

    @Override
    public Boolean checkEmailIsUnique(String email) {
        return sysUserAggMapper.exists(new LambdaQueryWrapper<SysUserAggEntity>().eq(SysUserAggEntity::getEmail, email));
    }

    @Override
    public Boolean checkEmailIsUnique(String email, Long excludeUserId) {
        return sysUserAggMapper.exists(new LambdaQueryWrapper<SysUserAggEntity>().eq(SysUserAggEntity::getEmail, email).ne(SysUserAggEntity::getUserId, excludeUserId));
    }

    @Override
    public Boolean checkPhoneNumberIsUnique(String phoneNumber) {
        return sysUserAggMapper.exists(new LambdaQueryWrapper<SysUserAggEntity>().eq(SysUserAggEntity::getPhoneNumber, phoneNumber));

    }

    @Override
    public Boolean checkPhoneNumberIsUnique(String phoneNumber, Long excludeUserId) {
        return sysUserAggMapper.exists(new LambdaQueryWrapper<SysUserAggEntity>().eq(SysUserAggEntity::getPhoneNumber, phoneNumber).ne(SysUserAggEntity::getUserId, excludeUserId));
    }

    @Override
    public Boolean checkDeptIsExist(Long deptId) {
        return sysDeptAggMapper.exists(new LambdaQueryWrapper<SysDeptAggEntity>().eq(SysDeptAggEntity::getDeptId, deptId));
    }
    @Override
    public Boolean checkRoleIsExist(Long roleId) {
        //TODO 到时要改成角色聚合根
        return sysRoleMapper.exists(new LambdaQueryWrapper<SysRoleEntity>().eq(SysRoleEntity::getRoleId, roleId));
    }
    @Override
    public Boolean checkPostIsExist(Long postId) {
        return sysPostAggMapper.exists(new LambdaQueryWrapper<SysPostAggEntity>().eq(SysPostAggEntity::getPostId, postId));
    }
}
