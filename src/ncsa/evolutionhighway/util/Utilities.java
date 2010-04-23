// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   Utilities.java

package ncsa.evolutionhighway.util;

import edu.umd.cs.piccolo.PNode;

public class Utilities
{

    private Utilities()
    {
    }

    public static void fit(edu.umd.cs.piccolo.PNode node, double x, double y, double w, double h)
    {
        double nw = node.getWidth();
        double nh = node.getHeight();
        node.setBounds(x + 0.5D * (w - nw), y + 0.5D * (h - nh), nw, nh);
        if(nh > h)
        {
            node.scaleAboutPoint(0.90000000000000002D * (h / nh), x + 0.5D * w, y + 0.5D * h);
            nw = node.getWidth();
        }
        if(nw > w)
            node.scaleAboutPoint(0.90000000000000002D * (w / nw), x + 0.5D * w, y + 0.5D * h);
    }
}
