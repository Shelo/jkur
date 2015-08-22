package com.shelodev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.shelodev.board.Board;
import com.shelodev.board.NumberBoard;
import com.shelodev.board.Puzzle;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.FileHandler;

public class PuzzleLoader
{
    private ArrayList<String> packages = new ArrayList<String>();
    private Puzzle puzzle;

    public PuzzleLoader(String baseDir)
    {
        loadPackages(baseDir);
    }

    private void loadPackages(String baseDir)
    {
        FileHandle levels = Gdx.files.internal(baseDir);
        FileHandle[] packageFiles = levels.list();

        if (packageFiles == null)
            return;

        for (FileHandle file : packageFiles)
        {
            if (file.name().equals(".DS_Store") || !file.isDirectory())
                continue;

            packages.add(file.toString());
        }
    }

    public Puzzle load(String path)
    {
        puzzle = new Puzzle();
        read(path);
        return puzzle;
    }

    private void read(String path)
    {
        ArrayList<String> lines = readFile(path);
        loadDimensions(lines);

        boolean readingTop = true;
        for (String line : lines)
        {
            if (line.length() > 0 && line.charAt(0) == '=')
            {
                readingTop = false;
                continue;
            }

            if (readingTop)
                readTopLine(line);
            else
                readLeftLine(line);
        }
    }

    private void readTopLine(String line)
    {
        String[] numbers = line.split("\t");

        for (String number : numbers)
        {
            byte value;
            if (number.isEmpty())
                value = -1;
            else
                value = Byte.parseByte(number);

            puzzle.getTop().setNext(value);
        }

        puzzle.getTop().skipLine();
    }

    private void readLeftLine(String line)
    {
        String[] numbers = line.split("\t");

        int i = 0;
        for (String number : numbers)
        {
            byte value;
            if (number.equals(""))
                value = -1;
            else
                value = Byte.parseByte(number);

            puzzle.getLeft().setInLine(i, value);

            i++;
        }

        puzzle.getLeft().skipLine();
    }

    private void loadDimensions(ArrayList<String> lines)
    {
        boolean readingTop = true;
        int width = 0;
        int height = 0;

        int topHeight = 0;
        int leftWidth = 1;

        for (String line : lines)
        {
            if (readingTop)
            {
                int numbers = 1;

                for (int i = 0; i < line.length(); i++)
                {
                    char c = line.charAt(i);

                    if (c == '\t')
                        numbers++;
                }

                if (numbers > width)
                    width = numbers;

                if (line.length() > 0 && line.charAt(0) == '=')
                    readingTop = false;
                else
                    topHeight++;
            }
            else
            {
                height++;

                int numbers = 1;
                for (int i = 0; i < line.length(); i++)
                {
                    char c = line.charAt(i);

                    if (c == '\t')
                        numbers++;
                }

                if (numbers > leftWidth)
                    leftWidth = numbers;
            }
        }

        NumberBoard left = new NumberBoard(leftWidth, height);
        NumberBoard top = new NumberBoard(width, topHeight);

        puzzle.setLeft(left);
        puzzle.setTop(top);
        puzzle.setBoard(new Board(width, height, left, top));
    }

    private ArrayList<String> readFile(String path)
    {
        FileHandle puzzleFile = Gdx.files.internal(path);
        BufferedReader buffer = new BufferedReader(puzzleFile.reader());

        ArrayList<String> lines = new ArrayList<String>();
        try
        {
            String line;
            while ((line = buffer.readLine()) != null)
            {
                lines.add(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return lines;
    }

    public ArrayList<String> getPackages()
    {
        return packages;
    }
}
