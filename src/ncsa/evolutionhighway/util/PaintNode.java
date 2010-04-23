// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   PaintNode.java

package ncsa.evolutionhighway.util;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PPaintContext;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

public class PaintNode extends edu.umd.cs.piccolo.PNode
{

    private java.awt.Paint _paint;
    private java.awt.geom.Rectangle2D _rectangle;
    boolean aa;

    public PaintNode(java.awt.Paint paint)
    {
        aa = true;
        _paint = paint;
        _rectangle = new java.awt.geom.Rectangle2D.Double();
    }

    public void setAA(boolean value)
    {
        aa = value;
    }

    public java.awt.Paint getPaint()
    {
        return _paint;
    }

    public void paint(edu.umd.cs.piccolo.util.PPaintContext aPaintContext)
    {
        java.awt.Graphics2D g2 = aPaintContext.getGraphics();
        if(!aa)
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setPaint(_paint);
        g2.fill(_rectangle);
    }

    public boolean setBounds(double x, double y, double w, double h)
    {
        if(super.setBounds(x, y, w, h))
        {
            _rectangle.setFrame(x, y, w, h);
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
