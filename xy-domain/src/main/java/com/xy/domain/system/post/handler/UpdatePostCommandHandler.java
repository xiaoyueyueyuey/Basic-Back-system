package com.xy.domain.system.post.handler;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.post.PostModel;
import com.xy.domain.system.post.PostRepository;
import com.xy.domain.system.post.command.UpdatePostCommand;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class UpdatePostCommandHandler implements CommandHandler<UpdatePostCommand> {
    @Resource
    private PostRepository postRepository;
    @Override
    public Boolean handle(EventQueue eventQueue, UpdatePostCommand command) {
        PostModel postModel = postRepository.findByIdOrError(command.getPostId());
        if(postModel.getPostId()==null){
            //必为false
            return postModel.handle(eventQueue, command);
        }
        //更新的时候先判断名字或者编码是否更改了，如果更改了则需要判断是否唯一
        if(!postModel.getPostName().equals(command.getPostName())){
            Boolean postNameUnique = postRepository.checkPostNameUnique(command.getPostName(),command.getPostId());
            postModel.setPostNameIsUnique(postNameUnique);
        }else {
            postModel.setPostNameIsUnique(true);
        }
        if(!postModel.getPostCode().equals(command.getPostCode())){
            Boolean postCodeUnique = postRepository.checkPostCodeUnique(command.getPostCode(),command.getPostId());
            postModel.setPostCodeIsUnique(postCodeUnique);
        }else {
            postModel.setPostCodeIsUnique(true);
        }
        Boolean handle = postModel.handle(eventQueue, command);
        if (handle) {
            return postRepository.save(postModel)>0;
        }
        return false;
    }
}
