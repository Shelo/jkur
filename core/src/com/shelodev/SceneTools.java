package com.shelodev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class SceneTools
{
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    public SceneTools()
    {
        camera = new OrthographicCamera(800, 600);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
    }

    public ShapeRenderer getShapeRenderer()
    {
        return shapeRenderer;
    }

    public SpriteBatch getSpriteBatch()
    {
        return spriteBatch;
    }

    public Camera getCamera()
    {
        return camera;
    }

    public void setCameraPosition(float x, float y)
    {
        camera.position.x = x;
        camera.position.y = y;
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    public void setCameraPosition(Vector2 position, float mul)
    {
        setCameraPosition(position.x * mul, position.y * mul);
    }

    public void resetCameraPosition()
    {
        setCameraPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }
}
