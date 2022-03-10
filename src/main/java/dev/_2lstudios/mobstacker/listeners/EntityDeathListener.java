package dev._2lstudios.mobstacker.listeners;

import dev._2lstudios.mobstacker.mob.StackedManager;
import dev._2lstudios.mobstacker.mob.Stacked;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener
implements Listener {
    private StackedManager stackedManager;

    public EntityDeathListener(StackedManager stackedManager) {
        this.stackedManager = stackedManager;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Creature) {
            Creature creature = (Creature)entity;
            Stacked mob = this.stackedManager.getMob(creature);
            int count = mob.add(-1);
            if (count > 0) {
                Location location = entity.getLocation();
                World world = location.getWorld();
                Creature creature1 = (Creature)world.spawn(location, creature.getClass());
                Stacked mob1 = this.stackedManager.getMob(creature1);
                creature.setCustomName(null);
                if (count > 1) {
                    creature1.setCustomName(String.valueOf((Object)ChatColor.YELLOW + "x" + count));
                }
                mob1.set(count);
            }
            this.stackedManager.removeMap(creature);
        }
    }
}

