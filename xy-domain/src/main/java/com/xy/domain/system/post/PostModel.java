package com.xy.domain.system.post;

import com.xy.common.exception.ApiException;
import com.xy.common.exception.error.ErrorCode;
import com.xy.domain.EventQueue;
import com.xy.domain.system.post.command.AddPostCommand;
import com.xy.domain.system.post.command.DeletePostCommand;
import com.xy.domain.system.post.command.UpdatePostCommand;
import com.xy.domain.system.post.event.PostAddEvent;
import com.xy.domain.system.post.event.PostAddFailedEvent;
import com.xy.domain.system.post.event.PostDeleteEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * @author valarchie
 */
@Data
@NoArgsConstructor
public class PostModel {
    private Long postId;
    private String postCode;
    private String postName;
    private Boolean postNameIsUnique;
    private Boolean postCodeIsUnique;
    private Integer postIsAssignedCount;

    /**
     * 处理新增岗位命令
     *
     * @param eventQueue
     * @param addCommand
     * @return
     */
    public Boolean handle(EventQueue eventQueue, AddPostCommand addCommand) {
        try {
            checkPostNameUnique();
            checkPostCodeUnique();
        } catch (ApiException e) {
            eventQueue.enqueue(new PostAddFailedEvent());
            return false;
        }
        this.setPostCode(addCommand.getPostCode());
        this.setPostName(addCommand.getPostName());
        this.postIsAssignedCount = 0;
        PostAddEvent postAddEvent = new PostAddEvent();
        BeanUtils.copyProperties(addCommand, postAddEvent);
        eventQueue.enqueue(postAddEvent);
        return true;
    }

    /**
     * 处理更新岗位命令
     *
     * @param eventQueue
     * @param command
     * @return
     */
    public Boolean handle(EventQueue eventQueue, UpdatePostCommand command) {
        if(this.postId == null){
            //必为false
            eventQueue.enqueue(new PostAddFailedEvent());
            return false;
        }
        try {
            checkPostNameUnique();
            checkPostCodeUnique();
        } catch (ApiException e) {
            eventQueue.enqueue(new PostAddFailedEvent());
            return false;
        }
        this.setPostCode(command.getPostCode());
        this.setPostName(command.getPostName());
        PostAddEvent postAddEvent = new PostAddEvent();
        BeanUtils.copyProperties(command, postAddEvent);
        eventQueue.enqueue(postAddEvent);
        return true;

    }

    /**
     * 处理删除岗位命令
     *
     * @param eventQueue
     * @param command
     * @return
     */
    public Boolean handle(EventQueue eventQueue, DeletePostCommand command) {
        try {
            checkCanBeDelete();
        } catch (ApiException e) {
            eventQueue.enqueue(new PostAddFailedEvent());
            return false;
        }
        PostDeleteEvent postDeleteEvent = new PostDeleteEvent();
        postDeleteEvent.setPostId(command.getPostId());
        eventQueue.enqueue(postDeleteEvent);
        return true;

    }

    public void checkPostNameUnique() {
        if (postNameIsUnique) {
            throw new ApiException(ErrorCode.Business.POST_NAME_IS_NOT_UNIQUE, getPostName());
        }

    }

    public void checkPostCodeUnique() {
        if (postCodeIsUnique) {
            throw new ApiException(ErrorCode.Business.POST_CODE_IS_NOT_UNIQUE, getPostCode());
        }

    }
    public void checkCanBeDelete() {
        if (postIsAssignedCount !=null&& postIsAssignedCount > 0){
            throw new ApiException(ErrorCode.Business.POST_ALREADY_ASSIGNED_TO_USER_CAN_NOT_BE_DELETED);
        }
    }

}
