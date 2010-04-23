// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   Genome.java

package ncsa.evolutionhighway.datatype;

import edu.umd.cs.piccolo.PNode;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import ncsa.evolutionhighway.util.GlobalSettings;
import ncsa.evolutionhighway.util.SpeciesOrderComparator;

// Referenced classes of package ncsa.evolutionhighway.datatype:
//            Chromosome

public class Genome extends edu.umd.cs.piccolo.PNode
{

    private java.lang.String id;
    private java.util.HashMap chromosomes;
    private java.util.HashSet ancestors;
    private java.util.HashSet species;
    private ncsa.evolutionhighway.util.SpeciesOrderComparator comparator;

    public Genome(java.lang.String genomeID)
    {
        chromosomes = new HashMap();
        id = genomeID;
        ancestors = new HashSet();
        species = new HashSet();
        comparator = new SpeciesOrderComparator();
    }

    public void addAncestorID(java.lang.String ancestorID)
    {
        ancestors.add(ancestorID);
    }

    public void addChromosome(ncsa.evolutionhighway.datatype.Chromosome c)
    {
        chromosomes.put(c.getID(), c);
        addChild(c);
    }

    public void addComparativeSpeciesID(java.lang.String speciesID)
    {
        species.add(speciesID);
    }

    public ncsa.evolutionhighway.datatype.Chromosome getChromosome(java.lang.String chromosomeID)
    {
        return (ncsa.evolutionhighway.datatype.Chromosome)chromosomes.get(chromosomeID);
    }

    public java.util.Iterator getAncestorIDIterator()
    {
        java.util.ArrayList ids = new ArrayList(ancestors);
        java.util.Collections.sort(ids);
        return ids.iterator();
    }

    public java.util.Iterator getChromosomeIterator()
    {
        java.util.ArrayList all = new ArrayList(chromosomes.size());
        for(java.util.Iterator chrkeyiter = chromosomes.keySet().iterator(); chrkeyiter.hasNext(); all.add((ncsa.evolutionhighway.datatype.Chromosome)chromosomes.get(chrkeyiter.next())));
        java.util.Collections.sort(all);
        return all.iterator();
    }

    public java.util.Iterator getComparativeSpeciesIDIterator()
    {
        java.util.ArrayList ids = new ArrayList(species);
        java.util.Collections.sort(ids, comparator);
        return ids.iterator();
    }

    public java.lang.String getID()
    {
        return id;
    }

    public ncsa.evolutionhighway.util.SpeciesOrderComparator getSpeciesOrderComparator()
    {
        return comparator;
    }

    public void rebuild(ncsa.evolutionhighway.util.GlobalSettings settings, java.lang.String activeAncID)
    {
        java.util.ArrayList visible = new ArrayList(chromosomes.size());
        ncsa.evolutionhighway.datatype.Chromosome c;
        for(java.util.Iterator chrkeyiter = chromosomes.keySet().iterator(); chrkeyiter.hasNext(); c.setTransform(new AffineTransform()))
        {
            c = (ncsa.evolutionhighway.datatype.Chromosome)chromosomes.get(chrkeyiter.next());
            c.setMsgAreaRef(settings.getMsgAreaRef());
            if(c.isDescendentOf(this))
                removeChild(c);
            if(c.getVisible())
            {
                c.rebuild(settings, activeAncID, comparator);
                visible.add(c);
            }
        }

        java.util.Collections.sort(visible);
        int width = 0;
        for(int i = 0; i < visible.size(); i++)
        {
            c = (ncsa.evolutionhighway.datatype.Chromosome)visible.get(i);
            if(!c.isDescendentOf(this))
                addChild(c);
            c.translate(width, 0.0D);
            width = (int)((double)width + (c.getChromosomeWidth() + 80D));
        }

    }

    public java.lang.String toString()
    {
        return id;
    }
}
