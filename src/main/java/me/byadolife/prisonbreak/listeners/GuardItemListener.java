package me.byadolife.prisonbreak.listeners;

import me.byadolife.prisonbreak.managers.TeamManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GuardItemListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player guard))
            return;

        if (!(event.getEntity() instanceof Player victim))
            return;

        if (!TeamManager.isGuard(guard))
            return;

        ItemStack item = guard.getInventory().getItemInMainHand();

        // Şok cihazı
        if (item.getType() == Material.BLAZE_ROD) {

            victim.addPotionEffect(
                    new PotionEffect(
                            PotionEffectType.SLOWNESS,
                            20 * 5,
                            4
                    )
            );

            victim.addPotionEffect(
                    new PotionEffect(
                            PotionEffectType.NAUSEA,
                            20 * 5,
                            1
                    )
            );

            victim.addPotionEffect(
                    new PotionEffect(
                            PotionEffectType.BLINDNESS,
                            20 * 3,
                            0
                    )
            );

            guard.sendMessage("§aŞok cihazını kullandın.");
        }

        // Kelepçe
        if (item.getType() == Material.LEAD) {

            victim.addPotionEffect(
                    new PotionEffect(
                            PotionEffectType.SLOWNESS,
                            20 * 10,
                            10
                    )
            );

            guard.sendMessage(
                    "§bOyuncu kelepçelendi."
            );

            victim.sendMessage(
                    "§cKelepçelendin!"
            );
        }
    }
}
