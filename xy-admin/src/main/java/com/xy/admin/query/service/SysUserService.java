package com.xy.admin.query.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.admin.common.dto.common.CurrentLoginUserDTO;
import com.xy.admin.dto.user.SearchUserDO;
import com.xy.admin.dto.user.UserDetailDTO;
import com.xy.admin.dto.user.UserProfileDTO;
import com.xy.admin.entity.SysPostEntity;
import com.xy.admin.entity.SysRoleEntity;
import com.xy.admin.entity.SysUserEntity;
import com.xy.infrastructure.page.AbstractPageQuery;
import com.xy.infrastructure.user.web.SystemLoginUser;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author valarchie
 * @since 2022-06-16
 */
public interface SysUserService extends IService<SysUserEntity> {




    /**
     * 获取用户的角色
     * @param userId 用户id
     * @return 用户角色
     */
    SysRoleEntity getRoleOfUser(Long userId);

    /**
     * 获取用户的岗位
     * @param userId 用户id
     * @return 用户岗位
     */
    SysPostEntity getPostOfUser(Long userId);


    /**
     * 通过用户名查询用户
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUserEntity getUserByUserName(String userName);


    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param query 查询参数
     * @return 用户信息集合信息
     */
    Page<SysUserEntity> getUserListByRole(AbstractPageQuery<SysUserEntity> query);

    /**
     * 根据条件分页查询用户列表
     *
     * @param query 查询参数
     * @return 用户信息集合信息
     */
    Page<SearchUserDO> getUserList(AbstractPageQuery<SearchUserDO> query);


    UserDetailDTO getUserDetailInfo(Long userId);

    UserProfileDTO getUserProfile(Long userId);

    CurrentLoginUserDTO getLoginUserInfo(SystemLoginUser loginUser);
}
