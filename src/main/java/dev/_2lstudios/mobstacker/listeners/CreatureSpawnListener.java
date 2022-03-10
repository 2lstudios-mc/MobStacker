package dev._2lstudios.mobstacker.listeners;

import dev._2lstudios.mobstacker.mob.MobManager;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawnListener
implements Listener {
    private MobManager mobManager;

    public CreatureSpawnListener(MobManager mobManager) {
        this.mobManager = mobManager;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (this.mobManager.isBlacklisted(event.getEntityType())) {
            return;
        }
        LivingEntity entity = event.getEntity();
        if (entity instanceof Creature) {
            Creature creature = (Creature)entity;
            this.mobManager.getMob(creature);
        }
    }
}

