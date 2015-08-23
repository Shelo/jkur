package com.shelodev.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.shelodev.board.Cursor;

import java.util.HashSet;

public class PlayInputProcessor implements InputProcessor, ControllerListener
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

    public void update()
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

            case Input.Keys.SPACE:
                cursor.toggleTemporal();
                break;

            case Input.Keys.ENTER:
                cursor.applyTemporal();
                break;

            case Input.Keys.BACKSPACE:
                cursor.discardTemporal();
                break;

            case Input.Keys.LEFT:
                cursor.move(-1, 0);
                break;

            case Input.Keys.RIGHT:
                cursor.move(1, 0);
                break;

            case Input.Keys.DOWN:
                cursor.move(0, 1);
                break;

            case Input.Keys.UP:
                cursor.move(0, -1);
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
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        switch (buttonCode)
        {
            case 1:
                keyDown(Input.Keys.Z);
                break;

            case 2:
                keyDown(Input.Keys.X);
                break;
        }

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    {
        switch (buttonCode)
        {
            case 1:
                keyUp(Input.Keys.Z);
                break;

            case 2:
                keyUp(Input.Keys.X);
                break;
        }

        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value)
    {
        if (Math.abs(value) < 0.5)
        {
            keyUp(heldDown);
            return false;
        }

        // horizontal.
        if (axisCode == 0)
        {
            boolean right = value > 0.5;
            keyDown(right ? Input.Keys.RIGHT : Input.Keys.LEFT);
        }
        // vertical.
        else
        {
            boolean up = value < 0.5;
            keyDown(up ? Input.Keys.UP : Input.Keys.DOWN);
        }

        return true;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value)
    {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
    {
        return false;
    }

    @Override
    public void connected(Controller controller)
    {

    }

    @Override
    public void disconnected(Controller controller)
    {

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
