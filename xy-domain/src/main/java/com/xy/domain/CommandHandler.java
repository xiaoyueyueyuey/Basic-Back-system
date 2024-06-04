package com.xy.domain;

import com.xy.domain.system.Command;

public interface CommandHandler<T extends Command> {
  Boolean handle(EventQueue eventQueue, T command);
}