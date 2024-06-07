package com.xy.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xy.admin.entity.agg.SysPostAggEntity;
import com.xy.admin.mapper.agg.SysPostAggMapper;
import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.domain.system.post.PostModel;
import com.xy.domain.system.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PostRepositoryImpl implements PostRepository {
    private final SysPostAggMapper sysPostAggMapper;

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
    public Long save(PostModel model) {
        SysPostAggEntity sysPostAggEntity = new SysPostAggEntity();
        BeanUtils.copyProperties(model, sysPostAggEntity);
        if (model.getPostId() == null) {
            int insert = sysPostAggMapper.insert(sysPostAggEntity);
            if (insert > 0) {
                return (sysPostAggEntity.getPostId());
            }
        } else {
            return (long) sysPostAggMapper.updateById(sysPostAggEntity);
        }
        throw new ApiException(ErrorCode.Internal.INTERNAL_ERROR);
    }

    @Override
    public Boolean deleteBatchByIds(List<Long> ids) {
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
