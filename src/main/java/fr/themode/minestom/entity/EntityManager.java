package fr.themode.minestom.entity;

import fr.themode.minestom.Main;
import fr.themode.minestom.instance.Instance;
import fr.themode.minestom.instance.InstanceManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EntityManager {

    private static InstanceManager instanceManager = Main.getInstanceManager();

    private ExecutorService creaturesPool = Executors.newFixedThreadPool(3);
    private ExecutorService playersPool = Executors.newFixedThreadPool(2);

    public void update() {

        for (Instance instance : instanceManager.getInstances()) {

            synchronized (instance) {
                // Creatures
                for (EntityCreature creature : instance.getCreatures()) {
                    creaturesPool.submit(() -> {
                        creature.update();
                        boolean shouldRemove = creature.shouldRemove();
                        if (shouldRemove) {
                            instance.removeEntity(creature);
                        }
                    });
                }

                // Players
                for (Player player : instance.getPlayers()) {
                    playersPool.submit(() -> {
                        player.update();
                        boolean shouldRemove = player.shouldRemove();
                        if (shouldRemove) {
                            instance.removeEntity(player);
                        }
                    });
                }
            }

        }

    }

}
