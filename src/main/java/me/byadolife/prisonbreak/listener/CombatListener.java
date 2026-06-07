package me.byadolife.prisonbreak.listener;

import me.byadolife.prisonbreak.manager.GameManager;
import me.byadolife.prisonbreak.models.GamePlayer;
import me.byadolife.prisonbreak.models.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Savaş ve arrest sistemini yönetir.
 */
public class CombatListener implements Listener {

    private final GameManager gameManager;

    public CombatListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Polis mahkumu vurduğunda tutuklama dener.
     * Hasar iptal edilir — vurma sadece tutuklama mekanizması.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (gameManager.getState() != GameState.ACTIVE) return;

        if (!(event.getDamager() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof Player victim)) return;

        GamePlayer attackerGP = gameManager.getGamePlayer(attacker);
        GamePlayer victimGP   = gameManager.getGamePlayer(victim);

        if (attackerGP == null || victimGP == null) return;

        // Her iki oyuncu da oyundaysa → hasarı iptal et, arrest dene
        event.setCancelled(true);

        // Polis mahkumu vurursa → arrest
        if (attackerGP.isPolice() && victimGP.isPrisoner()) {
            gameManager.tryArrest(attacker, victim);
        }

        // Mahkum mahkumu vuramaz, polis polisi vuramaz → hiçbir şey olmaz
    }

    /**
     * Oyundaki oyuncuların açlık kaybetmesini engelle (oyun dengesi için).
     */
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!gameManager.isInGame(player)) return;
        if (gameManager.getState() != GameState.ACTIVE) return;

        // Açlık seviyesi düşmesin
        if (event.getFoodLevel() < player.getFoodLevel()) {
            event.setCancelled(true);
        }
    }

    /**
     * Oyundaki oyuncuların çevre hasarı (fall, void) almasını engelle.
     */
    @EventHandler
    public void onEnvironmentDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!gameManager.isInGame(player)) return;
        if (gameManager.getState() != GameState.ACTIVE) return;

        // EntityDamageByEntityEvent tarafından zaten işleniyor, onu atla
        if (event instanceof EntityDamageByEntityEvent) return;

        // Çevre hasarını iptal et (düşme, boşluk, ateş vb.)
        event.setCancelled(true);
    }
}
