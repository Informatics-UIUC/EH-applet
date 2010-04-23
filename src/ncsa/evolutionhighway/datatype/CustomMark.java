// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   CustomMark.java

package ncsa.evolutionhighway.datatype;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import ncsa.evolutionhighway.util.PaintNode;

public class CustomMark extends edu.umd.cs.piccolo.PNode
{

    private static java.awt.geom.Point2D points[] = {
        new java.awt.geom.Point2D.Float(1.0F, 0.0F), new java.awt.geom.Point2D.Float(1.0F, 1.0F), new java.awt.geom.Point2D.Float(0.0F, 0.5F)
    };
    private static java.awt.Stroke stroke = new BasicStroke(0.0F);
    private double _h;
    private double _v;
    private ncsa.evolutionhighway.util.PaintNode startMarker;
    private ncsa.evolutionhighway.util.PaintNode endMarker;
    private edu.umd.cs.piccolo.nodes.PPath triangle;
    public edu.umd.cs.piccolo.nodes.PText text;

    public CustomMark(java.lang.String tag, double _h, double _v, java.awt.Color c)
    {
        this._h = _h;
        this._v = _v;
        startMarker = new PaintNode(java.awt.Color.BLACK);
        addChild(startMarker);
        endMarker = new PaintNode(java.awt.Color.BLACK);
        addChild(endMarker);
        triangle = edu.umd.cs.piccolo.nodes.PPath.createPolyline(points);
        triangle.setStroke(stroke);
        triangle.setPaint(c);
        addChild(triangle);
        text = new PText(tag);
        text.setTextPaint(c);
        addChild(text);
    }

    public boolean setBounds(double x, double y, double w, double h)
    {
        if(super.setBounds(x, y, w, h))
        {
            startMarker.setBounds(x - 0.5D * w, y, 0.59999999999999998D * w, 0.0050000000000000001D * w);
            endMarker.setBounds(x - 0.5D * w, (y + h) - 0.0050000000000000001D * w, 0.59999999999999998D * w, 0.0050000000000000001D * w);
            double new_h = 0.65000000000000002D * _h;
            double center = y + 0.5D * h;
            double half_new_h = 0.5D * new_h;
            y = center - half_new_h;
            h = new_h;
            triangle.setBounds(x + 0.050000000000000003D * w, y + 0.14999999999999999D * h, 0.20000000000000001D * w, 0.69999999999999996D * h);
            x += 0.20000000000000001D * w;
            w -= 0.20000000000000001D * w;
            if(text != null)
            {
                java.lang.String realText = text.getText();
                text.setText("999*");
                double tw = text.getWidth();
                double th = text.getHeight();
                text.setBounds((x + 0.5D * w) - tw / 2D, (y + 0.5D * h) - th / 2D, tw, th);
                if(th > h)
                {
                    text.scaleAboutPoint(0.90000000000000002D * (h / th), x + 0.5D * w, y + 0.5D * h);
                    tw = text.getWidth();
                    th = text.getHeight();
                }
                if(tw > w)
                {
                    text.scaleAboutPoint(0.90000000000000002D * (w / tw), x + 0.5D * w, y + 0.5D * h);
                    tw = text.getWidth();
                    th = text.getHeight();
                }
                text.setText(realText);
            }
            return true;
        } else
        {
            return false;
        }
    }

}
