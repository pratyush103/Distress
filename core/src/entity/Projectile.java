package entity;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;


public class Projectile extends Entity {
    float projectileX, projectileY;
    Vector2 projectilePosition;
    float projectileSpeed;
    int projectileDamage;
    String projectileName;
    Vector2 direction;
    Vector2 origin;
    float projectileRotationAngle;
    float distanceTravelled=0;
    SpriteBatch batch;
    int projectileId;
    public static ArrayList<Projectile> projectileList = new ArrayList<Projectile>();
    //Texture projectileTexture;
    //private boolean isMarkedForRemoval = false;
    public static Player player;

    public Projectile(Texture projectileTexture, float x, float y, float speed, int damage,World world, Player player,Vector2 direction,String projectileModifier, int projectileId) {
        super(projectileTexture, x, y,world, true, "projectile"+projectileModifier);
        projectileName = "projectile"+projectileModifier;
        Projectile.player = player;
        this.projectileId = projectileId;

        projectileX = x;
        projectileY = y;
        this.origin = new Vector2(projectileX, projectileY);

        this.projectilePosition = new Vector2(this.body.getPosition().x, this.body.getPosition().y);
        super.sprite.setPosition(projectilePosition.x, projectilePosition.y);
        projectileSpeed = speed;
        projectileDamage = damage;
        this.direction = direction;
        projectileRotationAngle = (float) Math.toDegrees(Math.atan2(direction.y, direction.x));
        body.setTransform(projectilePosition.x, projectilePosition.y, projectileRotationAngle);
        body.setFixedRotation(true);
        projectileList.add(this);
    }

    public void update(SpriteBatch batch) {
        // Move the bullet in its direction at its speed
        body.applyLinearImpulse(direction.scl(projectileSpeed), body.getWorldCenter(), true);
        
        distanceTravelled = distanceTravelled+origin.dst(projectilePosition);
        
        if (distanceTravelled>20){
            this.killBullet();
        }
        // if (isMarkedForRemoval) {
        //     player.removeBullet(this.projectileId);
        //     projectileList.remove(this);
        //     //removeEntity(this.projectileName);
        //     markForRemoval(this.projectileName);
        // }
        



    }

    public void draw(SpriteBatch batch) {
        super.draw(batch, projectilePosition.x,projectilePosition.y);
    }

    

    

    public void CollisionHandler(Entity entity) {
        // Check for collision with an enemy
        String userData =(String) entity.body.getUserData();
        if (entity instanceof Enemy) {
            ((Enemy) entity).takeDamage(projectileDamage);
            this.killBullet();
        }
    }

    public void killBullet() {
        player.removeBullet(this.projectileId);
        projectileList.remove(this);
        markForRemoval(this.projectileName);
    }
}
