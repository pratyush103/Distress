package entity;

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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;


public class Player extends Entity{
	float playerX, playerY;
    Vector2 playerPosition;
	float playerSpeed;
    int playerHealth;
	BitmapFont font;
	Color color;
	Label.LabelStyle labelStyle;
	Label label;
    Vector2 position;
    Texture playerTexture = new Texture("idle.png");
    int score;
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
    public Vector2 getPosition(){
        return position;
    }
    public int getPlayerHealth() {
        return playerHealth;
    }
    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }
    public void takeDamage(int damage){
        playerHealth -= damage;
    }
    @Override
    public void CollisionHandler(Entity entity){
        super.CollisionHandler(entity);
        if(entity.body.getUserData()=="enemy"){
            takeDamage(((Enemy) entity).getCollisionDamage());
        }
        

    }

}
