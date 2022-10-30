package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.model.maps.BiomCreator;
import com.mygdx.game.model.maps.CellType;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.maps.MapCell;

public class ConstructorMap implements Screen {
    final Start game;
    private final Stage stage;
    private Map map;
    private String n1="25",n2="25",n3="5";
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

        window.add(CreateMenu()).top().left();

        ScrollPane scrollPane = new ScrollPane(CreateMap());
        window.add(scrollPane).fill().expand();

        window.debug();
        return window;
    }

    //buttons,settings
    public Table CreateMenu(){
        Table table = new Table();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        BitmapFont myFont = new BitmapFont(Gdx.files.internal("bitmapfont/Amble-Regular-26.fnt"));
        labelStyle.font = myFont;
        labelStyle.fontColor = Color.RED;

        TextField.TextFieldStyle labelStyle1 = new TextField.TextFieldStyle();
        labelStyle1.font = myFont;
        labelStyle1.fontColor = Color.RED;

        BitmapFont font = new BitmapFont();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;

        final Label nameLabel = new Label("Count X:",labelStyle);
        final TextField nameText = new TextField(this.n1,labelStyle1);
        Label addressLabel = new Label("Count Y:", labelStyle);
        Label addressLabel1 = new Label("Seed:", labelStyle);
        final TextField addressText = new TextField(this.n2,labelStyle1);
        final TextField addressText1 = new TextField(this.n3,labelStyle1);

        Button button = new TextButton("Update map.", textButtonStyle);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                n1 = nameText.getText();
                n2 = addressText.getText();
                n3 = addressText1.getText();
                System.out.println(n1+" "+n2);
                BiomCreator a = new BiomCreator(
                        Integer.parseInt(n1),//x
                        Integer.parseInt(n2),//y
                        Integer.parseInt(n3)//random local
                );
                map = a.view_Up(0);
                UpdateWindow();
            }
        });

        table.add(nameLabel);   // Row 0, column 0.
        table.add(nameText);    // Row 0, column 1.
        table.row();            // Move to next row.
        table.add(addressLabel);// Row 1, column 0.
        table.add(addressText); // Row 1, column 1.
        table.row();
        table.add(addressLabel1);// Row 1, column 0.
        table.add(addressText1);
        table.add(button);

        return table;
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
        BiomCreator a = new BiomCreator(Integer.parseInt(n1), Integer.parseInt(n2),0);
        map = a.view_Up(0);
    }

    private void UpdateWindow(){
        stage.getActors().clear();
        stage.addActor(CreateWindow());
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