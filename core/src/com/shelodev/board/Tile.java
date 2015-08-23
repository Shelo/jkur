package com.shelodev.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.shelodev.Settings;

public class Tile
{
    public static final int SIZE = 20;

    private static final Color CLEAR_COLOR  = Settings.TILE_BLANK_COLOR;
    private static final float FADE_TIME    = 0.5f;

    // colors of the tile, take into account that for every tile there's a chance that this color varies.
    private Color filledColor = Settings.TILE_FILL_COLOR.cpy();
    private Color discardColor = Settings.TILE_DISCARD_COLOR.cpy();
    private Color fakeColor = new Color();
    private Color color = new Color(CLEAR_COLOR);

    // timer to control the fadeout/in.
    private float timer;

    // pixel positions on the board.
    private int x;
    private int y;

    // Fake highlight control.
    private boolean fake;
    private float fakeDist;

    // is this a temporal tile.
    private boolean temporal;
    private State prevTempState = State.BLANK;

    // the fade is controlled with a state machine.
    enum State { BLANK, FILLED, DISCARDED }
    private State prevState = State.BLANK;
    private State state = State.BLANK;

    public Tile()
    {
        filledColor.mul((float) (Math.random() * 0.2f + 0.8f));
        discardColor.mul((float) (Math.random() * 0.05f + 1));
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void draw(ShapeRenderer renderer)
    {
        if (timer > 0)
            timer -= Gdx.graphics.getDeltaTime();

        if (timer > 0)
        {
            switch (state)
            {
                case BLANK:
                    updateColorIfNotBlank(State.FILLED, filledColor, discardColor, CLEAR_COLOR);
                    break;

                case FILLED:
                    updateColorIfNotBlank(State.BLANK, CLEAR_COLOR, discardColor, filledColor);
                    break;

                case DISCARDED:
                    updateColorIfNotBlank(State.BLANK, CLEAR_COLOR, filledColor, discardColor);
                    break;
            }

            if (temporal)
                fakeColor.add(0.2f, 0.2f, 0.2f, 0);

            renderer.setColor(fakeColor);
        }
        else
        {
            if (fake)
            {
                fakeColor.set(color).mul(1.1f - fakeDist);

                if (temporal)
                    fakeColor.add(0.2f, 0.2f, 0.2f, 0);

                renderer.setColor(fakeColor);
            }
            else
            {
                fakeColor.set(color);

                if (temporal)
                    fakeColor.add(0.2f, 0.2f, 0.2f, 0);

                renderer.setColor(fakeColor);
            }
        }

        renderer.rect(x, y, SIZE, SIZE);
    }

    private void updateColorIfNotBlank(State conditional, Color prev1, Color prev2, Color target)
    {
        Color prevColor;

        if (prevState == conditional)
            prevColor = prev1;
        else
            prevColor = prev2;

        color.set(prevColor).lerp(target, 1 - timer / FADE_TIME);
        fakeColor.set(color);
    }

    public void fill(boolean temporal)
    {
        if (temporal && !this.temporal)
            enableTemporal();

        if (state != State.FILLED)
        {
            prevState = state;
            timer = FADE_TIME;

            state = State.FILLED;
        }
        else
        {
            clear();
        }
    }

    public void discard(boolean temporal)
    {
        if (temporal)
            enableTemporal();

        if (state != State.DISCARDED)
        {
            prevState = state;
            timer = FADE_TIME;

            state = State.DISCARDED;
        }
        else
        {
            clear();
        }
    }

    public void clear()
    {
        if (state != State.BLANK)
        {
            prevState = state;
            timer = FADE_TIME;
        }

        state = State.BLANK;
    }

    public int getPositionX()
    {
        return x;
    }

    public int getPositionY()
    {
        return y;
    }

    public boolean isFilled()
    {
        return state == State.FILLED;
    }

    public boolean isDiscarded()
    {
        return state == State.DISCARDED;
    }

    public boolean isBlank()
    {
        return state == State.BLANK;
    }

    public void setFake(boolean fake)
    {
        this.fake = fake;
    }

    public void setFake(boolean fake, int distance)
    {
        setFake(fake);

        fakeDist = distance * 0.04f;
    }

    public void enableTemporal()
    {
        prevTempState = state;
        temporal = true;
    }

    public void discardTemporal()
    {
        if (!temporal)
            return;

        temporal = false;
        state = prevTempState;

        switch (prevTempState)
        {
            case BLANK:
                color.set(CLEAR_COLOR);
                break;
            case FILLED:
                color.set(filledColor);
                break;
            case DISCARDED:
                color.set(discardColor);
                break;
        }
    }

    public void applyTemporal()
    {
        if (!temporal)
            return;

        temporal = false;
        prevTempState = state;
    }
}
