// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst
// Source File Name:   Chromosome.java

package ncsa.evolutionhighway.datatype;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolox.nodes.PClip;

import java.applet.AppletContext;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JTextArea;

import ncsa.evolutionhighway.components.support.EvolutionHighwayCanvas;
import ncsa.evolutionhighway.components.support.EvolutionHighwayApplet;
import ncsa.evolutionhighway.util.Constants;
import ncsa.evolutionhighway.util.Feature;
import ncsa.evolutionhighway.util.GlobalSettings;
import ncsa.evolutionhighway.util.PDensityOverlay;
import ncsa.evolutionhighway.util.PaintNode;
import ncsa.evolutionhighway.util.SpeciesOrderComparator;
//import ncsa.evolutionhighway.util.BareBonesBrowserLaunch;
//import org.jdesktop.jdic.desktop.Desktop;

// Referenced classes of package ncsa.evolutionhighway.datatype:
//            CustomMark, ComparativeSpecies, HSB, AncestorRegion,
//            CTOverlay

public class Chromosome extends edu.umd.cs.piccolo.PNode
    implements java.lang.Comparable, edu.umd.cs.piccolo.event.PInputEventListener
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


    private java.lang.String gen_id;
    private final java.lang.String id;
    private edu.umd.cs.piccolo.nodes.PText label;
    private edu.umd.cs.piccolox.nodes.PClip clip;
    private edu.umd.cs.piccolo.PNode children_ancestorInClip;
    private edu.umd.cs.piccolo.PNode children_ancestorOutOfClip;
    private edu.umd.cs.piccolo.PNode children_comparative;
    private edu.umd.cs.piccolo.PNode children_comparedClip;
    private edu.umd.cs.piccolo.PNode children_customTrack;
    private edu.umd.cs.piccolo.PNode children_dividers;
    private edu.umd.cs.piccolo.PNode children_regions;
    private edu.umd.cs.piccolo.PNode children_scale;
    private edu.umd.cs.piccolo.PNode children_spcLabels;
    private int centromere_mark;
    private int length;
    private int p_heterochromatin_mark;
    private int p_satellite_mark;
    private int q_heterochromatin_mark;
    private int q_satellite_mark;
    private java.util.HashMap aoMap;
    private java.util.HashMap csMap;
    private java.util.HashMap spcLabelMap;
    private java.util.ArrayList customTrackList;
    private ncsa.evolutionhighway.util.PaintNode hcPaintP;
    private ncsa.evolutionhighway.util.PaintNode hcPaintQ;
    private ncsa.evolutionhighway.util.PaintNode satPaintP;
    private ncsa.evolutionhighway.util.PaintNode satPaintQ;
    private double effectiveWidth;
    private double maxLabelWidth;
    private int selectionMode;
    private edu.umd.cs.piccolo.PNode selectionNode;
    private java.awt.geom.Point2D selectPressPos;
    private javax.swing.JTextArea msgAreaRef;
    private java.util.ArrayList compareMatches;
    private java.lang.String compareMatchesDesc;
    private ncsa.evolutionhighway.util.PDensityOverlay density;
    int granularity;
    private boolean drawEdgesOnMatches;
    private java.util.HashSet visibleOverrideHack;
    private GlobalSettings settings;

    public Chromosome(java.lang.String gid, java.lang.String cid, java.awt.image.BufferedImage hctex)
    {
        //System.out.println(String.format("Creating chromosome: [gid='%s'  cid='%s']", gid, cid));

        compareMatches = new ArrayList();
        compareMatchesDesc = null;
        granularity = -1;
        drawEdgesOnMatches = false;
        visibleOverrideHack = new HashSet();
        addInputEventListener(this);
        gen_id = gid;
        id = cid;
        label = new PText(gid + " " + cid);
        addChild(label);
        children_scale = new PNode();
        addChild(children_scale);
        children_customTrack = new PNode();
        addChild(children_customTrack);
        clip = new PClip();
        addChild(clip);
        children_comparative = new PNode();
        clip.addChild(children_comparative);
        children_dividers = new PNode();
        clip.addChild(children_dividers);
        children_ancestorInClip = new PNode();
        children_ancestorInClip.setVisible(false);
        clip.addChild(children_ancestorInClip);
        double paint_scale = 50D;
        children_regions = new PNode();
        children_regions.addChild(hcPaintP = new PaintNode(new TexturePaint(hctex, new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, paint_scale, paint_scale))));
        children_regions.addChild(hcPaintQ = new PaintNode(new TexturePaint(hctex, new java.awt.geom.Rectangle2D.Double(0.0D, 0.0D, paint_scale, paint_scale))));
        children_regions.addChild(satPaintP = new PaintNode(java.awt.Color.BLACK));
        children_regions.addChild(satPaintQ = new PaintNode(java.awt.Color.BLACK));
        clip.addChild(children_regions);
        children_ancestorOutOfClip = new PNode();
        addChild(children_ancestorOutOfClip);
        children_comparedClip = new PNode();
        children_comparedClip.setChildrenPickable(false);
        clip.addChild(children_comparedClip);
        children_spcLabels = new PNode();
        addChild(children_spcLabels);
        centromere_mark = -1;
        length = -1;
        p_heterochromatin_mark = -1;
        p_satellite_mark = -1;
        q_heterochromatin_mark = -1;
        q_satellite_mark = -1;
        aoMap = new HashMap();
        csMap = new HashMap();
        spcLabelMap = new HashMap();
        customTrackList = new ArrayList();
        selectionMode = EvolutionHighwayCanvas.SELECTION_MODE_GENOME_BROWSER;
        selectionNode = new SelectionNode();
        selectionNode.setVisible(false);
        selectionNode.setPickable(false);
        selectionNode.setChildrenPickable(false);
        clip.addChild(selectionNode);
    }

    public void addFeature(ncsa.evolutionhighway.util.Feature f)
    {
        if(density != null)
            density.addFeature(f);
    }

    public void clearFeatures()
    {
        if(density != null)
            density.clearFeatures();
    }

    public void setDensityGranularity(int value)
    {
        granularity = value;
    }

    public void setDensityShown(boolean value)
    {
        density.setVisible(value);
    }

    public void setMsgAreaRef(javax.swing.JTextArea msg)
    {
        msgAreaRef = msg;
    }

    public void addCustomMark(ncsa.evolutionhighway.datatype.CustomMark ctn)
    {
        children_customTrack.addChild(ctn);
        double top = ctn.getBoundsReference().y;
        int size = customTrackList.size();
        boolean added = false;
        int i = 0;
        do
        {
            if(i >= size)
                break;
            ncsa.evolutionhighway.datatype.CustomMark next = (ncsa.evolutionhighway.datatype.CustomMark)customTrackList.get(i);
            if(top < next.getBounds().y)
            {
                customTrackList.add(i, ctn);
                added = true;
                break;
            }
            i++;
        } while(true);
        if(!added)
            customTrackList.add(ctn);
        int limit = 10;
        boolean shifted;
        do
        {
            shifted = false;
            int numCT = customTrackList.size();
            for(i = 0; i < numCT - 1; i++)
            {
                ncsa.evolutionhighway.datatype.CustomMark ct1 = (ncsa.evolutionhighway.datatype.CustomMark)customTrackList.get(i);
                edu.umd.cs.piccolo.util.PBounds ct1b = ct1.text.getGlobalFullBounds();
                ncsa.evolutionhighway.datatype.CustomMark ct2 = (ncsa.evolutionhighway.datatype.CustomMark)customTrackList.get(i + 1);
                edu.umd.cs.piccolo.util.PBounds ct2b = ct2.text.getGlobalFullBounds();
                double ct1_bottom = ct1b.y + ct1b.height;
                double ct2_top = ct2b.y;
                if(ct1_bottom > ct2_top + 1.0D)
                {
                    double diff = (ct1_bottom - ct2_top) / 2D;
                    ct1.text.translate(0.0D, -diff);
                    ct2.text.translate(0.0D, diff);
                    shifted = true;
                }
            }

        } while(shifted && limit-- > 0);
    }

    public void clearCustomTrackNodes()
    {
        children_customTrack.removeAllChildren();
        customTrackList.clear();
    }

    private edu.umd.cs.piccolo.PNode rebuildScaleChildren()
    {
        edu.umd.cs.piccolo.PNode parent = new PNode();
        int modcount = 0;
        double increment = 5000000;//length/50; //added by LA to be data driven instead of 5000000
        System.out.println("Increment = "+increment);
        for(double bp = 0.0D; bp < (double)length; bp += increment)
        {
            if(modcount % 5 == 0)
            {
                ncsa.evolutionhighway.util.PaintNode prn = new PaintNode(java.awt.Color.BLACK);
                prn.setBounds(-10D, 3.9999999999999998E-006D * bp - 0.5D, 30D, 1.0D);
                parent.addChild(prn);
                edu.umd.cs.piccolo.nodes.PText ptn = new PText(java.lang.Integer.toString(5 * modcount));
                double ptw = ptn.getWidth();
                double pth = ptn.getHeight();
                ptn.setBounds(-11D - ptw, 3.9999999999999998E-006D * bp - 0.5D * pth, ptw, pth);
                ptw = ptn.getWidth();
                ptn.scaleAboutPoint(0.80000000000000004D, -11D - 0.5D * ptw, 3.9999999999999998E-006D * bp);
                parent.addChild(ptn);
            } else
            {
                ncsa.evolutionhighway.util.PaintNode prn = new PaintNode(java.awt.Color.BLACK);
                prn.setBounds(-5D, 3.9999999999999998E-006D * bp - 0.5D, 25D, 1.0D);
                parent.addChild(prn);
            }
            modcount++;
        }

        edu.umd.cs.piccolo.nodes.PText mbp = new PText("Mbp");
        double mbw = mbp.getWidth();
        double mbh = mbp.getHeight();
        mbp.setBounds(-10.5D - mbw, -12D - 0.5D * mbh, mbw, mbh);
        mbp.scaleAboutPoint(0.80000000000000004D, -10.5D - mbp.getWidth() / 2D, -12D);
        parent.addChild(mbp);
        return parent;
    }

    public void addAncestorOverlay(ncsa.evolutionhighway.datatype.AncestorRegion ao)
    {
        java.util.ArrayList aoList = (java.util.ArrayList)aoMap.get(ao.getAncestorID());
        if(aoList == null)
        {
            aoList = new ArrayList();
            aoMap.put(ao.getAncestorID(), aoList);
        }
        aoList.add(ao);
    }

    public void addCTOverlay(ncsa.evolutionhighway.datatype.CTOverlay cto)
    {
        ncsa.evolutionhighway.datatype.ComparativeSpecies cs = (ncsa.evolutionhighway.datatype.ComparativeSpecies)csMap.get(cto.getSpeciesID());
        if(cs == null) {
            System.out.println(String.format("Chromosome with id '%s' has not seen species with id '%s'", id, cto.getSpeciesID()));
            throw new IllegalArgumentException(id + " has not seen " + cto.getSpeciesID());
        }

        java.util.Iterator hsbiter = cs.getChildrenIterator();
        boolean found = false;
        do
        {
            if(!hsbiter.hasNext() || found)
                break;
            ncsa.evolutionhighway.datatype.HSB hsb = (ncsa.evolutionhighway.datatype.HSB)hsbiter.next();
            if(cto.getStart() >= hsb.getStart() && cto.getEnd() <= hsb.getEnd())
            {
                found = true;
                hsb.addOverlay(cto);
            }
        } while(true);
        if(!found)
            throw new IllegalArgumentException("no segment found for overlay: " + cto);
        else
            return;
    }

    public void addHSB(ncsa.evolutionhighway.datatype.HSB hsb)
    {
        ncsa.evolutionhighway.datatype.ComparativeSpecies cs = (ncsa.evolutionhighway.datatype.ComparativeSpecies)csMap.get(hsb.getSpeciesID());
        if(cs == null)
        {
            java.lang.String spcID = hsb.getSpeciesID();
            cs = new ComparativeSpecies(spcID);
            cs.setBounds(0.0D, 0.0D, 20D, (double)length * 3.9999999999999998E-006D);
            csMap.put(spcID, cs);
            cs.setChromosome(this);
            children_comparative.addChild(cs);
            edu.umd.cs.piccolo.nodes.PText txt[] = {
                new PText(spcID), new PText(spcID)
            };
            double w;
            if((w = txt[0].getWidth()) > maxLabelWidth)
                maxLabelWidth = w;
            children_spcLabels.addChild(txt[0]);
            children_spcLabels.addChild(txt[1]);
            spcLabelMap.put(spcID, txt);
            int num_comparative = children_comparative.getChildrenCount();
            int num_dividers = children_dividers.getChildrenCount();
            if(num_comparative > num_dividers + 1)
            {
                ncsa.evolutionhighway.util.PaintNode divider = new PaintNode(java.awt.Color.BLACK);
                divider.setBounds(20D * ((double)(num_dividers + 1) - 0.025000000000000001D), 0.0D, 1.0D, 3.9999999999999998E-006D * (double)length);
                children_dividers.addChild(divider);
            }
        }
        cs.addHSB(hsb);
        hsb.setChromosome(this);
    }

    public int compareTo(java.lang.Object o)
    {
        ncsa.evolutionhighway.datatype.Chromosome other = (ncsa.evolutionhighway.datatype.Chromosome)o;
        int thisNumber = 0;
        int otherNumber = 0;
        boolean thisIsNumeric;
        try
        {
            thisNumber = java.lang.Integer.parseInt(id);
            thisIsNumeric = true;
        }
        catch(java.lang.NumberFormatException nfe)
        {
            thisIsNumeric = false;
        }
        boolean otherIsNumeric;
        try
        {
            otherNumber = java.lang.Integer.parseInt(other.id);
            otherIsNumeric = true;
        }
        catch(java.lang.NumberFormatException nfe)
        {
            otherIsNumeric = false;
        }
        if(thisIsNumeric)
            if(otherIsNumeric)
                return thisNumber - otherNumber;
            else
                return -1;
        if(otherIsNumeric)
            return 1;
        else
            return id.compareTo(other.id);
    }

    public double getChromosomeWidth()
    {
        return effectiveWidth;
    }

    public ncsa.evolutionhighway.datatype.ComparativeSpecies getComparativeSpecies(java.lang.String id)
    {
        return (ncsa.evolutionhighway.datatype.ComparativeSpecies)csMap.get(id);
    }

    public java.util.Iterator getComparativeSpeciesIDIterator()
    {
        return csMap.keySet().iterator();
    }

    public java.lang.String getID()
    {
        return id;
    }

    public javax.swing.JTextArea getMsgAreaRef()
    {
        return msgAreaRef;
    }

    public void setCentromereMark(int mark)
    {
        centromere_mark = mark;
    }

    public java.util.List getCompareMatches()
    {
        return compareMatches;
    }

    public java.lang.String getCompareMatchesDescription()
    {
        return compareMatchesDesc;
    }

    public edu.umd.cs.piccolo.util.PBounds getDensityGlobalFullBounds()
    {
        if(density == null)
            return null;
        else
            return density.getGlobalFullBounds();
    }

    public void setDrawEdgesOnMatches(boolean value)
    {
        drawEdgesOnMatches = value;
        rebuildCompareMatches();
    }

    public void setCompareMatches(java.util.ArrayList matches, java.lang.String desc, double threshold)
    {
        if(compareMatches == null)
            compareMatches = new ArrayList();
        else
            compareMatches.clear();
        if(threshold > 0.0D)
        {
            threshold *= 3.9999999999999998E-006D;
            for(int i = 0; i < matches.size(); i++)
            {
                double match[] = (double[])(double[])matches.get(i);
                if(match[1] - match[0] < threshold)
                    compareMatches.add(match);
            }

        } else
        {
            compareMatches.addAll(matches);
        }
        compareMatchesDesc = desc;
        rebuildCompareMatches();
    }

    public void rebuildCompareMatches()
    {
        children_comparedClip.removeAllChildren();
        java.util.ArrayList matches = compareMatches;
        final boolean fDrawEdge = drawEdgesOnMatches;
        for(int i = matches.size() - 1; i >= 0; i--)
        {
            double dim[] = (double[])(double[])matches.get(i);
            edu.umd.cs.piccolo.PNode cmp = new edu.umd.cs.piccolo.PNode() {

                java.awt.geom.Rectangle2D rect;

                protected void paint(edu.umd.cs.piccolo.util.PPaintContext pc)
                {
                    java.awt.Graphics2D g = pc.getGraphics();
                    g.setColor(new Color(1.0F, 0.5F, 0.5F, 0.25F));
                    g.fill(rect);
                    if(fDrawEdge)
                    {
                        g.setStroke(new BasicStroke(1.0F / (float)pc.getScale()));
                        g.setColor(java.awt.Color.RED);
                        g.draw(rect);
                    }
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


            {
             //by la   super();
                rect = new java.awt.geom.Rectangle2D.Double();
            }
            }
;
            cmp.setBounds(0.0D, dim[0], effectiveWidth, dim[1] - dim[0]);
            children_comparedClip.addChild(cmp);
        }

    }

    public void processEvent(edu.umd.cs.piccolo.event.PInputEvent event, int type)
    {
        if(event.isMiddleMouseButton())
        {
            if(type == 501)
                selectPressPos = event.getPosition();
            else
            if(type == 506)
            {
                java.awt.geom.Point2D p = event.getPosition();
                double y;
                double h;
                if(p.getY() < selectPressPos.getY())
                {
                    y = p.getY();
                    h = selectPressPos.getY() - y;
                } else
                {
                    y = selectPressPos.getY();
                    h = p.getY() - selectPressPos.getY();
                }
                selectionNode.setBounds(0.0D, y - clip.getTransform().getTranslateY(), effectiveWidth, h);
                selectionNode.setVisible(true);
            } else
            if(type == 502)
            {
                edu.umd.cs.piccolo.util.PBounds b = selectionNode.getBounds();
                int y1 = (int)java.lang.Math.floor(b.getY() / 3.9999999999999998E-006D);
                int y2 = (int)java.lang.Math.ceil((b.getY() + b.getHeight()) / 3.9999999999999998E-006D);
                if(y1 < 0)
                    y1 = 0;
                if(y2 < 0)
                    y2 = 0;
                if(y1 > length)
                    y1 = length;
                if(y2 > length)
                    y2 = length;
                if(y1 != y2)
                {
                    msgAreaRef.setText("selected region: " + gen_id + " " + id + ": " + y1 + " - " + y2);
                    java.lang.StringBuffer url = new StringBuffer();
                    java.lang.String build = (java.lang.String)EvolutionHighwayApplet.buildMap.get(gen_id);
                    java.lang.String buildURL = (java.lang.String)EvolutionHighwayApplet.buildURLMap.get(gen_id);
                    if(buildURL != null)
                        url.append(buildURL + "?");
                    else
                        url.append("http://genome.ucsc.edu/cgi-bin/hgTracks?");
                    if(build != null)
                        url.append("db=" + build + "&");
                    else
                        throw new IllegalArgumentException("we don't know how to look up genome: " + gen_id);
                    url.append("position=chr" + id + "%3A" + y1 + "-" + y2);
                    try
                    {
                    	//BareBonesBrowserLaunch.openURL(url.toString());
                    	//org.jdesktop.jdic.desktop.Desktop.browse(new URL(url.toString()));
                    	//URLDisplayer.getDefault().showURL(URL url);
                    	//try
                    	{
                    		System.out.println(url.toString());
                    		AppletContext a = settings.getAppletContext();
                    		 a.showDocument(new URL(url.toString()), "_blank");
                    		 //a.showDocument(new URL(url.toString()),"Genome Browser");

                    	}
                    }
                    catch(java.lang.Exception e)
                    {
                        msgAreaRef.setText(e.getMessage());
                    }
                }
                selectionNode.setVisible(false);
            }
        } else
        if(event.isRightMouseButton() && type == 500)
        {
            java.awt.geom.Point2D p = event.getPosition();
            double y0 = p.getY() - clip.getTransform().getTranslateY();
            long y = java.lang.Math.round(y0 / 3.9999999999999998E-006D);
            if(y < 0L || y > (long)length)
                return;
            msgAreaRef.setText("click position: " + gen_id + " " + id + ": " + y);
        }
    }

    public void setVisibleOverride(java.lang.String sid)
    {
        visibleOverrideHack.add(sid);
    }

    public void clearVisibleOverride()
    {
        visibleOverrideHack.clear();
    }

    public void rebuild(ncsa.evolutionhighway.util.GlobalSettings _settings, java.lang.String activeAncID, ncsa.evolutionhighway.util.SpeciesOrderComparator comparator)
    {
        java.util.ArrayList visible = new ArrayList();
        settings = _settings;
        ncsa.evolutionhighway.datatype.ComparativeSpecies cs;
        for(java.util.Iterator cskeyiter = csMap.keySet().iterator(); cskeyiter.hasNext(); cs.setTransform(new AffineTransform()))
        {
            java.lang.String sid = (java.lang.String)cskeyiter.next();
            cs = (ncsa.evolutionhighway.datatype.ComparativeSpecies)csMap.get(sid);
            boolean vis;
            if(visibleOverrideHack.size() > 0)
            {
                if(visibleOverrideHack.contains(sid))
                    vis = true;
                else
                    vis = false;
            } else
            if(comparator.isVisible(sid))
                vis = true;
            else
                vis = false;
            if(vis)
            {
                cs.rebuild(settings, p_heterochromatin_mark < 0 ? p_satellite_mark : p_heterochromatin_mark, q_heterochromatin_mark < 0 ? q_satellite_mark : q_heterochromatin_mark);
                visible.add(sid);
                edu.umd.cs.piccolo.nodes.PText txt[] = (edu.umd.cs.piccolo.nodes.PText[])(edu.umd.cs.piccolo.nodes.PText[])spcLabelMap.get(sid);
                txt[0].setVisible(true);
                txt[1].setVisible(true);
                cs.setVisible(true);
                cs.setPickable(true);
                cs.setChildrenPickable(true);
            } else
            {
                edu.umd.cs.piccolo.nodes.PText txt[] = (edu.umd.cs.piccolo.nodes.PText[])(edu.umd.cs.piccolo.nodes.PText[])spcLabelMap.get(sid);
                txt[0].setVisible(false);
                txt[1].setVisible(false);
                cs.setVisible(false);
                cs.setPickable(false);
                cs.setChildrenPickable(false);
            }
        }

        java.util.Collections.sort(visible, comparator);
        int width = 0;
        for(int i = 0; i < visible.size(); i++)
        {
            java.lang.String sid = (java.lang.String)visible.get(i);
            cs = (ncsa.evolutionhighway.datatype.ComparativeSpecies)csMap.get(sid);
            cs.translate(width, 0.0D);
            width = (int)((double)width + 20D);
        }

        effectiveWidth = width;
        label.setTransform(new AffineTransform());
        label.translate(0.5D * (effectiveWidth - 1.25D * label.getWidth()), 0.0D);
        label.scale(1.25D);
        if(centromere_mark > 2)
        {
            clip.setPathTo(new java.awt.geom.RoundRectangle2D.Double(0.0D, 0.0D, effectiveWidth, 3.9999999999999998E-006D * (double)centromere_mark, 20D, 20D));
            clip.append(new java.awt.geom.RoundRectangle2D.Double(0.0D, 3.9999999999999998E-006D * (double)centromere_mark, effectiveWidth, 3.9999999999999998E-006D * (double)(length - centromere_mark), 20D, 20D), false);
        } else
        {
            clip.setPathTo(new java.awt.geom.RoundRectangle2D.Double(0.0D, 3.9999999999999998E-006D * (double)centromere_mark, effectiveWidth, 3.9999999999999998E-006D * (double)(length - centromere_mark), 20D, 20D));
        }
        double buffer_small = 6D;
        double buffer_large = 30D;
        width = 0;
        for(int i = 0; i < visible.size(); i++)
        {
            java.lang.String sid = (java.lang.String)visible.get(i);
            edu.umd.cs.piccolo.nodes.PText txt[] = (edu.umd.cs.piccolo.nodes.PText[])(edu.umd.cs.piccolo.nodes.PText[])spcLabelMap.get(sid);
            txt[0].setTransform(new AffineTransform());
            txt[1].setTransform(new AffineTransform());
            txt[0].translate(width, buffer_large + label.getHeight() + maxLabelWidth);
            txt[1].translate(width, buffer_large + 2D * buffer_small + 3.9999999999999998E-006D * (double)length + maxLabelWidth + txt[1].getWidth() + label.getHeight());
            txt[0].rotate(-1.5707963267948966D);
            txt[1].rotate(-1.5707963267948966D);
            width = (int)((double)width + 20D);
        }

        if(p_heterochromatin_mark >= 0)
            hcPaintP.setBounds(0.0D, 3.9999999999999998E-006D * (double)p_heterochromatin_mark, effectiveWidth, 3.9999999999999998E-006D * (double)(centromere_mark - p_heterochromatin_mark));
        if(q_heterochromatin_mark >= 0)
            hcPaintQ.setBounds(0.0D, 3.9999999999999998E-006D * (double)centromere_mark, effectiveWidth, 3.9999999999999998E-006D * (double)(q_heterochromatin_mark - centromere_mark));
        if(p_satellite_mark >= 0)
            satPaintP.setBounds(0.0D, 3.9999999999999998E-006D * (double)p_satellite_mark, effectiveWidth, 3.9999999999999998E-006D * (double)(centromere_mark - p_satellite_mark));
        if(q_satellite_mark >= 0)
            satPaintQ.setBounds(0.0D, 3.9999999999999998E-006D * (double)centromere_mark, effectiveWidth, 3.9999999999999998E-006D * (double)(q_satellite_mark - centromere_mark));
        double clip_y = buffer_small + buffer_large + label.getHeight() + maxLabelWidth;
        clip.setTransform(new AffineTransform());
        clip.translate(0.0D, clip_y);
        if(density != null && density.getVisible() && granularity > 0)
        {
            rebuildDensity();
            density.setTransform(new AffineTransform());
            density.translate(clip.getWidth() + 20D, clip_y);
            effectiveWidth += 40D;
        }
        rebuildAncestorRegions(settings, activeAncID, clip_y);
        children_scale.removeAllChildren();
        children_scale.addChild(rebuildScaleChildren());
        children_scale.setTransform(new AffineTransform());
        children_scale.translate(0.0D, clip_y);
        children_customTrack.setTransform(new AffineTransform());
        children_customTrack.translate(effectiveWidth, clip_y);
    }

    public void rebuildDensity()
    {
        density.rebuild(granularity);
    }

    private void rebuildAncestorRegions(ncsa.evolutionhighway.util.GlobalSettings settings, java.lang.String activeAncID, double clip_y)
    {
        if(activeAncID == null)
        {
            children_ancestorInClip.setVisible(false);
            children_ancestorOutOfClip.setVisible(false);
            return;
        }
        children_ancestorInClip.removeAllChildren();
        children_ancestorOutOfClip.removeAllChildren();
        java.util.ArrayList aoList = (java.util.ArrayList)aoMap.get(activeAncID);
        if(aoList == null)
            return;
        java.lang.String current = null;
        int index = 0;
        for(int i = 0; i < aoList.size(); i++)
        {
            ncsa.evolutionhighway.datatype.AncestorRegion ao = (ncsa.evolutionhighway.datatype.AncestorRegion)aoList.get(i);
            java.lang.String id = ao.getAncestorChromosomeID();
            if(current == null)
            {
                current = id.substring(0, id.length() - 1);
            } else
            {
                java.lang.String next = id.substring(0, id.length() - 1);
                if(!current.equals(next))
                {
                    index++;
                    current = next;
                }
            }
            int mode = settings.getAncestorColorMode();
            int rgb[];
            if(mode == 100)
            {
                rgb = ncsa.evolutionhighway.util.Constants.ANCESTOR_COLORS_CONSISTENT[index % ncsa.evolutionhighway.util.Constants.ANCESTOR_COLORS_CONSISTENT.length];
            } else
            {
                java.lang.String sub = id.substring(0, id.length() - 1);
                int aid;
                try
                {
                    aid = java.lang.Integer.parseInt(sub);
                }
                catch(java.lang.NumberFormatException nfe)
                {
                    aid = 0;
                }
                switch(mode)
                {
                case 101: // 'e'
                    rgb = ncsa.evolutionhighway.util.Constants.ANCESTOR_COLORS_DISTINCT_1[aid % ncsa.evolutionhighway.util.Constants.ANCESTOR_COLORS_DISTINCT_1.length];
                    break;

                case 102: // 'f'
                    rgb = ncsa.evolutionhighway.util.Constants.ANCESTOR_COLORS_DISTINCT_2[aid % ncsa.evolutionhighway.util.Constants.ANCESTOR_COLORS_DISTINCT_2.length];
                    break;

                case 103: // 'g'
                    rgb = ncsa.evolutionhighway.util.Constants.ANCESTOR_COLORS_DISTINCT_3[aid % ncsa.evolutionhighway.util.Constants.ANCESTOR_COLORS_DISTINCT_3.length];
                    break;

                default:
                    throw new RuntimeException();
                }
            }
            ncsa.evolutionhighway.util.PaintNode inside = new PaintNode(new Color(rgb[0], rgb[1], rgb[2], settings.getAncestorTransparency()));
            inside.setBounds(0.0D, 3.9999999999999998E-006D * (double)ao.getStart(), effectiveWidth, 3.9999999999999998E-006D * (double)(ao.getEnd() - ao.getStart()));
            children_ancestorInClip.addChild(inside);
            if(settings.getAncestorTransparency() != 0)
            {
                ao.setPaint(new Color(rgb[0], rgb[1], rgb[2], settings.getAncestorTransparency()));
                ao.setBounds(0.0D, 3.9999999999999998E-006D * (double)ao.getStart(), 20D, 3.9999999999999998E-006D * (double)(ao.getEnd() - ao.getStart()));
                children_ancestorOutOfClip.addChild(ao);
            }
        }

        children_ancestorOutOfClip.setTransform(new AffineTransform());
        children_ancestorOutOfClip.translate(effectiveWidth + 27D, clip_y);
        children_ancestorInClip.setVisible(settings.getShowAncestorsInside());
        children_ancestorOutOfClip.setVisible(true);
    }

    public void setLength(int len)
    {
        length = len;
        density = new PDensityOverlay(len);
        density.setVisible(false);
        density.setBounds(0.0D, 0.0D, 20D, (double)len * 3.9999999999999998E-006D);
        addChild(density);
    }

    public void setPHeterochromatinMark(int mark)
    {
        if(p_heterochromatin_mark < 0 || mark < p_heterochromatin_mark)
            p_heterochromatin_mark = mark;
    }

    public void setPSatelliteMark(int mark)
    {
        if(p_satellite_mark < 0 || mark < p_satellite_mark)
            p_satellite_mark = mark;
    }

    public void setQHeterochromatinMark(int mark)
    {
        if(q_heterochromatin_mark < 0 || mark > q_heterochromatin_mark)
            q_heterochromatin_mark = mark;
    }

    public void setQSatelliteMark(int mark)
    {
        if(q_satellite_mark < 0 || mark < q_satellite_mark)
            q_satellite_mark = mark;
    }

    public java.lang.String toString()
    {
        return id;
    }
}
