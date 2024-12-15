package com.mygdx.game.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    public static <T> List<T> replaceObject(List<T> list, T oldObject, T newObject){
        List<T> newList = new ArrayList<>(list);
        int index = newList.indexOf(oldObject);
        list.set(index, newObject);
        return newList;
    }
}
