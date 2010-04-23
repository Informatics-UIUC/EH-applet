// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst
// Source File Name:   HSB.java

package ncsa.evolutionhighway.datatype;

import java.awt.geom.AffineTransform;

import edu.umd.cs.piccolo.nodes.PText;

// Referenced classes of package ncsa.d2k.modules.projects.gpape.evolution.datatype:
//            CTOverlay, Chromosome

public class HSB extends edu.umd.cs.piccolo.PNode
{

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private edu.umd.cs.piccolo.nodes.PText _label;
    private java.awt.geom.Rectangle2D _rectangle;
    private java.lang.String comments;
    private final int end;
    private final int modified_order_end;
    private final int modified_order_start;
    private final java.lang.String referenceChromosome;
    private final java.lang.String referenceGenome;
    private final int segment_num;
    private final int sign;
    private final java.lang.String speciesChromosome;
    private final java.lang.String speciesID;
    private final int start;
    private double text_frac;
    private double text_start;
    private ncsa.evolutionhighway.datatype.Chromosome chromosome;

    public void setChromosome(ncsa.evolutionhighway.datatype.Chromosome c)
    {
        chromosome = c;
    }

    public HSB(java.lang.String referenceGenome, java.lang.String referenceChromosome, int start, int end, java.lang.String speciesChromosome, int modified_order_start, int modified_order_end,
            int sign, java.lang.String speciesID, int segment_num, java.lang.String comments)
    {
        text_frac = 1.0D;
        text_start = 0.0D;
        setChildrenPickable(false);
        this.referenceGenome = referenceGenome;
        this.referenceChromosome = referenceChromosome;
        this.start = start;
        this.end = end;
        this.speciesChromosome = speciesChromosome;
        this.modified_order_start = modified_order_start;
        this.modified_order_end = modified_order_end;
        this.sign = sign;
        this.speciesID = speciesID;
        this.segment_num = segment_num;
        this.comments = comments;
        _label = new PText(speciesChromosome);
        addChild(_label);
        _rectangle = new java.awt.geom.Rectangle2D.Double();
    }

    public void addOverlay(ncsa.evolutionhighway.datatype.CTOverlay cto)
    {
        if(cto.getType() == 102)
        {
            int bpheight = 0xf4240;
            int _center = cto.getStart() + (cto.getEnd() - cto.getStart() >> 1);
            int _top = _center - (bpheight >> 1);
            cto.setFlip(false);
            cto.setBounds(0.0D, 3.9999999999999998E-006D * (double)_top, 20D, 3.9999999999999998E-006D * (double)bpheight);
        } else
        {
            int bpheight = 0xf4240;
            if(cto.getType() == 103 || cto.getType() == 104)
                bpheight >>= 1;
            boolean align_top = cto.getStart() - getStart() < getEnd() - cto.getEnd();
            int _top = align_top ? getStart() : getEnd() - bpheight;
            boolean flip = cto.getType() == 103 && sign == -1 || cto.getType() == 104 && sign != -1;
            cto.setFlip(flip);
            cto.setBounds(0.0D, 3.9999999999999998E-006D * (double)_top, 20D, 3.9999999999999998E-006D * (double)bpheight);
        }
        addChild(cto);
    }

    public java.lang.String getComments()
    {
        return comments;
    }

    public int getEnd()
    {
        return end;
    }

    public int getModifiedOrderEnd()
    {
        return modified_order_end;
    }

    public int getModifiedOrderStart()
    {
        return modified_order_start;
    }

    public java.lang.String getReferenceChromosome()
    {
        return referenceChromosome;
    }

    public java.lang.String getReferenceGenome()
    {
        return referenceGenome;
    }

    public int getSegmentNum()
    {
        return segment_num;
    }

    public int getSign()
    {
        return sign;
    }

    public java.lang.String getSpeciesChromosome()
    {
        return speciesChromosome;
    }

    public java.lang.String getSpeciesID()
    {
        return speciesID;
    }

    public int getStart()
    {
        return start;
    }

    public void paint(edu.umd.cs.piccolo.util.PPaintContext aPaintContext)
    {
        java.awt.Graphics2D g2 = aPaintContext.getGraphics();
        System.out.println("width = "+this.getWidth());
        if (this.getComments().compareToIgnoreCase("Deletion") == 0){
            g2.setColor(java.awt.Color.BLUE);
            g2.fill(_rectangle);
        }
        else if (this.getComments().compareToIgnoreCase("Inverted") == 0){
            g2.setColor(java.awt.Color.GREEN);
            g2.fill(_rectangle);
        }
        else if (this.getComments().compareToIgnoreCase("Duplication") == 0){
            g2.setColor(java.awt.Color.RED);
            g2.fill(_rectangle);
        }
        else if (this.getComments().compareToIgnoreCase("Interchromosomal") == 0){
            g2.setColor(java.awt.Color.YELLOW);
            g2.fill(_rectangle);
        }
        else if (this.getComments().compareToIgnoreCase("Amplified") == 0){
            g2.setColor(java.awt.Color.CYAN);
            g2.fill(_rectangle);
        }
        else{
            g2.setColor(java.awt.Color.LIGHT_GRAY);
            g2.fill(_rectangle);
        }
        /*
        if (getSign()>0) {
          g2.setColor(java.awt.Color.LIGHT_GRAY);
          g2.fill(_rectangle);
        }
        else {
          g2.setColor(java.awt.Color.BLUE);
          g2.fill(_rectangle);
        }
        */
    }

    public void rebuild(ncsa.evolutionhighway.util.GlobalSettings settings, int break_start, int break_end)
    {
        java.util.Iterator iter = getChildrenIterator();
        do
        {
            if(!iter.hasNext())
                break;
            edu.umd.cs.piccolo.PNode next = (edu.umd.cs.piccolo.PNode)iter.next();
            if(next instanceof ncsa.evolutionhighway.datatype.CTOverlay)
                ((ncsa.evolutionhighway.datatype.CTOverlay)next).rebuild(settings);
        } while(true);
        if(start < break_start && end > break_start)
        {
            if(end > break_end)
            {
                if(break_start - start > end - break_end)
                {
                    text_start = 0.0D;
                    text_frac = (double)(break_start - start) / (double)(end - start);
                } else
                {
                    text_start = (double)(break_end - start) / (double)(end - start);
                    text_frac = (double)(end - break_end) / (double)(end - start);
                }
            } else
            {
                text_start = 0.0D;
                text_frac = (double)(break_start - start) / (double)(end - start);
            }
        } else
        if(start >= break_start && start < break_end)
        {
            if(end > break_end)
            {
                text_start = (double)(break_end - start) / (double)(end - start);
                text_frac = (double)(end - break_end) / (double)(end - start);
            }
        } else
        {
            text_start = 0.0D;
            text_frac = 1.0D;
        }
        if(text_start != 0.0D || text_frac != 1.0D)
        {
            java.awt.Rectangle rect = _rectangle.getBounds();
            double x = rect.x;
            double y = rect.y;
            double w = rect.width;
            double h = rect.height;
            y += h * text_start;
            h *= text_frac;
            _label.setTransform(new AffineTransform());
            ncsa.evolutionhighway.util.Utilities.fit(_label, x, y, w, h);
        }
        int threshold = settings.getVanishingThreshold();
        edu.umd.cs.piccolo.util.PBounds bounds = _label.getGlobalFullBounds();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        boolean text_passes_threshold;
        if(threshold > 0 && (width < (double)threshold || height < (double)threshold))
            text_passes_threshold = false;
        else
            text_passes_threshold = true;
        if(!text_passes_threshold)
        {
            _label.setVisible(false);
        } else
        {
            _label.setVisible(true);
            iter = getChildrenIterator();
            do
            {
                if(!iter.hasNext())
                    break;
                edu.umd.cs.piccolo.PNode next = (edu.umd.cs.piccolo.PNode)iter.next();
                if((next instanceof ncsa.evolutionhighway.datatype.CTOverlay) && next.getVisible() && next.getGlobalFullBounds().intersects(_label.getGlobalFullBounds()))
                    _label.setVisible(false);
            } while(true);
        }
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

    public void setComments(java.lang.String s)
    {
        comments = s;
    }
}
