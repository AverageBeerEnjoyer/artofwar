package com.mygdx.game.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen implements Screen {

    final ArtofWar stage;
    OrthographicCamera camera;
    CreateMenuScreen a;

    public MenuScreen(final ArtofWar stage) {
        this.stage = stage;
        camera = new OrthographicCamera();
        a = new CreateMenuScreen(stage);
        camera.setToOrtho(false, 1080, 720);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        stage.batch.setProjectionMatrix(camera.combined);

        stage.batch.begin();
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
