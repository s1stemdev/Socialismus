package me.whereareiam.socialismus.config.message;

import com.google.inject.Singleton;
import net.elytrium.serializer.SerializerConfig;
import net.elytrium.serializer.language.object.YamlSerializable;

@Singleton
public class MessagesConfig extends YamlSerializable {
    private static final SerializerConfig MESSAGES = new SerializerConfig.Builder().build();
    public String noPermission = " <gold>ꜱᴏᴄɪᴀʟɪꜱᴍᴜꜱ <dark_gray>| <white>You don't have enough <red>rights.";
    public ChatMessagesConfig chat = new ChatMessagesConfig();
    public CommandsMessagesConfig commands = new CommandsMessagesConfig();

    public MessagesConfig() {
        super(MessagesConfig.MESSAGES);
    }
}