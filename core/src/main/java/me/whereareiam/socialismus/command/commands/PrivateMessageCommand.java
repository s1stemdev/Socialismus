package me.whereareiam.socialismus.command.commands;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import com.google.inject.Inject;
import me.whereareiam.socialismus.command.base.CommandBase;
import me.whereareiam.socialismus.config.command.CommandsConfig;
import me.whereareiam.socialismus.config.message.MessagesConfig;
import me.whereareiam.socialismus.util.FormatterUtil;
import me.whereareiam.socialismus.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PrivateMessageCommand extends CommandBase {
    private final FormatterUtil formatterUtil;
    private final CommandsConfig commands;
    private final MessagesConfig messages;

    @Inject
    public PrivateMessageCommand(FormatterUtil formatterUtil, CommandsConfig commands,
                                 MessagesConfig messages) {
        this.formatterUtil = formatterUtil;
        this.commands = commands;
        this.messages = messages;
    }

    @CommandAlias("%command.privateMessage")
    @CommandCompletion("@players @nothing")
    @CommandPermission("%permission.privateMessage")
    @Description("%description.privateMessage")
    @Syntax("%syntax.privateMessage")
    public void onCommand(CommandIssuer issuer, String targetPlayerName, String message) {
        Player player = issuer.getIssuer();
        Player recipient = Bukkit.getPlayer(targetPlayerName);

        if (recipient == null) {
            MessageUtil.sendMessage(player, messages.commands.playerNotFound);
            return;
        }

        Component format = formatterUtil.formatMessage(player, commands.privateMessageCommand.format);

        TextReplacementConfig senderNamePlaceholder = TextReplacementConfig.builder()
                .matchLiteral("{senderName}")
                .replacement(player.getName())
                .build();

        TextReplacementConfig recipientNamePlaceholder = TextReplacementConfig.builder()
                .matchLiteral("{recipientName}")
                .replacement(recipient.getName())
                .build();

        TextReplacementConfig messagePlaceholder = TextReplacementConfig.builder()
                .matchLiteral("{message}")
                .replacement(message)
                .build();

        format = format
                .replaceText(senderNamePlaceholder)
                .replaceText(recipientNamePlaceholder)
                .replaceText(messagePlaceholder);

        MessageUtil.sendMessage(player, format);
        MessageUtil.sendMessage(recipient, format);
    }

    @Override
    public boolean isEnabled() {
        return commands.privateMessageCommand.enabled;
    }

    @Override
    public void addReplacements() {
        commandHelper.addReplacement(commands.privateMessageCommand.command, "command.privateMessage");
        commandHelper.addReplacement(commands.privateMessageCommand.permission, "permission.privateMessage");
        commandHelper.addReplacement(commands.privateMessageCommand.syntax, "syntax.privateMessage");
        commandHelper.addReplacement(messages.commands.privateMessageCommand.description, "description.privateMessage");
    }
}
