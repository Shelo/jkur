package com.shelodev.utils;

import java.util.ArrayList;

public class TypesAndGroups
{
    private ArrayList<Byte> types;
    private ArrayList<Byte> groups;

    public TypesAndGroups(ArrayList<Byte> types, ArrayList<Byte> groups)
    {
        this.types = types;
        this.groups = groups;
    }

    public ArrayList<Byte> getGroups()
    {
        return groups;
    }

    public ArrayList<Byte> getTypes()
    {
        return types;
    }
}
