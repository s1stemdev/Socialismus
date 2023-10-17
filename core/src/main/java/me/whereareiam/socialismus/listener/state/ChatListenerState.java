package me.whereareiam.socialismus.listener.state;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.whereareiam.socialismus.util.LoggerUtil;

@Singleton
public class ChatListenerState {
    private final LoggerUtil loggerUtil;
    private boolean chatListenerRequired = false;

    @Inject
    public ChatListenerState(LoggerUtil loggerUtil) {
        this.loggerUtil = loggerUtil;

        loggerUtil.trace("Initializing class: " + this);
    }

    public boolean isChatListenerRequired() {
        loggerUtil.debug("isChatListenerRequired: " + chatListenerRequired);
        return chatListenerRequired;
    }

    public void setChatListenerRequired(boolean state) {
        loggerUtil.debug("setChatListenerRequired: " + state);
        this.chatListenerRequired = state;
    }
}