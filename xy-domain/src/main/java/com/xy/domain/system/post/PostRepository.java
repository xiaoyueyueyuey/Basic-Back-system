package com.xy.domain.system.post;

import com.xy.domain.common.Repository;

public interface PostRepository extends Repository<PostModel> {
    Boolean checkPostNameUnique(String postName);
    Boolean checkPostCodeUnique(String postCode);
    Boolean checkPostNameUnique(String postName, Long postId);
    Boolean checkPostCodeUnique(String postCode, Long postId);
    Integer getPostIsAssignedCount(Long postId);
}
