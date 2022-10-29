package com.mygdx.game.cur_project;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen implements Screen {

    final Start stage;
    OrthographicCamera camera;
    CreateMenuScreen a;

    public MenuScreen(final Start stage) {
        this.stage = stage;
        camera = new OrthographicCamera();
        a = new CreateMenuScreen(stage);
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        stage.batch.setProjectionMatrix(camera.combined);

        stage.batch.begin();
        stage.font.draw(stage.batch, "Welcome to Drop!!! ", 100, 150);
        stage.font.draw(stage.batch, "Tap anywhere to begin!", 100, 100);
        a.getObjects().draw();
        stage.batch.end();

//        if (Gdx.input.isTouched()) {
//            game.setScreen(new GameScreen(game));
//            dispose();
//        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
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
    }
}
