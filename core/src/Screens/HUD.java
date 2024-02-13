package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import entity.*;
import com.badlogic.gdx.graphics.Color;

public class HUD {
    public Stage stage;
    private ScreenViewport viewport;
    private Integer worldTimer;
    private float timeCount;
    //private static Integer score;
    Label scoreLabel;
    Label timeLabel;
    Label playerHealthLabel;
    Label playerPositionLabel; 
    Table table;
    private Player player;
    

    public HUD(SpriteBatch batch, Player player){
        stage = new Stage(new ScreenViewport(), batch);
        viewport = new ScreenViewport();
        table = new Table();
        this.player = player;
        table.top();
        table.setFillParent(true);
        // Load the BitmapFont
        BitmapFont font = new BitmapFont();

        // Create the LabelStyle
        LabelStyle style = new LabelStyle();
        style.font = font;
        style.fontColor = Color.WHITE;

        
        scoreLabel = new Label(String.valueOf(player.getScore()), style);
        timeLabel = new Label("0:00", style);
        playerHealthLabel = new Label(String.valueOf(player.getPlayerHealth())+"/100", style);
        //playerPositionLabel = new Label((String.valueOf(player.getPosition().x)+","+(String.valueOf(player.getPosition().y))), style);
        table.add(scoreLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(playerHealthLabel).expandX().padTop(10);
        table.add(playerPositionLabel).expandX().padTop(10);
        stage.addActor(table);



    }
    void draw(){
        stage.draw();
    }
    public void update(int dt){
        scoreLabel.setText(String.valueOf(player.getScore()));
        playerHealthLabel.setText(String.valueOf(player.getPlayerHealth())+"/100");
        //playerPositionLabel.setText((String.valueOf(player.getPosition().x)+","+(String.valueOf(player.getPosition().y))));
        timeLabel.setText(String.valueOf(dt));
        draw();
    }
}
