package com.xy.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xy.admin.entity.agg.SysPostAggEntity;
import com.xy.admin.mapper.agg.SysPostAggMapper;
import com.xy.domain.system.post.PostModel;
import com.xy.domain.system.post.PostRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;

public class PostRepositoryImpl implements PostRepository{
    @Resource
    private SysPostAggMapper sysPostAggMapper;
    @Override
    public PostModel findByIdOrError(Long id) {
        SysPostAggEntity sysPostAggEntity = sysPostAggMapper.selectById(id);
        if (sysPostAggEntity == null) {
            return new PostModel();
        }
        PostModel postModel = new PostModel();
        BeanUtils.copyProperties(sysPostAggEntity, postModel);
        return postModel;
    }

    @Override
    public Boolean save(PostModel model) {
        return null;
    }

    @Override
    public Boolean deleteById(Long id) {
        return null;
    }

    @Override
    public Boolean checkPostNameUnique(String postName) {
        return sysPostAggMapper.exists(new LambdaQueryWrapper<SysPostAggEntity>()
                .eq(SysPostAggEntity::getPostName, postName));
    }
    @Override
    public Boolean checkPostCodeUnique(String postCode) {
        return sysPostAggMapper.exists(new LambdaQueryWrapper<SysPostAggEntity>()
                .eq(SysPostAggEntity::getPostCode, postCode));
    }

    @Override
    public Boolean checkPostNameUnique(String postName, Long postId) {
        return sysPostAggMapper.exists(new LambdaQueryWrapper<SysPostAggEntity>()
                .eq(SysPostAggEntity::getPostName, postName)
                .ne(SysPostAggEntity::getPostId, postId));
    }

    @Override
    public Boolean checkPostCodeUnique(String postCode, Long postId) {
        return sysPostAggMapper.exists(new LambdaQueryWrapper<SysPostAggEntity>()
                .eq(SysPostAggEntity::getPostCode, postCode)
                .ne(SysPostAggEntity::getPostId, postId));
    }

    @Override
    public Integer getPostIsAssignedCount(Long postId) {
        return sysPostAggMapper.selectPostIsAssignedCount(postId);
    }
}
