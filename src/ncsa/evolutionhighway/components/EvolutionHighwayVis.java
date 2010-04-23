// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   EvolutionHighwayVis.java

package ncsa.evolutionhighway.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.accessibility.AccessibleContext;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ncsa.d2k.batch.BatchInterface;
import ncsa.d2k.core.modules.Module;
import ncsa.d2k.core.modules.UserView;
import ncsa.d2k.core.modules.ViewModule;
import ncsa.d2k.core.modules.VisModule;
import ncsa.d2k.core.modules.agenda.AgendaManager;
import ncsa.d2k.modules.core.datatype.table.Table;
import ncsa.d2k.modules.core.io.file.input.ParseFileToTable;
import ncsa.evolutionhighway.util.GlobalSettings;
import ncsa.evolutionhighway.util.IO;
import ncsa.evolutionhighway.util.SizeChooser;
import ncsa.d2k.userviews.swing.JUserPane;
import ncsa.evolutionhighway.components.support.EvolutionHighwayCanvas;
import ncsa.evolutionhighway.components.support.EvolutionHighwayControl;
import ncsa.evolutionhighway.datatype.Chromosome;
import ncsa.evolutionhighway.datatype.CustomMark;
import ncsa.evolutionhighway.datatype.Genome;

// Referenced classes of package ncsa.evolutionhighway:
//            EvolutionHighwayCanvas, EvolutionHighwayControl

public class EvolutionHighwayVis extends ncsa.d2k.core.modules.VisModule
{
    private class EHView extends ncsa.d2k.userviews.swing.JUserPane
        implements java.awt.event.ActionListener
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
                java.awt.Frame frame = (parent instanceof java.awt.Frame) ? (java.awt.Frame)parent : (java.awt.Frame)javax.swing.SwingUtilities.getAncestorOfClass(EvolutionHighwayVis.class$java$awt$Frame != null ? EvolutionHighwayVis.class$java$awt$Frame : (EvolutionHighwayVis.class$java$awt$Frame = EvolutionHighwayVis._mthclass$("java.awt.Frame")), parent);
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
        private Table ancTable;
        private Table cenTable;
        private Table chrTable;
        private Table conTable;
        private Table buildTable;
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
                        java.io.File f = new File(EvolutionHighwayVis.res + java.io.File.separator + "Read Delimited.itn");
                        BatchInterface batch = new BatchInterface(f, getClass().getClassLoader());
                        Module collect = null;
                        java.util.HashMap map = batch.getAgendaManager().getModules();
                        java.util.Iterator iter = map.values().iterator();
                        do
                        {
                            if(!iter.hasNext())
                                break;
                            java.lang.Object o = iter.next();
                            if(o instanceof FileToTable)
                                collect = (Module)o;
                        } while(true);
                        batch.setModuleProperty("Input File Name", "fileName", fileChooser.getSelectedFile().getAbsolutePath());
                        batch.markOutput(collect, 0);
                        java.util.ArrayList al = batch.execute(new ArrayList(), 1000);
                        Table result = (Table)al.get(0);
                        try
                        {
                            IO.validateGenomeData(result);
                            IO.populateGenomeData(genomes, result, heterochromatinTexture);
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
                        java.io.File f = new File(EvolutionHighwayVis.res + java.io.File.separator + "Read Delimited.itn");
                        BatchInterface batch = new BatchInterface(f, getClass().getClassLoader());
                        Module collect = null;
                        java.util.HashMap map = batch.getAgendaManager().getModules();
                        java.util.Iterator iter = map.values().iterator();
                        do
                        {
                            if(!iter.hasNext())
                                break;
                            java.lang.Object o = iter.next();
                            if(o instanceof FileToTable)
                                collect = (Module)o;
                        } while(true);
                        batch.setModuleProperty("Input File Name", "fileName", fileChooser.getSelectedFile().getAbsolutePath());
                        batch.markOutput(collect, 0);
                        java.util.ArrayList al = batch.execute(new ArrayList(), 1000);
                        Table result = (Table)al.get(0);
                        try
                        {
                            IO.validateConsensusData(genomes, result);
                            IO.populateConsensusData(genomes, result);
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
                        java.io.File f = new File(EvolutionHighwayVis.res + java.io.File.separator + "Read Delimited.itn");
                        BatchInterface batch = new BatchInterface(f, getClass().getClassLoader());
                        Module collect = null;
                        java.util.HashMap map = batch.getAgendaManager().getModules();
                        java.util.Iterator iter = map.values().iterator();
                        do
                        {
                            if(!iter.hasNext())
                                break;
                            java.lang.Object o = iter.next();
                            if(o instanceof FileToTable)
                                collect = (Module)o;
                        } while(true);
                        batch.setModuleProperty("Input File Name", "fileName", fileChooser.getSelectedFile().getAbsolutePath());
                        batch.markOutput(collect, 0);
                        java.util.ArrayList al = batch.execute(new ArrayList(), 1000);
                        Table result = (Table)al.get(0);
                        try
                        {
                            IO.validateAncestorData(genomes, result);
                            IO.populateAncestorData(genomes, result);
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
                        java.io.File f = new File(EvolutionHighwayVis.res + java.io.File.separator + "Read Delimited.itn");
                        BatchInterface batch = new BatchInterface(f, getClass().getClassLoader());
                        Module collect = null;
                        java.util.HashMap map = batch.getAgendaManager().getModules();
                        java.util.Iterator iter = map.values().iterator();
                        do
                        {
                            if(!iter.hasNext())
                                break;
                            java.lang.Object o = iter.next();
                            if(o instanceof FileToTable)
                                collect = (Module)o;
                        } while(true);
                        batch.setModuleProperty("Input File Name", "fileName", fileChooser.getSelectedFile().getAbsolutePath());
                        batch.markOutput(collect, 0);
                        java.util.ArrayList al = batch.execute(new ArrayList(), 1000);
                        Table result = (Table)al.get(0);
                        try
                        {
                            IO.validateCentromereAndTelomereData(genomes, result);
                            IO.populateCentromereAndTelomereData(genomes, result);
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
                sb.append("Questions, suggestions, etc. to: gpape@ncsa.uiuc.edu");
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

        public void initView(ViewModule viewmodule)
        {
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

        public void setInput(java.lang.Object obj, int index)
        {
            switch(index)
            {
            case 0: // '\0'
                chrTable = (Table)obj;
                break;

            case 1: // '\001'
                conTable = (Table)obj;
                break;

            case 2: // '\002'
                ancTable = (Table)obj;
                break;

            case 3: // '\003'
                cenTable = (Table)obj;
                break;

            case 4: // '\004'
                buildTable = (Table)obj;
                break;
            }
            if(chrTable == null || conTable == null || ancTable == null || cenTable == null || buildTable == null)
                return;
            removeAll();
            EvolutionHighwayVis.buildMap.clear();
            EvolutionHighwayVis.buildURLMap.clear();
            for(int row = 0; row < buildTable.getNumRows(); row++)
            {
                if(!buildTable.isValueMissing(row, 1))
                    EvolutionHighwayVis.buildMap.put(buildTable.getString(row, 0), buildTable.getString(row, 1));
                if(!buildTable.isValueMissing(row, 2))
                    EvolutionHighwayVis.buildURLMap.put(buildTable.getString(row, 0), buildTable.getString(row, 2));
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
            genomes = new HashMap();
            IO.populateGenomeData(genomes, chrTable, heterochromatinTexture);
            IO.populateConsensusData(genomes, conTable);
            IO.populateAncestorData(genomes, ancTable);
            IO.populateCentromereAndTelomereData(genomes, cenTable);
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

        private EHView()
        {
            super();
            size = new Dimension(760, 480);
        }

    }


    public static java.util.Map buildMap = new HashMap();
    public static java.util.Map buildURLMap = new HashMap();
    public static java.lang.String res;
    static java.lang.Class class$java$awt$Frame; /* synthetic field */

    public EvolutionHighwayVis()
    {
    }

    public static void main(String args[])
    {
        try
        {
            java.io.File f = new File(res + java.io.File.separator + "Evolution Highway 1.3.itn");
            BatchInterface batch = new BatchInterface(f, EvolutionHighwayVis.class.getClassLoader());
            batch.setModuleProperty("InputChromosome", "fileName", res + java.io.File.separator + "chromosome_HSA_MMU.data");
            batch.setModuleProperty("InputConsensus", "fileName", res + java.io.File.separator + "consensus_HSA_MMU.data");
            batch.setModuleProperty("InputAncestral", "fileName", res + java.io.File.separator + "ancestral_HSA.data");
            batch.setModuleProperty("InputCentel", "fileName", res + java.io.File.separator + "cen_tel_HSA.data");
            batch.setModuleProperty("InputBuild", "fileName", res + java.io.File.separator + "build.data");
            EvolutionHighwayVis visModule = null;
            java.util.HashMap map = batch.getAgendaManager().getModules();
            java.util.Iterator iter = map.values().iterator();
            do
            {
                if(!iter.hasNext())
                    break;
                java.lang.Object o = iter.next();
                if(o instanceof EvolutionHighwayVis)
                    visModule = (EvolutionHighwayVis)o;
            } while(true);
            batch.execute(new ArrayList(), 1000);
            JUserPane view = (JUserPane)visModule.createUserView();
            view.initView(visModule);
            java.lang.Object inputs[] = visModule.getLastInputs();
            for(int i = 0; i < inputs.length; i++)
                try
                {
                    view.setInput(inputs[i], i);
                }
                catch(java.lang.Exception ex)
                {
                    ex.printStackTrace();
                }

            javax.swing.JFrame frame = new JFrame("Evolution Highway (alpha prerelease -- DO NOT DISTRIBUTE THIS VERSION -- testing purposes only)");
            frame.setDefaultCloseOperation(2);
            frame.getContentPane().add(view);
            frame.setJMenuBar((javax.swing.JMenuBar)view.getMenu());
            frame.pack();
            frame.show();
            frame.addWindowListener(new java.awt.event.WindowListener() {

                public void windowActivated(java.awt.event.WindowEvent windowevent)
                {
                }

                public void windowClosed(java.awt.event.WindowEvent we)
                {
                    java.lang.System.exit(0);
                }

                public void windowClosing(java.awt.event.WindowEvent windowevent)
                {
                }

                public void windowDeactivated(java.awt.event.WindowEvent windowevent)
                {
                }

                public void windowDeiconified(java.awt.event.WindowEvent windowevent)
                {
                }

                public void windowIconified(java.awt.event.WindowEvent windowevent)
                {
                }

                public void windowOpened(java.awt.event.WindowEvent windowevent)
                {
                }

            }
);
        }
        catch(java.lang.Exception exc)
        {
            exc.printStackTrace();
        }
    }

    public UserView createUserView()
    {
        return new EHView();
    }

    protected java.lang.String[] getFieldNameMapping()
    {
        return null;
    }

    public java.lang.String getInputInfo(int index)
    {
    	if (index == 0) {
            return "InputChromosome";
         }
         else if (index == 1) {
            return "InputConsensus";
         }
         else if (index == 2) {
            return "InputAncestral";
         }
         else if (index == 3) {
            return "InputCentel";
         }
         else if (index == 4) {
             return "InputBuild";
         }
         else {
            return null;
         }
    }
    
    public String getInputName(int index) {
    	if (index == 0) {
            return "Chromosome Table";
         }
         else if (index == 1) {
            return "Consensus Table";
         }
         else if (index == 2) {
            return "Ancestral Overlay Table";
         }
         else if (index == 3) {
            return "Centromere/Telomere Overlay Table";
        }
        else if (index == 4) {
            return "Input Build Table";
        }
        else {
           return null;
        }
     }

     public String[] getInputTypes() {
        return new String[] {
           "Table", // chromosome
           "Table", // consensus
           "Table", // ancestral
           "Table", // cen/tel
           "Table" // build
        };
     }


    /*public java.lang.String[] getInputTypes()
    {
        return (new java.lang.String[] {
            "Table", "Table", "Table", "Table", "Table"
        });
    }*/

    public java.lang.String getModuleInfo()
    {
        return null;
    }

    public java.lang.String getOutputInfo(int index)
    {
        return null;
    }

    public java.lang.String[] getOutputTypes()
    {
        return null;
    }

    static java.lang.Class _mthclass$(java.lang.String x0)
    {
        try
        {
            return java.lang.Class.forName(x0);
        }
        catch(java.lang.ClassNotFoundException x1)
        {
        	// throw (new NoClassDefFoundError()).initCause(x1);
        }
		return class$java$awt$Frame;
    }

    static 
    {
        res = java.lang.System.getProperty("user.dir") + java.io.File.separator + "res";
    }
}
