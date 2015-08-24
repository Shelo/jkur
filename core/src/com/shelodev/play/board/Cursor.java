package com.shelodev.play.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.shelodev.utils.ColorFading;
import com.shelodev.Res;

public class Cursor
{
    private static final Color POINTER_COLOR    = new Color(1, 1, 1, 0.1f);
    private static final Color CURSOR_COLOR     = new Color(0, 0, 0, 1);
    private static final Color BLINK_TARGET     = new Color(0.75f, 0.25f, 0.25f, 1);
    private static final Color TEMPORAL_TARGET  = new Color(0.25f, 0.75f, 0.25f, 1);

    // to get dimensions on the counter text.
    private GlyphLayout layout = new GlyphLayout();

    // origin of the count.
    private Vector2 countOrigin = new Vector2();
    private boolean startJoystick;

    // control color changes.
    private boolean toTarget = true;
    ColorFading color = new ColorFading(CURSOR_COLOR);

    // parent board.
    private Board board;

    // discrete positions on the board.
    private int x;
    private int y;

    // place temporal tiles.
    private boolean temporal;

    public Cursor(Board board, int x, int y)
    {
        this.board = board;
        this.x = x;
        this.y = y;
    }

    public Cursor(Board board)
    {
        this(board, 0, 0);
    }

    public void draw(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch)
    {
        int pixelX = board.getTileAt(x, y).getPositionX();
        int pixelY = board.getTileAt(x, y).getPositionY();

        Gdx.gl.glLineWidth(2);
        updateColor();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(color.getColor());
        shapeRenderer.rect(pixelX, pixelY, Tile.SIZE, Tile.SIZE);
        shapeRenderer.end();

        // draw row and column pointers.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glLineWidth(1);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(POINTER_COLOR);
        shapeRenderer.rect(- 400, pixelY, 2000, Tile.SIZE);
        shapeRenderer.rect(pixelX, - 400, Tile.SIZE, 2000);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // handle counter.
        boolean count = Gdx.input.isKeyPressed(Input.Keys.C);
        boolean start = Gdx.input.isKeyJustPressed(Input.Keys.C);

        if (Controllers.getControllers().size > 0)
        {
            boolean joyCount = Controllers.getControllers().get(0).getButton(4);
            count |= joyCount;

            if (joyCount)
            {
                if (startJoystick)
                {
                    startJoystick = false;
                    start |= true;
                }
            }
            else
            {
                startJoystick = true;
            }
        }

        // draw counter.
        if (count)
        {
            if (start)
                countOrigin.set(x, y);

            int vx = (int) Math.abs(x - countOrigin.x);
            int vy = (int) Math.abs(y - countOrigin.y);
            String dist;

            if (vx > vy)
                dist = String.valueOf(vx + 1);
            else
                dist = String.valueOf(vy + 1);

            spriteBatch.begin();
            Res.font.setColor(Color.BLACK);
            layout.setText(Res.font, dist);
            Res.font.draw(spriteBatch, dist, pixelX + (Tile.SIZE - layout.width) / 2, pixelY + 4);
            spriteBatch.end();
        }
    }

    public void updateColor()
    {
        color.update(0.05f);

        if (toTarget)
        {
            Color target = BLINK_TARGET;

            if (temporal)
                target = TEMPORAL_TARGET;

            color.setTarget(target);

            float distance = Math.abs(color.getColor().r - target.r);
            if (distance <= 0.01f)
                toTarget = false;
        }
        else
        {
            color.setTarget(CURSOR_COLOR);

            float distance = Math.abs(color.getColor().r - CURSOR_COLOR.r);
            if (distance <= 0.01f)
                toTarget = true;
        }
    }

    public void move(int dx, int dy)
    {
        if (x + dx < board.getWidth() && x + dx >= 0)
            x += dx;

        if (y + dy < board.getHeight() && y + dy >= 0)
            y += dy;

        boolean zDown = Gdx.input.isKeyPressed(Input.Keys.Z);
        boolean xDown = Gdx.input.isKeyPressed(Input.Keys.X);

        if (Controllers.getControllers().size > 0)
        {
            Controller controller = Controllers.getControllers().get(0);
            zDown |= controller.getButton(1);
            xDown |= controller.getButton(2);
        }

        if (zDown)
            fillIfBlank();

        if (xDown)
            discardIfBlank();
    }

    private void discardIfBlank()
    {
        if (board.getTileAt(x, y).isBlank())
        {
            board.getTileAt(x, y).discard(temporal);
            board.update(x, y);
        }
    }

    private void fillIfBlank()
    {
        if (board.getTileAt(x, y).isBlank())
        {
            board.getTileAt(x, y).fill(temporal);
            board.update(x, y);
        }
    }

    public void fill()
    {
        board.getTileAt(x, y).fill(temporal);
        board.update(x, y);
    }

    public void discard()
    {
        board.getTileAt(x, y).discard(temporal);
        board.update(x, y);
    }

    public void toggleTemporal()
    {
        temporal = !temporal;
    }

    public void applyTemporal()
    {
        temporal = false;
        board.applyTemporal();
    }

    public void discardTemporal()
    {
        temporal = false;
        board.discardTemporal();
    }
}
