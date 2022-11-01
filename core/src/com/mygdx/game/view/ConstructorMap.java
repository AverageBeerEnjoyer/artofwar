package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.model.maps.BiomCreator;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;

import java.util.HashMap;
import java.util.Objects;

public class ConstructorMap implements Screen {
    final Start game;
    private final Stage stage;
    private Map map;
    private String n1="25",n2="25",n3="5",n4="1.5",n5="2",n6="0.1";
    private String textRandom="Random: off";
    private java.util.Map<String,Integer> statInfo = new HashMap<>();
    private OrthographicCamera camera;

    public ConstructorMap(final Start game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        CreateTypeCell();
        stage.addActor(CreateWindow());

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1080, 720);
    }

    //Menu+Map
    public Table CreateWindow(){
        Table window = new Table();
        window.setFillParent(true);

        window.add(CreateMenu()).width(350).top().left();

        ScrollPane scrollPane = new ScrollPane(CreateMap());
        window.add(scrollPane).fill().expand();

        return window;
    }

    //buttons,settings
    public Table CreateMenu(){
        Table table = new Table();
        BitmapFont myFont = new BitmapFont(Gdx.files.internal("bitmapfont/Amble-Regular-26.fnt"));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = myFont;
        labelStyle.fontColor = Color.RED;

        TextField.TextFieldStyle labelStyle1 = new TextField.TextFieldStyle();
        labelStyle1.font = myFont;
        labelStyle1.fontColor = Color.RED;

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = myFont;
        textButtonStyle.fontColor = Color.WHITE;

        final Label labelX = new Label("Count X:", labelStyle);
        final Label labelY = new Label("Count Y:", labelStyle);
        final Label labelSeed = new Label("Seed:", labelStyle);
        final Label labelDegree = new Label("degree:", labelStyle);
        final Label labelOctaves = new Label("octaves:", labelStyle);
        final Label labelPersistence = new Label("persistence:", labelStyle);

        final TextField fieldX = new TextField(this.n1, labelStyle1);
        final TextField fieldY = new TextField(this.n2, labelStyle1);
        final TextField fieldSeed = new TextField(this.n3,labelStyle1);
        final TextField fieldDegree = new TextField(this.n4,labelStyle1);
        final TextField fieldOctaves = new TextField(this.n5,labelStyle1);
        final TextField fieldPersistence = new TextField(this.n6,labelStyle1);

        final Button lightingCheckBox = new TextButton(textRandom, textButtonStyle);
        lightingCheckBox.setName(textRandom);
        lightingCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(Objects.equals(lightingCheckBox.getName(), "Random: off")){
                    textRandom = "Random: on";
                }
                else{
                    textRandom = "Random: off";
                }
                lightingCheckBox.setName(textRandom);
                UpdateWindow();
            }
        });

        Button button = new TextButton("Update map", textButtonStyle);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                n1 = fieldX.getText();
                n2 = fieldY.getText();
                if(Objects.equals(textRandom, "Random: on")){
                    n3 = "-1";
                }else n3 = fieldSeed.getText();
                n4 = fieldDegree.getText();
                n5 = fieldOctaves.getText();
                n6 = fieldPersistence.getText();
                BiomCreator a = new BiomCreator(
                        Integer.parseInt(n1),//x
                        Integer.parseInt(n2),//y
                        (long)Double.parseDouble(n3)//seed
                );
                UpdateSettings(a);
                map = a.view_Up(0);
                n3 = String.valueOf(a.getSeed());
                statInfo = a.getStatInfo();
                UpdateWindow();
            }
        });

        table.add(labelX);
        table.add(fieldX).width(50);
        table.row();
        table.add(labelY);
        table.add(fieldY).width(50);
        table.add(lightingCheckBox);
        table.row();
        table.add(labelSeed);
        table.add(fieldSeed).width(50);
        table.add(button).colspan(2);
        table.row();
        table.add(labelDegree);
        table.add(fieldDegree).width(50);
        table.add(labelOctaves);
        table.add(fieldOctaves).width(50);
        table.row();
        table.add(labelPersistence);
        table.add(fieldPersistence).width(50);

        return CreateTableStatInfo(table);
    }

    //Take Map in type_cell
    public Table CreateMap(){
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("bitmapfont/Amble-Regular-26.fnt"));
        Table table = new Table();
        for (int i=0;i<map.cells.length;++i) {
            if((i&1)==1) table.add(new Label(" ",new Label.LabelStyle(labelStyle)));
            for (MapCell cell : map.cells[i]) {
                labelStyle.fontColor = cell.type.color();
                table.add(new Label(" ", new Label.LabelStyle(labelStyle)));
                table.add(new Label("@", new Label.LabelStyle(labelStyle)));
            }
            table.row();
        }
        return table;
    }

    private void CreateTypeCell(){
        BiomCreator a = new BiomCreator(Integer.parseInt(n1), Integer.parseInt(n2),Integer.parseInt(n3));
        map = a.view_Up(0);
        statInfo = a.getStatInfo();
    }

    private Table CreateTableStatInfo(Table tableMenu){
        BitmapFont myFont = new BitmapFont(Gdx.files.internal("bitmapfont/Amble-Regular-26.fnt"));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = myFont;
        labelStyle.fontColor = Color.RED;

        for(java.util.Map.Entry<String,Integer> entry : statInfo.entrySet()){
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            tableMenu.row();
            tableMenu.add(new Label(key, new Label.LabelStyle(labelStyle)));
            tableMenu.add(new Label(value, new Label.LabelStyle(labelStyle)));
        }
        return tableMenu;
    }

    private void UpdateWindow(){
        stage.getActors().clear();
        stage.addActor(CreateWindow());
    }

    private void UpdateSettings(BiomCreator a){
        a.setDegree(Double.parseDouble(n4));
        a.setOctaves(Integer.parseInt(n5));
        a.setPersistence(Double.parseDouble(n6));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

//        game.batch.begin();
        stage.act();
        stage.draw();
//        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
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
        stage.dispose();
        game.dispose();
    }
}