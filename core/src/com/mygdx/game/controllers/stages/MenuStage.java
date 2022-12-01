package com.mygdx.game.controllers.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;
import com.mygdx.game.controllers.actors.ActorsFactory;
import com.mygdx.game.controllers.listeners.menu_cl.BackCL;
import com.mygdx.game.controllers.listeners.menu_cl.ExitCL;
import com.mygdx.game.controllers.listeners.menu_cl.PreGameCL;
import com.mygdx.game.view.Start;

public class MenuStage extends Stage implements Screen {
    private final Start start;
    private Group main;
    private Group preGame;
    private Group settings;
    private Texture backgroundImage = new Texture(Gdx.files.internal("menuBackground.jpg"));
    private Spinner
            playersNumber,
            mapWidth,
            mapHeight;
    public MenuStage(Start start){
        this.start = start;
        VisUI.load();
        ((OrthographicCamera) getCamera()).setToOrtho(false, 1080, 720);
        createMainGroup();
        createPreGame();
        toMain();
    }
    public void createMainGroup(){
        main = new Group();
        BitmapFont myFont = new BitmapFont(Gdx.files.internal("bitmapfont/Amble-Regular-26.fnt"));


        Button play = new TextButton("Play", start.style);
        play.addListener(new PreGameCL(this));
        play.moveBy(0,100);
        play.debug();

        Button settings = new TextButton("Settings", start.style);
        settings.moveBy(0,200);

        Button exit = new TextButton("Exit", start.style);
        exit.moveBy(0,300);
        exit.addListener(new ExitCL());
        exit.debug();

        main.addActor(settings);
        main.addActor(play);
        main.addActor(exit);
        main.setPosition(0,0);
        main.debug();
    }
    public void toMain(){
        setRoot(main);
    }

    private void createPreGame(){
        preGame = new Group();
        playersNumber = createIntSpinner(2,10,"Players");
        playersNumber.moveBy(150,100);
        mapWidth = createIntSpinner(10,125,"Width of map");
        mapWidth.moveBy(150,200);
        mapHeight = createIntSpinner(10,125,"Height of map");
        mapHeight.moveBy(150,300);
        preGame.addActor(playersNumber);
        preGame.addActor(mapWidth);
        preGame.addActor(mapHeight);
        Button back = new TextButton("Back", start.style);
        back.addListener(new BackCL(this));
        back.moveBy(150,0);
        preGame.addActor(back);
    }
    public void toPreGame(){
        setRoot(preGame);
    }
    private Spinner createIntSpinner(int lowerBound, int upperBound, String name) {
        IntSpinnerModel model = new IntSpinnerModel(lowerBound, lowerBound, upperBound);
        return new Spinner(name, model);
    }
    public void startGame(){

    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0.2f,1);
        getCamera().update();
        getBatch().begin();
        getBatch().draw(backgroundImage,0,0);
        getBatch().end();
        act();
        draw();
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
}
