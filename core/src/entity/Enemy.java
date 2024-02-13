package entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.ai.*;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Enemy extends Entity {
    float enemyX, enemyY;
    float enemySpeed;
    int enemyHealth;
    int enemyCollisionDamage;
    Vector2 position;
    Texture enemyTexture;
    public static Player player;

    public Enemy(Texture enemyTexture, float x, float y, float speed, int health, int collisionDamage,World world, Player player) {
        super(enemyTexture, x, y,world, true, "enemy");
        
        Enemy.player = player;
        
        enemyX = x;
        enemyY = y;
        enemySpeed = speed;
        enemyHealth = health;
        enemyCollisionDamage = collisionDamage;
        position = new Vector2(enemyX, enemyY);
        super.sprite.setPosition(position.x, position.y);
    }
    

    public float getPlayerDistance(){
        float distance=(float) Math.sqrt(Math.pow(player.getPosition().x-position.x,2)+Math.pow(player.getPosition().y-position.y,2));
        return distance;        

    } 


    
    public int getCollisionDamage() {
        return enemyCollisionDamage;
    }
    
    public void draw(SpriteBatch batch) {
        super.draw(batch, position.x, position.y);
    }
}
