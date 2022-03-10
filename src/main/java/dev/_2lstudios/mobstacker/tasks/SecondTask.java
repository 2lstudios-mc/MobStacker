package dev._2lstudios.mobstacker.tasks;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.mobstacker.mob.StackedManager;
import dev._2lstudios.mobstacker.mob.Stacked;

public class SecondTask {
    private void merge(Creature creature, Stacked stacked, Creature otherCreature, Stacked otherStackedMob) {
        creature.remove();
        otherStackedMob.add(stacked.getCount());
        stacked.set(0);
        otherCreature.setCustomName(
                String.valueOf(ChatColor.YELLOW + "x" + otherStackedMob.getCount()));
    }

    public SecondTask(Plugin plugin, StackedManager stackedManager) {
        Server server = plugin.getServer();
        Map<UUID, Stacked> toAdd = new ConcurrentHashMap<>();

        server.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            Map<UUID, Stacked> mobsMap = stackedManager.getMobsMap();
            Iterator<Stacked> iterator = mobsMap.values().iterator();

            while (iterator.hasNext()) {
                Stacked stacked = iterator.next();
                Creature creature = stacked.getEntity();

                if (creature.isValid() || stacked.getCount() > 0) {
                    EntityType entityType = creature.getType();
                    Location location = creature.getLocation();
                    double y = location.getY();

                    for (Entity otherEntity : location.getChunk().getEntities()) {
                        if (creature != otherEntity && entityType == otherEntity.getType()
                                && otherEntity.isValid()
                                && Math.abs(y - otherEntity.getLocation().getY()) <= 10.0) {
                            Creature otherCreature = (Creature) otherEntity;
                            Stacked otherStackedMob = stackedManager.getMobOrNew(otherCreature);
                            int mobCount = stacked.getCount();
                            int otherMobCount = otherStackedMob.getCount();

                            if (otherMobCount > mobCount) {
                                merge(creature, stacked, otherCreature, otherStackedMob);
                            } else {
                                merge(otherCreature, otherStackedMob, creature, stacked);
                            }
                        }
                    }
                } else {
                    iterator.remove();
                }
            }

            mobsMap.putAll(toAdd);
            toAdd.clear();
        }, 20L, 20L);
    }
}
