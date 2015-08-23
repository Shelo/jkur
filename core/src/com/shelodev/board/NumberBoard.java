package com.shelodev.board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shelodev.Res;

import java.util.ArrayList;

public class NumberBoard
{
    private static final Color COMPLETED_COLOR = new Color(0.5f, 0.5f, 0.5f, 1);
    private static final Color NORMAL_COLOR = Color.WHITE;

    private GlyphLayout layout = new GlyphLayout();

    private boolean[][] completed;
    private byte[][] numbers;
    private int width;
    private int height;

    private int px;
    private int py;

    public NumberBoard(int width, int height)
    {
        this.width = width;
        this.height = height;

        numbers = new byte[width][height];
        completed = new boolean[width][height];

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                numbers[x][y] = -1;
            }
        }
    }

    public void setInLine(int x, byte value)
    {
        numbers[x][py] = value;
    }

    public void setNext(byte value)
    {
        numbers[px++][py] = value;

        if (px >= width)
        {
            px = 0;
        }
    }

    public void skipLine()
    {
        px = 0;
        py++;
    }

    public void draw(SpriteBatch spriteBatch)
    {
        for (int x = 0; x < width; x++)
        {
            int posX = Tile.SIZE * x + x + x / Board.SECTOR + 1;

            for (int y = 0; y < height; y++)
            {
                if (numbers[x][y] != -1)
                {
                    int posY = Tile.SIZE * y + y + y / Board.SECTOR + 4;

                    String number = String.valueOf(numbers[x][y]);

                    // to get the width of the text.
                    layout.setText(Res.font, number);

                    if (completed[x][y])
                    {
                        Res.font.setColor(COMPLETED_COLOR);
                    }
                    else
                    {
                        Res.font.setColor(NORMAL_COLOR);
                    }

                    // draws the font with the flipped coordinates.
                    Res.font.draw(spriteBatch, number, posX + (Tile.SIZE - layout.width) / 2, posY);
                }
            }
        }
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public byte getAt(int x, int y)
    {
        return numbers[x][y];
    }

    public ArrayList<Byte> getColumn(int x)
    {
        ArrayList<Byte> values = new ArrayList<Byte>();

        for (int y = 0; y < height; y++)
        {
            if (getAt(x, y) != -1)
            {
                values.add(getAt(x, y));
            }
        }

        return values;
    }

    public ArrayList<Byte> getRow(int y)
    {
        ArrayList<Byte> values = new ArrayList<Byte>();

        for (int x = 0; x < width; x++)
        {
            if (getAt(x, y) != -1)
            {
                values.add(getAt(x, y));
            }
        }

        return values;
    }

    public void setCompleted(int x, int y)
    {
        completed[x][y] = true;
    }

    public void setNormal(int x, int y)
    {
        completed[x][y] = false;
    }
}
