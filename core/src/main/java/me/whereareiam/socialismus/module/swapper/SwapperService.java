package me.whereareiam.socialismus.module.swapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.chat.message.ChatMessage;
import me.whereareiam.socialismus.chat.message.ChatMessageProcessor;
import me.whereareiam.socialismus.config.message.MessagesConfig;
import me.whereareiam.socialismus.config.setting.SettingsConfig;
import me.whereareiam.socialismus.module.swapper.model.Swapper;
import me.whereareiam.socialismus.util.FormatterUtil;
import me.whereareiam.socialismus.util.LoggerUtil;
import me.whereareiam.socialismus.util.MessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

@Singleton
public class SwapperService implements ChatMessageProcessor {
    private final LoggerUtil loggerUtil;
    private final SwapperManager swapperManager;
    private final MessageUtil messageUtil;
    private final FormatterUtil formatterUtil;

    private final SettingsConfig settingsConfig;
    private final MessagesConfig messagesConfig;

    private final Random random = new Random();

    @Inject
    public SwapperService(LoggerUtil loggerUtil, SwapperManager swapperManager,
                          MessageUtil MessageUtil, FormatterUtil formatterUtil,
                          SettingsConfig settingsConfig, MessagesConfig messagesConfig) {
        this.loggerUtil = loggerUtil;
        this.swapperManager = swapperManager;
        this.messageUtil = MessageUtil;
        this.formatterUtil = formatterUtil;

        this.settingsConfig = settingsConfig;
        this.messagesConfig = messagesConfig;

        loggerUtil.trace("Initializing class: " + this);
    }

    @Override
    public ChatMessage process(ChatMessage chatMessage) {
        return swapPlaceholders(chatMessage);
    }

    @Override
    public boolean isEnabled() {
        return settingsConfig.modules.swapper.enabled;
    }

    private ChatMessage swapPlaceholders(ChatMessage chatMessage) {
        loggerUtil.debug("Swapping message: " + chatMessage.getContent());
        Player player = chatMessage.getSender();

        List<Swapper> swappers = swapperManager.getSwappers();
        for (Swapper swapper : swappers) {
            for (int i = 0; i < swapper.placeholders.size(); i++) {
                String placeholder = swapper.placeholders.get(i);
                if (!chatMessage.getContent().toString().contains(placeholder)) {
                    continue;
                }
                if (!player.hasPermission(swapper.settings.permission)) {
                    loggerUtil.debug("Player didn't have the right permission");
                    String message = messagesConfig.swapper.noPermissionForSwapper;
                    if (message != null) {
                        messageUtil.sendMessage(player, message);
                    }
                    return chatMessage;
                }
                Component content;
                if (swapper.settings.randomContent) {
                    int randomIndex = random.nextInt(swapper.content.size());
                    content = formatterUtil.formatMessage(player, swapper.content.get(randomIndex));
                } else {
                    content = formatterUtil.formatMessage(player, swapper.content.get(0));
                }

                if (!swapper.contentHover.isEmpty()) {
                    StringBuilder hoverText = new StringBuilder();
                    for (int s = 0; s < swapper.contentHover.size(); s++) {
                        hoverText.append(swapper.contentHover.get(s));
                        if (s != swapper.contentHover.size() - 1) {
                            hoverText.append("\n");
                        }
                    }
                    content = content.hoverEvent(HoverEvent.showText(formatterUtil.formatMessage(player, hoverText.toString())));
                }

                TextReplacementConfig config = TextReplacementConfig.builder()
                        .matchLiteral(placeholder)
                        .replacement(content)
                        .build();

                Component newContent = chatMessage.getContent().replaceText(config);

                chatMessage.setContent(newContent);
            }
        }
        return chatMessage;
    }
}