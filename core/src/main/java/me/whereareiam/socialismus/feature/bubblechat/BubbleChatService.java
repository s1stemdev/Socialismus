package me.whereareiam.socialismus.feature.bubblechat;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.cache.Cacheable;
import me.whereareiam.socialismus.chat.message.ChatMessage;
import me.whereareiam.socialismus.config.message.MessagesConfig;
import me.whereareiam.socialismus.feature.bubblechat.message.BubbleMessage;
import me.whereareiam.socialismus.feature.bubblechat.message.BubbleMessageProcessor;
import me.whereareiam.socialismus.feature.bubblechat.requirement.validator.RecipientRequirementValidator;
import me.whereareiam.socialismus.feature.bubblechat.requirement.validator.SenderRequirementValidator;
import me.whereareiam.socialismus.util.LoggerUtil;
import me.whereareiam.socialismus.util.MessageUtil;
import me.whereareiam.socialismus.util.WorldPlayerUtil;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

@Singleton
public class BubbleChatService {
    private final Injector injector;
    private final LoggerUtil loggerUtil;
    private final WorldPlayerUtil worldPlayerUtil;
    private final MessagesConfig messagesConfig;
    private final MessageUtil messageUtil;

    private final SenderRequirementValidator senderRequirementValidator;
    private final RecipientRequirementValidator recipientRequirementValidator;

    private final BubbleMessageProcessor bubbleMessageProcessor;
    private final Map<Player, BubbleQueue> playerQueuesMap = new HashMap<>();

    @Inject
    public BubbleChatService(Injector injector, LoggerUtil loggerUtil, WorldPlayerUtil worldPlayerUtil,
                             MessagesConfig messagesConfig, MessageUtil messageUtil,
                             SenderRequirementValidator senderRequirementValidator,
                             RecipientRequirementValidator recipientRequirementValidator,
                             BubbleMessageProcessor bubbleMessageProcessor) {
        this.injector = injector;
        this.loggerUtil = loggerUtil;
        this.worldPlayerUtil = worldPlayerUtil;
        this.messagesConfig = messagesConfig;
        this.messageUtil = messageUtil;

        this.senderRequirementValidator = senderRequirementValidator;
        this.recipientRequirementValidator = recipientRequirementValidator;

        this.bubbleMessageProcessor = bubbleMessageProcessor;

        loggerUtil.trace("Initializing class: " + this);
    }

    public void distributeBubbleMessage(ChatMessage chatMessage) {
        loggerUtil.debug("Distributing bubble message");

        Player player = chatMessage.getSender();
        if (!senderRequirementValidator.checkRequirements(player)) {
            String message = messagesConfig.bubblechat.noSendPermission;
            if (message != null) {
                messageUtil.sendMessage(player, message);
            }
            return;
        }

        Collection<Player> recipients = getRecipients(player);

        Queue<BubbleMessage> queue = bubbleMessageProcessor.processMessage(chatMessage, recipients);
        loggerUtil.debug("Created a queue of " + queue.size() + " bubble messages");

        BubbleQueue bubbleQueue = playerQueuesMap.get(player);
        if (bubbleQueue == null) {
            bubbleQueue = injector.getInstance(BubbleQueue.class);
            bubbleQueue.setPlayer(player);
            playerQueuesMap.put(player, bubbleQueue);
        }

        while (!queue.isEmpty()) {
            bubbleQueue.addMessage(queue.poll());
        }
    }

    @Cacheable(duration = 2)
    private Collection<Player> getRecipients(Player sender) {
        return worldPlayerUtil.getPlayersInWorld(sender.getWorld())
                .stream()
                .filter(recipientRequirementValidator::checkRequirements)
                .collect(Collectors.toList());
    }
}
