package dev.pace.reboot;

import dev.pace.reboot.commands.RebootReload;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Reboot extends JavaPlugin {
    public int rebootTaskID = 0;
    public boolean isRebooting = false;
    public static FileConfiguration config;
    public static Reboot instance = null;


    public static Reboot getInstance() {
        return instance;
    }

    public void reloadConfiguration() {
        this.reloadConfig();
        config = this.getConfig();
    }

    @Override
    public void onEnable() {
        Reboot.instance = this;
        getCommand("rebootreload").setExecutor(new RebootReload());
        Reboot.config = this.getConfig();
        Reboot.config.options().copyDefaults(true);
        this.saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("reboot")) {
                if (!sender.hasPermission("reboot.use")) {
                    sender.sendMessage("You have no permission to execute this command.");
                    return true;
                }
                if (isRebooting) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Reboot.config.getString("reboot.already_rebooting")));
                    // In case someone decides to spam the reboot command, stops them from doing it.
                    return false;
                }

                Bukkit.broadcastMessage((ChatColor.translateAlternateColorCodes('&',Reboot.config.getString("reboot.highermessage"))));
                BukkitRunnable bkt = new BukkitRunnable() {
                    int counter = 60;
                    public void run() {
                        isRebooting = true;
                        counter--;
                        if (counter == 50 || counter == 40 || counter == 30 || counter == 20 || counter == 10 || counter == 1) {
                            Bukkit.broadcastMessage((ChatColor.translateAlternateColorCodes('&',Reboot.config.getString("reboot.lowermessage") + counter)));
                        }
                        if (counter == 0) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
                        }
                    }
                };
                bkt.runTaskTimer(this, 20, 20);
                this.rebootTaskID = bkt.getTaskId();
            } else if (cmd.getName().equalsIgnoreCase("cancelreboot")) {
                if (!sender.hasPermission("reboot.use")) {
                    sender.sendMessage("You have no permission to execute this command.");
                    return true;
                }
                if (isRebooting && rebootTaskID != 0) {
                    Bukkit.getScheduler().cancelTask(rebootTaskID);
                    isRebooting = false;
                    sender.sendMessage((ChatColor.translateAlternateColorCodes('&',Reboot.config.getString("reboot.cancel_message"))));
                }
            }
        }
        return true;
    }   
}
