// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   GenomeBrowser.java

package ncsa.evolutionhighway.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JTextArea;

public class GenomeBrowser
{
    private static class WebStatsThread extends java.lang.Thread
    {

        private java.lang.String gen_id;
        private java.lang.String chr_id;
        private int pos_begin;
        private int pos_end;
        private javax.swing.JTextArea msgAreaRef;

        public void run()
        {
            stats();
        }

        private void stats()
        {
            java.lang.StringBuffer params = new StringBuffer();
            if(gen_id.equalsIgnoreCase("HSA"))
                params.append("org=human&");
            else
            if(gen_id.equalsIgnoreCase("MMU"))
            {
                params.append("org=mouse&");
            } else
            {
                msgAreaRef.setText(gen_id + " is not mapped to genome browser query...");
                return;
            }
            params.append("hgta_group=genes&");
            params.append("hgta_track=knownGene&");
            params.append("hgta_table=knownGene&");
            params.append("hgta_regionType=range&");
            params.append("position=chr" + chr_id + "%3A" + pos_begin + "-" + pos_end + "&");
            params.append("hgta_outputType=primaryTable&");
            params.append("hgta_outFileName=&");
            params.append("hgta_compressType=hgta_compressNone&");
            params.append("hgta_doSummaryStats=summary%2Fstatistics");
            java.lang.StringBuffer post = new StringBuffer();
            post.append("GET http://genome.ucsc.edu/cgi-bin/hgTables?" + params + " HTTP/1.1\r\n");
            post.append("Host: http://genome.ucsc.edu\r\n");
            post.append("User-Agent: NCSA Evolution Highway 1.2 alpha (prerelease)\r\n");
            post.append("From: d2k@ncsa.uiuc.edu\r\n");
            post.append("Keep-Alive: 300\r\n");
            post.append("Connection: keep-alive\r\n");
            post.append("\r\n");
            try
            {
                java.net.InetAddress address = java.net.InetAddress.getByName("genome.ucsc.edu");
                java.net.Socket socket = new Socket(address, 80);
                java.io.BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
                java.io.BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer.write(post.toString());
                writer.flush();
                java.lang.StringBuffer sb = new StringBuffer();
                java.lang.String line = null;
                java.lang.String basesLine = null;
                do
                {
                    if((line = reader.readLine()) == null)
                        break;
                    try
                    {
                        java.lang.Integer.parseInt(line, 16);
                    }
                    catch(java.lang.NumberFormatException nfe)
                    {
                        sb.append(line);
                    }
                    if(line.indexOf("item bases") != -1)
                        basesLine = line;
                } while(line.indexOf("</html>") == -1);
                if(basesLine != null)
                {
                    int sub_begin = basesLine.indexOf("RIGHT") + 6;
                    int sub_end = basesLine.indexOf("</TD>", sub_begin);
                    basesLine = basesLine.substring(sub_begin, sub_end);
                    basesLine = basesLine.substring(0, basesLine.indexOf(" "));
                    basesLine = basesLine.replaceAll(",", "");
                    msgAreaRef.setText("item bases in selection: " + basesLine);
                } else
                {
                    msgAreaRef.setText("did not get statistics (error or empty region selected)");
                }
            }
            catch(java.io.IOException ioe)
            {
                ioe.printStackTrace();
            }
        }

        private WebStatsThread(java.lang.String gen_id, java.lang.String chr_id, int pos_begin, int pos_end, javax.swing.JTextArea msgAreaRef)
        {
            this.gen_id = gen_id;
            this.chr_id = chr_id;
            this.pos_begin = pos_begin;
            this.pos_end = pos_end;
            this.msgAreaRef = msgAreaRef;
        }

    }


    public GenomeBrowser()
    {
    }

    public static void statistics(java.lang.String gen_id, java.lang.String chr_id, int pos_begin, int pos_end, javax.swing.JTextArea msgAreaRef)
    {
        (new WebStatsThread(gen_id, chr_id, pos_begin, pos_end, msgAreaRef)).start();
    }
}
