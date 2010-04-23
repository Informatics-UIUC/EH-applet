// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst
// Source File Name:   GlobalSettings.java

package ncsa.evolutionhighway.util;

import javax.swing.JTextArea;
import java.applet.*;
import java.net.URL;
import java.util.List;

public class GlobalSettings
{

    private int ancestorColorMode;
    private int ancestorTransparency;
    private int vanishingThreshold;
    private boolean showAncestorsInside;
    private boolean showCentromeres;
    private boolean showTelomeres;
    private long lastUpdateTime;
    private javax.swing.JTextArea msgArea;
    private AppletContext apcontext;
    private URL codebase;

    private List genomeList;
    private List featureList;
    private List tableList;

    public GlobalSettings(javax.swing.JTextArea msgArea)
    {
        ancestorColorMode = 103;
        ancestorTransparency = 191;
        vanishingThreshold = 0;
        showAncestorsInside = false;
        showCentromeres = true;
        showTelomeres = true;
        this.msgArea = msgArea;
        update();
    }

    public javax.swing.JTextArea getMsgAreaRef()
    {
        return msgArea;
    }

    public int getAncestorColorMode()
    {
        return ancestorColorMode;
    }

    public int getAncestorTransparency()
    {
        return ancestorTransparency;
    }

    public int getVanishingThreshold()
    {
        return vanishingThreshold;
    }

    public boolean getShowAncestorsInside()
    {
        return showAncestorsInside;
    }

    public boolean getShowCentromeres()
    {
        return showCentromeres;
    }

    public boolean getShowTelomeres()
    {
        return showTelomeres;
    }

    public void setAncestorColorMode(int ancestorColorMode)
    {
        this.ancestorColorMode = ancestorColorMode;
        update();
    }

    public void setAncestorTransparency(int ancestorTransparency)
    {
        this.ancestorTransparency = ancestorTransparency;
        update();
    }

    public void setShowAncestorsInside(boolean showAncestorsInside)
    {
        this.showAncestorsInside = showAncestorsInside;
        update();
    }

    public void setShowCentromeres(boolean showCentromeres)
    {
        this.showCentromeres = showCentromeres;
        update();
    }

    public void setShowTelomeres(boolean showTelomeres)
    {
        this.showTelomeres = showTelomeres;
        update();
    }

    public void setVanishingThreshold(int vanishingThreshold)
    {
        this.vanishingThreshold = vanishingThreshold;
        update();
    }

    private void update()
    {
        lastUpdateTime = java.lang.System.currentTimeMillis();
    }

    public void setAppletContext(AppletContext ac)
    {
      apcontext = ac;
    }

    public AppletContext getAppletContext()
    {
      return apcontext;
    }

    public void setCodeBase(URL url)
    {
      codebase = url;
      //System.out.println("In global settings: codebase = "+url);
    }

    public URL getCodeBase()
    {
      return codebase;
    }

    public void setgenomeList(List obj)
    {
    	genomeList = obj;
      //System.out.println("In global settings: genomeList");
    }

    public List getgenomeList()
    {
      return genomeList;
    }

    public void setfeatureList(List obj)
    {
    	featureList = obj;
      //System.out.println("In global settings: featureList");
    }

    public List getfeatureList()
    {
      return featureList;
    }

    public void settableList(List obj)
    {
      tableList = obj;
      //System.out.println("In global settings: tableList");
    }

    public List gettableList()
    {
      return tableList;
    }
}
