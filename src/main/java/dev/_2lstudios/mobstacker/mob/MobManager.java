package dev._2lstudios.mobstacker.mob;

import dev._2lstudios.mobstacker.mob.StackedMob;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

public class MobManager {
    private Map<UUID, StackedMob> mobMap = new HashMap<UUID, StackedMob>();
    private Collection<EntityType> blacklist;

    public MobManager(Collection<EntityType> blacklist) {
        this.blacklist = blacklist;
    }

    public boolean isBlacklisted(EntityType type) {
        return this.blacklist.contains((Object)type);
    }

    public final StackedMob getMob(Creature entity) {
        StackedMob mob = this.mobMap.getOrDefault(entity.getUniqueId(), null);
        if (mob == null) {
            mob = new StackedMob(entity);
            this.mobMap.put(entity.getUniqueId(), mob);
        }
        return mob;
    }

    public final Collection<StackedMob> getMobs() {
        return this.mobMap.values();
    }

    public void removeMap(Creature entity) {
        this.mobMap.remove(entity.getUniqueId());
    }

    public void removeEntity(Creature entity) {
        this.removeMap(entity);
        entity.setCustomName(null);
        entity.remove();
        entity.setRemoveWhenFarAway(true);
    }
}

