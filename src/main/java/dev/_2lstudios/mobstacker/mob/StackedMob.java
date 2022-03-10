package dev._2lstudios.mobstacker.mob;

import org.bukkit.entity.Creature;

public class StackedMob {
    private final Creature entity;
    private int count = 1;

    public StackedMob(Creature entity) {
        this.entity = entity;
    }

    public int add(int count) {
        return this.count += count;
    }

    public int getCount() {
        return this.count;
    }

    public Creature getEntity() {
        return this.entity;
    }

    public void set(int count) {
        this.count = count;
    }
}

