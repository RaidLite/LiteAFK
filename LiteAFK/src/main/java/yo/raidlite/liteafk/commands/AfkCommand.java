package yo.raidlite.liteafk.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import yo.raidlite.liteafk.LiteAFK;
import yo.raidlite.liteafk.StatusAFK;

public record AfkCommand(LiteAFK plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equalsIgnoreCase("afk")) {
            if (commandSender instanceof Player player) {
                if (plugin.statusFreezeHashMap.get(player) != StatusAFK.AFK) {
                    plugin.statusFreezeHashMap.put(player, StatusAFK.AFK);
                    plugin.updateStatus(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&x&0&0&d&8&f&f▶ &fАфк включен!"));
                } else {
                    plugin.statusFreezeHashMap.remove(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&x&E&D&2&4&2&9▶ &fАфк выключен!"));
                    player.clearTitle();
                }
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&x&E&D&2&4&2&9▶ &fКоманду может использовать только &6игрок."));
            }
            return true;
        }
        return false;
    }
}