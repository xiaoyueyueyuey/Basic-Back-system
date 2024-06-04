package com.xy.admin.query.service.impl;


import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.admin.dto.dept.DeptDTO;
import com.xy.admin.entity.SysDeptEntity;
import com.xy.admin.entity.SysUserEntity;
import com.xy.admin.mapper.SysDeptMapper;
import com.xy.admin.mapper.SysUserMapper;
import com.xy.admin.query.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author valarchie
 * @since 2022-06-16
 */
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDeptEntity> implements SysDeptService {

    private final SysUserMapper userMapper;


    @Override
    public boolean isDeptNameDuplicated(String deptName, Long deptId, Long parentId) {
        QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_name", deptName)
            .ne(deptId != null, "dept_id", deptId)
            .eq(parentId != null, "parent_id", parentId);

        return this.baseMapper.exists(queryWrapper);
    }


    @Override
    public boolean hasChildrenDept(Long deptId, Boolean enabled) {
        QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(enabled != null, "status", 1)
            .and(o -> o.eq("parent_id", deptId).or()
                .apply("FIND_IN_SET (" + deptId + " , ancestors)")
            );
        return this.baseMapper.exists(queryWrapper);
    }


    @Override
    public boolean isChildOfTheDept(Long parentId, Long childId) {
        QueryWrapper<SysDeptEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("dept_id = '" + childId + "' and FIND_IN_SET ( " + parentId + " , ancestors)");
        return this.baseMapper.exists(queryWrapper);
    }


    @Override
    public boolean isDeptAssignedToUsers(Long deptId) {
        QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", deptId);
        return userMapper.exists(queryWrapper);
    }

    @Override
    public DeptDTO getDeptInfo(Long deptId) {

        SysDeptEntity byId = this.getById(deptId);
        return new DeptDTO(byId);
    }

    @Override
    public List<Tree<Long>> getDeptTree() {
        List<SysDeptEntity> list = this.list();
        return TreeUtil.build(list, 0L, (dept, tree) -> {
            tree.setId(dept.getDeptId());
            tree.setParentId(dept.getParentId());
            tree.putExtra("label", dept.getDeptName());
        });
    }

}
