package com.mygdx.engeldenkacma;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class EngeldenKacmaOyunu extends ApplicationAdapter implements InputProcessor{
	private SpriteBatch batch;
	private OrthographicCamera kamera;
	private Texture bird, barrier;
	private Rectangle rct_bird;
	private Boolean touch_active = false;
	private Array<Rectangle> barriers;
	private long last_barrier_time;
	private Sound fail_sound;

	@Override
	public void create () {
		batch = new SpriteBatch();

		kamera = new OrthographicCamera();
		kamera.setToOrtho(false, 800, 480);

		bird = new Texture("bird2.png");
		barrier = new Texture("barrier.png");

		barriers = new Array<Rectangle>();

		create_barrier();

		fail_sound = Gdx.audio.newSound(Gdx.files.internal("fail_sound.mp3"));

		Gdx.input.setInputProcessor(this);

		rct_bird = new Rectangle();
		rct_bird.width = 32;
		rct_bird.height = 50;
		rct_bird.x = 100 - rct_bird.width / 2;
		rct_bird.y = 20;
	}

	private void create_barrier() {
		Rectangle rct_barrier = new Rectangle();
		rct_barrier.height = 50;
		rct_barrier.width = 64;
		rct_barrier.x = 800;
		rct_barrier.y = MathUtils.random(0, 480 - 64);
		barriers.add(rct_barrier);
		last_barrier_time = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		kamera.update();
		batch.setProjectionMatrix(kamera.combined);

		batch.begin();
		batch.draw(bird, rct_bird.x, rct_bird.y);

		for (Rectangle rct_barrier : barriers){
			batch.draw(barrier, rct_barrier.x, rct_barrier.y);
		}

		batch.end();

		if (touch_active == true){
			Vector3 touch = new Vector3();
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			kamera.unproject(touch);

			if (rct_bird.y < 400){
				rct_bird.y += 200f * Gdx.graphics.getDeltaTime();

			}

		}
		else {
			Vector3 touch = new Vector3();
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			kamera.unproject(touch);

			if (rct_bird.y > 20){
				rct_bird.y -= 150f * Gdx.graphics.getDeltaTime();

			}

		}

		if (TimeUtils.nanoTime() - last_barrier_time > 250000000){
			create_barrier();
		}

		Iterator<Rectangle> barrier = barriers.iterator();
		while (barrier.hasNext()){
			Rectangle rct_barrier = barrier.next();
			rct_barrier.x -= 400 * Gdx.graphics.getDeltaTime();

			if (rct_barrier. x + 64 <0){
				barrier.remove();
			}

			if (rct_barrier.overlaps(rct_bird)){
				barrier.remove();
				fail_sound.play();
			}
		}

	}

	@Override
	public void dispose (){
		bird.dispose();
		barrier.dispose();
		fail_sound.dispose();

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touch_active = true;

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touch_active = false;

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
