package com.shelodev.utils;

public class GroupList
{
    private static final int SIZE_STEP = 3;

    private int size;
    private byte[] array;
    private int meta;

    public GroupList()
    {
        array = new byte[SIZE_STEP];
    }

    public void add(int i, byte value)
    {
        ensureCapacity(i);
        if (i >= size)
            size = i + 1;

        array[i] = value;
    }

    public void add(int i, int value)
    {
        add(i, (byte) value);
    }

    public void sum(int i)
    {
        add(i, get(i) + 1);
    }

    private void ensureCapacity(int top)
    {
        if (top >= array.length)
        {
            byte[] newArray = new byte[array.length + SIZE_STEP];
            for (int i = 0; i < array.length; i++)
                newArray[i] = array[i];

            array = newArray;
        }
    }

    public byte[] reverse()
    {
        close();

        for(int i = 0; i < array.length / 2; i++)
        {
            byte temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }

        return array;
    }

    public byte[] close()
    {
        byte[] newArray = new byte[size];
        for (int i = 0; i < size; i++)
            newArray[i] = array[i];

        array = newArray;
        return array;
    }

    public int size()
    {
        return size;
    }

    public byte get(int i)
    {
        if (i >= array.length)
        {
            return 0;
        }

        return array[i];
    }

    public void setMeta(int meta)
    {
        this.meta = meta;
    }

    public int getMeta()
    {
        return meta;
    }
}
