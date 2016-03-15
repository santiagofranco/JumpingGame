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

import static com.santiagofranco.Constants.PIXELS_IN_METERS;

/**
 * @author santiagofranco - @santicolombian
 */
public class Floor extends Actor {

    private Texture texture;

    private World world;
    private Body body;
    private Fixture fixture;

    private float width, height;

    public Floor(Texture texture, World world, Vector2 position, float widht, float height) {
        this.texture = texture;
        this.world = world;

        this.width = widht;
        this.height = height;

        //añadir fisica a el actor
        body = world.createBody(createDef(position));
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(widht / 2f, height / 2f);
        fixture = body.createFixture(polygon, 1);
        polygon.dispose();

        fixture.setUserData("floor");

        setSize(PIXELS_IN_METERS, PIXELS_IN_METERS * height);
        setPosition((body.getPosition().x - width / 2f) * PIXELS_IN_METERS, (body.getPosition().y - height / 2f) * PIXELS_IN_METERS);
    }

    private BodyDef createDef(Vector2 position) {
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.StaticBody;
        return def;
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < width; i++) {
            batch.draw(texture, i*PIXELS_IN_METERS, getY(), getWidth(), getHeight());
        }
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public void dispose() {
        texture.dispose();
    }
}
