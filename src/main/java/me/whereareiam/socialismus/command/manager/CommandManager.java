package me.whereareiam.socialismus.command.manager;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.locales.MessageKey;
import com.google.inject.Inject;
import com.google.inject.Injector;
import me.whereareiam.socialismus.config.command.CommandsConfig;
import me.whereareiam.socialismus.config.message.MessagesConfig;

import java.util.Locale;

public class CommandManager {
    private final Injector injector;
    private final BukkitCommandManager bukkitCommandManager;
    private final MessagesConfig messagesConfig;
    private final CommandsConfig commandsConfig;
    private int commandCount = 0;

    @Inject
    public CommandManager(Injector injector, BukkitCommandManager bukkitCommandManager, MessagesConfig messagesConfig, CommandsConfig commandsConfig) {
        this.injector = injector;
        this.bukkitCommandManager = bukkitCommandManager;
        this.messagesConfig = messagesConfig;
        this.commandsConfig = commandsConfig;

        addTranslations();
    }

    public void registerCommand(Class<? extends CommandBase> commandClass) {
        try {
            CommandBase commandInstance = injector.getInstance(commandClass);
            if (commandInstance.isEnabled()) {
                commandInstance.addReplacements();
                commandInstance.addTranslations();
                bukkitCommandManager.registerCommand(commandInstance);
            }
            commandCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTranslations() {
        addTranslation(messagesConfig.commands.unknownCommand, "acf-core.permission_denied");
        addTranslation(messagesConfig.commands.unknownCommand, "acf-core.permission_denied_parameter");
        addTranslation(messagesConfig.commands.wrongSyntax, "acf-core.invalid_syntax");
        addTranslation(messagesConfig.commands.unknownCommand, "acf-core.unknown_command");
        addTranslation(messagesConfig.commands.errorOccurred, "acf-core.error_performing_command");
        addTranslation(messagesConfig.commands.missingArgument, "acf-core.please_specify_one_of");
    }

    private void addTranslation(String message, String acfKey) {
        bukkitCommandManager.getLocales().addMessage(Locale.ENGLISH, MessageKey.of(acfKey), message);
    }

    public int getCommandCount() {
        return commandCount;
    }
}

