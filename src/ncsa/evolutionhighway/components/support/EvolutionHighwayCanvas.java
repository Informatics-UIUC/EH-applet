// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   EvolutionHighwayCanvas.java

package ncsa.evolutionhighway.components.support;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.util.PPaintContext;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.print.Book;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;

import ncsa.evolutionhighway.datatype.Chromosome;
import ncsa.evolutionhighway.datatype.Genome;

public class EvolutionHighwayCanvas extends edu.umd.cs.piccolo.PCanvas
    implements edu.umd.cs.piccolo.event.PInputEventListener
{
    private class SelectionNode extends edu.umd.cs.piccolo.PNode
    {

        java.awt.geom.Rectangle2D rect;

        protected void paint(edu.umd.cs.piccolo.util.PPaintContext pc)
        {
            java.awt.Graphics2D g = pc.getGraphics();
            g.setColor(new Color(0.5F, 0.5F, 1.0F, 0.25F));
            g.fill(rect);
            g.setStroke(new BasicStroke(1.0F / (float)pc.getScale()));
            g.setColor(java.awt.Color.BLUE);
            g.draw(rect);
        }

        public boolean setBounds(double x, double y, double w, double h)
        {
            if(super.setBounds(x, y, w, h))
            {
                rect.setFrame(x, y, w, h);
                return true;
            } else
            {
                return false;
            }
        }

        private SelectionNode()
        {
            super();
            rect = new java.awt.geom.Rectangle2D.Double();
        }
    }


    public static int SELECTION_MODE_GENOME_BROWSER = 101;
    public static int SELECTION_MODE_CONSERVED_SYNTENY = 102;
    public static int SELECTION_MODE_BREAKPOINT_CLASSIFICATION = 103;
    private java.util.HashMap genomesDirty;
    private edu.umd.cs.piccolo.PNode root;
    private java.awt.geom.AffineTransform originalView;

    public EvolutionHighwayCanvas(java.util.Map initMap)
    {
        addInputEventListener(this);
        genomesDirty = new HashMap();
        root = new PNode();
        getLayer().addChild(root);
        ncsa.evolutionhighway.datatype.Genome genome;
        for(java.util.Iterator genomeKeyIterator = initMap.keySet().iterator(); genomeKeyIterator.hasNext(); genomesDirty.put(genome, new Boolean(true)))
        {
            java.lang.String genomeID = (java.lang.String)genomeKeyIterator.next();
            genome = (ncsa.evolutionhighway.datatype.Genome)initMap.get(genomeID);
        }

        getCamera().scaleView(0.69999999999999996D);
        getCamera().translateView(40D, 10D);
        originalView = getCamera().getViewTransform();
    }

    public void processEvent(edu.umd.cs.piccolo.event.PInputEvent event, int type)
    {
        if(type == 502 && event.isRightMouseButton())
        {
            int granularity = (int)(1.0D / getCamera().getViewScale() / 3.9999999999999998E-006D);
            ncsa.evolutionhighway.datatype.Genome g = getGenome();
            if(g == null)
                return;
            java.util.Iterator citer = g.getChromosomeIterator();
            do
            {
                if(!citer.hasNext())
                    break;
                java.lang.Object n = citer.next();
                if(n instanceof ncsa.evolutionhighway.datatype.Chromosome)
                {
                    ncsa.evolutionhighway.datatype.Chromosome nc = (ncsa.evolutionhighway.datatype.Chromosome)n;
                    nc.setDensityGranularity(granularity);
                    nc.rebuildDensity();
                }
            } while(true);
        }
    }

    void clear()
    {
        root.removeAllChildren();
    }

    public ncsa.evolutionhighway.datatype.Genome getGenome()
    {
        if(root.getChildrenCount() > 0)
            return (ncsa.evolutionhighway.datatype.Genome)root.getChild(0);
        else
            return null;
    }

    public java.util.Iterator getGenomeIterator()
    {
        return genomesDirty.keySet().iterator();
    }

    public void print()
    {
        java.awt.print.PrinterJob printJob = java.awt.print.PrinterJob.getPrinterJob();
        java.awt.print.PageFormat pageFormat = printJob.pageDialog(printJob.defaultPage());
        java.awt.print.Book book = new Book();
        book.append(root, pageFormat);
        printJob.setPageable(book);
        if(printJob.printDialog())
            try
            {
                printJob.print();
            }
            catch(java.lang.Exception e)
            {
                e.printStackTrace();
            }
    }

    public void resetView()
    {
        getCamera().setViewTransform((java.awt.geom.AffineTransform)originalView.clone());
    }

    public void saveCanvas(java.io.File file, int width, int height)
    {
        java.awt.Image im = root.toImage(width, height, null);
        try
        {
            javax.imageio.ImageIO.write((java.awt.image.RenderedImage)im, "png", file);
        }
        catch(java.io.IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public void saveScreen(java.io.File file)
    {
        java.awt.image.BufferedImage image = new BufferedImage(getWidth(), getHeight(), 5);
        paint(image.getGraphics());
        try
        {
            javax.imageio.ImageIO.write(image, "png", file);
        }
        catch(java.io.IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    void setGenome(ncsa.evolutionhighway.datatype.Genome g)
    {
        root.removeAllChildren();
        if(g != null)
            root.addChild(g);
        int granularity = (int)(1.0D / getCamera().getViewScale() / 3.9999999999999998E-006D);
        java.util.Iterator citer = g.getChromosomeIterator();
        do
        {
            if(!citer.hasNext())
                break;
            java.lang.Object n = citer.next();
            if(n instanceof ncsa.evolutionhighway.datatype.Chromosome)
            {
                ((ncsa.evolutionhighway.datatype.Chromosome)n).setDensityGranularity(granularity);
                ((ncsa.evolutionhighway.datatype.Chromosome)n).rebuildDensity();
            }
        } while(true);
    }

}
