package com.mygdx.game.cur_project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    final Start game;

    private Texture dropImage;
    private Texture field;
    private OrthographicCamera camera;
    private Rectangle bucket;
    private Rectangle field_red;
    private Array<Rectangle> raindrops;
    private int govno;

    public GameScreen(final Start game) {
        this.game = game;

        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        field = new Texture(Gdx.files.internal("1.png"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // create a Rectangle to logically represent the bucket
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
        bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
        // the bottom screen edge
        bucket.width = 64;
        bucket.height = 64;

        field_red = new Rectangle();
        field_red.x = 0;
        field_red.y = 0;
        field_red.width = 32;
        field_red.height = 32;

        raindrops = new Array<Rectangle>();
        spawnRaindrop();
        spawnRaindrop();
    }

    public void drow_fields(){
        for(int j = 0; j< 10; ++j) {
            for (int i = 0; i < 10; ++i) {
                game.batch.draw(field,
                        field_red.x + field_red.width * i,
                        field_red.y + field_red.height * j,
                        field_red.width, field_red.height);
            }
        }
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 400);
        raindrop.y = 200;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        drow_fields();
        game.batch.draw(dropImage, bucket.x, bucket.y, bucket.width, bucket.height);
        for(Rectangle raindrop: raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        game.batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
                Rectangle raindrop = iter.next();
//                if(raindrop.){
//
//                }
                raindrop.x = touchPos.x;
                raindrop.y = touchPos.y;
            }

            bucket.x = touchPos.x - 64 / 2;
            bucket.y = touchPos.y - 64 / 2;
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        dropImage.dispose();
        field.dispose();
    }
}