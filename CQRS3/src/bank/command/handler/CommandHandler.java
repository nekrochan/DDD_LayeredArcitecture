package bank.command.handler;

import bank.command.command.Command;

public interface CommandHandler<T extends Command> {
    void handle(T command);
}
