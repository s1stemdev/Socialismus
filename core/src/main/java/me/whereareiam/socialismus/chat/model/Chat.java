package me.whereareiam.socialismus.chat.model;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    public String id;
    public String chatSymbol;
    public String messageFormat;
    public List<String> hoverFormat = new ArrayList<>();
    public ChatRequirements requirements = new ChatRequirements();
}