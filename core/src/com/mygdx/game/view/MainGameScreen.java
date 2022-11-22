package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.controllers.MainGameStage;
import com.mygdx.game.model.maps.MapCreator;

public class MainGameScreen implements Screen {
    private final Start start;
    private final MapCreator mapCreator;
    private final MainGameStage stage;
    private final OrthographicCamera camera = new OrthographicCamera();

    public MainGameScreen(int width, int height, Start start) {
        mapCreator = new MapCreator(width, height, 0, -1);
        stage = new MainGameStage(mapCreator);
        this.start = start;
        initialize();
    }

    private void initialize() {

        camera.setToOrtho(false, 1080, 720);
        stage.addListener(
                new DragListener() {
                    @Override
                    public void drag(InputEvent event, float x, float y, int pointer) {
                        camera.position.set(camera.position.x - getDeltaX(), camera.position.y - getDeltaY(), 0);
                        Group g = stage.getRoot().findActor("cellActors");
                        g.moveBy(getDeltaX(), getDeltaY());

                    }
                }
        );
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        stage.getRenderer().setView(camera);
        stage.getRenderer().render();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
