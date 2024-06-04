package com.xy.domain.system.role;

import com.xy.domain.common.Repository;

public interface RoleRepository extends Repository<RoleModel> {
    Boolean checkRoleNameIsUnique(String roleName);

    Boolean checkRoleKeyIsUnique(String roleKey);

    Boolean checkRoleNameIsUnique(String roleName, Long excludeRoleId);

    Boolean checkRoleKeyIsUnique(String roleKey, Long excludeRoleId);

}
