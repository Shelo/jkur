package com.shelodev.playscreen.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.shelodev.utils.CorrectGroups;
import com.shelodev.utils.GroupList;

import java.util.*;

public class Board
{
    public static final float FAKE_INTERVAL = 5;
    public static final float FAKE_STAY     = 0.001f;
    public static final int SECTOR          = 5;

    // matrix of tiles.
    private Tile[][] tiles;

    // dimension in pixels.
    private Vector2 size;

    // discrete dimensions of the board.
    private int width;
    private int height;

    // matrices of numbers.
    private NumberBoard left;
    private NumberBoard top;

    private boolean[] completedRows;
    private boolean[] completedColumns;

    // for the fake highlight effect.
    private int currentColumn;
    private float fakeStayTime;
    private float fakeTimer = FAKE_INTERVAL;

    // is this the first time we saw a winning puzzle.
    private boolean firstWin = true;

    public Board(int width, int height, NumberBoard left, NumberBoard top)
    {
        this.width = width;
        this.height = height;
        this.left = left;
        this.top = top;

        completedRows = new boolean[height];
        completedColumns = new boolean[width];

        size = new Vector2();
        tiles = new Tile[width][height];

        buildMatrix();
        calcSize();
    }

    private void calcSize()
    {
        size.x = Tile.SIZE * (width - 1) + (width - 1) + (width - 1) / SECTOR + Tile.SIZE;
        size.y = Tile.SIZE * (height - 1) + (height - 1) + (height - 1) / SECTOR + Tile.SIZE;
    }

    private void buildMatrix()
    {
        for (int x = 0; x < width; x++)
        {
            int posX = Tile.SIZE * x + x + x / SECTOR;

            for (int y = 0; y < height; y++)
            {
                int posY = Tile.SIZE * y + y + y / SECTOR;

                tiles[x][y] = new Tile();
                tiles[x][y].setPosition(posX, posY);
            }
        }
    }

    public void draw(ShapeRenderer shapeRenderer)
    {
        updateFaker();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);

        for (int x = 0; x < width; x++)
        {
            int c = currentColumn;
            for (int y = 0; y < height; y++)
            {
                // update fake effect for the tile.
                c--;

                if (fakeTimer <= 0 && (x == c || x == c - 1 || x == c + 1 || x == c - 2 || x == c + 2))
                    tiles[x][y].setFake(true, Math.abs(c - x));
                else
                    tiles[x][y].setFake(false);

                tiles[x][y].draw(shapeRenderer);
            }
        }

        shapeRenderer.end();

        if (isPuzzleCompleted() && firstWin)
        {
            fakeTimer = 0;
            currentColumn = 0;
            fakeStayTime = 0;
            firstWin = false;

            System.out.println("Puzzle completed!");
        }
    }

    private void updateFaker()
    {
        if (fakeTimer <= 0)
        {
            if (fakeStayTime <= 0)
            {
                currentColumn++;

                if (currentColumn >= width * 2)
                {
                    fakeTimer = FAKE_INTERVAL;
                    currentColumn = 0;
                }

                fakeStayTime = FAKE_STAY;
            }
            else
            {
                fakeStayTime -= Gdx.graphics.getDeltaTime();
            }
        }
        else
        {
            fakeTimer -= Gdx.graphics.getDeltaTime();
        }
    }

    public void update(int column, int row)
    {
        updateColumn(column);
        updateRow(row);
    }

    public void updateColumn(int column)
    {
        ArrayList<Byte> completedIndices = getCompletedIndicesColumn(column);

        int realY = 0;
        for (int y = 0; y < top.getHeight(); y++)
        {
            if (top.getAt(column, y) != -1)
            {
                if (completedIndices.contains((byte) realY))
                    top.setCompleted(column, y);
                else
                    top.setNormal(column, y);

                realY++;
            }
        }
    }

    public void updateRow(int row)
    {
        ArrayList<Byte> completedIndices = getCompletedIndicesRow(row);

        int realX = 0;
        for (int x = 0; x < left.getWidth(); x++)
        {
            if (left.getAt(x, row) != -1)
            {
                if (completedIndices.contains((byte) realX))
                    left.setCompleted(x, row);
                else
                    left.setNormal(x, row);

                realX++;
            }
        }
    }

    private ArrayList<Byte> getCompletedIndicesColumn(int i)
    {
        ArrayList<Byte> column = top.getColumn(i);
        CorrectGroups groups = ultimateColumnGroups(i);
        return getCompletedIndices(column, groups, false, i);
    }

    private ArrayList<Byte> getCompletedIndicesRow(int i)
    {
        ArrayList<Byte> row = left.getRow(i);
        CorrectGroups groups = ultimateRowGroups(i);
        return getCompletedIndices(row, groups, true, i);
    }

    public ArrayList<Byte> getCompletedIndices(ArrayList<Byte> expectedList, CorrectGroups groups, boolean row, int j)
    {
        byte[] start = groups.getStart();
        byte[] end = groups.getEnd();
        byte[] whole = groups.getWhole();

        if (start.length > expectedList.size() || end.length > expectedList.size())
            return new ArrayList<Byte>();

        if (expectedList.get(0) == 0 && whole.length == 0)
            return rangeList(1);

        if (areEqual(expectedList, whole))
        {
            setComplete(row, j, true);
            return rangeList(expectedList.size());
        }

        setComplete(row, j, false);

        ArrayList<Byte> indices = new ArrayList<Byte>();
        for (byte i = 0; i < start.length; i++)
        {
            if (i < expectedList.size())
            {
                if (start[i] != expectedList.get(i))
                    break;

                indices.add(i);
            }
        }

        for (byte i = (byte) (end.length - 1); i >= 0; i--)
        {
            if (i < expectedList.size())
            {
                if (end[i] != expectedList.get(expectedList.size() - end.length + i))
                    break;

                indices.add((byte) (expectedList.size() - end.length + i));
            }
        }

        return indices;
    }

    private void setComplete(boolean row, int i, boolean completed)
    {
        if (row)
            completedRows[i] = completed;
        else
            completedColumns[i] = completed;
    }

    public boolean isPuzzleCompleted()
    {
        for (boolean completedColumn : completedColumns)
        {
            if (!completedColumn)
                return false;
        }

        for (boolean completedRow : completedRows)
        {
            if (!completedRow)
                return false;
        }

        return true;
    }

    private ArrayList<Byte> rangeList(int end)
    {
        ArrayList<Byte> result = new ArrayList<Byte>();
        for (byte i = 0; i < end; i++)
            result.add(i);

        return result;
    }

    private boolean areEqual(ArrayList<Byte> first, byte[] second)
    {
        if (first.size() != second.length)
            return false;

        for (int i = 0; i < first.size(); i++)
            if (first.get(i) != second[i])
                return false;

        return true;
    }

    private CorrectGroups ultimateColumnGroups(int x)
    {
        GroupList start = findGroups(0, height, 1, false, x, false);
        GroupList end = findGroups(height - 1, start.getMeta(), - 1, false, x, false);
        GroupList whole = findGroups(0, height, 1, false, x, true);
        return new CorrectGroups(start.close(), end.reverse(), whole.close());
    }

    private CorrectGroups ultimateRowGroups(int y)
    {
        GroupList start = findGroups(0, width, 1, true, y, false);
        GroupList end = findGroups(width - 1, start.getMeta(), - 1, true, y, false);
        GroupList whole = findGroups(0, width, 1, true, y, true);
        return new CorrectGroups(start.close(), end.reverse(), whole.close());
    }

    private GroupList findGroups(int start, int end, int acc, boolean row, int i, boolean ignore)
    {
        GroupList groups = new GroupList();

        boolean discarding = true;
        int groupIndex = 0;
        int startEndIndex = 0;
        for (int p = start; acc > 0 ? p < end : p > end; p += acc)
        {
            Tile tile = tiles[row ? p : i][row ? i : p];
            startEndIndex = p;

            if (!ignore && tile.isBlank())
                break;

            if (tile.isFilled())
            {
                groups.sum(groupIndex);
                discarding = false;
            }
            else if ((tile.isDiscarded() || (ignore && tile.isBlank())) && !discarding)
            {
                discarding = true;
                groupIndex += 1;
            }
        }

        groups.setMeta(startEndIndex);
        return groups;
    }

    public Vector2 getSize()
    {
        return size;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public Tile getTileAt(int x, int y)
    {
        return tiles[x][y];
    }

    public void applyTemporal()
    {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                tiles[x][y].applyTemporal();
    }

    public void discardTemporal()
    {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                tiles[x][y].discardTemporal();

        for (int x = 0; x < width; x++)
            updateColumn(x);

        for (int y = 0; y < height; y++)
            updateRow(y);
    }
}
