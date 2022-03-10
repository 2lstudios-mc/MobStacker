package dev._2lstudios.mobstacker;

import dev._2lstudios.mobstacker.listeners.CreatureSpawnListener;
import dev._2lstudios.mobstacker.listeners.EntityDeathListener;
import dev._2lstudios.mobstacker.mob.StackedManager;
import dev._2lstudios.mobstacker.tasks.SecondTask;
import java.util.Collection;
import java.util.HashSet;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MobStacker
extends JavaPlugin {
    private Collection<EntityType> toEntityTypes(Collection<String> stringList) {
        HashSet<EntityType> result = new HashSet<EntityType>();
        for (String string : stringList) {
            try {
                result.add(EntityType.valueOf((String)string));
            }
            catch (Exception exception) {}
        }
        return result;
    }

    public void onEnable() {
        Collection<EntityType> blacklist = this.toEntityTypes(this.getConfig().getStringList("blacklist"));
        StackedManager stackedManager = new StackedManager(blacklist);
        PluginManager pluginManager = this.getServer().getPluginManager();
        new SecondTask((Plugin)this, stackedManager);
        pluginManager.registerEvents((Listener)new EntityDeathListener(stackedManager), (Plugin)this);
        pluginManager.registerEvents((Listener)new CreatureSpawnListener(stackedManager), (Plugin)this);
    }
}

