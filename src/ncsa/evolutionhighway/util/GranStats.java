// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   GranStats.java

package ncsa.evolutionhighway.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class GranStats
{

    public static void main(java.lang.String args[])
    {
        try
        {
            ncsa.evolutionhighway.util.GranStats.grantest(new File("c:\\documents and settings\\gpape\\desktop\\hgTables-chr2"));
        }
        catch(java.lang.Exception e)
        {
            e.printStackTrace();
        }
    }

    private GranStats()
    {
    }

    private static void grantest(java.io.File f)
        throws java.io.IOException
    {
        java.io.BufferedReader r = new BufferedReader(new FileReader(f));
        long len_total = 0L;
        int num_lines = 0;
        do
        {
            java.lang.String ln;
            if((ln = r.readLine()) == null)
                break;
            java.lang.String s[] = ln.split("\t");
            int start;
            int end;
            try
            {
                start = java.lang.Integer.parseInt(s[3]);
                end = java.lang.Integer.parseInt(s[4]);
            }
            catch(java.lang.NumberFormatException nfe)
            {
                continue;
            }
            len_total += end - start;
            num_lines++;
        } while(true);
        java.lang.System.out.println((double)len_total / (double)num_lines);
        r.close();
    }
}
