package dev._2lstudios.mobstacker.mob;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

public class StackedManager {
    private Map<UUID, Stacked> mobsMap = new HashMap<UUID, Stacked>();
    private Collection<EntityType> blacklist;

    public StackedManager(Collection<EntityType> blacklist) {
        this.blacklist = blacklist;
    }

    public boolean isBlacklisted(EntityType type) {
        return this.blacklist.contains((Object)type);
    }

    public Stacked getMobOrNull(Creature entity) {
        return this.mobsMap.getOrDefault(entity.getUniqueId(), null);
    }

    public Stacked getMobOrNew(Creature entity) {
        return this.mobsMap.getOrDefault(entity.getUniqueId(), new Stacked(entity));
    }

    public Stacked getMob(Creature entity) {
        Stacked mob = getMobOrNew(entity);
        UUID uniqueId = entity.getUniqueId();

        mobsMap.put(uniqueId, mob);

        return mob;
    }

    public Collection<Stacked> getMobs() {
        return this.mobsMap.values();
    }

    public void removeMap(Creature entity) {
        this.mobsMap.remove(entity.getUniqueId());
    }

    public void removeEntity(Creature entity) {
        this.removeMap(entity);
        entity.setCustomName(null);
        entity.remove();
        entity.setRemoveWhenFarAway(true);
    }

    public Map<UUID, Stacked> getMobsMap() {
        return mobsMap;
    }
}