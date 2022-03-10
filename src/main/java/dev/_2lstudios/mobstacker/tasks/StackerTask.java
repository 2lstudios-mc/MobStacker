package dev._2lstudios.mobstacker.tasks;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    private MobStacker mobStacker;
    private StackedManager stackedManager;
    private Map<UUID, Stacked> mobsMap;
    private Map<UUID, Stacked> toAdd;

    public StackerTask(MobStacker mobStacker, StackedManager stackedManager) {
        this.mobStacker = mobStacker;
        this.stackedManager = stackedManager;
        this.mobsMap = stackedManager.getMobsMap();
        this.toAdd = new ConcurrentHashMap<>();
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
        Iterator<Stacked> iterator = mobsMap.values().iterator();
        Configuration config = mobStacker.getConfig();
        int stackThreshold = config.getInt("stack_threshold");
        boolean separateAge = config.getBoolean("separate_age");

        while (iterator.hasNext()) {
            Stacked stacked = iterator.next();
            Creature creature = stacked.getEntity();

            if (creature.isValid() || stacked.getCount() > 0) {
                EntityType entityType = creature.getType();
                Location location = creature.getLocation();
                double y = location.getY();
                int age = stacked.getAge();
                int sameTypeCount = 0;

                for (Entity otherEntity : location.getChunk().getEntities()) {
                    if (creature != otherEntity && otherEntity.isValid() && entityType == otherEntity.getType()
                            && Math.abs(y - otherEntity.getLocation().getY()) <= 10.0) {
                        Creature otherCreature = (Creature) otherEntity;
                        Stacked otherStackedMob = stackedManager.getMobOrNew(otherCreature);
                        int otherAge = otherStackedMob.getAge();

                        if ((!separateAge || (age == otherAge)) && ++sameTypeCount >= stackThreshold) {
                            int mobCount = stacked.getCount();
                            int otherMobCount = otherStackedMob.getCount();

                            if (otherMobCount > mobCount) {
                                merge(creature, stacked, otherCreature, otherStackedMob);
                            } else {
                                merge(otherCreature, otherStackedMob, creature, stacked);
                            }
                        }
                    }
                }
            } else {
                iterator.remove();
            }
        }

        mobsMap.putAll(toAdd);
        toAdd.clear();
    }
}
