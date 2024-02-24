package entity;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import java.util.Queue;


public class Player extends Entity implements Steerable<Vector2>{
	float playerX, playerY;
    Vector2 playerPosition;
	float playerSpeed;
    int playerHealth;
	BitmapFont font;
	Color color;
	Label.LabelStyle labelStyle;
	Label label;
    Vector2 position;
    //Texture playerTexture = new Texture("idle.png");
    int score;
    private Hashtable<Integer, Projectile> projectileHashtable;
    private Queue<Integer> availableBulletIds;
    
    private boolean isTagged;
    public Player(Texture playerTexture,float viewWidth, float viewHeight, World world){
        
        
        super(playerTexture, 0, 0, world, true, "player");
        
		playerX = scaleToWorld(viewWidth / 2 - super.getWidth() / 2);
		playerY = scaleToWorld(viewHeight / 2 - super.getHeight() / 2);
        playerPosition = new Vector2(playerX, playerY);
        
        super.sprite.setPosition(playerPosition.x, playerPosition.y);
        playerSpeed = 100;
        playerHealth = 100;
        font = new BitmapFont();
        color = Color.WHITE;
        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = color;
        label = new Label(playerPosition.x + "," + playerPosition.y, labelStyle);
        score = 0;

        this.projectileHashtable = new Hashtable<>();
        this.availableBulletIds = new LinkedList<>();
        for (int i = 1; i <= 100; i++) {
            availableBulletIds.add(i);
        }

    }
    public void draw(SpriteBatch batch) {

        
        super.draw(batch, playerPosition.x,playerPosition.y);

    }
    public void move(){
        position = body.getPosition();
        
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            //playerY += playerSpeed;
            body.setLinearVelocity(body.getLinearVelocity().x, playerSpeed);
            playerY=position.y;
            playerPosition.set(playerX, playerY);
            //System.out.println(position.x + " " + position.y);
            
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            //playerY -= playerSpeed;
            body.setLinearVelocity(body.getLinearVelocity().x, -playerSpeed);
            playerY=position.y;
            playerPosition.set(playerX, playerY);
            //System.out.println(position.x + " " + position.y);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            //playerX -= playerSpeed;
            body.setLinearVelocity(-playerSpeed, body.getLinearVelocity().y);
            playerX=position.x;
            playerPosition.set(playerX, playerY);
            //System.out.println(position.x + " " + position.y);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            //playerX += playerSpeed;
            body.setLinearVelocity(playerSpeed, body.getLinearVelocity().y);
            playerX=position.x;
            playerPosition.set(playerX, playerY);
            //System.out.println(position.x + " " + position.y);
        }
    
    }
    public void update(){
        move();
        body.setTransform(playerPosition.x, playerPosition.y, 0);
        label.setText(playerPosition.x + "," + playerPosition.y);
        label.setPosition(playerPosition.x, playerPosition.y);
        
    }
    
    public int getScore(){
        return score;
    }

    public void setX(float x){
        super.sprite.setX(x);
    }
    public void setY(float y){
        super.sprite.setY(y);
    }
    public float getX(){
        return super.sprite.getX();
    }
    public float getY(){
        return super.sprite.getY();
    }
    public float getPlayerSpeed(){
        return playerSpeed;
    }
    
    @Override
    public Vector2 getPosition(){
        return playerPosition;
    }
    public int getPlayerHealth() {
        return playerHealth;
    }
    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }
    private long lastDamageTime = 0;
    private static final long DAMAGE_COOLDOWN = 500; // 500 milliseconds

    public void takeDamage(int damage) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDamageTime >= DAMAGE_COOLDOWN) {
            playerHealth -= damage;
            if (playerHealth < 0) {
                playerHealth = 0;
            }
            lastDamageTime = currentTime;
        }
    }

    @Override
    public void CollisionHandler(Entity entity) {
        super.CollisionHandler(entity);
        //TODO: Temporary Implementation better use any other java Object to store userdata 
        String userData =(String) entity.body.getUserData(); 
        if (userData.contains("enemy")) {
            takeDamage(((Enemy) entity).getCollisionDamage());
        }
    }

    public void increaseScore(int score) {
        this.score += score;
    }

    public void fireWeapon(Vector2 direction) {
        if (!availableBulletIds.isEmpty()) {
            int id = availableBulletIds.poll();
            // Create your bullet here and add it to the bullets list
            Projectile projectile = new Projectile(new Texture("basic_proj.png"), playerPosition.x, playerPosition.y, 5, 10, world, this, direction, "-player"+id, id);
            projectileHashtable.put(id, projectile);
        } else {
            System.out.println("Maximum number of bullets reached.");
        }
    }

    public void removeBullet(int id) {
        if (projectileHashtable.containsKey(id)) {
            projectileHashtable.remove(id);
            availableBulletIds.add(id);
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
    public float getBoundingRadius() {
        return this.getWidth()/2;
    }

    @Override
    public boolean isTagged() {
        return isTagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.isTagged = tagged;
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
