package com.xy.domain.system.post.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.post.PostModel;
import com.xy.domain.system.post.PostRepository;
import com.xy.domain.system.post.command.DeletePostCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component

public class DeletePostCommandHandler implements CommandHandler<DeletePostCommand> {
    @Resource
    private PostRepository postRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, DeletePostCommand command) {
        PostModel model = postRepository.findByIdOrError(command.getPostId());

        Boolean handle = model.handle(eventQueue, command);
        if (handle) {
            return postRepository.deleteBatchByIds(Collections.singletonList(command.getPostId()));
        }
        return false;
    }
}
