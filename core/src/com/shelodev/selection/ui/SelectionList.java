package com.shelodev.selection.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.shelodev.Res;
import com.shelodev.utils.LerpFloat;
import com.shelodev.utils.SceneTools;

import java.util.ArrayList;

public class SelectionList
{
    private static final int ITEM_PADDING       = 2;
    private static final int ITEM_HEIGHT        = 20;
    private static final int ITEM_TEXT_PAD      = 4;

    private ArrayList<SelectionItem> items = new ArrayList<SelectionItem>();

    private int cursorPosition;

    private LerpFloat width = new LerpFloat();
    private LerpFloat height = new LerpFloat();
    private LerpFloat x = new LerpFloat();
    private LerpFloat y = new LerpFloat();

    public SelectionList(float x, float y, float width, float height)
    {
        this.x.reset(x);
        this.y.reset(y);
        this.width.reset(width);
        this.height.reset(height);
    }

    public void draw(SceneTools tools)
    {
        updateValues();
        drawItemBackgrounds(tools.getShapeRenderer());
        drawItemTexts(tools.getSpriteBatch());
    }

    private void drawItemBackgrounds(ShapeRenderer renderer)
    {
        Gdx.gl.glEnable(GL20.GL_BLEND);

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < items.size(); i++)
        {
            if (cursorPosition == i)
                renderer.setColor(0.5f, 0.5f, 0.5f, 0.25f);
            else
                renderer.setColor(0, 0, 0, 0.25f);

            renderer.rect(x.get(), y.get() + i * (ITEM_PADDING + ITEM_HEIGHT), width.get(), ITEM_HEIGHT);
        }

        renderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawItemTexts(SpriteBatch batch)
    {
        batch.begin();

        for (int i = 0; i < items.size(); i++)
        {
            Res.font.draw(batch, items.get(i).text(), x.get() + ITEM_TEXT_PAD * 2,
                    y.get() + i * (ITEM_PADDING + ITEM_HEIGHT) + ITEM_TEXT_PAD);
        }

        batch.end();
    }

    private void updateValues()
    {
        width.update(0.1f);
        height.update(0.1f);
        x.update(0.1f);
        y.update(0.1f);
    }

    public void clear()
    {
        items.clear();
    }

    public void addItem(String item)
    {
        items.add(new SelectionItem(item));
    }

    public void setHeight(float height)
    {
        this.height.set(height);
    }

    public void setWidth(float width)
    {
        this.width.set(width);
    }

    public void setX(float x)
    {
        this.x.set(x);
    }

    public void setY(float y)
    {
        this.y.set(y);
    }

    public LerpFloat getX()
    {
        return x;
    }

    public LerpFloat getY()
    {
        return y;
    }

    public LerpFloat getWidth()
    {
        return width;
    }

    public LerpFloat getHeight()
    {
        return height;
    }
}
