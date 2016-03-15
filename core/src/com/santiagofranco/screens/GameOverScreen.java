package com.santiagofranco.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.santiagofranco.MainGame;

/**
 * @author santiagofranco - @santicolombian
 */
public class GameOverScreen extends BaseScreen {


    private Stage stage;
    private Skin skin;

    private TextButton retry, menu;

    public GameOverScreen(final MainGame game) {
        super(game);
        stage = new Stage(new FillViewport(360,640));
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        retry = new TextButton("Reintentar",skin);
        menu = new TextButton("Menu",skin);

        retry.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.getScreensManager().get("game"));
            }
        });

        menu.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.getScreensManager().get("menu"));
            }
        });

        retry.setSize(200, 200);
        retry.setPosition(80,330);
        menu.setSize(200,200);
        menu.setPosition(80,110);

        stage.addActor(retry);
        stage.addActor(menu);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
}
