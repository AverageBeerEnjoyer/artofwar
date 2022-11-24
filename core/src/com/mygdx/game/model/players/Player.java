package com.mygdx.game.model.players;

import com.mygdx.game.model.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public static Player NOBODY = new Player("");
    public final String name;
    private final List<GameObject> objects;
    private GameObject capital;
    private int farmCounter;
    private int gold;

    public Player(String name) {
        this.name = name;
        objects = new ArrayList<>();
    }

    public int getFarmCounter() {
        return farmCounter;
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public void addGameObject(GameObject gameObject){
        if(objects.contains(gameObject)) return;
        objects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject){
        objects.remove(gameObject);
    }

    public void countIncome(){
        for(GameObject gameObject:objects){
            gold+=gameObject.moneyPerTurn;
        }
        gold+=capital.moneyPerTurn;
    }
}
