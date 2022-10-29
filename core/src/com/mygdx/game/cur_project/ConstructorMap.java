package com.mygdx.game.cur_project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.cur_project.perlins.scrin;
import sun.jvm.hotspot.utilities.BitMap;

import java.lang.annotation.Target;
import java.util.Iterator;

public class ConstructorMap implements Screen {
    final Start game;
    private Stage stage;
    private Color[][] type_cell;
    private String n1="10",n2="10";
    private OrthographicCamera camera;

    public ConstructorMap(final Start game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        stage.addActor(CreateMenu().left().top());

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    public Table CreateMenu(){
        scrin a = new scrin(Integer.parseInt(n1), Integer.parseInt(n2),0);
        type_cell = new Color[Integer.parseInt(n1)][Integer.parseInt(n2)];
        type_cell = a.view_Up(0);

        Table table = new Table();
        table.setFillParent(true);

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
        final TextField nameText = new TextField("10",labelStyle1);
        Label addressLabel = new Label("Count Y:", labelStyle);
        final TextField addressText = new TextField("10",labelStyle1);

        Button button = new TextButton("Save settings.", textButtonStyle);
        Button update = new TextButton("update map.", textButtonStyle);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                n1 = nameText.getText();
                n2 = addressText.getText();
                System.out.println(n1+" "+n2);
                scrin a = new scrin(
                        Integer.parseInt(n1),//x
                        Integer.parseInt(n2),//y
                        0//random local
                );
                type_cell = new Color[Integer.parseInt(n1)][Integer.parseInt(n2)];
                type_cell = a.view_Up(0);
            }
        });
        update.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println(n1+" "+n2);
                scrin a = new scrin(
                        Integer.parseInt(n1),//x
                        Integer.parseInt(n2),//y
                        -1//random user
                );
                type_cell = new Color[Integer.parseInt(n1)][Integer.parseInt(n2)];
                type_cell = a.view_Up(0);
            }
        });

        table.add(nameLabel);              // Row 0, column 0.
        table.add(nameText).width(100);    // Row 0, column 1.
        table.row();                       // Move to next row.
        table.add(addressLabel);           // Row 1, column 0.
        table.add(addressText).width(100); // Row 1, column 1.
        table.row();
        table.add(button).width(100).height(100);
        table.row();
        table.add(update).width(100).height(100);

//        table.debug();
//        table.debugTable();
        return table;
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        String sym = "";
        game.batch.begin();
        stage.draw();
        for(int i =0;i<type_cell.length;i++){
            for(int j =0;j<type_cell[i].length;j++){
//                sym = type_cell[i][j]+"@"+"\u001B[0m"+" ";
                game.font.setColor(type_cell[i][j]);
                game.font.draw(
                        game.batch,
                        "@",
                        100+(45*i),100+(30*j)
                );
            }
        }
        game.batch.end();
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
    }
}