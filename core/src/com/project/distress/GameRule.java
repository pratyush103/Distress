package com.project.distress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import entity.*;
import Screens.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import java.util.ArrayList;
import com.project.distress.DistressGame;


/**
 * Represents a 2D animation in the LibGDX graphics library.
 */
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class GameRule {
    Texture playTexture;
	long startTime;
	Texture whoTexture; //!Temp
    Texture playerTexture; //!Temp
    Texture enemyTexture; //!Temp
    Player player;
	Entity entity;
    Color color;
	Label.LabelStyle labelStyle;
	Label label;
    private float scale = 0.3f;
    ArrayList <String> CollisionList= new ArrayList<String>(2);
    SpriteBatch batch;
    World world;
    float viewWidth;
    float viewHeight;
    

    public GameRule(SpriteBatch batch, World world, long startTime, float viewWidth, float viewHeight) {
        this.batch = batch;
        this.world = world;
        this.startTime = startTime;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;

        playTexture = new Texture("idle.png");
		player = new Player(playTexture, viewWidth, viewHeight, world);

        whoTexture= new Texture("move.png");
		entity = new Entity(whoTexture, scaleToWorld(3), scaleToWorld(4), world, false, "NullEntity");
    }





}

