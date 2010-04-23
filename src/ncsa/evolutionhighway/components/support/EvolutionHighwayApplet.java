//Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
//Jad home page: http://www.kpdus.com/jad.html
//Decompiler options: fullnames fieldsfirst
//Source File Name:   EvolutionHighwayVis.java

package ncsa.evolutionhighway.components.support;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import java.applet.*;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import org.seasr.datatypes.table.Table;
//import ncsa.d2k.modules.core.datatype.table.Table;
//import ncsa.d2k.batch.BatchInterface;

import ncsa.evolutionhighway.util.GlobalSettings;
import ncsa.evolutionhighway.util.SizeChooser;
//import ncsa.evolutionhighway.components.EvolutionHighwayVis;
//import ncsa.evolutionhighway.components.EvolutionHighwayVis.EHView.CustomTrackInput;
import ncsa.evolutionhighway.components.support.EvolutionHighwayCanvas;
import ncsa.evolutionhighway.components.support.EvolutionHighwayControl;
import ncsa.evolutionhighway.datatype.AncestorRegion;
import ncsa.evolutionhighway.datatype.CTOverlay;
import ncsa.evolutionhighway.datatype.Chromosome;
import ncsa.evolutionhighway.datatype.CustomMark;
import ncsa.evolutionhighway.datatype.Genome;
import ncsa.evolutionhighway.datatype.HSB;

//Referenced classes of package ncsa.evolutionhighway:
//EvolutionHighwayCanvas, EvolutionHighwayControl

public class EvolutionHighwayApplet extends JApplet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Table ancTable;
	private Table cenTable;
	private Table chrTable;
	private Table conTable;
	private Table buildTable;

    private List genomeList;
    private List featureList;
    private List tableList;

	public static java.util.Map buildMap = new HashMap();
    public static java.util.Map buildURLMap = new HashMap();

	/**
	 * Executed each time the applet is loaded or reloaded.
	 */
	public void init() {
		//String location = "http://wireless-portal-52-197.ncsa.illinois.edu:1715/meandre://seasr.org/zigzag/1237394172265/7178441238048576351/flow/run-console-1237394180665-mau/instance/vis" + "?applet=true";
		String location = getParameter("servletURL") + "?applet=true";
		try {
			URL testServlet = new URL(location);
			URLConnection servletConnection = (URLConnection) testServlet.openConnection();
			//servletConnection.setRequestProperty("Content-Type","application/octet-stream");
			InputStream inputStreamFromServlet = servletConnection.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(
					inputStreamFromServlet);

			ancTable = (Table) ois.readObject();
			cenTable = (Table) ois.readObject();
			chrTable = (Table) ois.readObject();
			conTable = (Table) ois.readObject();
			buildTable = (Table) ois.readObject();

			genomeList = (List) ois.readObject();
			featureList = (List) ois.readObject();
			tableList = (List) ois.readObject();

			System.out.print("Ancestral Cols = "+ancTable.getNumColumns());
			System.out.println(" Rows = "+ancTable.getNumRows());
			System.out.print("CenTel Cols = "+cenTable.getNumColumns());
			System.out.println(" Rows = "+cenTable.getNumRows());
			System.out.print("Chromosome Cols = "+chrTable.getNumColumns());
			System.out.println(" Rows = "+chrTable.getNumRows());
			System.out.print("Consensus Cols = "+conTable.getNumColumns());
			System.out.println(" Rows = "+conTable.getNumRows());
			System.out.print("Build Cols = "+buildTable.getNumColumns());
			System.out.println(" Rows = "+buildTable.getNumRows());
			System.out.println("Num genomeList = "+genomeList.size());
			System.out.println("Num featureList = "+featureList.size());
			System.out.println("Num tableList = "+tableList.size());

			if (this.getCodeBase() != null){
				System.out.println("codebase = "+this.getCodeBase().toString());
			}

			System.out.flush();
			/*ClassLoader classloader = this.getAccessibleContext().getClass().getClassLoader();
			System.out.println("url1 = "+classloader.getResource("hgTables").toString());
			System.out.println("url2 = "+classloader.getResource("hgTables"+java.io.File.separator+"HSA").toString());
			System.out.println("url3 = "+classloader.getResource("hgTables"+java.io.File.separator+"HSA"+java.io.File.separator+"known_genes").toString());
			System.out.println("url4 = "+classloader.getResources("hgTables"+java.io.File.separator+"HSA"+java.io.File.separator+"*").toString());
			System.out.println("url4 = "+classloader.getResources("hgTables"+java.io.File.separator+"HSA"+java.io.File.separator+"known_genes"+java.io.File.separator+"*").toString());
			System.out.flush();*/

			System.out.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		/*ancTable = loaddata("published_resource/res/ancestral_HSA.data");
		buildTable = loaddata("published_resource/res/build.data");
		cenTable = loaddata("published_resource/res/cen_tel_HSA.data");
		chrTable = loaddata("published_resource/res/chromosome_HSA_MMU.data");
		conTable = loaddata("published_resource/res/consensus_HSA_MMU.data");*/

		EHView eh = new EHView();
		add(eh);
		//getContentPane().add(eh);
		//eh.settings.setAppletContext(this.getAppletContext());

		setJMenuBar((javax.swing.JMenuBar)eh.getMenu());
	}
/*
	public Table loaddata(String filename){
		Table result = null;
		try
		{
			java.io.File f = new File("res/Read Delimited.itn");
			ncsa.d2k.batch.BatchInterface batch = new BatchInterface(f, getClass().getClassLoader());
			ncsa.d2k.core.modules.Module collect = null;
			java.util.HashMap map = batch.getAgendaManager().getModules();
			java.util.Iterator iter = map.values().iterator();
			do
			{
				if(!iter.hasNext())
					break;
				java.lang.Object o = iter.next();
				if(o instanceof ncsa.d2k.modules.core.io.file.input.ParseFileToTable)
					collect = (ncsa.d2k.core.modules.Module)o;
			} while(true);
			batch.setModuleProperty("Input File Name", "fileName", filename);
			batch.markOutput(collect, 0);
			java.util.ArrayList al = batch.execute(new ArrayList());
			//java.util.ArrayList al = batch.execute(new ArrayList(), 1000);
			result = (Table)al.get(0);
			System.out.print("Cols = "+result.getNumColumns());
			System.out.println("Rows = "+result.getNumRows());
			System.out.flush();

        try
        {
        //    ncsa.d2k.modules.projects.evolutionhighway.util.IO.validateConsensusData(genomes, result);
         //   ncsa.d2k.modules.projects.evolutionhighway.util.IO.populateConsensusData(genomes, result);
         //   control.rebuild(genomes);
        }
        catch(java.lang.IllegalArgumentException iae)
        {
            javax.swing.JOptionPane.showMessageDialog(null, iae.getMessage(), "Error", 0);
        }
		}
		catch(java.lang.Exception exc)
		{
			exc.printStackTrace();
		}
		return (result);
	}*/

	/**
	 * Executed when the applet is loaded or revisited.
	 */
	public void start() {
	}

	/**
	 * Executed when the user leaves the applet's page.
	 */
	public void stop() {
	}

	/**
	 * Clean up the applet.
	 */
	public void destroy() {
	}

	private class EHView extends JPanel implements ActionListener
	{
		private class CustomTrackInput extends javax.swing.JPanel
		implements java.awt.event.ActionListener
		{

			javax.swing.JTextArea inputArea;
			javax.swing.JButton cancelButton;
			javax.swing.JButton doneButton;
			javax.swing.JButton pasteButton;
			javax.swing.JDialog dialog;
			java.awt.Insets insets_empty;
			java.awt.Insets insets_2;
			int window_status;

			public void actionPerformed(java.awt.event.ActionEvent e)
			{
				java.lang.Object src = e.getSource();
				if(src == doneButton)
				{
					window_status = 1;
					dialog.dispose();
				} else
					if(src == cancelButton)
					{
						window_status = -1;
						dialog.dispose();
					} else
						if(src == pasteButton)
							try
				{
								java.awt.datatransfer.Transferable t = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
								java.lang.String s = (java.lang.String)t.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
								s = s.replaceAll("\r", "\n");
								inputArea.setText(s);
				}
				catch(java.lang.Exception ex) { }
			}

			protected javax.swing.JDialog createDialog(java.awt.Component parent)
			throws java.awt.HeadlessException
			{
				//java.awt.Frame frame = (parent instanceof java.awt.Frame) ? (java.awt.Frame)parent : (java.awt.Frame)javax.swing.SwingUtilities.getAncestorOfClass(EvolutionHighwayVis.class$java$awt$Frame != null ? EvolutionHighwayVis.class$java$awt$Frame : (EvolutionHighwayVis.class$java$awt$Frame = EvolutionHighwayVis._mthclass$("java.awt.Frame")), parent);
				java.awt.Frame frame = new Frame("Input custom track data");
				java.lang.String title = "Input custom track data";
				getAccessibleContext().setAccessibleDescription(title);
				dialog = new javax.swing.JDialog(frame, title, true) {

					public java.awt.Dimension getMinimumSize()
					{
						return new Dimension(640, 480);
					}

					public java.awt.Dimension getPreferredSize()
					{
						return new Dimension(640, 480);
					}

					/*{
                    super(x0, x1, x2);
                }*/
				}
				;
				dialog.setResizable(false);
				java.awt.Container contentPane = dialog.getContentPane();
				contentPane.setLayout(new BorderLayout());
				contentPane.add(this, "Center");
				if(javax.swing.JDialog.isDefaultLookAndFeelDecorated())
				{
					boolean supportsWindowDecorations = javax.swing.UIManager.getLookAndFeel().getSupportsWindowDecorations();
					if(supportsWindowDecorations)
						dialog.getRootPane().setWindowDecorationStyle(6);
				}
				dialog.pack();
				dialog.setLocationRelativeTo(parent);
				return dialog;
			}

			java.lang.String getText()
			{
				return inputArea.getText();
			}

			public int showDialog(java.awt.Component parent)
			throws java.awt.HeadlessException
			{
				javax.swing.JDialog dialog = createDialog(parent);
				dialog.show();
				dialog.dispose();
				dialog = null;
				return window_status;
			}

			CustomTrackInput()
			{
				super();
				insets_empty = new Insets(0, 0, 0, 0);
				insets_2 = new Insets(2, 2, 2, 2);
				window_status = -1;
				inputArea = new JTextArea();
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				doneButton = new JButton("Done");
				doneButton.addActionListener(this);
				pasteButton = new JButton("Set from clipboard");
				pasteButton.addActionListener(this);
				java.awt.GridBagLayout layout = new GridBagLayout();
				setLayout(layout);
				javax.swing.JScrollPane inputScroll = new JScrollPane(inputArea);
				layout.addLayoutComponent(inputScroll, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 1, insets_2, 0, 0));
				add(inputScroll);
				javax.swing.JSeparator separator = new JSeparator();
				layout.addLayoutComponent(separator, new GridBagConstraints(0, 1, 1, 1, 1.0D, 0.0D, 10, 2, insets_empty, 0, 0));
				add(separator);
				javax.swing.JPanel buttonPanel = new JPanel();
				java.awt.GridBagLayout buttonLayout = new GridBagLayout();
				buttonPanel.setLayout(buttonLayout);
				buttonLayout.addLayoutComponent(pasteButton, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 10, 0, insets_empty, 0, 0));
				buttonPanel.add(pasteButton);
				javax.swing.JLabel buttonFiller = new JLabel();
				buttonLayout.addLayoutComponent(buttonFiller, new GridBagConstraints(2, 0, 1, 1, 1.0D, 0.0D, 10, 2, insets_empty, 0, 0));
				buttonPanel.add(buttonFiller);
				buttonLayout.addLayoutComponent(cancelButton, new GridBagConstraints(3, 0, 1, 1, 0.0D, 0.0D, 10, 0, insets_empty, 0, 0));
				buttonPanel.add(cancelButton);
				buttonLayout.addLayoutComponent(doneButton, new GridBagConstraints(4, 0, 1, 1, 0.0D, 0.0D, 10, 0, insets_empty, 0, 0));
				buttonPanel.add(doneButton);
				layout.addLayoutComponent(buttonPanel, new GridBagConstraints(0, 2, 1, 1, 1.0D, 0.0D, 10, 2, insets_empty, 0, 0));
				add(buttonPanel);
			}
		}

		private java.awt.Dimension size;
		private EvolutionHighwayCanvas canvas;
		private EvolutionHighwayControl control;
		private javax.swing.JTextArea msgArea;

		private GlobalSettings settings;
		private java.util.HashMap genomes;
		private java.awt.image.BufferedImage heterochromatinTexture;
		private javax.swing.JFileChooser fileChooser;
		private SizeChooser sizeChooser;
		private javax.swing.JMenuBar menuBar;
		private javax.swing.JMenuItem menu_loadChromosome;
		private javax.swing.JMenuItem menu_loadConsensus;
		private javax.swing.JMenuItem menu_loadAncestor;
		private javax.swing.JMenuItem menu_loadCenTel;
		private javax.swing.JMenuItem menu_saveAll;
		private javax.swing.JMenuItem menu_saveScreen;
		private javax.swing.JMenuItem menu_print;
		private javax.swing.JMenuItem menu_reset;
		private javax.swing.JMenuItem menu_customInput;
		private javax.swing.JMenuItem menu_helpNew;
		private javax.swing.JMenuItem menu_saveMatches;
		private javax.swing.JCheckBoxMenuItem menu_dispCen;
		private javax.swing.JCheckBoxMenuItem menu_dispTel;
		private javax.swing.JCheckBoxMenuItem menu_dispAnc;
		private javax.swing.JCheckBoxMenuItem menu_dispEdges;
		private javax.swing.JCheckBoxMenuItem menu_schemeCon;
		private javax.swing.JCheckBoxMenuItem menu_schemeDis1;
		private javax.swing.JCheckBoxMenuItem menu_schemeDis2;
		private javax.swing.JCheckBoxMenuItem menu_schemeDis3;
		private CustomTrackInput customInput;

		public void actionPerformed(java.awt.event.ActionEvent ae)
		{
			java.lang.Object src = ae.getSource();

			if(src == menu_loadChromosome)
			{
				int status = fileChooser.showOpenDialog(this);
				if(status == 0)
					try
				{
						File file = fileChooser.getSelectedFile();
						//System.out.println("Selected file: " + file.toString());
						//System.out.println("Trying to create a delimited file parser");

						DelimitedFileParser dfp = new DelimitedFileParser();
						dfp.initialize();
						DelimitedFileParserURL dfpUrl = dfp.execute(file);

						//System.out.println("Created the delimited file parser");

						FileToTable pftt = new FileToTable();
						pftt.initialize();

						Table result = pftt.execute(dfpUrl);

						//System.out.println("Got the table");
						try
						{
							validateGenomeData(result);
							populateGenomeData(genomes, result, heterochromatinTexture);
							control.rebuild(genomes);
						}
						catch(java.lang.IllegalArgumentException iae)
						{
							javax.swing.JOptionPane.showMessageDialog(null, iae.getMessage(), "Error", 0);
						}
				}
				catch(java.lang.Exception exc)
				{
					exc.printStackTrace();
				}
			} else
				if(src == menu_loadConsensus)
				{
					int status = fileChooser.showOpenDialog(this);
					if(status == 0)
						try
					{
						    File file = fileChooser.getSelectedFile();
	                        //System.out.println("Selected file: " + file.toString());
	                        //System.out.println("Trying to create a delimited file parser");

	                        DelimitedFileParser dfp = new DelimitedFileParser();
	                        dfp.initialize();
	                        DelimitedFileParserURL dfpUrl = dfp.execute(file);

	                        //System.out.println("Created the delimited file parser");

	                        FileToTable pftt = new FileToTable();
	                        pftt.initialize();

	                        Table result = pftt.execute(dfpUrl);
	                        //System.out.println("Got the table");

							try
							{
							    //System.out.println("Validating consensus data");
								validateConsensusData(genomes, result);
								//System.out.println("Populating consensus data");
								populateConsensusData(genomes, result);
								//System.out.println("Rebuilding viz");
								control.rebuild(genomes);
							}
							catch(java.lang.IllegalArgumentException iae)
							{
								javax.swing.JOptionPane.showMessageDialog(null, iae.getMessage(), "Error", 0);
							}
					}
					catch(java.lang.Exception exc)
					{
						exc.printStackTrace();
					}
				} else
					if(src == menu_loadAncestor)
					{
						int status = fileChooser.showOpenDialog(this);
						if(status == 0)
							try
						{
							    File file = fileChooser.getSelectedFile();
		                        //System.out.println("Selected file: " + file.toString());
		                        //System.out.println("Trying to create a delimited file parser");

		                        DelimitedFileParser dfp = new DelimitedFileParser();
		                        dfp.initialize();
		                        DelimitedFileParserURL dfpUrl = dfp.execute(file);

		                        //System.out.println("Created the delimited file parser");

		                        FileToTable pftt = new FileToTable();
		                        pftt.initialize();

		                        Table result = pftt.execute(dfpUrl);

		                        //System.out.println("Got the table");
								try
								{
									validateAncestorData(genomes, result);
									populateAncestorData(genomes, result);
									control.rebuild(genomes);
								}
								catch(java.lang.IllegalArgumentException iae)
								{
									javax.swing.JOptionPane.showMessageDialog(null, iae.getMessage(), "Error", 0);
								}
						}
						catch(java.lang.Exception exc)
						{
							exc.printStackTrace();
						}
					} else
						if(src == menu_loadCenTel)
						{
							int status = fileChooser.showOpenDialog(this);
							if(status == 0)
								try
							{
								    File file = fileChooser.getSelectedFile();
			                        //System.out.println("Selected file: " + file.toString());
			                        //System.out.println("Trying to create a delimited file parser");

			                        DelimitedFileParser dfp = new DelimitedFileParser();
			                        dfp.initialize();
			                        DelimitedFileParserURL dfpUrl = dfp.execute(file);

			                        //System.out.println("Created the delimited file parser");

			                        FileToTable pftt = new FileToTable();
			                        pftt.initialize();

			                        Table result = pftt.execute(dfpUrl);

			                        //System.out.println("Got the table");
									try
									{
										validateCentromereAndTelomereData(genomes, result);
										populateCentromereAndTelomereData(genomes, result);
										control.rebuild(genomes);
									}
									catch(java.lang.IllegalArgumentException iae)
									{
										javax.swing.JOptionPane.showMessageDialog(null, iae.getMessage(), "Error", 0);
									}
							}
							catch(java.lang.Exception exc)
							{
								exc.printStackTrace();
							}
						} else
							if(src == menu_saveScreen)
							{
								int status = fileChooser.showSaveDialog(this);
								if(status == 0)
									canvas.saveScreen(fileChooser.getSelectedFile());
							} else
								if(src == menu_saveAll)
								{
									if(sizeChooser == null)
										sizeChooser = new SizeChooser();
									int ret = sizeChooser.showDialog(this);
									int w;
									int h;
									if(ret > 0)
									{
										w = sizeChooser.getChosenWidth();
										h = sizeChooser.getChosenHeight();
									} else
									{
										return;
									}
									ret = fileChooser.showSaveDialog(this);
									if(ret == 0)
										canvas.saveCanvas(fileChooser.getSelectedFile(), w, h);
								} else
									if(src == menu_saveMatches)
									{
										int status = fileChooser.showSaveDialog(this);
										if(status == 0)
											try
										{
												control.saveCompareMatches(fileChooser.getSelectedFile());
										}
										catch(java.lang.Exception e)
										{
											msgArea.setText(e.getMessage());
										}
									} else
										if(src == menu_print)
											canvas.print();
										else
											if(src == menu_reset)
												canvas.resetView();
											else
												if(src == menu_dispCen)
												{
													settings.setShowCentromeres(!settings.getShowCentromeres());
													control.rebuild(new HashMap());
												} else
													if(src == menu_dispTel)
													{
														settings.setShowTelomeres(!settings.getShowTelomeres());
														control.rebuild(new HashMap());
													} else
														if(src == menu_dispAnc)
														{
															settings.setShowAncestorsInside(!settings.getShowAncestorsInside());
															control.rebuild(new HashMap());
														} else
															if(src == menu_schemeCon)
															{
																settings.setAncestorColorMode(100);
																control.rebuild(new HashMap());
																menu_schemeCon.setSelected(true);
																menu_schemeDis1.setSelected(false);
																menu_schemeDis2.setSelected(false);
																menu_schemeDis3.setSelected(false);
															} else
																if(src == menu_schemeDis1)
																{
																	settings.setAncestorColorMode(101);
																	control.rebuild(new HashMap());
																	menu_schemeCon.setSelected(false);
																	menu_schemeDis1.setSelected(true);
																	menu_schemeDis2.setSelected(false);
																	menu_schemeDis3.setSelected(false);
																} else
																	if(src == menu_schemeDis2)
																	{
																		settings.setAncestorColorMode(102);
																		control.rebuild(new HashMap());
																		menu_schemeCon.setSelected(false);
																		menu_schemeDis1.setSelected(false);
																		menu_schemeDis2.setSelected(true);
																		menu_schemeDis3.setSelected(false);
																	} else
																		if(src == menu_schemeDis3)
																		{
																			settings.setAncestorColorMode(103);
																			control.rebuild(new HashMap());
																			menu_schemeCon.setSelected(false);
																			menu_schemeDis1.setSelected(false);
																			menu_schemeDis2.setSelected(false);
																			menu_schemeDis3.setSelected(true);
																		} else
																			if(src == menu_dispEdges)
																			{
																				for(java.util.Iterator giter = canvas.getGenomeIterator(); giter.hasNext();)
																				{
																					Genome g = (Genome)giter.next();
																					java.util.Iterator citer = g.getChromosomeIterator();
																					while(citer.hasNext())
																						((Chromosome)citer.next()).setDrawEdgesOnMatches(menu_dispEdges.isSelected());
																				}

																			} else
																				if(src == menu_customInput)
																				{
																					if(customInput == null)
																						customInput = new CustomTrackInput();
																					int ret = customInput.showDialog(this);
																					if(ret > 0)
																						setCustomTrack(customInput.getText());
																				} else
																					if(src == menu_helpNew)
																					{
																						javax.swing.JEditorPane ep = new JEditorPane();
																						ep.setContentType("text/html");
																						ep.setEditable(false);
																						java.lang.StringBuffer sb = new StringBuffer();
																						sb.append("<html><body>");
																						sb.append("<p>");
																						sb.append("This is a prerelease demonstration of features that ");
																						sb.append("may appear in some form in future versions of ");
																						sb.append("Evolution Highway. Please do not distribute it.");
																						sb.append("<br><br>");
																						sb.append("Questions, suggestions, etc. to: lauvil@illinois.edu");
																						sb.append("<h3>Genome Browser integration</h3>");
																						sb.append("There are now a couple of different ways to launch ");
																						sb.append("the UCSC Genome Browser. A single left-click inside ");
																						sb.append("either a homologous synteny block or a breakpoint ");
																						sb.append("region will open the Genome Browser set to that region ");
																						sb.append("of either the human or mouse chromosome. (This should ");
																						sb.append("launch in a new web browser on your desktop; please let ");
																						sb.append("me know if it doesn't work as you expect.) Clicking and ");
																						sb.append("dragging any region with the middle mouse button should ");
																						sb.append("allow any region to be opened in the Genome Browser. ");
																						sb.append("If you just want to see the coordinate of any position, ");
																						sb.append("a single right-click inside the chromosome will show this ");
																						sb.append("information in the text message window at the bottom right.");
																						sb.append("<h3>Conserved Synteny and Breakpoint Classification</h3>");
																						sb.append("These features are now multiple-selection lists in the ");
																						sb.append("left-hand control panel.");
																						sb.append("</p>");
																						sb.append("</body></html>");
																						ep.setText(sb.toString());
																						ep.setCaretPosition(0);
																						javax.swing.JScrollPane epScroll = new javax.swing.JScrollPane(ep) {

																							java.awt.Dimension size;

																							public java.awt.Dimension getMinimumSize()
																							{
																								return size;
																							}

																							public java.awt.Dimension getPreferredSize()
																							{
																								return size;
																							}


																							{
																								//-la super(x0);
																								size = new Dimension(400, 400);
																							}
																						}
																						;
																						javax.swing.JFrame frame = new JFrame();
																						frame.setDefaultCloseOperation(2);
																						frame.getContentPane().add(epScroll);
																						frame.setSize(400, 400);
																						frame.pack();
																						frame.show();
																					}

		}

		private void createMenuBar()
		{
			menuBar = new JMenuBar();
			javax.swing.JMenu fileMenu = new JMenu("File");
			menuBar.add(fileMenu);
			javax.swing.JMenu fileLoad = new JMenu("Load New");
			fileMenu.add(fileLoad);
			menu_loadChromosome = new JMenuItem("Chromosome data");
			menu_loadChromosome.addActionListener(this);
			fileLoad.add(menu_loadChromosome);
			menu_loadConsensus = new JMenuItem("Consensus data");
			menu_loadConsensus.addActionListener(this);
			fileLoad.add(menu_loadConsensus);
			menu_loadAncestor = new JMenuItem("Ancestor data");
			menu_loadAncestor.addActionListener(this);
			fileLoad.add(menu_loadAncestor);
			menu_loadCenTel = new JMenuItem("Centromere/Telomere data");
			menu_loadCenTel.addActionListener(this);
			fileLoad.add(menu_loadCenTel);
			menu_saveScreen = new JMenuItem("Save Screen (PNG)...");
			menu_saveScreen.addActionListener(this);
			fileMenu.add(menu_saveScreen);
			menu_saveAll = new JMenuItem("Save All (PNG)...");
			menu_saveAll.addActionListener(this);
			fileMenu.add(menu_saveAll);
			menu_saveMatches = new JMenuItem("Save synteny/breakpoint matches");
			menu_saveMatches.addActionListener(this);
			fileMenu.add(menu_saveMatches);
			menu_print = new JMenuItem("Print...");
			menu_print.addActionListener(this);
			fileMenu.add(menu_print);
			javax.swing.JMenu viewMenu = new JMenu("View");
			menuBar.add(viewMenu);
			menu_reset = new JMenuItem("Reset View");
			menu_reset.addActionListener(this);
			viewMenu.add(menu_reset);
			viewMenu.add(new JSeparator());
			javax.swing.JMenu displayMenu = new JMenu("Display");
			menu_dispCen = new JCheckBoxMenuItem("Centromeres", settings.getShowCentromeres());
			menu_dispCen.addActionListener(this);
			displayMenu.add(menu_dispCen);
			menu_dispTel = new JCheckBoxMenuItem("Telomeres", settings.getShowTelomeres());
			menu_dispTel.addActionListener(this);
			displayMenu.add(menu_dispTel);
			menu_dispAnc = new JCheckBoxMenuItem("Ancestral segments inside chromosomes", settings.getShowAncestorsInside());
			menu_dispAnc.addActionListener(this);
			displayMenu.add(menu_dispAnc);
			viewMenu.add(displayMenu);
			menu_dispEdges = new JCheckBoxMenuItem("Edges on synteny/breakpoint matches", false);
			menu_dispEdges.addActionListener(this);
			displayMenu.add(menu_dispEdges);
			viewMenu.add(new JSeparator());
			javax.swing.JMenu ancestorOverlayMenu = new JMenu("Ancestor Overlay Mode");
			menu_schemeCon = new JCheckBoxMenuItem("Consistent", settings.getAncestorColorMode() == 100);
			menu_schemeCon.addActionListener(this);
			ancestorOverlayMenu.add(menu_schemeCon);
			menu_schemeDis1 = new JCheckBoxMenuItem("Distinct #1", settings.getAncestorColorMode() == 101);
			menu_schemeDis1.addActionListener(this);
			ancestorOverlayMenu.add(menu_schemeDis1);
			menu_schemeDis2 = new JCheckBoxMenuItem("Distinct #2", settings.getAncestorColorMode() == 102);
			menu_schemeDis2.addActionListener(this);
			ancestorOverlayMenu.add(menu_schemeDis2);
			menu_schemeDis3 = new JCheckBoxMenuItem("Distinct #3", settings.getAncestorColorMode() == 103);
			menu_schemeDis3.addActionListener(this);
			ancestorOverlayMenu.add(menu_schemeDis3);
			viewMenu.add(ancestorOverlayMenu);
			javax.swing.JMenu customMenu = new JMenu("Custom Track");
			menuBar.add(customMenu);
			menu_customInput = new JMenuItem("Input...");
			menu_customInput.addActionListener(this);
			customMenu.add(menu_customInput);
			javax.swing.JMenu helpMenu = new JMenu("Help");
			menuBar.add(helpMenu);
			menu_helpNew = new JMenuItem("New Features");
			menu_helpNew.addActionListener(this);
			helpMenu.add(menu_helpNew);
			try
			{
				fileChooser = new JFileChooser();
			}
			catch(java.security.AccessControlException ace)
			{
			    System.err.println("EH: Could not create a file chooser!!!!");
				fileLoad.setEnabled(false);
				menu_saveScreen.setEnabled(false);
				menu_saveAll.setEnabled(false);
			}
		}

		public java.awt.Dimension getMinimumSize()
		{
			return size;
		}

		public java.lang.Object getMenu()
		{
			if(menuBar == null)
				createMenuBar();
			return menuBar;
		}

		public java.awt.Dimension getPreferredSize()
		{
			return size;
		}

		public void setCustomTrack(java.lang.String cts)
		{
			for(java.util.Iterator geniter = genomes.values().iterator(); geniter.hasNext();)
			{
				Genome genome = (Genome)geniter.next();
				java.util.Iterator chriter = genome.getChromosomeIterator();
				while(chriter.hasNext())
				{
					Chromosome chr = (Chromosome)chriter.next();
					chr.clearCustomTrackNodes();
				}
			}

			java.lang.String lines[] = cts.split("\n");
			for(int i = 0; i < lines.length; i++)
			{
				java.lang.String data[] = lines[i].split("\t");
				if(data.length < 5)
					continue;
				java.lang.String refgen = data[0];
				java.lang.String refchr = data[1];
				Genome gen = (Genome)genomes.get(refgen);
				if(gen == null)
					continue;
				Chromosome chr = gen.getChromosome(refchr);
				if(chr == null)
					continue;
				java.lang.String tag = data[2];
				double start;
				double end;
				try
				{
					start = java.lang.Double.parseDouble(data[3]);
					end = java.lang.Double.parseDouble(data[4]);
				}
				catch(java.lang.NumberFormatException nfe)
				{
					continue;
				}
				java.awt.Color c;
				if(data.length < 6)
				{
					c = java.awt.Color.BLACK;
				} else
				{
					java.lang.String cs = data[5];
					if(cs.equalsIgnoreCase("black"))
						c = java.awt.Color.BLACK;
					else
						if(cs.equalsIgnoreCase("red"))
							c = java.awt.Color.RED;
						else
							if(cs.equalsIgnoreCase("green"))
								c = java.awt.Color.GREEN;
							else
								if(cs.equalsIgnoreCase("blue"))
									c = java.awt.Color.BLUE;
								else
									if(cs.equalsIgnoreCase("gray"))
										c = java.awt.Color.GRAY;
									else
										if(cs.equalsIgnoreCase("darkgray"))
											c = java.awt.Color.DARK_GRAY;
										else
											try
					{
												c = java.awt.Color.decode(cs);
					}
					catch(java.lang.NumberFormatException nfe)
					{
						c = java.awt.Color.BLACK;
					}
				}
				CustomMark ctn = new CustomMark(tag, 20D, 3.9999999999999998E-006D, c);
				ctn.setBounds(0.0D, 3.9999999999999998E-006D * start, 24D, 3.9999999999999998E-006D * (end - start));
				chr.addCustomMark(ctn);
			}
		}

		private EHView()
		{
			super();
			size = super.getSize();
			//size = new Dimension(760, 480);
			removeAll();
			buildMap.clear();
		    buildURLMap.clear();
			for(int row = 0; row < buildTable.getNumRows(); row++)
			{
				if(!buildTable.isValueMissing(row, 1))
					buildMap.put(buildTable.getString(row, 0), buildTable.getString(row, 1));
				if(!buildTable.isValueMissing(row, 2))
					buildURLMap.put(buildTable.getString(row, 0), buildTable.getString(row, 2));
			}

			java.lang.ClassLoader classLoader = getClass().getClassLoader();
			java.net.URL hashURL = classLoader.getResource("images/hsahash.gif");

			try
			{
				heterochromatinTexture = javax.imageio.ImageIO.read(hashURL);
			}
			catch(java.io.IOException ioe)
			{
				throw new RuntimeException(ioe);
			}
			msgArea = new JTextArea();
			msgArea.setEditable(false);
			msgArea.setBackground(java.awt.Color.LIGHT_GRAY);
			javax.swing.JScrollPane msgScroll = new javax.swing.JScrollPane(msgArea) {

				java.awt.Dimension size;

				public java.awt.Dimension getMinimumSize()
				{
					return size;
				}

				public java.awt.Dimension getPreferredSize()
				{
					return size;
				}

				{
					// -la super(x0);
					size = new Dimension(40, 40);
				}
			}
			;
			settings = new GlobalSettings(msgArea);
			settings.setCodeBase(getCodeBase());
			settings.setAppletContext(getAppletContext());
			settings.setgenomeList(genomeList);
			settings.setfeatureList(featureList);
			settings.settableList(tableList);
			genomes = new HashMap();
			populateGenomeData(genomes, chrTable, heterochromatinTexture);
			populateConsensusData(genomes, conTable);
			populateAncestorData(genomes, ancTable);
			populateCentromereAndTelomereData(genomes, cenTable);
			canvas = new EvolutionHighwayCanvas(genomes);
			control = new EvolutionHighwayControl(genomes, canvas, msgArea, settings, classLoader);
			javax.swing.JScrollPane controlScroll = new javax.swing.JScrollPane(control) {

				java.awt.Dimension size;

				public java.awt.Dimension getMinimumSize()
				{
					return size;
				}

				public java.awt.Dimension getPreferredSize()
				{
					return size;
				}

				{
					// -la super(x0);
					size = new Dimension(220, 200);
				}
			}
			;
			java.awt.GridBagLayout layout = new GridBagLayout();
			setLayout(layout);
			layout.addLayoutComponent(controlScroll, new GridBagConstraints(0, 0, 1, 2, 0.0D, 1.0D, 10, 3, new Insets(0, 0, 0, 0), 0, 0));
			add(controlScroll);
			layout.addLayoutComponent(canvas, new GridBagConstraints(1, 0, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
			add(canvas);
			layout.addLayoutComponent(msgScroll, new GridBagConstraints(1, 1, 1, 1, 1.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
			add(msgScroll);
		}
	}

	private static final java.lang.String _CEN_STR = "centromere";
    private static final java.lang.String _HET_STR = "heterochromatin";
    private static final java.lang.String _P_STR = "p";
    private static final java.lang.String _Q_STR = "q";
    private static final java.lang.String _SAT_ALT = "satelite_dna";
    private static final java.lang.String _SAT_STR = "satellite_dna";
    private static final java.lang.String _TEL_STR = "telomere";

    public static void populateAncestorData(java.util.Map map, Table ancTable)
    {
        int _anc = 0;
        int _gen = 1;
        int _chr = 2;
        int _ach = 3;
        int _sbp = 4;
        int _ebp = 5;
        //if(!ancTable.isColumnNominal(0) || !ancTable.isColumnNominal(1) || !ancTable.isColumnNominal(2) || !ancTable.isColumnNominal(3) || !ancTable.isColumnNumeric(4) || !ancTable.isColumnNumeric(5))
        //    throw new IllegalArgumentException("The loaded table does not appear to have the correct structure for ancestor data.");
        int numRows = ancTable.getNumRows();
        for(int row = 0; row < numRows; row++)
        {
            java.lang.String genomeID = ancTable.getString(row, 1);
            ncsa.evolutionhighway.datatype.Genome genome = (ncsa.evolutionhighway.datatype.Genome)map.get(genomeID);
            if(genome == null)
                throw new IllegalArgumentException("row " + row + ": unknown reference genome " + genomeID);
            java.lang.String chromosomeID = ancTable.getString(row, 2);
            ncsa.evolutionhighway.datatype.Chromosome chromosome = genome.getChromosome(chromosomeID);
            if(chromosome == null)
                throw new IllegalArgumentException("row " + row + ": unknown (reference genome, reference " + "chromosome) pair: (" + genomeID + ", " + chromosomeID + ")");
            java.lang.String ancestorID = ancTable.getString(row, 0);
            java.lang.String ancestorChromosome = ancTable.getString(row, 3);
            int start = ancTable.getInt(row, 4);
            int end = ancTable.getInt(row, 5);
            ncsa.evolutionhighway.datatype.AncestorRegion ar = new AncestorRegion(ancestorID, ancestorChromosome, start, end);
            chromosome.addAncestorOverlay(ar);
            genome.addAncestorID(ancestorID);
        }
    }

    public static void populateCentromereAndTelomereData(java.util.Map map, Table ctTable)
    {
        int _gen = 0;
        int _chr = 1;
        int _sbp = 2;
        int _ebp = 3;
        int _sid = 4;
        int _sch = 5;
        int _cen = 6;
        int _tel = 7;

        String missingValueString = "?";
        //if(!ctTable.isColumnNominal(0) || !ctTable.isColumnNominal(1) || !ctTable.isColumnNumeric(2) || !ctTable.isColumnNumeric(3) || !ctTable.isColumnNominal(4) || !ctTable.isColumnNominal(5) || !ctTable.isColumnNominal(6) || !ctTable.isColumnNominal(7))
        //    throw new IllegalArgumentException("The loaded table does not appear to have the correct structure for centromere/telomere data.");
        java.lang.String _acro_str = "Acrocentric";
        java.lang.String _cent_str = "CENTROMERE";
        java.lang.String _cen_p_str = "CEN p-arm";
        java.lang.String _cen_q_str = "CEN q-arm";
        java.lang.String _tel_p_str = "Telomere p-arm";
        java.lang.String _tel_q_str = "Telomere q-arm";
        int numRows = ctTable.getNumRows();
        boolean hasThrownMismatchException = false;
        for(int row = 0; row < numRows; row++)
        {
            java.lang.String genomeID = ctTable.getString(row, 0);
            System.out.println("popCentroTelo: Looking for genome id: " + genomeID + " on row " + row);

            ncsa.evolutionhighway.datatype.Genome genome = (ncsa.evolutionhighway.datatype.Genome)map.get(genomeID);
            if(genome == null)
                throw new IllegalArgumentException("row " + row + ": unknown reference genome " + genomeID);
            java.lang.String chromosomeID = ctTable.getString(row, 1);
            System.out.println("popCentroTelo: Looking for chromosome id: " + chromosomeID + " on row " + row);

            ncsa.evolutionhighway.datatype.Chromosome chromosome = genome.getChromosome(chromosomeID);
            if(chromosome == null)
                throw new IllegalArgumentException("row " + row + ": unknown (reference genome, reference " + "chromosome) pair: (" + genomeID + ", " + chromosomeID + ")");
            java.lang.String speciesID = ctTable.getString(row, 4);
            System.out.println("popCentroTelo: Got species id: " + speciesID);
            int type;
            if(!ctTable.isValueMissing(row, 6) && !ctTable.getString(row, 6).equalsIgnoreCase(missingValueString))
            {
                java.lang.String typestr = ctTable.getString(row, 6);
                if(typestr.equalsIgnoreCase("Acrocentric"))
                    type = 101;
                else
                if(typestr.equalsIgnoreCase("CENTROMERE"))
                    type = 102;
                else
                if(typestr.equalsIgnoreCase("CEN p-arm"))
                    type = 103;
                else
                if(typestr.equalsIgnoreCase("CEN q-arm"))
                    type = 104;
                else
                    throw new IllegalArgumentException("row " + row + ": unknown centromere type: " + typestr);
            } else
            if(!ctTable.isValueMissing(row, 7) && !ctTable.getString(row, 7).equalsIgnoreCase(missingValueString))
            {
                java.lang.String typestr = ctTable.getString(row, 7);
                if(typestr.equalsIgnoreCase("Telomere p-arm"))
                    type = 105;
                else
                if(typestr.equalsIgnoreCase("Telomere q-arm"))
                    type = 106;
                else
                    throw new IllegalArgumentException("row " + row + ": unknown telomere type: " + typestr);
            } else
            {
                throw new IllegalArgumentException("row " + row + " contains both centromere and telomere identifiers");
            }
            int start = ctTable.getInt(row, 2);
            int end = ctTable.getInt(row, 3);
            java.lang.String speciesChromosome = ctTable.getString(row, 5);
            ncsa.evolutionhighway.datatype.CTOverlay cto = new CTOverlay(type, speciesID, speciesChromosome, start, end);
            try
            {
                chromosome.addCTOverlay(cto);
                continue;
            }
            catch(java.lang.IllegalArgumentException iae)
            {
                if(iae.getMessage().indexOf("no segment") == -1)
                    throw new IllegalArgumentException("row " + row + ": " + iae.getMessage());
            }
            if(!hasThrownMismatchException)
            {
                hasThrownMismatchException = true;
                javax.swing.JOptionPane.showMessageDialog(null, "row " + row + ": " + "this centromere or telomere (and possibly others) " + "cannot be exactly matched to a homologous synteny " + "block. These will be ignored.", "Error", 0);
            }
        }
    }

    public static void populateConsensusData(java.util.Map map, Table conTable)
    {
        int _gen = 0;
        int _chr = 1;
        int _sbp = 2;
        int _ebp = 3;
        /*
        int _sch = 8;
        int _msi = 4;
        int _mei = 5;
        int _sgn = 6;
        int _spc = 7;
        */
        int _sch = 4;
        int _msi = 5;
        int _mei = 6;
        int _sgn = 7;
        int _spc = 8;
        int _seg = 9;
        int _com = 10;
        //if(!conTable.isColumnNominal(0) || !conTable.isColumnNominal(1) || !conTable.isColumnNumeric(2) || !conTable.isColumnNumeric(3) || !conTable.isColumnNominal(4) || !conTable.isColumnNumeric(5) || !conTable.isColumnNumeric(6) || !conTable.isColumnNumeric(7) || !conTable.isColumnNominal(8) || !conTable.isColumnNumeric(9) || !conTable.isColumnNominal(10))
        //    throw new IllegalArgumentException("The loaded table does not appear to have the correct structure for consensus data.");
        int numRows = conTable.getNumRows();
        for(int row = 0; row < numRows; row++)
        {
            java.lang.String genomeID = conTable.getString(row, 0);
            ncsa.evolutionhighway.datatype.Genome genome = (ncsa.evolutionhighway.datatype.Genome)map.get(genomeID);
            if(genome == null)
                throw new IllegalArgumentException("row " + row + ": unknown reference genome " + genomeID);
            java.lang.String chromosomeID = conTable.getString(row, 1);
            ncsa.evolutionhighway.datatype.Chromosome chromosome = genome.getChromosome(chromosomeID);
            if(chromosome == null)
                throw new IllegalArgumentException("row " + row + ": unknown (reference genome, reference " + "chromosome) pair: (" + genomeID + ", " + chromosomeID + ")");
            int start = conTable.getInt(row, 2);
            int end = conTable.getInt(row, 3);
            java.lang.String speciesChr = conTable.getString(row, 8);
            int modified_order_start = conTable.getInt(row, 4);
            int modified_order_end = conTable.getInt(row, 5);
            int sign = conTable.getInt(row, 6);
            java.lang.String speciesID = conTable.getString(row, 7);
            int segment_num = conTable.getInt(row, 9);
            java.lang.String comments;
            if(conTable.isValueMissing(row, 10))
                comments = null;
            else
                comments = conTable.getString(row, 10);
            ncsa.evolutionhighway.datatype.HSB hsb = new HSB(genomeID, chromosomeID, start, end, speciesChr, modified_order_start, modified_order_end, sign, speciesID, segment_num, comments);
            chromosome.addHSB(hsb);
            genome.addComparativeSpeciesID(speciesID);
        }
    }

    public static void populateGenomeData(java.util.Map map, Table chrTable, java.awt.image.BufferedImage heterochromatinTexture)
    {
        int _gen = 0;
        int _chr = 1;
        int _sbp = 2;
        int _ebp = 3;
        int _com = 4;
        int _arm = 5;
        //if(!chrTable.isColumnNominal(0) || !chrTable.isColumnNominal(1) || !chrTable.isColumnNumeric(2) || !chrTable.isColumnNumeric(3) || !chrTable.isColumnNominal(4) || !chrTable.isColumnNominal(5))
        //    throw new IllegalArgumentException("The loaded table does not appear to have the correct structure for chromosome data.");
        int numRows = chrTable.getNumRows();
        for(int row = 0; row < numRows; row++)
        {
            java.lang.String genomeID = chrTable.getString(row, 0);
            ncsa.evolutionhighway.datatype.Genome genome = (ncsa.evolutionhighway.datatype.Genome)map.get(genomeID);
            if(genome == null)
            {
                genome = new Genome(genomeID);
                map.put(genomeID, genome);
            }
            java.lang.String chromosomeID = chrTable.getString(row, 1);
            ncsa.evolutionhighway.datatype.Chromosome chromosome = genome.getChromosome(chromosomeID);
            if(chromosome == null)
            {
                chromosome = new Chromosome(genomeID, chromosomeID, heterochromatinTexture);
                genome.addChromosome(chromosome);
            }
            java.lang.String comment = chrTable.getString(row, 4);
            if(comment.equalsIgnoreCase("telomere"))
            {
                int length = chrTable.getInt(row, 3);
                chromosome.setLength(length);
                continue;
            }
            if(comment.equalsIgnoreCase("centromere"))
            {
                int mark = chrTable.getInt(row, 2);
                chromosome.setCentromereMark(mark);
                continue;
            }
            java.lang.String arm;
            if(comment.equalsIgnoreCase("heterochromatin"))
            {
                arm = chrTable.getString(row, 5);
                if(arm.equalsIgnoreCase("p"))
                {
                    int mark = chrTable.getInt(row, 2);
                    chromosome.setPHeterochromatinMark(mark);
                    continue;
                }
                if(arm.equalsIgnoreCase("q"))
                {
                    int mark = chrTable.getInt(row, 3);
                    chromosome.setQHeterochromatinMark(mark);
                } else
                {
                    throw new IllegalArgumentException("row " + row + ": invalid chromosome arm identifier for " + "heterochromatin: " + arm);
                }
                continue;
            }
            if(!comment.equalsIgnoreCase("satellite_dna") && !comment.equalsIgnoreCase("satelite_dna"))
                continue;
            arm = chrTable.getString(row, 5);
            if(arm.equalsIgnoreCase("p"))
            {
                int mark = chrTable.getInt(row, 2);
                chromosome.setPSatelliteMark(mark);
                continue;
            }
            if(arm.equalsIgnoreCase("q"))
            {
                int mark = chrTable.getInt(row, 3);
                chromosome.setQSatelliteMark(mark);
            } else
            {
                throw new IllegalArgumentException("row " + row + ": invalid chromosome arm identifier for " + "satellite DNA: " + arm);
            }
        }
    }

    public static void validateAncestorData(java.util.Map map, Table ancTable)
    {
        int _gen = 1;
        int numRows = ancTable.getNumRows();
        for(int row = 0; row < numRows; row++)
        {
            java.lang.String genomeID = ancTable.getString(row, 1);
            ncsa.evolutionhighway.datatype.Genome genome = (ncsa.evolutionhighway.datatype.Genome)map.get(genomeID);
            if(genome == null)
                throw new IllegalArgumentException("row " + row + ": unknown reference genome " + genomeID);
            if(genomeID.equalsIgnoreCase("HSA") || genomeID.equalsIgnoreCase("MMU"))
                throw new IllegalArgumentException("row " + row + ": genome " + genomeID + " already exists in Evolution Highway and " + "cannot be modified.");
        }
    }

    public static void validateCentromereAndTelomereData(java.util.Map map, Table conTable)
    {
        int _gen = 0;
        int _chr = 1;
        int numRows = conTable.getNumRows();
        for(int row = 0; row < numRows; row++)
        {
            java.lang.String genomeID = conTable.getString(row, 0);
            ncsa.evolutionhighway.datatype.Genome genome = (ncsa.evolutionhighway.datatype.Genome)map.get(genomeID);
            if(genome == null)
                throw new IllegalArgumentException("row " + row + ": unknown reference genome " + genomeID);
            java.lang.String chromosomeID = conTable.getString(row, 1);
            ncsa.evolutionhighway.datatype.Chromosome chromosome = genome.getChromosome(chromosomeID);
            if(chromosome == null)
                throw new IllegalArgumentException("row " + row + ": unknown (reference genome, reference chromosome) pair: (" + genomeID + ", " + chromosomeID + ")");
            if(genomeID.equalsIgnoreCase("HSA") || genomeID.equalsIgnoreCase("MMU"))
                throw new IllegalArgumentException("row " + row + ": genome " + genomeID + " already exists in Evolution Highway and " + "cannot be modified.");
        }

    }

    public static void validateConsensusData(java.util.Map map, Table conTable)
    {
        int _gen = 0;
        int _chr = 1;
        int numRows = conTable.getNumRows();
        for(int row = 0; row < numRows; row++)
        {
            java.lang.String genomeID = conTable.getString(row, 0);
            ncsa.evolutionhighway.datatype.Genome genome = (ncsa.evolutionhighway.datatype.Genome)map.get(genomeID);
            if(genome == null)
                throw new IllegalArgumentException("row " + row + ": unknown reference genome " + genomeID);
            java.lang.String chromosomeID = conTable.getString(row, 1);
            ncsa.evolutionhighway.datatype.Chromosome chromosome = genome.getChromosome(chromosomeID);
            if(chromosome == null)
                throw new IllegalArgumentException("row " + row + ": unknown (reference genome, reference chromosome) pair: (" + genomeID + ", " + chromosomeID + ")");
            if(genomeID.equalsIgnoreCase("HSA") || genomeID.equalsIgnoreCase("MMU"))
                throw new IllegalArgumentException("row " + row + ": genome " + genomeID + " already exists in Evolution Highway and " + "cannot be modified.");
        }
    }

    public static void validateGenomeData(Table chrTable)
    {
        int _gen = 0;
        int numRows = chrTable.getNumRows();
        for(int row = 0; row < numRows; row++)
        {
            java.lang.String gid = chrTable.getString(row, 0);
            if(gid.equalsIgnoreCase("HSA") || gid.equalsIgnoreCase("MMU"))
                throw new IllegalArgumentException("row " + row + ": genome " + gid + " already exists in Evolution Highway and " + "cannot be modified.");
        }
    }
}
