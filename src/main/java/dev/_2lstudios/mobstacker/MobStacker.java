package dev._2lstudios.mobstacker;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.mobstacker.commands.MobStackerCommand;
import dev._2lstudios.mobstacker.listeners.CreatureSpawnListener;
import dev._2lstudios.mobstacker.listeners.EntityDeathListener;
import dev._2lstudios.mobstacker.mob.StackedManager;
import dev._2lstudios.mobstacker.tasks.StackerTask;

public class MobStacker extends JavaPlugin {
    private StackedManager stackedManager;

    private Collection<EntityType> toEntityTypes(Collection<String> stringList) {
        Collection<EntityType> result = new HashSet<EntityType>();

        for (String string : stringList) {
            try {
                result.add(EntityType.valueOf((String) string));
            } catch (Exception exception) {
            }
        }

        return result;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Configuration config = getConfig();
        Collection<EntityType> blacklist = toEntityTypes(config.getStringList("blacklist"));
        Server server = getServer();
        PluginManager pluginManager = server.getPluginManager();
        int stackerTickRate = config.getInt("stacker_tick_rate");

        stackedManager = new StackedManager(blacklist);

        server.getScheduler().runTaskTimer(this, new StackerTask(this, stackedManager),
                stackerTickRate,
                stackerTickRate);

        pluginManager.registerEvents(new EntityDeathListener(stackedManager), this);
        pluginManager.registerEvents(new CreatureSpawnListener(stackedManager), this);

        getCommand("mobstacker").setExecutor(new MobStackerCommand(this, stackedManager));
    }

    public void reload() {
        reloadConfig();

        Collection<EntityType> blacklist = toEntityTypes(getConfig().getStringList("blacklist"));

        stackedManager.setBlacklist(blacklist);
    }
}
