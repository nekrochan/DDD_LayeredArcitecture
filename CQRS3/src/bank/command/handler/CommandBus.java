package bank.command.handler;

import bank.command.command.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandBus {
    private final Map<Class<? extends Command>, CommandHandler<? extends Command>> handlers = new HashMap<>();

    public <T extends Command> void register(Class<T> commandClass, CommandHandler<T> handler) {
        handlers.put(commandClass, handler);
    }

    @SuppressWarnings("unchecked")
    public <T extends Command> void dispatch(T command) {
        CommandHandler<T> handler = (CommandHandler<T>) handlers.get(command.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler registered for " + command.getClass().getName());
        }
        handler.handle(command);
    }
}
