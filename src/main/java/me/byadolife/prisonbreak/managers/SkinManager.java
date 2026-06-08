package me.byadolife.prisonbreak.managers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkinManager {

    // PRISONER SKIN
    private static final String PRISONER_VALUE = "eyJ0aW1lc3RhbXAiOjE1LCJwcm9maWxlSWQiOiJ..."; 
    private static final String PRISONER_SIGNATURE = "signature_here";

    // GUARD SKIN
    private static final String GUARD_VALUE = "eyJ0aW1lc3RhbXAiOjE1LCJwcm9maWxlSWQiOiJ...";
    private static final String GUARD_SIGNATURE = "signature_here";

    public static void setPrisonerSkin(Player player) {
        setSkin(player, PRISONER_VALUE, PRISONER_SIGNATURE);
    }

    public static void setGuardSkin(Player player) {
        setSkin(player, GUARD_VALUE, GUARD_SIGNATURE);
    }

    public static void restoreSkin(Player player) {
        player.setHealth(player.getHealth()); // refresh trick
        Bukkit.getScheduler().runTaskLater(PrisonBreak.getInstance(), () -> {
            player.kickPlayer("Skin refresh");
        }, 1L);
    }

    private static void setSkin(Player player, String value, String signature) {
        try {
            GameProfile profile = getProfile(player);

            profile.getProperties().removeAll("textures");
            profile.getProperties().put("textures", new Property("textures", value, signature));

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.hidePlayer(PrisonBreak.getInstance(), player);
                p.showPlayer(PrisonBreak.getInstance(), player);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static GameProfile getProfile(Player player) throws Exception {
        Object craftPlayer = player.getClass().getMethod("getHandle").invoke(player);
        Field field = craftPlayer.getClass().getDeclaredField("gameProfile");
        field.setAccessible(true);
        return (GameProfile) field.get(craftPlayer);
    }
}
