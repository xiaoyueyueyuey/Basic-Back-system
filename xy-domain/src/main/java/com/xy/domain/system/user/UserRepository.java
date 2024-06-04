package com.xy.domain.system.user;


import com.xy.domain.common.Repository;

public interface UserRepository extends Repository<UserModel> {
    Boolean checkUsernameIsUnique(String username);
    Boolean checkUsernameIsUnique(String username,Long excludeUserId);
    Boolean checkEmailIsUnique(String email);
    Boolean checkEmailIsUnique(String email,Long excludeUserId);
    Boolean checkPhoneNumberIsUnique(String phoneNumber);
    Boolean checkPhoneNumberIsUnique(String phoneNumber,Long excludeUserId);
    Boolean checkDeptIsExist(Long deptId);
    Boolean checkRoleIsExist(Long roleId);
    Boolean checkPostIsExist(Long postId);

    String getPasswordByUserId(Long userId);
    Boolean save(UserProfileModel model);
}
