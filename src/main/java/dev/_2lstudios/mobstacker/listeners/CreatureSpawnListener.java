package dev._2lstudios.mobstacker.listeners;

import dev._2lstudios.mobstacker.mob.StackedManager;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawnListener
        implements Listener {
    private StackedManager stackedManager;

    public CreatureSpawnListener(StackedManager stackedManager) {
        this.stackedManager = stackedManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!this.stackedManager.isBlacklisted(event.getEntityType())) {
            LivingEntity entity = event.getEntity();

            if (entity instanceof Creature) {
                Creature creature = (Creature) entity;

                this.stackedManager.getMob(creature);
            }
        }

        this.stackedManager.addTotalMobsSpawned();
    }
}
