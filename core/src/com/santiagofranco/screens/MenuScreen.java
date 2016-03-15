package com.santiagofranco.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.santiagofranco.MainGame;

/**
 * @author santiagofranco - @santicolombian
 */
public class MenuScreen extends BaseScreen {

    private Stage stage;
    private Skin skin;

    private TextButton play;

    public MenuScreen(final MainGame game) {
        super(game);
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        play = new TextButton("Jugar",skin);

        play.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.getScreensManager().get("game"));
            }
        });

        play.setSize(400,200);
        play.setPosition(80,220);

        stage.addActor(play);
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
