package me.whereareiam.socialismus.config.setting;

import net.elytrium.serializer.language.object.YamlSerializable;

public class FeaturesSettingsConfig extends YamlSerializable {
    public boolean chats = true;
    public SwapperSettingsConfig swapper = new SwapperSettingsConfig();
    public boolean bubblechat = true;
}
