package com.xy.admin.query.service.impl;


import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.admin.common.cache.CacheCenter;
import com.xy.admin.common.dto.common.CurrentLoginUserDTO;
import com.xy.admin.dto.post.PostDTO;
import com.xy.admin.dto.role.RoleDTO;
import com.xy.admin.dto.user.SearchUserDO;
import com.xy.admin.dto.user.UserDTO;
import com.xy.admin.dto.user.UserDetailDTO;
import com.xy.admin.dto.user.UserProfileDTO;
import com.xy.admin.entity.SysPostEntity;
import com.xy.admin.entity.SysRoleEntity;
import com.xy.admin.entity.SysUserEntity;
import com.xy.admin.mapper.SysUserMapper;
import com.xy.admin.query.service.SysPostService;
import com.xy.admin.query.service.SysRoleService;
import com.xy.admin.query.service.SysUserService;
import com.xy.infrastructure.page.AbstractPageQuery;
import com.xy.infrastructure.user.web.SystemLoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2022-06-16
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {
//    private final SysRoleService roleService;
    private final SysPostService postService;

    @Override
    public SysRoleEntity getRoleOfUser(Long userId) {
        List<SysRoleEntity> list = baseMapper.getRolesByUserId(userId);
        return list.isEmpty() ? null : list.get(0);
    }


    @Override
    public SysPostEntity getPostOfUser(Long userId) {
        List<SysPostEntity> list = baseMapper.getPostsByUserId(userId);
        return list.isEmpty() ? null : list.get(0);
    }



    @Override
    public SysUserEntity getUserByUserName(String userName) {
        QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userName);
        return this.getOne(queryWrapper);
    }

    @Override
    public Page<SysUserEntity> getUserListByRole(AbstractPageQuery<SysUserEntity> query) {
        return baseMapper.getUserListByRole(query.toPage(), query.toQueryWrapper());
    }
    @Override
    public Page<SearchUserDO> getUserList(AbstractPageQuery<SearchUserDO> query) {
        return baseMapper.getUserList(query.toPage(), query.toQueryWrapper());
    }
    @Override
    public UserDetailDTO getUserDetailInfo(Long userId) {
        SysUserEntity userEntity = this.getById(userId);
        UserDetailDTO detailDTO = new UserDetailDTO();
        LambdaQueryWrapper<SysRoleEntity> roleQuery = new LambdaQueryWrapper<SysRoleEntity>()
                .orderByAsc(SysRoleEntity::getRoleSort);

        //因爲角色IMPL也以來於用戶IMPL，所以這裏使用SpringUtil.getBean()方法獲取bean解決循環依賴問題
        SysRoleService roleService = SpringUtil.getBean(SysRoleService.class);

        List<RoleDTO> roleDtoList = roleService.list(roleQuery).stream().map(RoleDTO::new).collect(Collectors.toList());
        List<PostDTO> postDtoList = postService.list().stream().map(PostDTO::new).collect(Collectors.toList());
        detailDTO.setRoleOptions(roleDtoList);
        detailDTO.setPostOptions(postDtoList);
        if (userEntity != null) {
            detailDTO.setUser(new UserDTO(userEntity));
            detailDTO.setRoleId(userEntity.getRoleId());
            detailDTO.setPostId(userEntity.getPostId());
        }
        return detailDTO;
    }
    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        SysUserEntity userEntity = this.getById(userId);
        SysPostEntity postEntity = this.getPostOfUser(userId);
        SysRoleEntity roleEntity = this.getRoleOfUser(userId);
        return new UserProfileDTO(userEntity, postEntity, roleEntity);
    }

    @Override
    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    public CurrentLoginUserDTO getLoginUserInfo(SystemLoginUser loginUser) {
        CurrentLoginUserDTO permissionDTO = new CurrentLoginUserDTO();
        permissionDTO.setUserInfo(new UserDTO(CacheCenter.userCache.getObjectById(loginUser.getUserId())));
        permissionDTO.setRoleKey(loginUser.getRoleInfo().getRoleKey());
        permissionDTO.setPermissions(loginUser.getRoleInfo().getMenuPermissions());
        return permissionDTO;
    }


}
