package dev.prisonbreak.models;

import org.bukkit.entity.Player;

public class GamePlayer {

    private final Player player;
    private GameRole role;
    private int money;
    private int escapes;
    private int arrests;
    private boolean isArrested;
    private long lastArrestTime;

    public GamePlayer(Player player, GameRole role) {
        this.player = player;
        this.role = role;
        this.money = 0;
        this.escapes = 0;
        this.arrests = 0;
        this.isArrested = false;
        this.lastArrestTime = 0;
    }

    // --- Getters ---

    public Player getPlayer() { return player; }
    public GameRole getRole() { return role; }
    public int getMoney() { return money; }
    public int getEscapes() { return escapes; }
    public int getArrests() { return arrests; }
    public boolean isArrested() { return isArrested; }
    public long getLastArrestTime() { return lastArrestTime; }

    // --- Setters ---

    public void setRole(GameRole role) { this.role = role; }
    public void setArrested(boolean arrested) { this.isArrested = arrested; }
    public void setLastArrestTime(long time) { this.lastArrestTime = time; }

    // --- Money ---

    public void addMoney(int amount) { this.money += amount; }
    public void removeMoney(int amount) { this.money = Math.max(0, this.money - amount); }
    public void resetMoney() { this.money = 0; }

    // --- Stats ---

    public void incrementEscapes() { this.escapes++; }
    public void incrementArrests() { this.arrests++; }

    // --- Helpers ---

    public boolean isPrisoner() { return role == GameRole.PRISONER; }
    public boolean isPolice() { return role == GameRole.POLICE; }

    /**
     * Polisin arrest cooldown'ı doldu mu?
     */
    public boolean canArrest(long cooldownMillis) {
        return System.currentTimeMillis() - lastArrestTime >= cooldownMillis;
    }
}
