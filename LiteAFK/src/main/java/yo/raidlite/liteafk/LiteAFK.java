package yo.raidlite.liteafk;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import yo.raidlite.liteafk.commands.AfkCommand;
import yo.raidlite.liteafk.listeners.AfkListener;

import java.util.HashMap;
import java.util.Objects;

public final class LiteAFK extends JavaPlugin {

    public HashMap<Player, StatusAFK> statusFreezeHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        AfkCommand commandFreeze = new AfkCommand(this);
        Objects.requireNonNull(getCommand("afk")).setExecutor(commandFreeze);
        getServer().getPluginManager().registerEvents(new AfkListener(this), this);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateStatus(player);
                }
            }
        }.runTaskTimer(this, 0, 20L);
    }

    public void updateStatus(Player player) {
        if (statusFreezeHashMap.get(player) != null) {
            if (statusFreezeHashMap.get(player) == StatusAFK.AFK) {
                player.sendTitle("", "§cAFK", 0, 30, 0);
            }
        }
    }
}
