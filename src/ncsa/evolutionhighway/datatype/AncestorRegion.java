// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   AncestorRegion.java

package ncsa.evolutionhighway.datatype;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PPaintContext;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import ncsa.evolutionhighway.util.Utilities;

public class AncestorRegion extends edu.umd.cs.piccolo.PNode
{

    private java.awt.Paint _paint;
    private edu.umd.cs.piccolo.nodes.PText _label;
    private java.awt.geom.Rectangle2D _rectangle;
    private java.lang.String id;
    private java.lang.String chr;
    private int start;
    private int end;

    public AncestorRegion(java.lang.String ancestorID, java.lang.String ancestorChromosomeID, int start, int end)
    {
        id = ancestorID;
        chr = ancestorChromosomeID;
        this.start = start;
        this.end = end;
        _label = new PText(ancestorChromosomeID);
        addChild(_label);
        _rectangle = new java.awt.geom.Rectangle2D.Double();
    }

    public java.lang.String getAncestorChromosomeID()
    {
        return chr;
    }

    public java.lang.String getAncestorID()
    {
        return id;
    }

    public int getEnd()
    {
        return end;
    }

    public int getStart()
    {
        return start;
    }

    public void paint(edu.umd.cs.piccolo.util.PPaintContext aPaintContext)
    {
        java.awt.Graphics2D g2 = aPaintContext.getGraphics();
        g2.setPaint(_paint);
        g2.fill(_rectangle);
    }

    public boolean setBounds(double x, double y, double w, double h)
    {
        if(super.setBounds(x, y, w, h))
        {
            _rectangle.setFrame(x, y, w, h);
            ncsa.evolutionhighway.util.Utilities.fit(_label, x, y, w, h);
            return true;
        } else
        {
            return false;
        }
    }

    public void setPaint(java.awt.Paint p)
    {
        _paint = p;
    }
}
