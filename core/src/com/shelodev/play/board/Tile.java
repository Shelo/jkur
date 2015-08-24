package com.shelodev.play.board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.shelodev.utils.ColorFading;
import com.shelodev.Settings;

public class Tile
{
    public static final Color BLANK_COLOR   = Settings.TILE_BLANK_COLOR;
    public static final int SIZE            = 20;

    private ColorFading color = new ColorFading(BLANK_COLOR);
    private Color buffer = new Color();

    private float discardDistortion = 1;
    private float fillDistortion = 1;
    private float colorMul = 1;

    // pixel positions on the board.
    private int x;
    private int y;

    // Fake highlight control.
    private boolean fake;
    private float fakeDist;

    // is this a temporal tile.
    private boolean temporal;
    private State prevTempState = State.BLANK;

    enum State { BLANK, FILLED, DISCARDED }
    private State state = State.BLANK;

    public Tile()
    {
        fillDistortion = (float) (Math.random() * 0.2f + 0.8f);
        discardDistortion = (float) (Math.random() * 0.05f + 1);
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void draw(ShapeRenderer renderer)
    {
        setColorOnState(state);
        color.update(0.1f);

        buffer.set(color.getColor());

        if (fake)
            buffer.mul(1.1f - fakeDist);

        renderer.setColor(buffer);
        renderer.rect(x, y, SIZE, SIZE);
    }

    private void setColorOnState(State state)
    {
        switch (state)
        {
            case BLANK:
                color.setTarget(BLANK_COLOR);
                break;
            case FILLED:
                color.setTarget(Settings.TILE_FILL_COLOR, fillDistortion);
                break;
            case DISCARDED:
                color.setTarget(Settings.TILE_DISCARD_COLOR, discardDistortion);
                break;
        }

        if (temporal)
            color.addTarget(0.2f, 0.2f, 0.2f, 0);

        color.mulTarget(colorMul);
    }

    public void fill(boolean temporal)
    {
        if (temporal && !this.temporal)
            enableTemporal();

        if (state != State.FILLED)
            state = State.FILLED;
        else
            clear();
    }

    public void discard(boolean temporal)
    {
        if (temporal && !this.temporal)
            enableTemporal();

        if (state != State.DISCARDED)
            state = State.DISCARDED;
        else
            clear();
    }

    public void clear()
    {
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
    }

    public void applyTemporal()
    {
        if (!temporal)
            return;

        temporal = false;
        prevTempState = state;
    }

    public ColorFading getColor()
    {
        return color;
    }

    public void setColorMul(float colorMul)
    {
        this.colorMul = colorMul;
    }
}
