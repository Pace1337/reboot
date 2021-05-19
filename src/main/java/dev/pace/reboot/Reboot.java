package dev.pace.reboot;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Reboot extends JavaPlugin {
    public static int rebootTaskID = 0;
    public static boolean isRebooting = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("reboot")) {
                if (!sender.hasPermission("reboot.use")) {
                    sender.sendMessage("You have no permission to execute this command.");
                    return true;
                }
                if (isRebooting) {
                    sender.sendMessage("This server is already rebooting :), use /cancelreboot to cancel the reboot.");
                    // In case someone decides to spam the reboot command, stops them from doing it.
                    return false;
                }

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say Restarting in 60 seconds!");
                new BukkitRunnable() {
                    int counter = 60;

                    public void run() {
                        isRebooting = true;
                        rebootTaskID = this.getTaskId();
                        counter--;
                        if (counter == 50 || counter == 40 || counter == 30 || counter == 20 || counter == 10 || counter == 1) {
                            Bukkit.broadcastMessage("Server Restarting in " + counter + " seconds!");
                        }

                        if (counter == 0) {

                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
                        }
                    }
                }.runTaskTimer(this, 20, 20);
            } else if (cmd.getName().equalsIgnoreCase("cancelreboot")) {
                if (!sender.hasPermission("reboot.use")) {
                    sender.sendMessage("You have no permission to execute this command.");
                    return true;
                }

                if (isRebooting && rebootTaskID != 0) {
                    Bukkit.getScheduler().cancelTask(rebootTaskID);
                    isRebooting = false;
                    sender.sendMessage("The reboot has been cancelled by a operator.");
                }
            }

        }
        return true;
    }

}
