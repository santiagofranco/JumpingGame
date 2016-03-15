package com.santiagofranco;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.santiagofranco.screens.BaseScreen;
import com.santiagofranco.screens.GameOverScreen;
import com.santiagofranco.screens.GameScreen;
import com.santiagofranco.screens.MenuScreen;

import java.util.HashMap;
import java.util.Map;

public class MainGame extends Game {

    private Map<String, BaseScreen> screens;
    private AssetManager assetManager;

    @Override
    public void create() {

        assetManager = new AssetManager();
        assetManager.load("player.png", Texture.class);
        assetManager.load("floor.png", Texture.class);
        assetManager.load("platform.png", Texture.class);
        assetManager.load("sfx/jump.ogg", Sound.class);
        assetManager.load("sfx/die.ogg", Sound.class);
        assetManager.load("mfx/song.ogg", Music.class);
        assetManager.finishLoading();

        screens = new HashMap<String, BaseScreen>();
        screens.put("game", new GameScreen(this));
        screens.put("gameover", new GameOverScreen(this));
        screens.put("menu", new MenuScreen(this));

        setScreen(screens.get("menu"));
    }


    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Map<String, BaseScreen> getScreensManager() {
        return screens;
    }
}
