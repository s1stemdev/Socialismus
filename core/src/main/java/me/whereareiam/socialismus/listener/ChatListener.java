package me.whereareiam.socialismus.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface ChatListener extends Listener {
    boolean onPlayerChatEvent(Player player, String message);
}
