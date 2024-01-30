package com.project.distress;

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


public class DistressGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture tile;
	Sprite player;
	float playerX, playerY, bgX, bgY;
	float playerSpeed = 5f;
	BitmapFont font;
	Color color;
	Label.LabelStyle labelStyle;
	Label label;
	OrthographicCamera cam;

	@Override
	public void create() {
		batch = new SpriteBatch();
		float viewWidth = Gdx.graphics.getWidth();
		float viewHeight = Gdx.graphics.getHeight();
		player = new Sprite(new Texture("idle.png"));
		player.setPosition(viewWidth / 2 - player.getWidth() / 2, viewHeight / 2 - player.getHeight() / 2);
		playerX = viewWidth / 2 - player.getWidth() / 2;
		playerY = viewHeight / 2 - player.getHeight() / 2;

		cam = new OrthographicCamera(viewWidth, viewHeight);
		cam.position.set(viewWidth / 2f, viewHeight / 2f, 0);
		cam.update();

		tile = new Texture("tile.png");
		tile.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		bgX = 0;
		bgY = 0;

		font = new BitmapFont();
		color = Color.WHITE;
		labelStyle = new Label.LabelStyle();
		labelStyle.font = font;
		labelStyle.fontColor = color;
		label = new Label(playerX + "," + playerY, labelStyle);
	}

	@Override
	public void render() {
		handleInput();
		cam.update();
		ScreenUtils.clear(0, 0, 0, 1);
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		// Draw the repeating tile to fill the screen
		batch.draw(tile, bgX, bgY, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Draw the player at the center of the screen
		batch.draw(player, playerX, playerY);

		label.draw(batch, 1);

		batch.end();
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			playerY += playerSpeed;
			cam.translate(0, playerSpeed, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			playerY -= playerSpeed;
			cam.translate(0, -playerSpeed, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			playerX -= playerSpeed;
			cam.translate(-playerSpeed, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			playerX += playerSpeed;
			cam.translate(playerSpeed, 0, 0);
		}
		label.setText(playerX + "," + playerY);
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
		player.getTexture().dispose();
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
