package com.xy.domain.system.user.event.manager;

import com.xy.domain.DomainEvent;
import lombok.Data;

@Data
public class UserAddEvent implements DomainEvent {
    private Long userId;
    private Long deptId;
    private String username;
    private String nickname;
    private String email;
    private String phoneNumber;
    private Integer sex;
    private String avatar;
    private String password;
    private Integer status;
    private Long roleId;
    private Long postId;
    private String remark;
    public void setAggregateId(Long aggregateId){
        this.userId = aggregateId;
    }

}
