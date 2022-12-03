package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.view.ArtofWar;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static int
            screenWidth = 1080,
            screenHeight = 720;

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("ArtofWar");
        config.setWindowedMode(screenWidth, screenHeight);
        config.useVsync(true);
        new Lwjgl3Application(new ArtofWar(), config);
    }
}
