// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   Feature.java

package ncsa.evolutionhighway.util;


public class Feature
    implements java.lang.Comparable
{

    protected int start;
    protected int end;

    public Feature(int start, int end)
    {
        this.start = start;
        this.end = end;
    }

    public int compareTo(java.lang.Object o)
    {
        return start - ((ncsa.evolutionhighway.util.Feature)o).start;
    }

    public boolean equals(java.lang.Object o)
    {
        if(o instanceof ncsa.evolutionhighway.util.Feature)
            return start == ((ncsa.evolutionhighway.util.Feature)o).start;
        else
            return false;
    }

    public int getStart()
    {
        return start;
    }

    public int getEnd()
    {
        return end;
    }

    public java.lang.String toString()
    {
        return start + " - " + end;
    }
}
