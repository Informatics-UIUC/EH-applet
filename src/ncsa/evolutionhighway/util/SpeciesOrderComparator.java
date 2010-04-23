// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   SpeciesOrderComparator.java

package ncsa.evolutionhighway.util;

import gnu.trove.TObjectIntHashMap;
import java.util.Comparator;
import java.util.HashMap;

public class SpeciesOrderComparator extends gnu.trove.TObjectIntHashMap
    implements java.util.Comparator
{

    private long lastUpdateTime;
    private java.util.HashMap visible;

    public SpeciesOrderComparator()
    {
        visible = new HashMap();
        put("horse", 0);
        put("cat", 1);
        put("dog", 2);
        put("pig", 3);
        put("cattle", 4);
        put("rat", 5);
        put("mouse", 6);
        put("human", 6);
    }

    public int compare(java.lang.Object o1, java.lang.Object o2)
    {
        if(containsKey(o1) && containsKey(o2))
            return get(o1) - get(o2);
        else
            return ((java.lang.String)o1).compareTo((java.lang.String)o2);
    }

    public long getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public int put(java.lang.Object o, int i)
    {
        lastUpdateTime = java.lang.System.currentTimeMillis();
        return super.put(o, i);
    }

    public boolean isVisible(java.lang.String id)
    {
        java.lang.Boolean b = (java.lang.Boolean)visible.get(id);
        if(b == null)
            return true;
        else
            return b.booleanValue();
    }

    public void setVisible(java.lang.String id, boolean status)
    {
        visible.put(id, new Boolean(status));
    }
}
