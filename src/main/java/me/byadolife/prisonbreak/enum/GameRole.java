package me.byadolife.prisonbreak.models;

import org.bukkit.ChatColor;

public enum GameRole {

    PRISONER("Mahkum", ChatColor.RED, ChatColor.DARK_RED),
    POLICE("Polis", ChatColor.BLUE, ChatColor.DARK_BLUE),
    SPECTATOR("İzleyici", ChatColor.GRAY, ChatColor.DARK_GRAY);

    private final String displayName;
    private final ChatColor color;
    private final ChatColor darkColor;

    GameRole(String displayName, ChatColor color, ChatColor darkColor) {
        this.displayName = displayName;
        this.color = color;
        this.darkColor = darkColor;
    }

    public String getDisplayName() { return displayName; }
    public ChatColor getColor() { return color; }
    public ChatColor getDarkColor() { return darkColor; }

    public String getColored() {
        return color + displayName;
    }
}
