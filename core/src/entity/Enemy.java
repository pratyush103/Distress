package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.ai.*;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.ai.utils.Location;



public class Enemy extends Entity implements Steerable<Vector2>{
    float enemyX, enemyY;
    Vector2 enemyPosition;
    float enemySpeed;
    int enemyHealth;
    int enemyCollisionDamage;
    String enemyName;
    //Texture enemyTexture;
    SteeringBehavior<Vector2> behavior;
    public static Player player;

    public Enemy(Texture enemyTexture, float x, float y, float speed, int health, int collisionDamage,World world, Player player,String enemyModifier) {
        super(enemyTexture, x, y,world, true, "enemy"+enemyModifier);
        enemyName = "enemy"+enemyModifier;
        Enemy.player = player;
        
        enemyX = x;
        enemyY = y;
        this.enemyPosition = new Vector2(this.body.getPosition().x, this.body.getPosition().y);
        super.sprite.setPosition(enemyX, enemyY);
        enemySpeed = speed;
        enemyHealth = health;
        enemyCollisionDamage = collisionDamage;
        behavior = new Seek<Vector2>(this, player);


    }
    

    public float getPlayerDistance(){
        float distance=(float) Math.sqrt(Math.pow(player.getPosition().x-enemyPosition.x,2)+Math.pow(player.getPosition().y-enemyPosition.y,2));
        return distance;        

    }
    public int getCollisionDamage() {
        return enemyCollisionDamage;
    }

    public void handleCollision(Entity entity) {
        if (entity instanceof Enemy) {
            // Do nothing
        }
        
    }

    public void update(SpriteBatch batch) {
        // Calculate the steering acceleration
        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        behavior.calculateSteering(steering);
    
        // Calculate the direction to the player
        Vector2 direction = player.getPosition().cpy().sub(body.getPosition());
    
        // Normalize the direction, scale it by the enemy's speed, and apply the steering acceleration
        Vector2 velocity = direction.nor().scl(enemySpeed).add(steering.linear);
    
        // Set the enemy's linear velocity
        body.setLinearVelocity(velocity);
        
        enemyPosition.set(this.body.getPosition().x, this.body.getPosition().y);
        
        draw(batch);
        // Update the sprite's position to match the body's position
        //sprite.setPosition(body.getPosition().x, body.getPosition().y);
    }
    
    public void draw(SpriteBatch batch) {
        super.draw(batch, enemyPosition.x, enemyPosition.y);
    }

    public void killEnemy() {
        markForRemoval(this.enemyName);     
    }

    public void takeDamage(int damage) {
        enemyHealth -= damage;
        if (enemyHealth <= 0) {
            killEnemy();
        }
    }

    // Implementing the missing abstract methods
    @Override
    public Location<Vector2> newLocation() {
        return null;
    }

    @Override
    public float getOrientation() {
        return 0;
    }

    @Override
    public void setOrientation(float orientation) {
    }

    
    public float vectorToAngle(Vector2 vector) {
        return 0;
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return null;
    }

    @Override
    public float getMaxAngularSpeed() {
        return 0;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
    }

    @Override
    public float getMaxAngularAcceleration() {
        return 0;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
    }

    @Override
    public float getMaxLinearSpeed() {
        return 0;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
    }

    @Override
    public float getMaxLinearAcceleration() {
        return 0;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        // Implement the logic for setting the maximum linear acceleration
    }

    @Override
    public Vector2 getPosition() {
        return enemyPosition;
    }

    @Override
    public float getBoundingRadius() {
        return this.getWidth()/2;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public void setTagged(boolean tagged) {

    }

    @Override
    public Vector2 getLinearVelocity() {
        return this.body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return this.body.getAngularVelocity();
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return this.body.getLinearVelocity().len();
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
    }

    

}
