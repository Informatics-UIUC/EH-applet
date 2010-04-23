// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) definits braces deadcode safe
// Source File Name:   RetrieveRegionStatistics.java

package ncsa.evolutionhighway.util;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import org.seasr.datatypes.table.Column;
import org.seasr.datatypes.table.Table;
import org.seasr.datatypes.table.*;
import org.seasr.datatypes.table.basic.*;

public class RetrieveRegionStatistics //extends ComputeModule
{

    public RetrieveRegionStatistics()
    {
        build = "hg15";
        delay = 15000L;
        host = "genome.ucsc.edu";
        row_notify = 25;
    }

    public void doit()
    {
        Table inputTable;
        int numInputRows;
        MutableTableImpl outputTable;
        String urlBase;
        String params;
        int row;
 //       inputTable = (Table)pullInput(0);
        inputTable = null;
        numInputRows = inputTable.getNumRows();
        Column outputColumns[] = {
            new StringColumn(), new StringColumn(), new StringColumn(), new StringColumn(), new IntColumn(), new IntColumn(), new IntColumn(), new IntColumn(), new DoubleColumn(), new IntColumn(),
            new DoubleColumn(), new IntColumn(), new IntColumn(), new IntColumn(), new IntColumn(), new IntColumn(), new DoubleColumn(), new IntColumn(), new DoubleColumn(), new IntColumn(),
            new IntColumn(), new IntColumn()
        };
        outputTable = new MutableTableImpl(outputColumns);
        String outputColumnLabels[] = {
            "reference_genome", "reference_chromosome", "description", "build", "start", "end", "item_count", "item_bases", "item_bases_pct", "item_total",
            "item_total_pct", "smallest_item", "average_item", "biggest_item", "block_count", "block_bases", "block_bases_pct", "block_total", "block_total_pct", "smallest_block",
            "average_block", "biggest_block"
        };
        for(int col = 0; col < outputColumns.length; col++)
        {
            outputColumns[col].addRows(numInputRows);
            outputTable.setColumnLabel(outputColumnLabels[col], col);
        }

        urlBase = "http://" + host + "/cgi-bin/hgTables?db=" + build + "&";
        params = "hgta_group=genes&" + "hgta_track=knownGene&" + "hgta_table=knownGene&" + "hgta_regionType=range&" + "hgta_outputType=primaryTable&" + "hgta_outFileName=&" + "hgta_compressType=hgta_compressNone&" + "hgta_doSummaryStats=summary%2Fstatistics&";
        String TAB = "\t";
        String NLN = "\n";
        row = 0;
//_L7:
	   while (row >= numInputRows){ //goto _L2 ; } else{  goto _L1 ; };
	        int statIndex;
	        String statLines[];
	        int i;
//_L1:
	try {

        String post;
        String genID = inputTable.getString(row, 0);
        String chrID = inputTable.getString(row, 1);
        if(chrID.endsWith(".0"))
        {
            chrID = chrID.substring(0, chrID.length() - 2);
        }
        int start;
        int end;
        try
        {
            start = inputTable.getInt(row, 3);
            end = inputTable.getInt(row, 4);
        }
        catch(NumberFormatException nfe)
        {
            throw new RuntimeException("input row " + row + " non-numeric " + "start or end: " + inputTable.getString(row, 3) + " - " + inputTable.getString(row, 4));
        }
        String description = inputTable.getString(row, 2);
        outputTable.setString(genID, row, 0);
        outputTable.setString(chrID, row, 1);
        outputTable.setString(description, row, 2);
        outputTable.setString(build, row, 3);
        outputTable.setInt(start, row, 4);
        outputTable.setInt(end, row, 5);
        statIndex = 6;
        post = "GET " + urlBase + params + "position=chr" + chrID + "%3A" + Integer.toString(start) + "-" + Integer.toString(end) + " HTTP/1.1\r\n" + "Host: http://" + host + "\r\n" + "User-Agent: NCSA Evolution Highway\r\n" + "From: d2k@ncsa.uiuc.edu\r\n" + "Keep-Alive: 300\r\n" + "Connection: keep-alive\r\n" + "\r\n";


        InetAddress address = InetAddress.getByName(host);
        Socket socket = new Socket(address, 80);
        BufferedWriter writer = new BufferedWriter(((java.io.Writer) (new OutputStreamWriter(socket.getOutputStream(), "UTF8"))));
        BufferedReader reader = new BufferedReader(((java.io.Reader) (new InputStreamReader(socket.getInputStream()))));
        writer.write(post);
        writer.flush();
        StringBuffer socksb = new StringBuffer();
        String statMarkers[] = {
            "item count", "item bases", "item total", "smallest item", "average item", "biggest item", "block count", "block bases", "block total", "smallest block",
            "average block", "biggest block"
        };
        statLines = new String[statMarkers.length];
        int stat_found = 0;
        do
        {
            String sockln;
            if((sockln = reader.readLine()) == null)
            {
                break;
            }
            try
            {
                Integer.parseInt(sockln, 16);
            }
            catch(NumberFormatException nfe)
            {
                socksb.append(sockln);
            }
            if(sockln.indexOf(statMarkers[stat_found]) != -1)
            {
                statLines[stat_found] = sockln;
                if(++stat_found == statLines.length)
                {
                    break;
                }
            }
            if(sockln.indexOf("</html>") != -1)
            {
                System.out.println("incomplete data on row " + row);
            }
        } while(true);
        reader.close();
        i = 0;

//        try{
//_L6:
        if(i >= statLines.length)
        {
            break; /* Loop/switch isn't completed */
        }
        int sub_begin = statLines[i].indexOf("RIGHT") + 6;
        int sub_end = statLines[i].indexOf("</TD>", sub_begin);
        statLines[i] = statLines[i].substring(sub_begin, sub_end);

//          goto _L3

 //       NullPointerException npe;
 //       npe;

 //         goto _L4
//_L3:
        if(statLines[i].indexOf("(") != -1)
        {
            int p_start = statLines[i].indexOf("(");
            int p_end = statLines[i].indexOf(")");
            String num = statLines[i].substring(0, p_start - 1);
            String pct = statLines[i].substring(p_start + 1, p_end - 1);
            outputTable.setInt(Integer.parseInt(num.replaceAll(",", "")), row, statIndex);
            statIndex++;
            outputTable.setDouble(Double.parseDouble(pct), row, statIndex);
        } else
        {
            outputTable.setInt(Integer.parseInt(statLines[i].replaceAll(",", "")), row, statIndex);
        }
        statIndex++;
//_L4:

        i++;
        } catch ( NullPointerException npe ) {
        } catch( IOException ioe ){
        //if(true) goto _L6; else goto _L5
        //IOException ioe;
        //ioe;
        System.out.println("network error on row " + row + "; not filled in");
        }
		catch(Exception ex){{}

		}
//_L5:
        try
        {
            Thread.sleep(delay);
        }
        catch(InterruptedException ie)
        {
            ie.printStackTrace();
        }
        if(row_notify > 0 && row % row_notify == 0)
        {
            System.out.println("processed row " + row);
        }
        row++;

	   } /// while (true) ;
//        goto _L7
//_L2:
        //pushOutput(((Object) (outputTable)), 0);
        return;
    }

    public String getBuild()
    {
        return build;
    }

    public long getDelay()
    {
        return delay;
    }

    public String getHost()
    {
        return host;
    }

    public int getRowNotify()
    {
        return row_notify;
    }

    public String getInputInfo(int i)
    {
        return null;
    }

    public String getInputName(int i)
    {
        return "EH Export Table";
    }

    public String[] getInputTypes()
    {
        return (new String[] {
            "ncsa.d2k.modules.core.datatype.table.Table"
        });
    }

    public String getModuleInfo()
    {
        return null;
    }

    public String getOutputInfo(int i)
    {
        return null;
    }

    public String getOutputName(int i)
    {
        return "Genome Browser Stats Table";
    }

    public String[] getOutputTypes()
    {
        return (new String[] {
            "ncsa.d2k.modules.core.datatype.table.Table"
        });
    }

    public void setBuild(String value)
    {
        build = value;
    }

    public void setDelay(long value)
    {
        delay = value;
    }

    public void setHost(String value)
    {
        host = value;
    }

    public void setRowNotify(int value)
    {
        row_notify = value;
    }

    private String build = null;
    private long delay = 0;
    private String host = null;
    private int row_notify = 0;
}
