// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst
// Source File Name:   EvolutionHighwayVis.java
package ncsa.evolutionhighway.components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentNature;
import org.meandre.annotations.Component.Mode;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import org.meandre.webui.WebUIException;
import org.meandre.webui.WebUIFragmentCallback;
import org.seasr.datatypes.table.Table;
import org.seasr.meandre.support.io.JARInstaller;
import org.seasr.meandre.support.io.JARInstaller.InstallStatus;

/** This component is the visualizer for Evolution Highway.
 *
 * @author Loretta Auvil
 */

@Component(creator="Loretta Auvil",
        description="This module provides a visualizer for Evolution Highway.",
name="EvolutionHighwayVisualizer",
tags="evolution highway, chromosome, genome, visualization",
mode=Mode.webui,
baseURL="meandre://seasr.org/components/"
)

@ComponentNature(type="applet",
        extClass=ncsa.evolutionhighway.components.support.EvolutionHighwayApplet.class,
        dependency={"hgTables.jar","piccolo-1.2.1.jar", "piccolox-1.2.1.jar", "foundry-datatypes.jar", "foundry-support.jar", "foundry-commons.jar", "trove-2.0.3.jar", "eh_icons.jar"},
        resources={"hsadown.gif", "hsahash.gif", "hsaright.gif"}
)

public class EvolutionHighwayVisualizer implements ExecutableComponent, WebUIFragmentCallback {

	@ComponentInput(description="Chromosome Table is .",
			name= "Chromosome-Table")
			final static String DATA_INPUT_Chromosome = "Chromosome-Table";
	@ComponentInput(description="Consensus Table is .",
			name= "Consensus-Table")
			final static String DATA_INPUT_Consensus = "Consensus-Table";
	@ComponentInput(description="Ancestral Overlay Table is .",
			name= "Ancestral-Overlay-Table")
			final static String DATA_INPUT_Ancestral = "Ancestral-Overlay-Table";
	@ComponentInput(description="Centromere/Telomere Overlay Table is .",
			name= "Centromere-Telomere-Overlay-Table")
			final static String DATA_INPUT_CTomere = "Centromere-Telomere-Overlay-Table";
	@ComponentInput(description="Input Build Table is .",
			name= "Input-Build-Table")
			final static String DATA_INPUT_Build = "Input-Build-Table";

    /** The instance ID */
    private String sInstanceID = null;
    private String webUIUrl = null;

    private Table ancTable;
    private Table cenTable;
    private Table chrTable;
    private Table conTable;
    private Table buildTable;

    private List<String> genomeList;
    private List<String> featureList;
    private List<String> tableList;
    private Boolean _done;
    private PrintStream console;


     /** This method is invoked when the Meandre Flow is being prepared for
      * getting run.
      *
      * @param ccp The component context properties
      */
     public void initialize ( ComponentContextProperties ccp ) throws ComponentContextException {
         console = ccp.getOutputConsole();

         String sResDir = ccp.getPublicResourcesDirectory()+File.separator+"contexts"+File.separator+"java"+File.separator;
         String sTablesDir = ccp.getPublicResourcesDirectory() + File.separator + "EH" + File.separator;

         File depJar = new File(sResDir + "hgTables.jar");
         if (!depJar.exists())
             throw new ComponentContextException("Could not find dependency: " + depJar.toString());

         ccp.getOutputConsole().println("Installing dependencies from: " + depJar.toString());

         InstallStatus status;

         try {
             status = JARInstaller.installFromStream(new FileInputStream(depJar), sTablesDir, false);
         }
         catch (FileNotFoundException e) {
             throw new ComponentContextException(e);
         }

         if (status == InstallStatus.SKIPPED)
             ccp.getOutputConsole().println("Installation skipped - models already installed");

         if (status == InstallStatus.FAILED)
             throw new ComponentContextException("Failed to install dependencies at " + new File(sTablesDir).getAbsolutePath());
     }

     /** This component is the visualizer for Evolution Highway.
      *
      * @throws ComponentExecutionException If a fatal condition arises during
      *         the execution of a component, a ComponentExecutionException
      *         should be thrown to signal termination of execution required.
      * @throws ComponentContextException A violation of the component context
      *         access was detected
      */
     /** When ready for execution.
     *
     * @param cc The component context
     * @throws ComponentExecutionException An exception occurred during execution
     * @throws ComponentContextException Illigal access to context
     */
    public void execute(ComponentContext cc) throws ComponentExecutionException,
            ComponentContextException {

        ancTable = (Table)cc.getDataComponentFromInput(DATA_INPUT_Ancestral);
        cenTable = (Table)cc.getDataComponentFromInput(DATA_INPUT_CTomere);
        chrTable = (Table)cc.getDataComponentFromInput(DATA_INPUT_Chromosome);
        conTable = (Table)cc.getDataComponentFromInput(DATA_INPUT_Consensus);
        buildTable = (Table)cc.getDataComponentFromInput(DATA_INPUT_Build);

        //Setup for finding feature density data
        String resourceDir = cc.getPublicResourcesDirectory()+java.io.File.separator+"contexts"+java.io.File.separator+"java"+java.io.File.separator;
        System.out.println("publicresourcedir = "+resourceDir);
        File hglist;
        String glistFiles[];
        String featureFiles[];

        genomeList = new ArrayList<String>();
        featureList = new ArrayList<String>();
        tableList = new ArrayList<String>();
        hglist = new File (resourceDir+"hgTables");
        if (hglist != null){
        	System.out.println("hgtables = "+resourceDir+"hgTables");
        	System.out.println("getPath:"+hglist.getPath());
        	if (hglist.list() != null) {
        		glistFiles = hglist.list();
        		for (int i=0;i<glistFiles.length; i++){  //HSA
        			System.out.println("glistFiles "+ i + " "+glistFiles[i]);
        			File glist = new File (resourceDir+"hgTables"+java.io.File.separator+glistFiles[i]);
        			if (glist != null){
        				if (glist.list() != null) {
        					genomeList.add(glistFiles[i]);
        					featureFiles = glist.list();
        					for (int j=0;j<featureFiles.length; j++){
        						System.out.println("featureFiles "+ j + " "+featureFiles[j]);
        						featureList.add(featureFiles[j]);
        						File tablesDir = new File(resourceDir+"hgTables"+java.io.File.separator+glistFiles[i]+java.io.File.separator+featureFiles[j]);
        						if (tablesDir != null){
        							File tablesFiles[] = tablesDir.listFiles();
        							/*File tablesFiles[] = tablesDir.listFiles(new java.io.FilenameFilter() {
        								public boolean accept(File dir, java.lang.String name)
        								{
        									return name.startsWith("chr");
        								}
        							});*/
        							int startk = tableList.size();
                					for (int k=0;k<tablesFiles.length; k++){
        								tableList.add(cc.getWebUIUrl(true)+"public"+"/"+"resources/contexts/java/hgTables/"+glistFiles[i]+"/"+featureFiles[j]+"/"+tablesFiles[k].getName());
        								System.out.println("tableList "+ k+startk + " "+tableList.get(k+startk));
        							}
        						}
        						else
        							System.out.println("tablesdir is null");
        					}
        				}
        				else
        					System.out.println("featurelistdir is null");
        			}else
        				System.out.println("glist is null");
        		}
        	}
        }else
        	System.out.println("hglist is null");

        System.out.flush();

        sInstanceID = cc.getExecutionInstanceID();
        webUIUrl = cc.getWebUIUrl(true).toString();

        _done = false;

        cc.startWebUIFragment(this);

        while (!cc.isFlowAborting() && !_done)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        if (cc.isFlowAborting())
            console.println("Flow abort requested - terminating component execution...");
        /*
        if (!webUIUrl.endsWith("/")) webUIUrl += "/";
        try {
            sem.acquire();
            cc.startWebUIFragment(this);
            sem.acquire();
            sem.release();
        } catch (InterruptedException iex) {
            throw new ComponentExecutionException(iex);
        }
*/
        cc.stopWebUIFragment(this);

        System.out.flush();
    }

     /** This method is called when the Meandre Flow execution is completed.
      *
      * @param ccp The component context properties
      */
     public void dispose ( ComponentContextProperties ccp ) {
     }

     /** This method gets call when a request with no parameters is made to a
      * component WebUI fragment.
      *
      * @param response The response object
      * @throws WebUIException Some problem encountered during execution and something went wrong
      */
     public void emptyRequest(HttpServletResponse response) throws WebUIException {
         try {
             response.getWriter().println(getViz());
         } catch (IOException e) {
             throw new WebUIException(e);
         }
     }

     /** A simple message.
      *
      * @return The HTML containing the page
      */
     private String getViz() {
         StringBuffer sb = new StringBuffer();
         sb.append("<html> ");
         sb.append("<body> ");
         sb.append("<p ALIGN='center'> ");
         sb.append("<APPLET ");
         sb.append("ARCHIVE='ncsa.evolutionhighway.components.support.evolutionhighwayapplet.jar, piccolo-1.2.1.jar, piccolox-1.2.1.jar, foundry-datatypes.jar, foundry-support.jar, foundry-commons.jar, eh_icons.jar, trove-2.0.3.jar' WIDTH='95%' HEIGHT='95%' ");

         sb.append("CODEBASE='" + webUIUrl + "public/resources/contexts/java/' ");
         sb.append("CODE='ncsa.evolutionhighway.components.support.EvolutionHighwayApplet.class'> ");
         sb.append("<PARAM name='servletURL' value='" + webUIUrl).append(sInstanceID).append("'> ");
         sb.append("</APPLET> ");
         /*sb.append("</p> ");
         sb.append("<br/><br/> ");
         sb.append("<div align='center'> ");
         sb.append("<table align='center'><font size='2'> <a id='url' href='" + webUIUrl +
                   sInstanceID + "?done=true'>DONE</a></font></table> ");
         sb.append("</div> ");*/
         sb.append("</body> ");
         sb.append("</html> ");
         return sb.toString();
     }

     /** This method gets called when a call with parameters is done to a given component
      * webUI fragment
      *
      * @param target The target path
      * @param request The request object
      * @param response The response object
      * @throws WebUIException A problem occurred during the call back
      */
     public void handle(HttpServletRequest request, HttpServletResponse response) throws
             WebUIException {

         console.println("handle"+ response);
         String sDone = request.getParameter("done");
         String theApplet = request.getParameter("applet");

         if (sDone != null)
             _done=true;

		 else if (theApplet != null)
         //if (request.getParameterMap().isEmpty()) {
             //emptyRequest(response);
	         try {
	             ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());

	             out.writeObject(ancTable);
	             out.writeObject(cenTable);
	             out.writeObject(chrTable);
	             out.writeObject(conTable);
	             out.writeObject(buildTable);

	             out.writeObject(genomeList);
	             out.writeObject(featureList);
	             out.writeObject(tableList);
	             out.flush();
	             out.close();
	         } catch (Exception ex) {
	             throw new WebUIException(ex);
	         }

         else
             emptyRequest(response);
     }

}
