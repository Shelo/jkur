package com.shelodev;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.shelodev.playscreen.PlayScreen;
import com.shelodev.utils.PuzzleLoader;
import com.shelodev.utils.SceneTools;

public class Main extends ApplicationAdapter
{
    private PlayScreen playScreen;
    private SceneTools sceneTools;
    private PuzzleLoader puzzleLoader;

    @Override
    public void create()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        puzzleLoader = new PuzzleLoader("levels");
        sceneTools = new SceneTools();

        playScreen = new PlayScreen(puzzleLoader.load("levels/1/0.krk"));

        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void render()
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sceneTools.resetCameraPosition();

        sceneTools.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        sceneTools.getShapeRenderer().setColor(Settings.BACKGROUND_COLOR);
        sceneTools.getShapeRenderer().rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sceneTools.getShapeRenderer().end();

        playScreen.draw(sceneTools);
    }
}
