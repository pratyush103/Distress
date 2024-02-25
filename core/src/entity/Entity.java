package entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.MathUtils;

import java.util.*;

// TODO: Implement an Object to store additional data in the setUserData method
//! Current Implementation: String entityName and Typecast to String in the getEntity method

public class Entity {
    Sprite sprite;
    float x,y;
    SpriteBatch batch;
    Body body;
    private float scale = 0.3f;
    public static HashMap<String, Entity> entityList = new HashMap<String, Entity>();
    public static World world;
    private static HashMap<String, Entity> markedForRemoval= new HashMap<String, Entity>();
    /**
     * Represents an entity in the game world.
     * An entity has a position, a sprite, and a physical body.
     */
    public Entity(Texture texture, float posX,float posY, World world, boolean isDynamic,String entityName){
        x=posX;
        y=posY;
        this.world=world;
        sprite= new Sprite(texture);
        sprite.setPosition(x,y);
        sprite.setScale(scale);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isDynamic? (BodyDef.BodyType.DynamicBody):(BodyDef.BodyType.StaticBody); // Change to StaticBody for immovable objects
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((sprite.getWidth()*scale) / 2, (sprite.getHeight()*scale) / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        body.createFixture(fixtureDef);
        body.setUserData(entityName);
        entityList.put(entityName, this);

        shape.dispose();
    }
    
    public void draw(SpriteBatch batch, float x, float y) {
       
        float bodyX = body.getPosition().x - (sprite.getWidth()) / 2;
        float bodyY = body.getPosition().y - (sprite.getHeight()) / 2;

        batch.draw(sprite, bodyX, bodyY,
                    sprite.getWidth() / 2, 
                    sprite.getHeight() / 2, 
                    sprite.getWidth(), 
                    sprite.getHeight(), scale, scale, 
                    MathUtils.radiansToDegrees * body.getAngle());
    }

    public Body getBody(){
        return body;
    }
        

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        sprite.setSize(width, sprite.getHeight());
    }

    public void setHeight(float height) {
        sprite.setSize(sprite.getWidth(), height);
    }
    public static void destroyEntityBody(Body body) {
        // Ensure we are not updating the world
        // Box2D does not allow bodies to be destroyed during an update
        if (!world.isLocked()) {
            world.destroyBody(body);
        }
    }

    public void dispose() {
        this.sprite.getTexture().dispose();
    }
    public float scaleToWorld(float value) {
		return value * scale;
	}
    public void CollisionHandler(Entity entity){
        //Override this method in the subclass
        //*Contains a useful piece of code */
        //System.out.println("Collision detected with " + entity.getClass().getName());
    }

    public static Entity getEntity(String entityName){
        return entityList.get(entityName);
    }

    protected static void markForRemoval(String entityName){
        Entity entity = getEntity(entityName);
        if(entityName.contains("player")){
            System.err.println("Player cannot be marked for death");
            return;
        }
        markedForRemoval.put(entityName, entity);

    }


    // public static void removeEntity(){
    //     if(!markedForRemoval.isEmpty()){
    //         for (Entity entity : markedForRemoval.values()){
    //             String entityName = entity.body.getUserData().toString();
    //             entity.dispose();        
    //             destroyEntityBody(entity.body);
    //             entity=null;
    //             System.err.println("Entity " + entityName + " has been removed from the game world");
    //             markedForRemoval.remove(entityName);
    //         }
    //     }
    //     else{
    //         System.err.println("No entities to remove");
    //     }

    //     //Entity entity = getEntity(entityName);
        

    // }



    public static boolean isRemovable(){
        return !markedForRemoval.isEmpty();
    }
    

    public static void removeEntity() {
    if (!(markedForRemoval.isEmpty())) {
        Iterator<Map.Entry<String, Entity>> iterator = markedForRemoval.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Entity> entry = iterator.next();
            String entityName = entry.getKey();
            Entity entity = entry.getValue();
            if (entity != null) {
                
                if (entity.body != null) {
                    entity.dispose();
                    destroyEntityBody(entity.body);
                    entityList.remove(entityName);
                    entity=null;
                }
                System.err.println("Entity " + entityName + " has been removed from the game world");
                iterator.remove();
            }
        }
    } else {
        System.err.println("No entities to remove");
    }
}

    

    //float speed, x, y;
}
