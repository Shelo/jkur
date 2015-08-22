package com.shelodev.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.shelodev.PuzzleLoader;
import com.shelodev.SceneTools;
import com.shelodev.board.*;

public class PlayScreen extends Screen
{
    private static final int TOP_OFFSET = 50;

    private PlayInputProcessor inputProcessor;

    // tiles board.
    private Board board;

    // number boards.
    private NumberBoard leftBoard;
    private NumberBoard topBoard;

    private Cursor cursor;

    public PlayScreen(PuzzleLoader loader, String puzzlePath)
    {
        Puzzle puzzle = loader.load(puzzlePath);

        board = puzzle.getBoard();
        leftBoard = puzzle.getLeft();
        topBoard = puzzle.getTop();

        cursor = new Cursor(board);
        cursor.move(board.getWidth() / 2, board.getHeight() / 2);

        inputProcessor = new PlayInputProcessor(cursor);
        Gdx.input.setInputProcessor(inputProcessor);
        Controllers.addListener(inputProcessor);
    }

    @Override
    public void draw(SceneTools tools)
    {
        inputProcessor.update();

        Vector2 boardSize = board.getSize();
        tools.setCameraPosition(boardSize.x * 0.5f, boardSize.y * 0.5f + TOP_OFFSET);

        // draw the board.
        board.draw(tools.getShapeRenderer());

        // draw the cursor.
        cursor.draw(tools.getShapeRenderer(), tools.getSpriteBatch());

        // draw left board.
        tools.setCameraPosition(boardSize.x / 2 + leftBoard.getWidth() * Tile.SIZE + 10,
                - boardSize.y / 2 + TOP_OFFSET);

        tools.getSpriteBatch().begin();
        leftBoard.draw(tools.getSpriteBatch());
        tools.getSpriteBatch().end();

        // draw top board.
        tools.setCameraPosition(boardSize.x / 2,
                - boardSize.y / 2 - topBoard.getHeight() * Tile.SIZE - 10 + TOP_OFFSET);

        tools.getSpriteBatch().begin();
        topBoard.draw(tools.getSpriteBatch());
        tools.getSpriteBatch().end();
    }
}
