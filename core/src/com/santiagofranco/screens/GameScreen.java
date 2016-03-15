package com.santiagofranco.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.santiagofranco.MainGame;
import com.santiagofranco.actors.Floor;
import com.santiagofranco.actors.Platform;
import com.santiagofranco.actors.Player;

import java.util.ArrayList;

/**
 * @author santiagofranco - @santicolombian
 */
public class GameScreen extends BaseScreen {

    private Stage stage;
    private World world;
    private Vector3 cameraInitialPosition;
    private Box2DDebugRenderer renderer;

    private Floor floor;
    private Player player;
    private ArrayList<Platform> platforms;

    private Sound jumpSound, dieSound;
    private Music song;

    private boolean playing;
    private int score;
    private int jumps;

    public GameScreen(MainGame game) {
        super(game);
        stage = new Stage(new FillViewport(360, 640));
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new GameContactsListener());

        cameraInitialPosition = new Vector3(stage.getCamera().position);

        renderer = new Box2DDebugRenderer();

        platforms = new ArrayList<Platform>();

        jumpSound = game.getAssetManager().get("sfx/jump.ogg");
        dieSound = game.getAssetManager().get("sfx/die.ogg");
        song = game.getAssetManager().get("mfx/song.ogg");
    }

    @Override
    public void show() {

        player = new Player(game.getAssetManager().get("player.png", Texture.class), world, new Vector2(1, 2));
        floor = new Floor(game.getAssetManager().get("floor.png", Texture.class), world, new Vector2(0, 0 + 0.5f), 20f, 1f);

        stage.addActor(floor);
        stage.addActor(player);

        //TODO: Improve algorithm to generate platforms, and autogenerate when the player goes up.
        platforms.clear();
        int ax = (int) (Math.random() * 5 + 0);
        platforms.add(new Platform(game.getAssetManager().get("platform.png", Texture.class), world, new Vector2(ax, 3), 1.2f, 0.3f));
        for (int i = 3; i <= 25; i++) {

            int x, y;
            x = (int) (Math.random() * 5 + 0);
            y = ((int) (Math.random() * 3 + 1)) + i;
            while ((x > ax && x < ax * 0.6f) || (x < ax && x > ax - 0.6f)) {
                x = (int) (Math.random() * 5 + 0);
            }

            platforms.add(new Platform(game.getAssetManager().get("platform.png", Texture.class), world, new Vector2(x, y), 1.2f, 0.3f));
            ax = x;
        }

        for (Platform platform : platforms) {
            stage.addActor(platform);
        }

        stage.getCamera().position.set(cameraInitialPosition);
        stage.getCamera().update();

        playing = false;
        score = 0;
        jumps = 0;

        song.play();

    }

    @Override
    public void hide() {

        stage.clear();

        player.detach();
        floor.detach();
        for (Platform platform : platforms) {
            platform.detach();
        }

        song.stop();

    }

    @Override
    public void dispose() {
        player.dispose();
        floor.dispose();
        for (Platform p : platforms) p.dispose();
        jumpSound.dispose();
        dieSound.dispose();
        song.dispose();
        stage.dispose();
        world.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        world.step(delta, 6, 2);
        updateCamera();
        //TODO: update score
        stage.draw();

    }

    private void updateCamera() {
        float y = player.getY();
        if (playing && player.isAlive() && y - 320f > 0) {
            stage.getCamera().position.set(180f, y, 0);
            stage.getCamera().update();
        }
    }


    private class GameContactsListener implements ContactListener {

        private boolean areCollided(Contact contact, Object userA, Object userB) {
            Object userDataA = contact.getFixtureA().getUserData();
            Object userDataB = contact.getFixtureB().getUserData();

//            if (userDataA == null || userDataB == null) {
//                return false;
//            }
//
//            return (userDataA.equals(userA) && userDataB.equals(userB)) ||
//                    (userDataA.equals(userB) && userDataB.equals(userA));

            return !(userDataA == null || userDataB == null) && ((userDataA.equals(userA) && userDataB.equals(userB)) || (userDataA.equals(userB) && userDataB.equals(userA)));

        }


        @Override
        public void beginContact(Contact contact) {
            if (areCollided(contact, "player", "floor")) {
                if (!playing)
                    playerJump();
                else
                    gameOver();
            }

            if (areCollided(contact, "player", "platformTopLimit")) {
                if (!playing && jumps > 0) playing = true;
                else jumps++;
                //TODO: sumar puntos cuando la plataforma este mas alta que la anterior tocada
                score++;
                playerJump();
            }
        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }
    }

    private void playerJump() {
        player.mustJump();
        jumpSound.play();
    }

    private void playerDie() {
        player.die();
        dieSound.play();
    }

    private void gameOver() {

        if (player.isAlive()) playerDie();

        stage.addAction(Actions.sequence(
                Actions.delay(1.5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(game.getScreensManager().get("gameover"));
                    }
                })
        ));

    }
}
