// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst
// Source File Name:   ComparativeSpecies.java

package ncsa.evolutionhighway.datatype;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PPaintContext;

import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import javax.swing.JTextArea;

import ncsa.evolutionhighway.components.support.EvolutionHighwayApplet;
//import ncsa.evolutionhighway.util.BareBonesBrowserLaunch;
import ncsa.evolutionhighway.util.GlobalSettings;

// Referenced classes of package ncsa.evolutionhighway.datatype:
//            HSB, Chromosome

public class ComparativeSpecies extends edu.umd.cs.piccolo.PNode
    implements java.lang.Comparable, edu.umd.cs.piccolo.event.PInputEventListener
{

    private java.lang.String id;
    private java.awt.geom.Rectangle2D _rectangle;
    private ncsa.evolutionhighway.datatype.Chromosome chromosome;
    private GlobalSettings settings;

    public ComparativeSpecies(java.lang.String speciesID)
    {
        addInputEventListener(this);
        id = speciesID;
        _rectangle = new java.awt.geom.Rectangle2D.Double();
    }

    public void setChromosome(ncsa.evolutionhighway.datatype.Chromosome c)
    {
        chromosome = c;
    }

    public void addHSB(ncsa.evolutionhighway.datatype.HSB hsb)
    {
        addChild(hsb);
        hsb.setBounds(0.0D, 3.9999999999999998E-006D * (double)hsb.getStart(), 20D, 3.9999999999999998E-006D * (double)(hsb.getEnd() - hsb.getStart()));
    }

    public int compareTo(java.lang.Object o)
    {
        ncsa.evolutionhighway.datatype.ComparativeSpecies other = (ncsa.evolutionhighway.datatype.ComparativeSpecies)o;
        return id.compareTo(other.id);
    }

    public java.util.List getConservedSyntenyBounds()
    {
        java.util.ArrayList list = new ArrayList();
        java.util.ListIterator childiter = getChildrenIterator();
        do
        {
            if(!childiter.hasNext())
                break;
            edu.umd.cs.piccolo.PNode node = (edu.umd.cs.piccolo.PNode)childiter.next();
            if(node instanceof ncsa.evolutionhighway.datatype.HSB)
            {
                edu.umd.cs.piccolo.util.PBounds bounds = node.getBounds();
                double d[] = {
                    bounds.getY(), bounds.getY() + bounds.getHeight()
                };
                list.add(d);
            }
        } while(true);
        java.util.Collections.sort(list, new java.util.Comparator() {

            public int compare(java.lang.Object o1, java.lang.Object o2)
            {
                return ((double[])(double[])o1)[0] - ((double[])(double[])o2)[0] >= 0.0D ? 1 : -1;
            }


           // {
           //     super();
           // }
        }
);
        for(int li = 1; li < list.size(); li++)
        {
            double da[] = (double[])list.get(li - 1);
            double db[] = (double[])(double[])list.get(li);
            if(db[0] >= da[1])
                continue;
            if(db[1] < da[1])
            {
                list.remove(li);
                li--;
            } else
            {
                list.remove(li);
            	list.remove(li - 1);
                list.add(li - 1, new double[] {
                    da[0], db[1]
                });
                li--;
            }
        }

        return list;
    }

    public java.util.List getBreakpointBounds()
    {
        java.util.ArrayList synteny = (java.util.ArrayList)getConservedSyntenyBounds();
        java.util.ArrayList list = new ArrayList();
        for(int i = 0; i < synteny.size() - 1; i++)
        {
            double d1[] = (double[])(double[])synteny.get(i);
            double d2[] = (double[])(double[])synteny.get(i + 1);
            list.add(new double[] {
                d1[1], d2[0]
            });
        }

        return list;
    }

    public java.lang.String getSpeciesID()
    {
        return id;
    }

    public void paint(edu.umd.cs.piccolo.util.PPaintContext aPaintContext)
    {
        java.awt.Graphics2D g2 = aPaintContext.getGraphics();
        g2.setPaint(java.awt.Color.WHITE);
        g2.fill(_rectangle);
    }

    public void processEvent(edu.umd.cs.piccolo.event.PInputEvent event, int type)
    {
        if(event.isLeftMouseButton() && type == 500)
        {
            edu.umd.cs.piccolo.PNode picked = event.getPickedNode();
            if(picked != null && (picked instanceof ncsa.evolutionhighway.datatype.HSB))
            {
                try
                {
                    ncsa.evolutionhighway.datatype.HSB hsb = (ncsa.evolutionhighway.datatype.HSB)picked;
                    chromosome.getMsgAreaRef().setText("click region: synteny: " + hsb.getReferenceGenome() + " " + hsb.getReferenceChromosome() + " (" + hsb.getSpeciesID() + " " + hsb.getSpeciesChromosome() + "): " + hsb.getStart() + " - " + hsb.getEnd());
                    java.lang.StringBuffer url = new StringBuffer();
                    java.lang.String build = (java.lang.String)EvolutionHighwayApplet.buildMap.get(hsb.getReferenceGenome());
                    java.lang.String buildURL = (java.lang.String)EvolutionHighwayApplet.buildURLMap.get(hsb.getReferenceGenome());
                    if((buildURL != null)&&(buildURL.compareTo("?") != 0))
                        url.append(buildURL + "?");
                    else
                        url.replace(0, url.length(), "http://genome.ucsc.edu/cgi-bin/hgTracks?");
                    if((build != null)&&(build.compareTo("?") != 0))
                        url.append("db=" + build + "&");
                    else
                        throw new IllegalArgumentException("we don't know how to look up genome: " + hsb.getReferenceGenome());
                    url.append("position=chr" + hsb.getReferenceChromosome() + "%3A" + hsb.getStart() + "-" + hsb.getEnd());
                    System.out.println(url.toString());
            		AppletContext a = settings.getAppletContext();
            		 a.showDocument(new URL(url.toString()), "_blank");
                }
                catch(java.lang.Exception e)
                {
                    chromosome.getMsgAreaRef().setText(e.getMessage());
                }
            } else
            {
                java.util.ArrayList gaps = (java.util.ArrayList)getBreakpointBounds();
                double y = event.getPositionRelativeTo(this).getY();
                for(int i = 0; i < gaps.size(); i++)
                {
                    double gap[] = (double[])(double[])gaps.get(i);
                    if(y > gap[0] && y < gap[1])
                    {
                        try
                        {
                            ncsa.evolutionhighway.datatype.HSB someHSB = (ncsa.evolutionhighway.datatype.HSB)getChild(0);
                            long start = java.lang.Math.round(gap[0] / 3.9999999999999998E-006D);
                            long end = java.lang.Math.round(gap[1] / 3.9999999999999998E-006D);
                            chromosome.getMsgAreaRef().setText("click region: gap: " + someHSB.getReferenceGenome() + " " + someHSB.getReferenceChromosome() + " (" + someHSB.getSpeciesID() + "): " + start + " - " + end);
                            java.lang.StringBuffer url = new StringBuffer();
                            java.lang.String build = (java.lang.String)EvolutionHighwayApplet.buildMap.get(someHSB.getReferenceGenome());
                            java.lang.String buildURL = (java.lang.String)EvolutionHighwayApplet.buildURLMap.get(someHSB.getReferenceGenome());
                            if((buildURL != null)&&(buildURL.compareTo("?") != 0))
                                url.append(buildURL + "?");
                            else
                                url.replace(0, url.length(), "http://genome.ucsc.edu/cgi-bin/hgTracks?");
                            if((build != null)&&(build.compareTo("?") != 0))
                                url.append("db=" + build + "&");
                            else
                                throw new IllegalArgumentException("we don't know how to look up genome: " + someHSB.getReferenceGenome());
                            url.append("position=chr" + someHSB.getReferenceChromosome() + "%3A" + start + "-" + end);
                            System.out.println(url.toString());
                    		AppletContext a = settings.getAppletContext();
                    		 a.showDocument(new URL(url.toString()), "_blank");
                        }
                        catch(java.lang.Exception e)
                        {
                            chromosome.getMsgAreaRef().setText(e.getMessage());
                        }
                        return;
                    }
                }

            }
        }
    }

    public void rebuild(ncsa.evolutionhighway.util.GlobalSettings _settings, int break_start, int break_end)
    {
        settings = _settings;
    	for(java.util.Iterator hsbiter = getChildrenIterator(); hsbiter.hasNext(); ((ncsa.evolutionhighway.datatype.HSB)hsbiter.next()).rebuild(settings, break_start, break_end));
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
}
