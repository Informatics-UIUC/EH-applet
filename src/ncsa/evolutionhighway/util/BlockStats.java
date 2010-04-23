// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   BlockStats.java

package ncsa.evolutionhighway.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BlockStats
{
    private static class Region
        implements java.lang.Comparable
    {

        private int bounds[];

        public int compareTo(java.lang.Object o)
        {
            return bounds[0] - ((Region)o).bounds[0];
        }

        public boolean equals(java.lang.Object o)
        {
            return bounds[0] == ((Region)o).bounds[0];
        }


        private Region(int bounds[])
        {
            if(bounds[0] < 0 || bounds[1] <= bounds[0])
            {
                throw new IllegalArgumentException(bounds[0] + " - " + bounds[1]);
            } else
            {
                this.bounds = bounds;
                return;
            }
        }

    }


    private static int hsaLengths[] = {
        0xe9d83ba, 0xe80b154, 0xbe2c813, 0xb6bbe9b, 0xac9577f, 0xa2d4b3d, 0x9717843, 0x8b26402, 0x804655b, 0x813462a, 
        0x80b9ce0, 0x7f48172, 0x6cdd0e8, 0x646ebf0, 0x5f79e87, 0x55d3adf, 0x4de8250, 0x4a26ca6, 0x3cd5f0c, 0x3cb24c4, 
        0x2ccce19, 0x2f2f56c, 0x9190336
    };

    public static void main(java.lang.String args[])
    {
        try
        {
            ncsa.evolutionhighway.util.BlockStats.filterParens(new File("C:\\Documents and Settings\\gpape\\Desktop\\hsa-stats-1.txt"), new File("C:\\Documents and Settings\\gpape\\Desktop\\hsa-stats-1-filtered.txt"));
        }
        catch(java.io.IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    private BlockStats()
    {
    }

    public static void getStats(java.io.File consensusInputFile, java.io.File statsOutputFile, java.lang.String host, java.lang.String db, long delay)
        throws java.io.IOException
    {
        java.lang.String urlBase = "http://" + host + "/cgi-bin/hgTables?db=" + db + "&";
        java.lang.String params = "hgta_group=genes&" + "hgta_track=knownGene&" + "hgta_table=knownGene&" + "hgta_regionType=range&" + "hgta_outputType=primaryTable&" + "hgta_outFileName=&" + "hgta_compressType=hgta_compressNone&" + "hgta_doSummaryStats=summary%2Fstatistics&";
        java.lang.String TAB = "\t";
        java.lang.String NLN = "\n";
        java.io.BufferedReader br = new BufferedReader(new FileReader(consensusInputFile));
        java.io.BufferedWriter bw = new BufferedWriter(new FileWriter(statsOutputFile));
label0:
        do
        {
label1:
            {
                java.lang.String ln;
                if((ln = br.readLine()) == null)
                    break label0;
                java.lang.String sp[] = ln.split(TAB);
                if(sp.length == 0)
                    continue;
                java.lang.String genID = sp[0];
                java.lang.String chrID = sp[1];
                int start;
                int end;
                try
                {
                    start = java.lang.Integer.parseInt(sp[2]);
                    end = java.lang.Integer.parseInt(sp[3]);
                }
                catch(java.lang.NumberFormatException nfe)
                {
                    continue;
                }
                java.lang.String spcChr = sp[4];
                int sign = java.lang.Integer.parseInt(sp[7]);
                java.lang.String spcID = sp[8];
                java.lang.String post = "GET " + urlBase + params + "position=chr" + chrID + "%3A" + java.lang.Integer.toString(start) + "-" + java.lang.Integer.toString(end) + " HTTP/1.1\r\n" + "Host: http://" + host + "\r\n" + "User-Agent: NCSA Evolution Highway\r\n" + "From: d2k@ncsa.uiuc.edu\r\n" + "Keep-Alive: 300\r\n" + "Connection: keep-alive\r\n" + "\r\n";
                try
                {
                    java.lang.String statLines[];
label2:
                    {
                        java.net.InetAddress address = java.net.InetAddress.getByName(host);
                        java.net.Socket socket = new Socket(address, 80);
                        java.io.BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
                        java.io.BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        writer.write(post);
                        writer.flush();
                        java.lang.StringBuffer socksb = new StringBuffer();
                        java.lang.String statMarkers[] = {
                            "item count", "item bases", "item total", "smallest item", "average item", "biggest item", "block count", "block bases", "block total", "smallest block", 
                            "average block", "biggest block"
                        };
                        statLines = new java.lang.String[statMarkers.length];
                        int stat_found = 0;
                        java.lang.String sockln;
                        do
                        {
                            if((sockln = reader.readLine()) == null)
                                break label2;
                            try
                            {
                                java.lang.Integer.parseInt(sockln, 16);
                            }
                            catch(java.lang.NumberFormatException nfe)
                            {
                                socksb.append(sockln);
                            }
                            if(sockln.indexOf(statMarkers[stat_found]) == -1)
                                continue;
                            statLines[stat_found] = sockln;
                            if(++stat_found == statLines.length)
                                break label2;
                        } while(sockln.indexOf("</html>") == -1);
                        throw new RuntimeException("didn't get all " + start + " - " + end);
                    }
                    bw.write(genID + TAB);
                    bw.write(chrID + TAB);
                    bw.write(start + TAB);
                    bw.write(end + TAB);
                    bw.write(db + TAB);
                    bw.write(spcID + TAB);
                    bw.write(spcChr + TAB);
                    bw.write(sign + TAB);
                    for(int i = 0; i < statLines.length; i++)
                    {
                        try
                        {
                            int sub_begin = statLines[i].indexOf("RIGHT") + 6;
                            int sub_end = statLines[i].indexOf("</TD>", sub_begin);
                            statLines[i] = statLines[i].substring(sub_begin, sub_end);
                        }
                        catch(java.lang.NullPointerException npe)
                        {
                            statLines[i] = null;
                        }
                        if(statLines[i] != null)
                            bw.write(statLines[i]);
                        else
                            bw.write("0");
                        if(i < statLines.length - 1)
                            bw.write(TAB);
                    }

                    bw.write(NLN);
                    bw.flush();
                    break label1;
                }
                catch(java.io.IOException ioe)
                {
                    ioe.printStackTrace();
                    if(ioe instanceof java.net.SocketException)
                    {
                        java.lang.System.err.println("!!! did not connect on " + start + " - " + end);
                        bw.write("!!! did not connect on " + start + " - " + end + NLN);
                    } else
                    {
                        throw ioe;
                    }
                    continue;
                }
            }
            try
            {
                java.lang.Thread.sleep(delay);
            }
            catch(java.lang.InterruptedException ie)
            {
                ie.printStackTrace();
            }
        } while(true);
        br.close();
        bw.flush();
        bw.close();
    }

    private static void convertToBreakpoints(java.io.File consensusInputFile, java.io.File breakpointOutputFile)
        throws java.io.IOException
    {
        java.util.HashMap map = new HashMap();
        java.lang.String TAB = "\t";
        java.lang.String NLN = "\n";
        java.io.BufferedReader br = new BufferedReader(new FileReader(consensusInputFile));
        do
        {
            java.lang.String ln;
            if((ln = br.readLine()) == null)
                break;
            java.lang.String sp[] = ln.split(TAB);
            if(sp.length == 0)
                continue;
            java.lang.String genID = sp[0];
            java.lang.String chrID = sp[1];
            int start;
            int end;
            try
            {
                start = java.lang.Integer.parseInt(sp[2]);
                end = java.lang.Integer.parseInt(sp[3]);
            }
            catch(java.lang.NumberFormatException nfe)
            {
                continue;
            }
            java.lang.String spcID = sp[5];
            Region r = new Region(new int[] {
                start, end
            });
            java.util.Map cMap = (java.util.Map)map.get(genID);
            if(cMap == null)
            {
                cMap = new HashMap();
                map.put(genID, cMap);
            }
            java.util.Map sMap = (java.util.Map)cMap.get(chrID);
            if(sMap == null)
            {
                sMap = new HashMap();
                cMap.put(chrID, sMap);
            }
            java.util.ArrayList rList = (java.util.ArrayList)sMap.get(spcID);
            if(rList == null)
            {
                rList = new ArrayList();
                sMap.put(spcID, rList);
            }
            int pos = java.util.Collections.binarySearch(rList, r);
            if(pos >= 0)
                throw new RuntimeException(genID + " " + chrID + " " + spcID + " " + start + "-" + end);
            rList.add(-pos - 1, r);
        } while(true);
        br.close();
        java.io.BufferedWriter bw = new BufferedWriter(new FileWriter(breakpointOutputFile));
        for(java.util.Iterator gIter = map.keySet().iterator(); gIter.hasNext();)
        {
            java.lang.String genID = (java.lang.String)gIter.next();
            java.util.Map cMap = (java.util.Map)map.get(genID);
            java.util.Iterator cIter = cMap.keySet().iterator();
            while(cIter.hasNext()) 
            {
                java.lang.String chrID = (java.lang.String)cIter.next();
                java.util.Map sMap = (java.util.Map)cMap.get(chrID);
                java.util.Iterator sIter = sMap.keySet().iterator();
                while(sIter.hasNext()) 
                {
                    java.lang.String spcID = (java.lang.String)sIter.next();
                    java.util.ArrayList rList = (java.util.ArrayList)sMap.get(spcID);
                    Region rF = (Region)rList.get(0);
                    if(rF.bounds[0] > 1)
                    {
                        bw.write(genID + TAB);
                        bw.write(chrID + TAB);
                        bw.write(0 + TAB);
                        bw.write((rF.bounds[0] - 1) + TAB);
                        bw.write("-" + TAB);
                        bw.write("-" + TAB);
                        bw.write("-" + TAB);
                        bw.write(0 + TAB);
                        bw.write(spcID + TAB);
                        bw.write("-" + TAB);
                        bw.write("-" + NLN);
                    }
                    for(int i = 0; i < rList.size() - 1; i++)
                    {
                        Region r1 = (Region)rList.get(i);
                        Region r2 = (Region)rList.get(i + 1);
                        bw.write(genID + TAB);
                        bw.write(chrID + TAB);
                        bw.write((r1.bounds[1] + 1) + TAB);
                        bw.write((r2.bounds[0] - 1) + TAB);
                        bw.write("-" + TAB);
                        bw.write("-" + TAB);
                        bw.write("-" + TAB);
                        bw.write(0 + TAB);
                        bw.write(spcID + TAB);
                        bw.write("-" + TAB);
                        bw.write("-" + NLN);
                        bw.flush();
                    }

                    if(!genID.equalsIgnoreCase("HSA"))
                        throw new RuntimeException("this method only supports HSA for now");
                    int chrIndex;
                    try
                    {
                        chrIndex = java.lang.Integer.parseInt(chrID) - 1;
                    }
                    catch(java.lang.NumberFormatException nfe)
                    {
                        chrIndex = 22;
                    }
                    Region rL = (Region)rList.get(rList.size() - 1);
                    if(rL.bounds[1] < hsaLengths[chrIndex])
                    {
                        bw.write(genID + TAB);
                        bw.write(chrID + TAB);
                        bw.write((rL.bounds[1] + 1) + TAB);
                        bw.write(hsaLengths[chrIndex] + TAB);
                        bw.write("-" + TAB);
                        bw.write("-" + TAB);
                        bw.write("-" + TAB);
                        bw.write(0 + TAB);
                        bw.write(spcID + TAB);
                        bw.write("-" + TAB);
                        bw.write("-" + NLN);
                    }
                }
            }
        }

        bw.flush();
        bw.close();
    }

    private static void filterParens(java.io.File inFile, java.io.File outFile)
        throws java.io.IOException
    {
        java.lang.String TAB = "\t";
        java.lang.String NLN = "\n";
        java.io.BufferedReader br = new BufferedReader(new FileReader(inFile));
        java.io.BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
        do
        {
            java.lang.String ln;
            if((ln = br.readLine()) == null)
                break;
            java.lang.String sp[] = ln.split(TAB);
            if(sp.length != 0)
            {
                int i = 0;
                while(i < sp.length) 
                {
                    if(sp[i].indexOf("(") != -1)
                    {
                        int p_start = sp[i].indexOf("(");
                        int p_end = sp[i].indexOf(")");
                        java.lang.String num = sp[i].substring(0, p_start - 1);
                        java.lang.String pct = sp[i].substring(p_start + 1, p_end - 1);
                        bw.write(num.replaceAll(",", ""));
                        bw.write(TAB);
                        bw.write(pct);
                    } else
                    {
                        bw.write(sp[i].replaceAll(",", ""));
                    }
                    if(i < sp.length - 1)
                        bw.write(TAB);
                    else
                        bw.write(NLN);
                    bw.flush();
                    i++;
                }
            }
        } while(true);
        br.close();
        bw.flush();
        bw.close();
    }

}
