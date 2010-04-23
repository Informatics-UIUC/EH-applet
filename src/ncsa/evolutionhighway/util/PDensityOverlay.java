// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   PDensityOverlay.java

package ncsa.evolutionhighway.util;

import edu.umd.cs.piccolo.PNode;
import java.awt.Color;
import java.util.ArrayList;
import java.util.ListIterator;

// Referenced classes of package ncsa.d2k.modules.projects.gpape.evolution.util:
//            Feature, PaintNode

public class PDensityOverlay extends edu.umd.cs.piccolo.PNode
{

    private int chr_length;
    private final java.util.ArrayList features = new ArrayList();

    public PDensityOverlay(int chr_length)
    {
        this.chr_length = chr_length;
    }

    public void addFeature(ncsa.evolutionhighway.util.Feature f)
    {
        synchronized(features)
        {
            features.add(f);
        }
    }

    public void clearFeatures()
    {
        synchronized(features)
        {
            features.clear();
        }
    }

    private java.awt.Color interpolate(float frac)
    {
        java.awt.Color high = java.awt.Color.YELLOW;
        java.awt.Color low = java.awt.Color.DARK_GRAY;
        float low_red = low.getRed();
        float low_green = low.getGreen();
        float low_blue = low.getBlue();
        float red = low_red + frac * ((float)high.getRed() - low_red);
        float green = low_green + frac * ((float)high.getGreen() - low_green);
        float blue = low_blue + frac * ((float)high.getBlue() - low_blue);
        return new Color(java.lang.Math.round(red), java.lang.Math.round(green), java.lang.Math.round(blue));
    }

    public void rebuild(int granularity)
    {
        if(granularity < 0)
            return;
        if(granularity < 50000)
            granularity = 50000;
        removeAllChildren();
        int num_bins = chr_length / granularity + 1;
        double bins[] = new double[num_bins];
        int last_bin_granularity = chr_length % granularity;
        if(last_bin_granularity == 0)
            last_bin_granularity = granularity;
        synchronized(features)
        {
            int num_features = features.size();
            java.util.ListIterator fIter = features.listIterator();
            for(int i = 0; i < num_features; i++)
            {
                ncsa.evolutionhighway.util.Feature f = (ncsa.evolutionhighway.util.Feature)fIter.next();
                int start = f.getStart();
                int end = f.getEnd();
                int start_bin = start / granularity;
                int end_bin = end / granularity;
                if(start_bin >= bins.length)
                    continue;
                if(end_bin >= bins.length)
                    end_bin = bins.length - 1;
                if(start_bin == end_bin)
                {
                    bins[start_bin]++;
                    continue;
                }
                bins[start_bin] += (double)((start_bin + 1) * granularity - start) / (double)granularity;
                if(end_bin == num_bins - 1)
                    bins[end_bin] += (double)(end - end_bin * granularity) / (double)last_bin_granularity;
                else
                    bins[end_bin] += (double)(end - end_bin * granularity) / (double)granularity;
                for(int j = start_bin + 1; j < end_bin; j++)
                    bins[j]++;

            }

        }
        double max = 0.0D;
        for(int i = 0; i < num_bins; i++)
            if(bins[i] > max)
                max = bins[i];

        for(int i = 0; i < num_bins; i++)
        {
            float frac = (float)(bins[i] / max);
            ncsa.evolutionhighway.util.PaintNode p = new PaintNode(interpolate(frac));
            p.setAA(false);
            if(i == num_bins - 1)
                p.setBounds(0.0D, (double)(i * granularity) * 3.9999999999999998E-006D, 20D, (double)last_bin_granularity * 3.9999999999999998E-006D);
            else
                p.setBounds(0.0D, (double)(i * granularity) * 3.9999999999999998E-006D, 20D, (double)granularity * 3.9999999999999998E-006D);
            addChild(p);
        }

    }
}
