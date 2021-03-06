package dev._2lstudios.mobstacker.mob;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creature;

public class Stacked {
    private final Creature entity;
    private int count = 1;

    public Stacked(Creature entity) {
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

    public int getAge() {
        if (entity instanceof Ageable) {
            int age = ((Ageable) entity).getAge();

            return age < 0 ? -1 : 0;
        }

        return 0;
    }
}

