package entity;

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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;


public class Player extends Entity{
	float playerX, playerY;
	float playerSpeed;
    int playerHealth;
	BitmapFont font;
	Color color;
	Label.LabelStyle labelStyle;
	Label label;
    Texture playerTexture = new Texture("idle.png");
    public Player(Texture playerTexture,float viewWidth, float viewHeight, World world){
        
        
        super(playerTexture, 0, 0, world, true);
        super.sprite.setPosition(viewWidth / 2 - super.getWidth() / 2, viewHeight / 2 - super.getHeight() / 2);
		playerX = viewWidth / 2 - super.getWidth() / 2;
		playerY = viewHeight / 2 - super.getHeight() / 2;
        playerSpeed = 5f;
        playerHealth = 100;
        font = new BitmapFont();
        color = Color.WHITE;
        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = color;
        label = new Label(playerX + "," + playerY, labelStyle);

    }
    public void draw(SpriteBatch batch) {

        
        super.draw(batch, playerX, playerY);

    }
    public void move(){
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            playerY += playerSpeed;
            //body.applyForceToCenter(0, playerSpeed, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            playerY -= playerSpeed;
            //body.applyForceToCenter(0, -playerSpeed, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            playerX -= playerSpeed;
            //body.applyForceToCenter(-playerSpeed, 0, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            playerX += playerSpeed;
            //body.applyForceToCenter(playerSpeed, 0, true);
        }
    
    }
    public void update(){
        move();
        body.setTransform(playerX, playerY, body.getAngle());
        label.setText(playerX + "," + playerY);
        label.setPosition(playerX, playerY);
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

}
