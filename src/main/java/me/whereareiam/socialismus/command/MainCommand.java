package me.whereareiam.socialismus.command;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import com.google.inject.Inject;
import me.whereareiam.socialismus.command.manager.CommandBase;
import me.whereareiam.socialismus.config.command.CommandsConfig;
import me.whereareiam.socialismus.config.message.MessagesConfig;
import me.whereareiam.socialismus.util.FormatterUtil;

public class MainCommand extends CommandBase {
    private final FormatterUtil formatterUtil;
    private final CommandsConfig commandsConfig;
    private final MessagesConfig messagesConfig;

    @Inject
    public MainCommand(FormatterUtil formatterUtil, CommandsConfig commandsConfig, MessagesConfig messagesConfig) {
        this.formatterUtil = formatterUtil;
        this.commandsConfig = commandsConfig;
        this.messagesConfig = messagesConfig;
    }

    @CommandAlias("%command.main")
    @CommandPermission("%permission.main")
    public void onCommand(CommandIssuer issuer) {
        // TODO
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void addTranslations() {
    }

    @Override
    public void addReplacements() {
        commandHelper.addReplacement(commandsConfig.mainCommand.command, "command.main");
        commandHelper.addReplacement(commandsConfig.mainCommand.permission, "permission.main");
    }
}
