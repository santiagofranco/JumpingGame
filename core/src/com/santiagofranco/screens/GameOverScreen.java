package com.santiagofranco.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.santiagofranco.Constants;
import com.santiagofranco.MainGame;

/**
 * @author santiagofranco - @santicolombian
 */
public class GameOverScreen extends BaseScreen {


    private Stage stage;
    private Skin skin;

    private TextButton retry, menu;
    private Image bg;
    private Label scoreLabel;

    public GameOverScreen(final MainGame game) {
        super(game);
        stage = new Stage(new FillViewport(Constants.GAME_WIDHT, Constants.GAME_HEIGHT));
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        retry = new TextButton("Reintentar", skin);
        menu = new TextButton("Menu", skin);

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

        retry.setSize(280, 50);
        retry.setPosition(Constants.GAME_WIDHT / 2 - retry.getWidth() / 2, 200);
        menu.setSize(280, 50);
        menu.setPosition(Constants.GAME_WIDHT / 2 - retry.getWidth() / 2, 140);

        bg = new Image(game.getAssetManager().get("bggameover.png", Texture.class));
        bg.setPosition(0, 0);

        scoreLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel.setFontScale(4);

        stage.addActor(bg);
        stage.addActor(scoreLabel);
        stage.addActor(retry);
        stage.addActor(menu);
    }


    @Override
    public void show() {
        float labelX = ((Constants.GAME_WIDHT / 2) / 2 - (scoreLabel.getWidth() / 2));
        float labelY = ((Constants.GAME_HEIGHT / 2) - (scoreLabel.getHeight() / 2));
        scoreLabel.setPosition(labelX-20, labelY);
        scoreLabel.setText("Score: " + game.getFinalScore());
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
