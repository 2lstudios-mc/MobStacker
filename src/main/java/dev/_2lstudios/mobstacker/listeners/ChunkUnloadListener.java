package dev._2lstudios.mobstacker.listeners;

import dev._2lstudios.mobstacker.mob.MobManager;
import org.bukkit.Chunk;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnloadListener
implements Listener {
    private MobManager mobManager;

    public ChunkUnloadListener(MobManager mobManager) {
        this.mobManager = mobManager;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        for (Entity entity : chunk.getEntities()) {
            if (!(entity instanceof Creature) || !entity.isValid()) continue;
            this.mobManager.removeEntity((Creature)entity);
        }
    }
}

