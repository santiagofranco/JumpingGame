package com.santiagofranco.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.santiagofranco.Constants.*;

/**
 * @author santiagofranco - @santicolombian
 */
public class Player extends Actor {

    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    private Vector2 positionInitial;
    private boolean jumping;
    private boolean mustJump;
    private boolean alive;
    private float width, height;

    public Player(Texture texture, World world, Vector2 positionInitial) {
        this.texture = texture;
        this.world = world;

        this.positionInitial = positionInitial;
        width = height = 0.6f;

        //añadir fisica a el actor
        body = world.createBody(createPlayerDef(positionInitial));
        PolygonShape playerShape = new PolygonShape();
        playerShape.setAsBox(width / 2f, height / 2f);
        fixture = body.createFixture(playerShape, 1);
        playerShape.dispose();

        fixture.setUserData("player");

        setSize(PIXELS_IN_METERS * width, PIXELS_IN_METERS * height);

        live();

    }


    @Override
    public void act(float delta) {

        if (alive) move(Gdx.input.getAccelerometerX());

        if (mustJump) {
            mustJump = false;
            jump();
        }

        if (jumping) {
            body.applyForceToCenter(0, -IMPULSE_JUMP * 1.15f, true);
        }

    }

    private void move(float accX) {

        if (accX > 1) {
            body.applyForce(-IMPULSE_PLAYER, 0, body.getPosition().x, body.getPosition().y, true);
        }

        if (accX < -1) {
            body.applyForce(IMPULSE_PLAYER, 0, body.getPosition().x, body.getPosition().y, true);
        }

        //Se sale por el borde derecho
        if (body.getPosition().x > GAME_WIDHT / PIXELS_IN_METERS) {
            body.setTransform(new Vector2(0, body.getPosition().y), body.getAngle());
        }

        //Se sale por el borde izquierdo
        if (body.getPosition().x < 0) {
            body.setTransform(new Vector2((GAME_WIDHT / PIXELS_IN_METERS), body.getPosition().y), body.getAngle());
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.5f) * PIXELS_IN_METERS, (body.getPosition().y - 0.5f) * PIXELS_IN_METERS);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }


    private BodyDef createPlayerDef(Vector2 position) {
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        return def;
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public void jump() {
        jumping = true;
        mustJump = false;
        Vector2 position = body.getPosition();
        body.applyLinearImpulse(0, IMPULSE_JUMP, position.x, position.y, true);
    }


    public void mustJump() {
        this.mustJump = true;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
    }

    public void live() {
        mustJump = jumping = false;
        alive = true;
        body.getPosition().set(positionInitial);
    }

    public void dispose(){
        texture.dispose();
    }
}
