package com.xy.domain.system.post.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.post.PostModel;
import com.xy.domain.system.post.PostRepository;
import com.xy.domain.system.post.command.AddPostCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class AddPostCommandHandler implements CommandHandler<AddPostCommand> {
    @Resource
    private PostRepository postRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, AddPostCommand command) {
        Boolean postNameUnique = postRepository.checkPostNameUnique(command.getPostName());
        Boolean postCodeUnique = postRepository.checkPostCodeUnique(command.getPostCode());
        PostModel postModel = new PostModel();
        //赋命令上没有的值
        postModel.setPostCodeIsUnique(postCodeUnique);
        postModel.setPostNameIsUnique(postNameUnique);
        Boolean handle = postModel.handle(eventQueue, command);
        if (handle) {
            return postRepository.save(postModel);
        }
        return false;

    }
}
