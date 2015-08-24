package com.shelodev.utils;

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

    public void setTarget(Color color, float m)
    {
        tr = color.r * m;
        tg = color.g * m;
        tb = color.b * m;
        ta = color.a * m;
    }

    public void update(float d)
    {
        r += (tr - r) * d;
        g += (tg - g) * d;
        b += (tb - b) * d;
        a += (ta - a) * d;
    }

    public void mulTarget(float m)
    {
        tr *= m;
        tg *= m;
        tb *= m;
        ta *= m;
    }

    public void addTarget(float r, float g, float b, int a)
    {
        tr += r;
        tg += g;
        tb += b;
        ta += a;
    }

    public Color getColor()
    {
        return BUFFER_COLOR.set(r, g, b, a);
    }
}
