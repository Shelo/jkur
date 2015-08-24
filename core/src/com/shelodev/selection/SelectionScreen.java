package com.shelodev.selection;

import com.badlogic.gdx.Gdx;
import com.shelodev.Screen;
import com.shelodev.Settings;
import com.shelodev.selection.ui.SelectionList;
import com.shelodev.utils.SceneTools;

public class SelectionScreen extends Screen
{
    private static final int MARGIN_X_START = 50;
    private static final int MARGIN_Y_START = 100;
    private static final int MARGIN_Y_END = 100;

    private SelectionList packages;
    private SelectionSquares levels;
    private SelectionList currentSelection;

    private SelectionInputProcessor inputProcessor;

    private boolean showLevels;

    public SelectionScreen()
    {
        packages = new SelectionList(800, MARGIN_Y_START, Settings.SCREEN_WIDTH - MARGIN_X_START * 2,
                Settings.SCREEN_HEIGHT - MARGIN_Y_START - MARGIN_Y_END);

        levels = new SelectionSquares(800, MARGIN_Y_START, Settings.SCREEN_WIDTH / 2 - MARGIN_X_START,
                Settings.SCREEN_HEIGHT - MARGIN_Y_START - MARGIN_Y_END);

        currentSelection = packages;

        packages.getX().set(MARGIN_X_START);

        packages.addItem("1");
        packages.addItem("2");

        levels.addItem("0");
        levels.addItem("1");
        levels.addItem("2");

        inputProcessor = new SelectionInputProcessor(this);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void draw(SceneTools tools)
    {
        packages.draw(tools);
        levels.draw(tools);
    }

    @Override
    public void exit()
    {

    }

    @Override
    public void enter()
    {

    }
}
