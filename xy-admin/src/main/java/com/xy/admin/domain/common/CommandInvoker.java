package com.xy.admin.domain.common;

import com.xy.domain.CommandHandler;
import com.xy.domain.EventQueue;
import com.xy.domain.system.Command;

import java.util.function.Function;

public interface CommandInvoker {
     <R> R invoke(Function<EventQueue, R> run);
      <T extends Command> Boolean execute(CommandHandler<T> commandHandler, T command);
}
