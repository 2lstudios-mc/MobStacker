package dev._2lstudios.mobstacker.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;

import dev._2lstudios.mobstacker.MobStacker;
import dev._2lstudios.mobstacker.mob.StackedManager;
import dev._2lstudios.mobstacker.placeholder.Placeholder;

public class MobStackerCommand implements CommandExecutor {
    private MobStacker mobStacker;
    private StackedManager stackedManager;

    public MobStackerCommand(MobStacker mobStacker, StackedManager stackedManager) {
        this.mobStacker = mobStacker;
        this.stackedManager = stackedManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Configuration config = mobStacker.getConfig();

        if (args.length > 0) {
            if (args[0].equals("reload")) {
                if (sender.hasPermission("mobstacker.reload")) {
                    sender.sendMessage(Placeholder.replace(config.getString("reload")));
                } else {
                    sender.sendMessage(Placeholder.replace(config.getString("no_permission")));
                }
            } else {
                sender.sendMessage(Placeholder.replace(config.getString("argument_not_found"),
                        new Placeholder("%argument%", args[0])));
            }
        } else {
            if (sender.hasPermission("mobstacker.usage")) {
                sender.sendMessage(Placeholder.replace(config.getString("statistics"),
                        new Placeholder("%total_mobs%", stackedManager.getTotalMobsSpawned()),
                        new Placeholder("%total_stacked%", stackedManager.getTotalMobsStacked()),
                        new Placeholder("%stack_threshold%", config.getInt("stack_threshold"))));
            } else {
                sender.sendMessage(Placeholder.replace(config.getString("no_permission")));
            }
        }

        return true;
    }
}
