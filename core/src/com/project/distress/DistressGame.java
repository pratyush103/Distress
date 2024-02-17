package com.project.distress;

import java.util.ArrayList;
import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.OrthographicCamera;
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


public class DistressGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture tile;
	Texture playTexture;
	private long startTime;
	Texture whoTexture;
	World world;
	Player player;
	Entity entity;
	
	Texture enemyTexture;
	Enemy enemy;
	
	float playerX, playerY, bgX, bgY;
	float playerSpeed = 5f;
	BitmapFont font;
	Color color;
	Label.LabelStyle labelStyle;
	Label label;
	OrthographicCamera cam;
	Box2DDebugRenderer debugRenderer;
	FitViewport viewport;
	private float scale = 0.3f;
	ArrayList <String> CollisionList= new ArrayList<String>(2);
	long elapsedTime;
	float elapsedTimeInSeconds;
	private boolean isPaused; //! to be added in game rule
	HUD hud;
	

	@Override
	public void create() {
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		startTime = TimeUtils.millis();
		Box2D.init();
        world = new World(new Vector2(0, 0), false);
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Body a = contact.getFixtureA().getBody();
				Body b = contact.getFixtureB().getBody();
				String aName = (String) a.getUserData();
				String bName = (String) b.getUserData();
				if (aName != null && bName != null) {
					CollisionList.add(aName);
					CollisionList.add(bName);
					//System.out.println(CollisionList.get(0));
				}
				

				System.out.println("Contact");
			}

			@Override
			public void endContact(Contact contact) {
				System.out.println("End Contact");
				CollisionList.clear();
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				System.out.println("Pre Solve");
				contact.setEnabled(true);
				contact.setFriction(scale*0.5f); // Change the friction
				contact.setRestitution(scale); // Change the restitution
				handleCollision();
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				System.out.println("Post Solve");
			}
		});

		// Create a new Box2DDebugRenderer
		debugRenderer = new Box2DDebugRenderer();

		batch = new SpriteBatch();
		float viewWidth = Gdx.graphics.getWidth();
		float viewHeight = Gdx.graphics.getHeight();

		
		
		playTexture = new Texture("idle.png");
		player = new Player(playTexture, viewWidth, viewHeight, world);

		whoTexture= new Texture("idle.png");
		entity = new Entity(whoTexture, scaleToWorld(3), scaleToWorld(4), world, false, "NullEntity");
		
		enemyTexture = new Texture("move.png");
		enemy = new Enemy(enemyTexture, 40, 50, 75, 100, 10, world, player,"");
		//player = new Sprite(new Texture("idle.png"));
		//player.setPosition(viewWidth / 2 - player.getWidth() / 2, viewHeight / 2 - player.getHeight() / 2);
		//playerX = viewWidth / 2 - player.getWidth() / 2;
		//playerY = viewHeight / 2 - player.getHeight() / 2;

		

		cam = new OrthographicCamera(viewWidth, viewHeight);
		
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
        viewport.apply();

		cam.zoom = scale;
		cam.position.set((viewWidth / 2f)*scale, (viewHeight / 2f)*scale, 0);
		
		cam.update();

		

		tile = new Texture("tile.png");
		
		tile.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

		font = new BitmapFont();
		color = Color.WHITE;
		labelStyle = new Label.LabelStyle();
		labelStyle.font = font;
		labelStyle.fontColor = color;
		hud = new HUD(batch, player);
	}
	

	@Override
	public void render() {
		if(!isPaused){
		world.step((Gdx.graphics.getDeltaTime()), 6, 2);
		elapsedTime = TimeUtils.timeSinceMillis(startTime);
		elapsedTimeInSeconds = elapsedTime / 1000f;
		

		handleInput();
		player.update();
		
		

		cam.position.set(Math.round(player.getPosition().x), Math.round(player.getPosition().y), 0);
		cam.update();
		
		ScreenUtils.clear(0, 0, 0, 1);
		
		//TODO: When Adding Tile Map tilemap->batch->debugRenderer->hud
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		// Draw the repeating tile to fill the screen
		batch.draw(tile, 0, 0, Gdx.graphics.getWidth()*scale, Gdx.graphics.getHeight()*scale);
		enemy.update(Gdx.graphics.getDeltaTime(), batch);
		// Draw the player at the center of the screen
		//enemy.draw(batch);
		player.draw(batch);
		entity.draw(batch, 0, 0);
		batch.end();
		debugRenderer.render(world, cam.combined);
		hud.update((int) elapsedTimeInSeconds);
	}
		if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
			resume();
		}
	}

	private void scaleSprites(Sprite sprite) {
        sprite.setScale(scale);
    }

	private Vector2 screenToWorldCoordinates(float screenX, float screenY) {
        Vector3 worldCoordinates = cam.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(worldCoordinates.x * scale, worldCoordinates.y * scale);
    }

	void handleCollision() {
		int i = 0;
		int j= CollisionList.size()-1;
		Entity entity1;
		Entity entity2;

		while (i<= CollisionList.size()-1 && j>=0){
			try {
				entity1= Entity.getEntity(CollisionList.get(i));
				entity2= Entity.getEntity(CollisionList.get(j));

				entity1.CollisionHandler(entity2);
			} catch (Exception e) {
				
				continue;
			}
			i++;
			j--;
		}
	}

	private float scaleToWorld(float value) {
		return value * scale;
	}
	
	
	private void handleInput() {
		boolean isWindowed = false;
		int width = Gdx.graphics.getWidth()-2; // Set this to your desired window width
		int height = Gdx.graphics.getHeight()-2;
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			//playerY += playerSpeed;
			//cam.translate(0, (player.getPlayerSpeed()), 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			//playerY -= playerSpeed;
			//cam.translate(0, -(player.getPlayerSpeed()), 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			//playerX -= playerSpeed;
			//cam.translate(-(player.getPlayerSpeed()), 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			//playerX += playerSpeed;
			//cam.translate((player.getPlayerSpeed()), 0, 0);
		}
		//label.setText(playerX + "," a+ playerY);
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			pause();
			
			// if(!isWindowed){
				 
			// 	Gdx.graphics.setWindowedMode(width, height);
			// 	//cam.zoom = scale;
			// 	isWindowed = true;
			// }
		}
		if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
			resume();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_SUBTRACT)){
			cam.zoom += 0.02;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.NUMPAD_ADD)){
			cam.zoom -= 0.02;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F11)){
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			isWindowed = false;
		}
	}

	@Override
	public void pause() {
		super.pause();
		isPaused = true;
	}

	@Override
	public void resume() {
		super.resume();
		isPaused = false;
	}

	@Override
	public void dispose() {
		batch.dispose();
		player.dispose();
		tile.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.setToOrtho(false, width, height);
		cam.update();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
}
