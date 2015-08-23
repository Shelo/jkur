package com.shelodev.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.shelodev.PuzzleLoader;
import com.shelodev.SceneTools;
import com.shelodev.board.*;

public class PlayScreen extends Screen
{
    private static final int TOP_OFFSET = - 50;

    private PlayInputProcessor inputProcessor;

    // tiles board.
    private Board board;

    // number boards.
    private NumberBoard leftBoard;
    private NumberBoard topBoard;

    private Cursor cursor;

    public PlayScreen(Puzzle puzzle)
    {
        board = puzzle.getBoard();
        leftBoard = puzzle.getLeft();
        topBoard = puzzle.getTop();

        cursor = new Cursor(board);
        cursor.move(board.getWidth() / 2, board.getHeight() / 2);

        inputProcessor = new PlayInputProcessor(cursor);
        Gdx.input.setInputProcessor(inputProcessor);
        Controllers.addListener(inputProcessor);

        for (int x = 0; x < board.getWidth(); x++)
            board.updateColumn(x);

        for (int y = 0; y < board.getHeight(); y++)
            board.updateRow(y);
    }

    @Override
    public void draw(SceneTools tools)
    {
        inputProcessor.update();

        Vector2 boardSize = board.getSize();
        float fullWidth = boardSize.x - leftBoard.getWidth() * Tile.SIZE;
        float fullHeight = boardSize.y - topBoard.getHeight() * Tile.SIZE;

        // draw the board.
        tools.setCameraPosition(fullWidth * 0.5f, fullHeight * 0.5f);
        board.draw(tools.getShapeRenderer());

        // draw the cursor.
        cursor.draw(tools.getShapeRenderer(), tools.getSpriteBatch());

        // draw left board.
        tools.setCameraPosition(fullWidth / 2 + leftBoard.getWidth() * Tile.SIZE + 10, fullHeight / 2);
        tools.getSpriteBatch().begin();
        leftBoard.draw(tools.getSpriteBatch());
        tools.getSpriteBatch().end();

        // draw top board.
        tools.setCameraPosition(fullWidth / 2, fullHeight / 2 + topBoard.getHeight() * Tile.SIZE + 10);
        tools.getSpriteBatch().begin();
        topBoard.draw(tools.getSpriteBatch());
        tools.getSpriteBatch().end();
    }
}
