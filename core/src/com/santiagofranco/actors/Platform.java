package com.santiagofranco.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.santiagofranco.Constants;

import static com.santiagofranco.Constants.*;

/**
 * @author santiagofranco - @santicolombian
 */
public class Platform extends Actor {

    private Texture texture;
    private World world;
    private Body body, topBody;
    private Fixture fixture, topFixture;

    private float width, height;

    public Platform(Texture texture, World world, Vector2 position, float width, float height) {
        this.texture = texture;
        this.world = world;

        this.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.width = width;
        this.height = height;

        //Body normal
        body = world.createBody(createDef(position));
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(width/2f, height/2f);
        fixture = body.createFixture(polygon,1);
        polygon.dispose();

        //Body top limit
        BodyDef def = new BodyDef();
        def.position.set(position.x,position.y+height/2f);
        def.type = BodyDef.BodyType.KinematicBody;
        topBody = world.createBody(def);
        polygon = new PolygonShape();
        polygon.setAsBox(width/2f,0.01f);
        topFixture = topBody.createFixture(polygon,1);
        polygon.dispose();

        fixture.setUserData("platform");
        topFixture.setUserData("platformTopLimit");

        setSize(width * PIXELS_IN_METERS, height * PIXELS_IN_METERS);
        setPosition((position.x - width/2f) * PIXELS_IN_METERS,  (position.y-height/2f)*PIXELS_IN_METERS);

    }

    private BodyDef createDef(Vector2 position) {
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.StaticBody;
        return def;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,getX(),getY(),getWidth(),getHeight());
    }

    public void detach(){
        topBody.destroyFixture(topFixture);
        world.destroyBody(topBody);
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public void dispose(){
        texture.dispose();
    }
}
