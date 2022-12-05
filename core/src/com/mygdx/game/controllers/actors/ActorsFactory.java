package com.mygdx.game.controllers.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;
import com.mygdx.game.controllers.listeners.game_cl.MoveToCellCL;
import com.mygdx.game.controllers.listeners.game_cl.PlaceCapitalFirstRoundCL;
import com.mygdx.game.controllers.listeners.game_cl.PlaceToCellCL;
import com.mygdx.game.controllers.listeners.game_cl.SelectCellCL;
import com.mygdx.game.controllers.stages.MainGameStage;
import com.mygdx.game.controllers.stages.MenuStage;
import com.mygdx.game.model.maps.MapCell;
import org.w3c.dom.Text;

public class ActorsFactory {
    private MainGameStage gameStage;
    private MenuStage menuStage;
    public TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
    public TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
    public Label.LabelStyle labelStyle = new Label.LabelStyle();

    public ActorsFactory() {
        BitmapFont font = new BitmapFont(Gdx.files.internal("bitmapfont/Amble-Regular-26.fnt"));
        textButtonStyle.font = font;
        textFieldStyle.font = font;
        labelStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
    }

    public void setGameStage(MainGameStage gameStage) {
        this.gameStage = gameStage;
    }

    public void setMenuStage(MenuStage menuStage) {
        this.menuStage = menuStage;
    }

    public TiledMapActor createTiledMapActor(MapCell cell, ClickListener listener, int zIndex) {
        TiledMapActor actor = new TiledMapActor(cell, zIndex);
        actor.addListener(listener);
        return actor;
    }

    public Spinner createIntSpinner(int lowerBound, int upperBound, String name) {
        IntSpinnerModel model = new IntSpinnerModel(lowerBound, lowerBound, upperBound);
        return new Spinner(name, model);
    }

    public Button createTextButton(int x, int y, String text, ClickListener listener) {
        Button button = new TextButton(text, textButtonStyle);
        button.moveBy(x, y);
        button.addListener(listener);
        return button;
    }

    public TextField createTextField(String text) {
        TextField textField = new TextField(text, textFieldStyle);
        return textField;
    }
    public Label createLabel(int x, int y, String text){
        Label label = new Label(text, labelStyle);
        label.moveBy(x,y);
        return label;
    }
}
