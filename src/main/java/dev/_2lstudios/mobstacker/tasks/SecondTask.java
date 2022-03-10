package dev._2lstudios.mobstacker.tasks;

import dev._2lstudios.mobstacker.mob.MobManager;
import dev._2lstudios.mobstacker.mob.StackedMob;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

public class SecondTask {
    public SecondTask(Plugin plugin, MobManager mobManager) {
        Server server = plugin.getServer();
        server.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            Iterator<StackedMob> iterator = mobManager.getMobs().iterator();
            while (iterator.hasNext()) {
                StackedMob mob = iterator.next();
                Creature creature = mob.getEntity();
                if (creature.isValid()) {
                    EntityType entityType = creature.getType();
                    Location location = creature.getLocation();
                    double y = location.getY();
                    for (Entity entity1 : location.getChunk().getEntities()) {
                        if (entity1 == null || creature == entity1 || entityType != entity1.getType() || !(entity1 instanceof Creature) || !entity1.isValid() || !(Math.abs(y - entity1.getLocation().getY()) < 10.0)) continue;
                        Creature creature1 = (Creature)entity1;
                        StackedMob mob1 = mobManager.getMob(creature1);
                        int mobCount = mob.getCount();
                        int mob1Count = mob1.getCount();
                        int mobCountPlus = mob.getCount() + mob1.getCount();
                        if (mob1Count >= mobCount) {
                            mob1.set(mobCountPlus);
                            creature.remove();
                            iterator.remove();
                            entity1.setCustomName(String.valueOf((Object)ChatColor.YELLOW + "x" + mobCountPlus));
                            continue;
                        }
                        mob.set(mobCountPlus);
                        creature1.remove();
                        creature.setCustomName(String.valueOf((Object)ChatColor.YELLOW + "x" + mobCountPlus));
                    }
                    continue;
                }
                iterator.remove();
            }
        }, 20L, 20L);
    }
}

