package com.shelodev.utils;

public class LerpFloat
{
    private float value;
    private float target;

    public void update(float d)
    {
        value += (target - value) * d;
    }

    public void reset(float value)
    {
        this.value = value;
        this.target = value;
    }

    public void set(float target)
    {
        this.target = target;
    }

    public float get()
    {
        return value;
    }
}
