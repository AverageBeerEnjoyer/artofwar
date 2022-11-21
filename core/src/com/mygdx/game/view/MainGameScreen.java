package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.model.maps.Map;
import controllers.MainGameStage;

public class MainGameScreen implements Screen {
    private final Map map;
    private final MainGameStage stage;
    private final OrthographicCamera camera = new OrthographicCamera();

    public MainGameScreen(int height, int width) {
        map = new Map(height, width, 0,-1);
        stage = new MainGameStage(map);
        initialize();
    }

    private void initialize(){
        Gdx.input.setInputProcessor(stage);
        camera.setToOrtho(false,1080,720);
        stage.addListener(new DragListener(){
                    @Override
                    public void drag(InputEvent event, float x, float y, int pointer) {
                        camera.position.set(camera.position.x-getDeltaX(),camera.position.y-getDeltaY(), 0);
                        Group g = stage.group;
                        g.moveBy(getDeltaX(),getDeltaY());

                    }
                });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0.2f,1);
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
