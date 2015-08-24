package com.shelodev.selection;

import com.badlogic.gdx.InputProcessor;

public class SelectionInputProcessor implements InputProcessor
{
    private SelectionScreen selectionScreen;

    private int heldDown = - 1;
    private float holdTime;
    private boolean firstDown;

    public SelectionInputProcessor(SelectionScreen selectionScreen)
    {
        this.selectionScreen = selectionScreen;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}
