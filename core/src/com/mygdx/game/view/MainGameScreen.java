package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.controllers.MapToRendererTransformator;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.model.GamingProcess;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class MainGameScreen implements Screen {
    private final ArtofWar artofWar;
    private final Map map;
    private final MainGameStage stage;
    private final OrthographicCamera camera = new OrthographicCamera();
    private final MapToRendererTransformator mapToRendererTransformator;

    public MainGameScreen(int width, int height, ArtofWar artofWar) {
        map = new Map(width, height);

        List<Player> players = new ArrayList<>();
        players.add(new Player("player 1", map));
        players.add(new Player("player 2", map));
        players.add(new Player("player 3", map));
        map.setPlayerList(players);
        GamingProcess gamingProcess = new GamingProcess(map);
        stage = new MainGameStage(map, gamingProcess, artofWar);
        mapToRendererTransformator = map.getMapToRendererTransformator();
        this.artofWar = artofWar;
        initialize();
    }

    private void initialize() {

        camera.setToOrtho(false, 1080, 720);
        stage.addListener(
                new DragListener() {
                    @Override
                    public void drag(InputEvent event, float x, float y, int pointer) {
                        camera.position.set(camera.position.x - getDeltaX(), camera.position.y - getDeltaY(), 0);
                        Group g = stage.getMovableActors();

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
        mapToRendererTransformator.getRenderer().setView(camera);
        mapToRendererTransformator.getRenderer().render();
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
