package com.shelodev.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.shelodev.board.Cursor;

import java.util.HashSet;

public class PlayInputProcessor implements InputProcessor
{
    private static final float FIRST_WAIT = 0.3f;
    private static final float NORMAL_WAIT = 0.05f;

    private HashSet<Integer> heldKeys;
    private Cursor cursor;
    private int heldDown = - 1;
    private float holdTime;
    private boolean firstDown;

    public PlayInputProcessor(Cursor cursor)
    {
        this.cursor = cursor;

        heldKeys = new HashSet<Integer>();
        heldKeys.add(Input.Keys.RIGHT);
        heldKeys.add(Input.Keys.UP);
        heldKeys.add(Input.Keys.LEFT);
        heldKeys.add(Input.Keys.DOWN);
    }

    public void update(Cursor cursor)
    {
        if (heldDown != -1)
        {
            holdTime -= Gdx.graphics.getDeltaTime();

            if (firstDown)
            {
                trigger(heldDown);
                holdTime = FIRST_WAIT;
                firstDown = false;
            }
            else
            {
                if (holdTime <= 0)
                {
                    holdTime = NORMAL_WAIT;
                    trigger(heldDown);
                }
            }
        }
    }

    public void trigger(int keycode)
    {
        switch (keycode)
        {
            case Input.Keys.Z:
                cursor.fill();
                break;

            case Input.Keys.X:
                cursor.discard();
                break;

            case Input.Keys.C:
                cursor.toggleCount();
                break;

            case Input.Keys.LEFT:
                cursor.move(-1, 0);
                break;

            case Input.Keys.RIGHT:
                cursor.move(1, 0);
                break;

            case Input.Keys.DOWN:
                cursor.move(0, -1);
                break;

            case Input.Keys.UP:
                cursor.move(0, 1);
                break;
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        if (heldKeys.contains(keycode))
        {
            if (heldDown != keycode)
            {
                heldDown = keycode;
                firstDown = true;
                holdTime = 0;
            }
        }
        else
        {
            trigger(keycode);
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        if (heldKeys.contains(keycode))
            if (keycode == heldDown)
                heldDown = - 1;

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
