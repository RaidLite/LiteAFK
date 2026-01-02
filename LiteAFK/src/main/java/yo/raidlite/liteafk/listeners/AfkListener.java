package yo.raidlite.liteafk.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import yo.raidlite.liteafk.LiteAFK;
import yo.raidlite.liteafk.StatusAFK;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AfkListener implements Listener {

    private final Map<UUID, Double[]> movementHistory = new HashMap<>();
    public LiteAFK plugin;

    public AfkListener(LiteAFK plugin) {
        this.plugin = plugin;
    }

    private void removeAfk(Player player) {
        if (plugin.statusFreezeHashMap.containsKey(player) && plugin.statusFreezeHashMap.get(player) == StatusAFK.AFK) {
            plugin.statusFreezeHashMap.remove(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&x&E&D&2&4&2&9▶ &fАфк выключен!"));
            player.clearTitle();
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!plugin.statusFreezeHashMap.containsKey(player)) return;

        double distance = event.getFrom().distance(event.getTo());
        UUID uuid = player.getUniqueId();
        Double[] history = movementHistory.getOrDefault(uuid, new Double[]{0.0, 0.0, 0.0});
        history[0] = history[1];
        history[1] = history[2];
        history[2] = distance;
        movementHistory.put(uuid, history);

        boolean stableMovement = history[0] >= 0.3 && history[1] >= 0.3 && history[2] >= 0.3;
        if (stableMovement) {
            removeAfk(player);
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            removeAfk(player);
        }
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        switch (event.getCause()) {
            case DROWNING,
                 ENTITY_ATTACK,
                 ENTITY_SWEEP_ATTACK,
                 FIRE,
                 FIRE_TICK,
                 LAVA,
                 MAGIC,
                 PROJECTILE -> removeAfk(player);
            default -> {
            }
        }
    }
}