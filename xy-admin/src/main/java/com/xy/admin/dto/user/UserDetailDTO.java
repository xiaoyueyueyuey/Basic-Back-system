package com.xy.admin.dto.user;


import com.xy.admin.dto.post.PostDTO;
import com.xy.admin.dto.role.RoleDTO;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author valarchie
 */
@Data
public class UserDetailDTO {

    private UserDTO user;

    /**
     * 返回所有role
     */
    private List<RoleDTO> roleOptions;

    /**
     * 返回所有posts
     */
    private List<PostDTO> postOptions;

    private Long postId;

    private Long roleId;

    private Set<String> permissions;

}
