package com.xy.domain.system.post.command;

import com.xy.domain.system.Command;
import lombok.Data;

@Data
public class DeletePostCommand implements Command {
    private Long postId;
}
