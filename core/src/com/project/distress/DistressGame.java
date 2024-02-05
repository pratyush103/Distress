package com.project.distress;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import entity.*;


public class DistressGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture tile;
	Texture playTexture;

	Texture whoTexture;
	World world;
	Player player;
	Entity entity;
	float playerX, playerY, bgX, bgY;
	float playerSpeed = 5f;
	BitmapFont font;
	Color color;
	Label.LabelStyle labelStyle;
	Label label;
	OrthographicCamera cam;
	Box2DDebugRenderer debugRenderer;

	@Override
	public void create() {

		Box2D.init();
        world = new World(new Vector2(0, 0), false);
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				System.out.println("Contact");
			}

			@Override
			public void endContact(Contact contact) {
				System.out.println("End Contact");
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				System.out.println("Pre Solve");
				contact.setEnabled(true);
				contact.setFriction(0.5f); // Change the friction
				contact.setRestitution(1.0f); // Change the restitution
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

		whoTexture= new Texture("move.png");
		entity = new Entity(whoTexture, 3, 4, world, false);
		
		//player = new Sprite(new Texture("idle.png"));
		//player.setPosition(viewWidth / 2 - player.getWidth() / 2, viewHeight / 2 - player.getHeight() / 2);
		//playerX = viewWidth / 2 - player.getWidth() / 2;
		//playerY = viewHeight / 2 - player.getHeight() / 2;

		cam = new OrthographicCamera(viewWidth, viewHeight);
		cam.position.set(viewWidth / 2f, viewHeight / 2f, 0);
		cam.update();

		tile = new Texture("tile.png");
		
		tile.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

		font = new BitmapFont();
		color = Color.WHITE;
		labelStyle = new Label.LabelStyle();
		labelStyle.font = font;
		labelStyle.fontColor = color;
	}

	@Override
	public void render() {
		player.update();
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		handleInput();
		cam.update();
		ScreenUtils.clear(0, 0, 0, 1);

		debugRenderer.render(world, cam.combined);
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		// Draw the repeating tile to fill the screen
		batch.draw(tile, bgX, bgY, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Draw the player at the center of the screen
		player.draw(batch);
		entity.draw(batch, 3, 4);

		batch.end();
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			//playerY += playerSpeed;
			cam.translate(0, (player.getPlayerSpeed()), 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			//playerY -= playerSpeed;
			cam.translate(0, -(player.getPlayerSpeed()), 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			//playerX -= playerSpeed;
			cam.translate(-(player.getPlayerSpeed()), 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			//playerX += playerSpeed;
			cam.translate((player.getPlayerSpeed()), 0, 0);
		}
		//label.setText(playerX + "," + playerY);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
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
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.update();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
}
