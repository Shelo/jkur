package com.shelodev;

import com.badlogic.gdx.graphics.Color;

public class ColorFading
{
    private static final Color BUFFER_COLOR = new Color();

    private float r;
    private float g;
    private float b;
    private float a;

    private float tr;
    private float tg;
    private float tb;
    private float ta;

    public ColorFading()
    {
        ;
    }

    public ColorFading(Color color)
    {
        r = color.r;
        g = color.g;
        b = color.b;
        a = color.a;
    }

    public void setTarget(Color color)
    {
        tr = color.r;
        tg = color.g;
        tb = color.b;
        ta = color.a;
    }

    public void update(float d)
    {
        r += (tr - r) * d;
        g += (tg - g) * d;
        b += (tb - b) * d;
        a += (ta - a) * d;
    }

    public Color getColor()
    {
        return BUFFER_COLOR.set(r, g, b, a);
    }
}
