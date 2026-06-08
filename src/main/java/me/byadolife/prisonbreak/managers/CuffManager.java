package me.byadolife.prisonbreak.managers;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.UUID;

public class CuffManager {

    private static final HashSet<UUID> cuffed = new HashSet<>();

    public static boolean isCuffed(Player p) {
        return cuffed.contains(p.getUniqueId());
    }

    public static void cuff(Player p) {

        cuffed.add(p.getUniqueId());

        p.setGameMode(GameMode.ADVENTURE);

        p.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOWNESS,
                20 * 5,
                10
        ));

        p.addPotionEffect(new PotionEffect(
                PotionEffectType.BLINDNESS,
                20 * 3,
                1
        ));

        p.sendMessage("§cKelepçelendin!");
    }

    public static void uncuff(Player p) {

        cuffed.remove(p.getUniqueId());

        p.setGameMode(GameMode.SURVIVAL);

        p.sendMessage("§aKelepçen açıldı!");
    }
}
