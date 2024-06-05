package com.xy.admin.dto.user;

import com.xy.admin.dto.role.RoleDTO;
import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class UserInfoDTO {
    private UserDTO user;
    private RoleDTO role;
}
