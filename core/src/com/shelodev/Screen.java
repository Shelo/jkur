package com.shelodev;

import com.shelodev.utils.SceneTools;

public abstract class Screen
{
    public abstract void draw(SceneTools tools);
    public abstract void exit();
    public abstract void enter();
}
