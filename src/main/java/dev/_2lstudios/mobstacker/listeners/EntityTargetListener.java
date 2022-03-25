package dev._2lstudios.mobstacker.listeners;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import dev._2lstudios.mobstacker.mob.Stacked;
import dev._2lstudios.mobstacker.mob.StackedManager;

public class EntityTargetListener implements Listener {
    private Map<UUID, Stacked> mobsMap;
    private Collection<Stacked> updatedMobs;

    public EntityTargetListener(StackedManager stackedManager) {
        this.mobsMap = stackedManager.getMobsMap();
        this.updatedMobs = stackedManager.getUpdatedMobs();
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Creature) {
            UUID uuid = entity.getUniqueId();

            if (mobsMap.containsKey(uuid)) {
                Stacked stacked = mobsMap.get(uuid);

                updatedMobs.add(stacked);
            }
        }
    }
}