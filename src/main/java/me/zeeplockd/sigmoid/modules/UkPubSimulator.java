package me.zeeplockd.sigmoid.modules;

import me.zeeplockd.sigmoid.Sigmoid;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

public class UkPubSimulator extends Module implements Runnable {
    private static final float MAX_REACH = 6.0f;
    private Thread thread;
    private volatile boolean running = false;

    public UkPubSimulator() {
        super(Sigmoid.CATEGORY, "uk-pub-simulator", "A module that simulates a pub brawl with Killaura.");
    }

    @Override
    public void onActivate() {
        super.onActivate();
        running = true;
        thread = new Thread(this);
        thread.start();
    }

            public void onDeactivate() {
            super.onDeactivate();
            running = false;
            if (thread != null && thread.isAlive()) thread.interrupt();
        }

        @Override
        public void run() {
            while (running) {
                if (mc.player == null || mc.world == null || mc.interactionManager == null) return;

                LivingEntity nearest = null;
                double closest = Double.MAX_VALUE;

                for (Entity entity : mc.world.getEntities()) {
                    if (entity == mc.player) continue;
                    if (!(entity instanceof LivingEntity)) continue;

                    double dist = mc.player.distanceTo(entity);
                    if (dist < closest) {
                        closest = dist;
                        nearest = (LivingEntity) entity;
                    }
                    if (nearest != null && mc.player.distanceTo(nearest) <= MAX_REACH) {
                        mc.interactionManager.attackEntity(mc.player, nearest);
                        try {
	                        Thread.sleep(0, 1);
                        } catch (InterruptedException e) {
	                        e.printStackTrace();
	                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}


