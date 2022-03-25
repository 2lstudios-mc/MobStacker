package dev._2lstudios.mobstacker.listeners;

import dev._2lstudios.mobstacker.mob.StackedManager;
import org.bukkit.Chunk;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnloadListener
        implements Listener {
    private StackedManager stackedManager;

    public ChunkUnloadListener(StackedManager stackedManager) {
        this.stackedManager = stackedManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        
        for (Entity entity : chunk.getEntities()) {
            if (!(entity instanceof Creature)) {
                continue;
            }

            stackedManager.removeEntity((Creature) entity);
        }
    }
}
