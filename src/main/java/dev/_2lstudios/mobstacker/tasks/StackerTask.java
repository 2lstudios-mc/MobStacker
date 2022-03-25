package dev._2lstudios.mobstacker.tasks;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import dev._2lstudios.mobstacker.MobStacker;
import dev._2lstudios.mobstacker.mob.Stacked;
import dev._2lstudios.mobstacker.mob.StackedManager;

public class StackerTask implements Runnable {
    private static int MERGE_DISTANCE = 10;

    private MobStacker mobStacker;
    private StackedManager stackedManager;
    private Map<UUID, Stacked> mobsMap;
    private Collection<Stacked> updatedMobs;

    public StackerTask(MobStacker mobStacker, StackedManager stackedManager) {
        this.mobStacker = mobStacker;
        this.stackedManager = stackedManager;
        this.mobsMap = stackedManager.getMobsMap();
        this.updatedMobs = stackedManager.getUpdatedMobs();
    }

    private void merge(Creature creature, Stacked stacked, Creature otherCreature, Stacked otherStackedMob) {
        int stackedCount = stacked.getCount();

        creature.remove();

        if (stackedCount > 0) {
            otherStackedMob.add(stackedCount);
            stacked.set(0);
            otherCreature.setCustomName(
                    String.valueOf(ChatColor.YELLOW + "x" + otherStackedMob.getCount()));
            stackedManager.addTotalMobsStacked();
        }
    }

    public void run() {
        Iterator<Stacked> iterator = updatedMobs.iterator();
        Configuration config = mobStacker.getConfig();
        int stackThreshold = config.getInt("stack_threshold");
        boolean separateAge = config.getBoolean("separate_age");

        while (iterator.hasNext()) {
            Stacked stacked = iterator.next();
            Creature creature = stacked.getEntity();

            iterator.remove();

            if (creature.isValid() && stacked.getCount() > 0) {
                Location location = creature.getLocation();
                Entity[] entities = location.getChunk().getEntities();
                EntityType entityType = creature.getType();
                int age = stacked.getAge();
                int sameTypeCount = 0;

                for (Entity otherEntity : entities) {
                    if (creature != otherEntity && otherEntity.isValid() && entityType == otherEntity.getType()
                            && location.distance(otherEntity.getLocation()) <= MERGE_DISTANCE) {
                        Creature otherCreature = (Creature) otherEntity;
                        Stacked otherStacked = stackedManager.getMob(otherCreature);
                        int otherAge = otherStacked.getAge();

                        if ((!separateAge || (age == otherAge)) && ++sameTypeCount >= stackThreshold) {
                            int mobCount = stacked.getCount();
                            int otherMobCount = otherStacked.getCount();

                            if (otherMobCount > mobCount) {
                                merge(creature, stacked, otherCreature, otherStacked);
                                mobsMap.remove(creature.getUniqueId());
                                mobsMap.put(otherCreature.getUniqueId(), otherStacked);
                                break;
                            } else {
                                merge(otherCreature, otherStacked, creature, stacked);
                            }
                        }
                    }
                }
            } else {
                mobsMap.remove(creature.getUniqueId());
            }
        }
    }
}
