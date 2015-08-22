package com.shelodev.utils;

/**
 * Wrapper for correct groups from side to side of one row or column.
 */
public class CorrectGroups
{
    private byte[] whole;
    private byte[] start;
    private byte[] end;

    public CorrectGroups(byte[] start, byte[] end, byte[] whole)
    {
        this.start = start;
        this.end = end;
        this.whole = whole;
    }

    public byte[] getStart()
    {
        return start;
    }

    public byte[] getEnd()
    {
        return end;
    }

    public int getTotal()
    {
        return whole.length;
    }

    public byte[] getWhole()
    {
        return whole;
    }

    public int getSum()
    {
        return end.length + start.length;
    }
}
