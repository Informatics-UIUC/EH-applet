// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   CTOverlay.java

package ncsa.evolutionhighway.datatype;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PPaintContext;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import ncsa.evolutionhighway.util.GlobalSettings;

public class CTOverlay extends edu.umd.cs.piccolo.PNode
{

    public static final int TYPE_ACROCENTRIC = 101;
    public static final int TYPE_CENTROMERE = 102;
    public static final int TYPE_CENTROMERE_P_ARM = 103;
    public static final int TYPE_CENTROMERE_Q_ARM = 104;
    public static final int TYPE_TELOMERE_P_ARM = 105;
    public static final int TYPE_TELOMERE_Q_ARM = 106;
    private final java.lang.String id;
    private final java.lang.String chr;
    private final int start;
    private final int end;
    private final int type;
    private static java.awt.Color _gray = new Color(96, 96, 96);
    private static java.lang.String _p_str = "p";
    private static java.lang.String _q_str = "q";
    private java.awt.Color color;
    private java.awt.geom.RectangularShape shape;
    private boolean flip;
    private edu.umd.cs.piccolo.nodes.PText text;

    public CTOverlay(int type, java.lang.String speciesID, java.lang.String speciesChromosomeID, int start, int end)
    {
        this.type = type;
        id = speciesID;
        chr = speciesChromosomeID;
        this.start = start;
        this.end = end;
    }

    public int getEnd()
    {
        return end;
    }

    public java.lang.String getSpeciesID()
    {
        return id;
    }

    public int getStart()
    {
        return start;
    }

    public int getType()
    {
        return type;
    }

    public void paint(edu.umd.cs.piccolo.util.PPaintContext aPaintContext)
    {
        java.awt.Graphics2D g2 = aPaintContext.getGraphics();
        g2.setPaint(color);
        g2.fill(shape);
    }

    public void rebuild(ncsa.evolutionhighway.util.GlobalSettings settings)
    {
        if(type == 101 || type == 102 || type == 103 || type == 104)
            setVisible(settings.getShowCentromeres());
        else
        if(type == 105 || type == 106)
            setVisible(settings.getShowTelomeres());
    }

    public boolean setBounds(double x, double y, double w, double h)
    {
        if(super.setBounds(x, y, w, h))
        {
            switch(type)
            {
            case 103: // 'g'
            case 104: // 'h'
                shape.setFrame(x, flip ? y - h : y, w, flip ? h + h : h + h);
                break;

            default:
                shape.setFrame(x, y, w, h);
                break;
            }
            if(text != null)
            {
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
                    double d = text.getHeight();
                }
            }
            return true;
        } else
        {
            return false;
        }
    }

    public void setFlip(boolean f)
    {
        flip = f;
        switch(type)
        {
        case 101: // 'e'
            color = _gray;
            shape = new java.awt.geom.Ellipse2D.Double();
            break;

        case 102: // 'f'
            color = java.awt.Color.BLACK;
            shape = new java.awt.geom.Ellipse2D.Double();
            break;

        case 103: // 'g'
        case 104: // 'h'
            color = java.awt.Color.BLACK;
            shape = new java.awt.geom.Arc2D.Double(0.0D, 0.0D, 1.0D, 1.0D, 0.0D, flip ? -180D : 180D, 1);
            break;

        case 105: // 'i'
            color = _gray;
            shape = new java.awt.geom.Rectangle2D.Double();
            text = new PText(_p_str);
            text.setTextPaint(java.awt.Color.BLACK);
            addChild(text);
            break;

        case 106: // 'j'
            color = _gray;
            shape = new java.awt.geom.Rectangle2D.Double();
            text = new PText(_q_str);
            text.setTextPaint(java.awt.Color.BLACK);
            addChild(text);
            break;

        default:
            throw new IllegalArgumentException("unknown overlay type");
        }
    }

}
