package com.shelodev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Res
{
    public static BitmapFont font;

    static
    {
        font = new BitmapFont(Gdx.files.internal("fonts/numbers14.fnt"));
    }
}
