package entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class EnemyController {
    private long lastSpawnTime;
    private float spawnCooldown = 5; // spawn every 5 seconds
    private float spawnRadius = 800; // spawn within 800 units of the player
    private Player player;
    private Hashtable<Integer, Enemy> enemies;
    private Queue<Integer> availableIds;
    Texture enemyTexture;
	Enemy enemy;

    public EnemyController(Player player, World world) {
        this.player = player;
        this.enemies = new Hashtable<>();
        this.availableIds = new LinkedList<>();
        for (int i = 1; i <= 100; i++) {
            availableIds.add(i);
        }
        this.lastSpawnTime = TimeUtils.nanoTime();
    }

    public void update() {
        if (TimeUtils.timeSinceNanos(lastSpawnTime) > spawnCooldown * 10000) {
            spawnEnemy();
            lastSpawnTime = TimeUtils.nanoTime();
        }
    }
    //TODO: Implement Spawning Method in base enemy class
    private void spawnEnemy() {
        if (!availableIds.isEmpty()) {
            int id = availableIds.poll();
            float angle = (float) (Math.random() * 2 * Math.PI);
            float x = player.getPosition().x + spawnRadius * (float) Math.cos(angle);
            float y = player.getPosition().y + spawnRadius * (float) Math.sin(angle);

            // Create your enemy here and add it to the enemies list
            Enemy enemy = new Enemy(,x, y, 5, 100, 10, world, player, ""+id);
            enemy.setName("enemy" + id);
            enemies.put(id, enemy);
        } else {
            System.out.println("Maximum number of enemies reached.");
        }
    }

    public void killEnemy(int id) {
        if (enemies.containsKey(id)) {
            enemies.remove(id);
            availableIds.add(id);
        }
    }
}
