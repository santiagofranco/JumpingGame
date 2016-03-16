package com.santiagofranco.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * @author santiagofranco - @santicolombian
 */
public class HUD {

    private int score;

    private BitmapFont scoreLabel;

    public HUD() {
        scoreLabel = new BitmapFont(Gdx.files.internal("skins/default.fnt"));
    }

    public void update(Batch batch, float x, float y) {
        batch.begin();
        scoreLabel.draw(batch, "SCORE: " + score, x, y);
        batch.end();
    }

    public void incScore() {
        score++;
    }

    public int getScore(){
        return score;
    }

}
