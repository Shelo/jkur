package com.shelodev.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Tile
{
    private static final float FADE_TIME = 0.5f;
    public static final int SIZE = 20;

    private static final Color CLEAR_COLOR = new Color(1, 1, 1, 1);

    private Color filledColor = new Color(0.15234375f, 0.4453125f, 0.69140625f, 1);
    private Color discardColor = new Color(0.2f, 0.2f, 0.2f, 1);
    private Color fakeColor = new Color();

    private Color color = new Color(CLEAR_COLOR);
    private float timer;
    private int x;
    private int y;
    private boolean fake;
    private float fakeDist;

    enum State
    {
        BLANK(0), FILLED(1), DISCARDED(2);

        private byte value;

        State(int value)
        {
            this.value = (byte) value;
        }

        public byte value()
        {
            return value;
        }
    }

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
        {
            timer -= Gdx.graphics.getDeltaTime();
        }

        if (timer > 0)
        {
            if (state == State.FILLED)
            {
                Color prevColor;

                if (prevState == State.BLANK)
                    prevColor = CLEAR_COLOR;
                else
                    prevColor = discardColor;

                color.set(prevColor).lerp(filledColor, 1 - timer / FADE_TIME);
            }
            else if (state == State.DISCARDED)
            {
                Color prevColor;

                if (prevState == State.BLANK)
                    prevColor = CLEAR_COLOR;
                else
                    prevColor = filledColor;

                color.set(prevColor).lerp(discardColor, 1 - timer / FADE_TIME);
            }
            else if (state == State.BLANK)
            {
                Color prevColor;

                if (prevState == State.FILLED)
                    prevColor = filledColor;
                else
                    prevColor = discardColor;

                color.set(prevColor).lerp(CLEAR_COLOR, 1 - timer / FADE_TIME);
            }

            renderer.setColor(color);
        }
        else
        {
            if (fake)
            {
                fakeColor.set(color).mul(1.1f - fakeDist);
                renderer.setColor(fakeColor);
            }
            else
            {
                renderer.setColor(color);
            }
        }

        renderer.rect(x, y, SIZE, SIZE);
    }

    public void fill()
    {
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

    public void discard()
    {
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

    public byte getState()
    {
        return state.value();
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
}
