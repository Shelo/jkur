package com.shelodev.board;

public class Puzzle
{
    Board board;
    NumberBoard left;
    NumberBoard top;

    public Board getBoard()
    {
        return board;
    }

    public NumberBoard getLeft()
    {
        return left;
    }

    public NumberBoard getTop()
    {
        return top;
    }

    public void setBoard(Board board)
    {
        this.board = board;
    }

    public void setLeft(NumberBoard left)
    {
        this.left = left;
    }

    public void setTop(NumberBoard top)
    {
        this.top = top;
    }
}
