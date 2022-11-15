package com.mygdx.game.model.players;

import com.mygdx.game.model.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<GameObject> objects;

    public Player(String name) {
        this.name = name;
        objects = new ArrayList<>();
    }
}
