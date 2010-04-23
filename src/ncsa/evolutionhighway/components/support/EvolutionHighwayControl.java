// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst
// Source File Name:   EvolutionHighwayControl.java

package ncsa.evolutionhighway.components.support;

import gnu.trove.TIntArrayList;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sun.applet.AppletSecurity;

//import ncsa.d2k.modules.projects.EvolutionHighwayVis;
import ncsa.evolutionhighway.datatype.Chromosome;
import ncsa.evolutionhighway.datatype.ComparativeSpecies;
import ncsa.evolutionhighway.datatype.Genome;
import ncsa.evolutionhighway.util.CompareNode;
import ncsa.evolutionhighway.util.Feature;
import ncsa.evolutionhighway.util.GlobalSettings;
import ncsa.evolutionhighway.util.SpeciesOrderComparator;

// Referenced classes of package ncsa.d2k.modules.projects.gpape.evolution:
//            EvolutionHighwayVis, EvolutionHighwayCanvas

public class EvolutionHighwayControl extends javax.swing.JPanel
    implements java.awt.event.ActionListener, javax.swing.event.ChangeListener, javax.swing.event.ListSelectionListener
{
    private class SpeciesOrderControl extends javax.swing.JPanel
        implements Collapsible
    {
        private class SpeciesOrderListCellRenderer extends javax.swing.DefaultListCellRenderer
        {

            public java.awt.Component getListCellRendererComponent(javax.swing.JList list, java.lang.Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                java.awt.Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(canvas.getGenome() == null)
                {
                    c.setEnabled(false);
                } else
                {
                    ncsa.evolutionhighway.datatype.Genome g = canvas.getGenome();
                    ncsa.evolutionhighway.util.SpeciesOrderComparator comp = g.getSpeciesOrderComparator();
                    c.setEnabled(comp.isVisible(value.toString()));
                }
                return c;
            }

            SpeciesOrderListCellRenderer()
            {
                super();
            }
        }


        private boolean collapsed;
        private java.awt.Dimension dimCol;
        private java.awt.Dimension dimExt;
        private CollapsibleJScrollPane listScroll;
        private CollapsibleJPanel buttonPanel;
        private javax.swing.JButton up;
        private javax.swing.JButton down;
        private javax.swing.JButton toggle;
        private javax.swing.DefaultListModel model;
        private javax.swing.JList list;

        javax.swing.JButton down()
        {
            return down;
        }

        public boolean getIsCollapsed()
        {
            return collapsed;
        }

        public void setIsCollapsed(boolean c)
        {
            collapsed = c;
            listScroll.setIsCollapsed(c);
            buttonPanel.setIsCollapsed(c);
        }

        public java.awt.Dimension getMinimumSize()
        {
            return collapsed ? dimCol : dimExt;
        }

        public java.awt.Dimension getPreferredSize()
        {
            return collapsed ? dimCol : dimExt;
        }

        javax.swing.DefaultListModel model()
        {
            return model;
        }

        javax.swing.JButton toggle()
        {
            return toggle;
        }

        javax.swing.JButton up()
        {
            return up;
        }




        public SpeciesOrderControl(int width, int height)
        {
            super();
            collapsed = true;
            dimCol = new Dimension(width, 0);
            dimExt = new Dimension(width, height);
            java.awt.Font buttonFont = new Font("SansSerif", 0, 10);
            java.awt.Insets buttonMargin = new Insets(2, 2, 2, 2);
            java.awt.Insets emptyInsets = new Insets(0, 0, 0, 0);
            int btn_y = 26;
            list = new JList(model = new DefaultListModel());
            list.setSelectionMode(0);
            list.setCellRenderer(new SpeciesOrderListCellRenderer());
            up = new JButton("Up");
            up.setFont(buttonFont);
            up.setMargin(buttonMargin);
            up.addActionListener(EvolutionHighwayControl.this);
            down = new JButton("Down");
            down.setFont(buttonFont);
            down.setMargin(buttonMargin);
            down.addActionListener(EvolutionHighwayControl.this);
            toggle = new JButton("Toggle");
            toggle.setFont(buttonFont);
            toggle.setMargin(buttonMargin);
            toggle.addActionListener(EvolutionHighwayControl.this);
            javax.swing.JPanel buttons = new JPanel();
            java.awt.GridBagLayout buttonLayout = new GridBagLayout();
            buttons.setLayout(buttonLayout);
            buttonLayout.addLayoutComponent(up, new GridBagConstraints(0, 0, 1, 1, 0.33000000000000002D, 1.0D, 10, 1, emptyInsets, 0, 0));
            buttons.add(up);
            buttonLayout.addLayoutComponent(down, new GridBagConstraints(1, 0, 1, 1, 0.34000000000000002D, 1.0D, 10, 1, emptyInsets, 0, 0));
            buttons.add(down);
            buttonLayout.addLayoutComponent(toggle, new GridBagConstraints(2, 0, 1, 1, 0.33000000000000002D, 1.0D, 10, 1, emptyInsets, 0, 0));
            buttons.add(toggle);
            listScroll = new CollapsibleJScrollPane(list, width, height - btn_y);
            buttonPanel = new CollapsibleJPanel(buttons, width, btn_y);
            java.awt.GridBagLayout layout = new GridBagLayout();
            setLayout(layout);
            layout.addLayoutComponent(listScroll, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 1, emptyInsets, 0, 0));
            add(listScroll);
            layout.addLayoutComponent(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
            add(buttonPanel);
        }
    }
    private class CollapsibleJPanel extends JPanel implements Collapsible{

        private boolean collapsed;
        private Dimension dimCol;
        private Dimension dimExt;

        CollapsibleJPanel(int width, int height) {
           super();
           collapsed = true;
           dimCol = new Dimension(width, 0);
           dimExt = new Dimension(width, height);
        }

        CollapsibleJPanel(javax.swing.JComponent component, int width, int height)
        {
            super();
            java.awt.GridBagLayout layout = new GridBagLayout();
            setLayout(layout);
            layout.addLayoutComponent(component, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
            add(component);
            collapsed = true;
            dimCol = new Dimension(width, 0);
            dimExt = new Dimension(width, height);
        }


        public boolean getIsCollapsed() {
           return collapsed;
        }

        public void setIsCollapsed(boolean c) {
           collapsed = c;
        }

        public Dimension getMinimumSize() {
           return collapsed ? dimCol : dimExt;
        }

        public Dimension getPreferredSize() {
           return collapsed ? dimCol : dimExt;
        }

     }

     private class CollapsibleJScrollPane extends JScrollPane implements Collapsible{

        private boolean collapsed;
        private Dimension dimCol;
        private Dimension dimExt;

        CollapsibleJScrollPane(Component c, int width, int height) {
           super(c);
           collapsed = true;
           dimCol = new Dimension(width, 0);
           dimExt = new Dimension(width, height);
        }

        public boolean getIsCollapsed() {
           return collapsed;
        }

        public void setIsCollapsed(boolean c) {
           collapsed = c;
        }

        public Dimension getMinimumSize() {
           return collapsed ? dimCol : dimExt;
        }

        public Dimension getPreferredSize() {
           return collapsed ? dimCol : dimExt;
        }

     }

     private class newJScrollPane extends JScrollPane {

         java.awt.Dimension size;

         public java.awt.Dimension getMinimumSize()
         {
             return size;
         }

         public java.awt.Dimension getPreferredSize()
         {
             return size;
         }

         newJScrollPane(JList list_breakpoint)
         {
           super(list_breakpoint);
             size = new Dimension(180, 75);
         }
     }

    private static interface Collapsible
    {

        public abstract boolean getIsCollapsed();

        public abstract void setIsCollapsed(boolean flag);
    }

    private class CollapserButton extends JButton
    {

        void init()
        {
            //addActionListener(this);
            setBorderPainted(false);
            setFocusPainted(false);
            setHorizontalAlignment(2);
            setMargin(new Insets(2, 2, 2, 2));
        }

        CollapserButton(java.lang.String text)
        {
            super(text, collapseIcon_right);
            init();
        }
    }


    private EvolutionHighwayCanvas canvas;
    private GlobalSettings settings;
    private javax.swing.JTextArea msgAreaRef;
    private javax.swing.ImageIcon collapseIcon_down;
    private javax.swing.ImageIcon collapseIcon_right;
    private CollapserButton collapser_genome;
    private CollapserButton collapser_chromosome;
    private CollapserButton collapser_ancID;
    private CollapserButton collapser_ancAlpha;
    private CollapserButton collapser_threshold;
    private CollapserButton collapser_order;
    private CollapserButton collapser_compareSynteny;
    private CollapserButton collapser_compareBreakpoint;
    private CollapserButton collapser_hgTables;
    private java.util.HashMap collapsers;
    private CollapsibleJPanel panel_ancAlpha;
    private CollapsibleJPanel panel_threshold;
    private CollapsibleJPanel panel_compareBreakpoint;
    private CollapsibleJPanel panel_hgTables;
    private CollapsibleJScrollPane scroll_genome;
    private CollapsibleJScrollPane scroll_chromosome;
    private CollapsibleJScrollPane scroll_ancID;
    private CollapsibleJScrollPane scroll_synteny;
    private javax.swing.DefaultListModel model_genome;
    private javax.swing.DefaultListModel model_chromosome;
    private javax.swing.DefaultListModel model_ancID;
    private javax.swing.DefaultListModel model_synteny;
    private javax.swing.DefaultListModel model_breakpoint;
    private javax.swing.DefaultComboBoxModel model_hgTables;
    private javax.swing.JList list_genome;
    private javax.swing.JList list_chromosome;
    private javax.swing.JList list_ancID;
    private javax.swing.JList list_synteny;
    private javax.swing.JList list_breakpoint;
    private javax.swing.JSlider slider_ancAlpha;
    private javax.swing.JSlider slider_threshold;
    private javax.swing.JTextField compareBreakpointThreshold;
    private javax.swing.JComboBox combo_hgTables;
    private SpeciesOrderControl order;
    private java.io.File hgTablesFile;
    private ClassLoader classloader;

    public EvolutionHighwayControl(java.util.Map initMap, EvolutionHighwayCanvas canvas, javax.swing.JTextArea msgAreaRef, ncsa.evolutionhighway.util.GlobalSettings settings, java.lang.ClassLoader classLoader)
    {
        this.canvas = canvas;
        this.msgAreaRef = msgAreaRef;
        this.settings = settings;
        this.classloader = classLoader;
        try
        {
            collapseIcon_down = new ImageIcon(javax.imageio.ImageIO.read(classLoader.getResource("images/hsadown.gif")));
            collapseIcon_right = new ImageIcon(javax.imageio.ImageIO.read(classLoader.getResource("images/hsaright.gif")));
        }
        catch(java.io.IOException ioe)
        {
            throw new RuntimeException(ioe);
        }
        collapser_genome = new CollapserButton("Reference genome:");
        collapser_genome.addActionListener(this);
        collapser_chromosome = new CollapserButton("Reference chromosomes:");
        collapser_chromosome.addActionListener(this);
        collapser_ancID = new CollapserButton("Ancestor ID:");
        collapser_ancID.addActionListener(this);
        collapser_ancAlpha = new CollapserButton("Ancestor transparency:");
        collapser_ancAlpha.addActionListener(this);
        collapser_threshold = new CollapserButton("Vanishing threshold:");
        collapser_threshold.addActionListener(this);
        collapser_order = new CollapserButton("Species order/visibility:");
        collapser_order.addActionListener(this);
        collapser_compareSynteny = new CollapserButton("Conserved synteny:");
        collapser_compareSynteny.addActionListener(this);
        collapser_compareBreakpoint = new CollapserButton("Breakpoint classification:");
        collapser_compareBreakpoint.addActionListener(this);
        list_genome = new JList(model_genome = new DefaultListModel());
        list_genome.addListSelectionListener(this);
        list_genome.setSelectionMode(0);
        for(java.util.Iterator keyiter = initMap.keySet().iterator(); keyiter.hasNext(); model_genome.addElement(initMap.get(keyiter.next())));
        list_chromosome = new JList(model_chromosome = new DefaultListModel());
        list_chromosome.addListSelectionListener(this);
        list_ancID = new JList(model_ancID = new DefaultListModel());
        list_ancID.addListSelectionListener(this);
        list_ancID.setSelectionMode(0);
        slider_ancAlpha = new JSlider(0, 0, 255, 255 - settings.getAncestorTransparency());
        slider_ancAlpha.addChangeListener(this);
        slider_threshold = new JSlider(0, 0, 16, settings.getVanishingThreshold());
        slider_threshold.addChangeListener(this);
        order = new SpeciesOrderControl(180, 105);
        list_synteny = new JList(model_synteny = new DefaultListModel());
        list_synteny.addListSelectionListener(this);
        compareBreakpointThreshold = new JTextField("4.0");
        javax.swing.JLabel cbtLabel = new JLabel("Max. threshold (Mbp): ");
        javax.swing.JPanel cbtPanel = new JPanel();
        java.awt.GridBagLayout cbtLayout = new GridBagLayout();
        cbtPanel.setLayout(cbtLayout);
        cbtLayout.addLayoutComponent(cbtLabel, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
        cbtPanel.add(cbtLabel);
        cbtLayout.addLayoutComponent(compareBreakpointThreshold, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        cbtPanel.add(compareBreakpointThreshold);
        list_breakpoint = new JList(model_breakpoint = new DefaultListModel());
        list_breakpoint.addListSelectionListener(this);
        javax.swing.JScrollPane scroll_breakpoint = new newJScrollPane(list_breakpoint);


        javax.swing.JPanel cmpBpanel = new JPanel();
        java.awt.GridBagLayout compareBreakpointLayout = new GridBagLayout();
        cmpBpanel.setLayout(compareBreakpointLayout);
        compareBreakpointLayout.addLayoutComponent(scroll_breakpoint, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        cmpBpanel.add(scroll_breakpoint);
        compareBreakpointLayout.addLayoutComponent(cbtPanel, new GridBagConstraints(0, 1, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 4, 0, 4), 0, 0));
        cmpBpanel.add(cbtPanel);
        panel_compareBreakpoint = new CollapsibleJPanel(cmpBpanel, 180, 95);
        java.awt.GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        java.awt.Insets emptyInsets = new Insets(0, 0, 0, 0);
        int layout_y = 0;
        layout.addLayoutComponent(collapser_genome, new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(collapser_genome);
        layout.addLayoutComponent(scroll_genome = new CollapsibleJScrollPane(list_genome, 180, 75), new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(scroll_genome);
        layout.addLayoutComponent(collapser_chromosome, new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(collapser_chromosome);
        layout.addLayoutComponent(scroll_chromosome = new CollapsibleJScrollPane(list_chromosome, 180, 125), new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(scroll_chromosome);
        layout.addLayoutComponent(collapser_ancID, new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(collapser_ancID);
        layout.addLayoutComponent(scroll_ancID = new CollapsibleJScrollPane(list_ancID, 180, 75), new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(scroll_ancID);
        layout.addLayoutComponent(collapser_ancAlpha, new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(collapser_ancAlpha);
        layout.addLayoutComponent(panel_ancAlpha = new CollapsibleJPanel(slider_ancAlpha, 180, 40), new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(panel_ancAlpha);
        layout.addLayoutComponent(collapser_threshold, new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(collapser_threshold);
        layout.addLayoutComponent(panel_threshold = new CollapsibleJPanel(slider_threshold, 180, 40), new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(panel_threshold);
        layout.addLayoutComponent(collapser_order, new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(collapser_order);
        layout.addLayoutComponent(order, new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(order);
        layout.addLayoutComponent(collapser_compareSynteny, new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(collapser_compareSynteny);
        layout.addLayoutComponent(scroll_synteny = new CollapsibleJScrollPane(list_synteny, 180, 75), new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(scroll_synteny);
        layout.addLayoutComponent(collapser_compareBreakpoint, new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(collapser_compareBreakpoint);
        layout.addLayoutComponent(panel_compareBreakpoint, new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
        add(panel_compareBreakpoint);

        System.out.println("globalsetting for codebase = "+settings.getCodeBase());

        //hgTablesFile = new File("hgTables");
        //if(hgTablesFile.exists() && hgTablesFile.isDirectory())
        if (settings.getgenomeList().size() > 0){
        	System.out.println("Found hgTables\n");
    		System.out.flush();
    		combo_hgTables = new JComboBox(model_hgTables = new DefaultComboBoxModel());
            combo_hgTables.addActionListener(this);
            javax.swing.JPanel hgp = new JPanel();
            java.awt.GridBagLayout hgl = new GridBagLayout();
            hgp.setLayout(hgl);
            hgl.addLayoutComponent(combo_hgTables, new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
            hgp.add(combo_hgTables);
            collapser_hgTables = new CollapserButton("Feature density:");
            System.out.println("Adding Feature density button\n");
            collapser_hgTables.addActionListener(this);
            panel_hgTables = new CollapsibleJPanel(hgp, 180, 35);
            layout.addLayoutComponent(collapser_hgTables, new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
            add(collapser_hgTables);
            layout.addLayoutComponent(panel_hgTables, new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 0.0D, 10, 2, emptyInsets, 0, 0));
            add(panel_hgTables);
        }
        javax.swing.JLabel filler = new JLabel();
        layout.addLayoutComponent(filler, new GridBagConstraints(0, layout_y++, 1, 1, 1.0D, 1.0D, 10, 1, emptyInsets, 0, 0));
        add(filler);
        collapsers = new HashMap(8);
        collapsers.put(collapser_genome, scroll_genome);
        collapsers.put(collapser_chromosome, scroll_chromosome);
        collapsers.put(collapser_ancID, scroll_ancID);
        collapsers.put(collapser_ancAlpha, panel_ancAlpha);
        collapsers.put(collapser_threshold, panel_threshold);
        collapsers.put(collapser_order, order);
        collapsers.put(collapser_compareSynteny, scroll_synteny);
        collapsers.put(collapser_compareBreakpoint, panel_compareBreakpoint);
        if(collapser_hgTables != null)
            collapsers.put(collapser_hgTables, panel_hgTables);
    }

    public void actionPerformed(java.awt.event.ActionEvent ae)
    {
        java.lang.Object src = ae.getSource();
        if(collapsers.containsKey(src))
        {
            CollapserButton key = (CollapserButton)src;
            Collapsible value = (Collapsible)collapsers.get(src);
            boolean collapsed = value.getIsCollapsed();
            value.setIsCollapsed(collapsed = !collapsed);
            key.setIcon(collapsed ? ((javax.swing.Icon) (collapseIcon_right)) : ((javax.swing.Icon) (collapseIcon_down)));
            revalidate();
            repaint();
        } else
        if(src == order.up())
        {
            ncsa.evolutionhighway.datatype.Genome g = canvas.getGenome();
            if(g == null)
                return;
            ncsa.evolutionhighway.util.SpeciesOrderComparator orderComp = g.getSpeciesOrderComparator();
            int ndx = order.list.getSelectedIndex();
            if(ndx >= 1)
            {
                java.lang.Object tmp = order.model.elementAt(ndx - 1);
                order.model.setElementAt(order.model.elementAt(ndx), ndx - 1);
                order.model.setElementAt(tmp, ndx);
            }
            for(int i = 0; i < order.model.size(); i++)
                orderComp.put(order.model.elementAt(i).toString(), i);

            rebuild(new HashMap());
            if(ndx >= 1)
                order.list.setSelectedIndex(ndx - 1);
            else
                order.list.setSelectedIndex(0);
            order.list.repaint();
        } else
        if(src == order.down())
        {
            ncsa.evolutionhighway.datatype.Genome g = canvas.getGenome();
            if(g == null)
                return;
            ncsa.evolutionhighway.util.SpeciesOrderComparator orderComp = g.getSpeciesOrderComparator();
            int ndx = order.list.getSelectedIndex();
            if(ndx >= 0 && ndx < order.model.getSize() - 1)
            {
                java.lang.Object tmp = order.model.elementAt(ndx + 1);
                order.model.setElementAt(order.model.elementAt(ndx), ndx + 1);
                order.model.setElementAt(tmp, ndx);
            }
            for(int i = 0; i < order.model.size(); i++)
                orderComp.put(order.model.elementAt(i).toString(), i);

            rebuild(new HashMap());
            if(ndx >= 0 && ndx < order.model.getSize() - 1)
                order.list.setSelectedIndex(ndx + 1);
            else
                order.list.setSelectedIndex(order.model.getSize() - 1);
            order.list.repaint();
        } else
        if(src == order.toggle())
        {
            ncsa.evolutionhighway.datatype.Genome g = canvas.getGenome();
            if(g == null)
                return;
            ncsa.evolutionhighway.util.SpeciesOrderComparator orderComp = g.getSpeciesOrderComparator();
            int ndx = order.list.getSelectedIndex();
            if(ndx < 0)
                return;
            orderComp.setVisible(order.model.elementAt(ndx).toString(), !orderComp.isVisible(order.model.elementAt(ndx).toString()));
            rebuild(new HashMap());
            order.list.setSelectedIndex(ndx);
        } else
        if(src == combo_hgTables)
        {
        	//System.out.println("Getting selected model files - chr files");
            ncsa.evolutionhighway.datatype.Genome g = canvas.getGenome();
            if(g == null)
                return;
            if (model_hgTables.getSelectedItem() == "(none)")
            	return;

            //String tablesDirstr = settings.getCodeBase().toString()+"hgTables" + java.io.File.separator + g.getID() + java.io.File.separator + model_hgTables.getSelectedItem();
            //msgAreaRef.setText(tablesDirstr);
            //System.out.println("tablesDirstr = "+tablesDirstr);

            //java.io.File tablesDir = new File(tablesDirstr);
            ncsa.evolutionhighway.datatype.Chromosome c;
            for(java.util.Iterator citer = g.getChromosomeIterator(); citer.hasNext(); c.setDensityShown(false))
            {
                c = (ncsa.evolutionhighway.datatype.Chromosome)citer.next();
                c.clearFeatures();
            }
            System.out.println("tablelist.size ="+settings.gettableList().size());
            if (settings.gettableList().size() > 0)
            //if(!tablesDir.exists() || !tablesDir.isDirectory())
            	{
               // rebuild(new HashMap());
               // return;
            }

            /*java.io.File tableFiles[] = tablesDir.listFiles(new java.io.FilenameFilter() {

                public boolean accept(java.io.File dir, java.lang.String name)
                {
                    return name.startsWith("chr");
                }
            });*/

            File tableFile;
            //= new File[settings.gettableList().size()];
            URL url;
            String feature = "/"+(String) model_hgTables.getSelectedItem()+"/";
            String genome = "/"+g.toString()+"/";
            for (int i=0; i< settings.gettableList().size(); i++)
            { //check file i if it contains the genome and the selected feature density
            	if (settings.gettableList().get(i).toString().contains(feature)
            			&& (settings.gettableList().get(i).toString()).contains(genome))
            	{
            	tableFile = new File((String) settings.getCodeBase().toString()+settings.gettableList().get(i));
            	//System.out.println("tableFile ="+tableFiles[i].getPath());
            	System.out.println("tableList ="+i+settings.getCodeBase().toString()+settings.gettableList().get(i));

                c = g.getChromosome(tableFile.getName().replace("chr", "").replace(".txt", ""));

				System.out.println("file urls = " + c.toString());
	            System.out.flush();

				//msgAreaRef.setText(c.toString());
                //c = g.getChromosome(c.toString());
				String line;

			    if(c == null)
                    continue;
                //c.clearFeatures();

                try
                {
                	// Open a stream to the file using the URL.
                      url = new URL((String) settings.gettableList().get(i));

                      System.out.println("Loading feature from url = "+url.toString());

                      InputStream in=url.openStream();
                      BufferedReader r =
                        new BufferedReader (new InputStreamReader (in));

                      line = r.readLine (); //read first line with attribute labels
                      while ( (line = r.readLine ()) != null) {
                        java.lang.String sp[] = line.split("\t");
                        int start;
                        int end;
                        try
                        {
                            start = java.lang.Integer.parseInt(sp[3]);
                            end = java.lang.Integer.parseInt(sp[4]);
                        }
                        catch(java.lang.NumberFormatException nfe)
                        {
                            continue;
                        }
                        try
                        {
                            c.addFeature(new Feature(start, end));
                        }
                        catch(java.lang.IllegalArgumentException iae) { }
                      }

                      in.close ();

                	/*java.io.BufferedReader r = new BufferedReader(new FileReader(tableFiles[i]));
                    java.lang.String ln = null;
                    ln = r.readLine(); //read first line with attribute labels
                    System.out.println("line = "+ ln);
                    do
                    {
                        if((ln = r.readLine()) == null)
                            break;
                        System.out.println("line = "+ ln);
                        java.lang.String sp[] = ln.split("\t");
                        System.out.println("sp3 = "+sp[3]+" sp4 = "+sp[4]);

                        int start;
                        int end;
                        try
                        {
                            start = java.lang.Integer.parseInt(sp[3]);
                            end = java.lang.Integer.parseInt(sp[4]);
                        }
                        catch(java.lang.NumberFormatException nfe)
                        {
                            continue;
                        }
                        try
                        {
                            c.addFeature(new Feature(start, end));
                        }
                        catch(java.lang.IllegalArgumentException iae) { }
                    } while(true);
                    r.close();*/
                }
                catch(java.io.IOException ioe)
                {
                    msgAreaRef.setText("IO Exception: " + settings.getCodeBase().toString()+settings.gettableList().get(i));
                	System.out.println ("IO Exception: "+ioe);
                }

                c.setDensityShown(true);
                c.rebuildDensity();
            } //if loop
            } //for loop

            rebuild(new HashMap());
			/*} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
        }
    }

    public void rebuild(java.util.Map initMap)
    {
        java.util.Iterator keyiter = initMap.keySet().iterator();
        do
        {
            if(!keyiter.hasNext())
                break;
            ncsa.evolutionhighway.datatype.Genome gen = (ncsa.evolutionhighway.datatype.Genome)initMap.get(keyiter.next());
            if(!model_genome.contains(gen))
                model_genome.addElement(gen);
        } while(true);
        if(canvas.getGenome() != null)
        {
            java.lang.String activeAncID = null;
            if(list_ancID.getSelectedIndex() >= 0)
                activeAncID = list_ancID.getSelectedValue().toString();
            ncsa.evolutionhighway.datatype.Genome active = canvas.getGenome();
            active.rebuild(settings, activeAncID);
            java.util.Iterator anciter = active.getAncestorIDIterator();
            do
            {
                if(!anciter.hasNext())
                    break;
                java.lang.String aid = (java.lang.String)anciter.next();
                if(!model_ancID.contains(aid))
                    model_ancID.addElement(aid);
            } while(true);
            order.model.clear();
            for(java.util.Iterator ciditer = active.getComparativeSpeciesIDIterator(); ciditer.hasNext(); order.model.addElement(ciditer.next()));
        }
    }

    public void saveCompareMatches(java.io.File f)
        throws java.io.IOException
    {
    	msgAreaRef.setText("Saving Compare Matches");
    	java.io.BufferedWriter br = new BufferedWriter(new FileWriter(f));
        ncsa.evolutionhighway.datatype.Genome g = canvas.getGenome();
        if(g == null)
        {
            br.flush();
            br.close();
            return;
        }
        java.lang.String gid = g.getID();
        java.lang.String TAB = "\t";
        for(java.util.Iterator chromosomeIterator = g.getChromosomeIterator(); chromosomeIterator.hasNext();)
        {
            ncsa.evolutionhighway.datatype.Chromosome c = (ncsa.evolutionhighway.datatype.Chromosome)chromosomeIterator.next();
            java.util.Iterator matchIter = c.getCompareMatches().iterator();
            while(matchIter.hasNext())
            {
                double match[] = (double[])(double[])matchIter.next();
                br.write(gid + TAB);
                br.write(c.getID() + TAB);
                br.write(c.getCompareMatchesDescription() + TAB);
                br.write(java.lang.Math.round(match[0] / 3.9999999999999998E-006D) + TAB);
                br.write(java.lang.Math.round(match[1] / 3.9999999999999998E-006D) + "\n");
            }
        }

        br.flush();
        br.close();
    }

    public void stateChanged(javax.swing.event.ChangeEvent ce)
    {
        java.lang.Object src = ce.getSource();
        if(src == slider_ancAlpha)
        {
            if(slider_ancAlpha.getValueIsAdjusting())
                return;
            settings.setAncestorTransparency(255 - slider_ancAlpha.getValue());
            rebuild(new HashMap());
        } else
        if(src == slider_threshold)
        {
            if(slider_threshold.getValueIsAdjusting())
                return;
            settings.setVanishingThreshold(slider_threshold.getValue());
            rebuild(new HashMap());
        }
    }

    public void valueChanged(javax.swing.event.ListSelectionEvent lse)
    {
        if(lse.getValueIsAdjusting())
            return;
        java.lang.Object src = lse.getSource();
        if(src == list_genome)
        {
            list_synteny.clearSelection();
            list_breakpoint.clearSelection();
            java.lang.Object obj = list_genome.getSelectedValue();
            if(obj == null)
            {
                canvas.clear();
                list_chromosome.removeListSelectionListener(this);
                model_chromosome.removeAllElements();
                list_chromosome.addListSelectionListener(this);
                list_ancID.removeListSelectionListener(this);
                model_ancID.removeAllElements();
                list_ancID.addListSelectionListener(this);
                order.model().removeAllElements();
                list_synteny.removeListSelectionListener(this);
                model_synteny.removeAllElements();
                list_synteny.addListSelectionListener(this);
                list_breakpoint.removeListSelectionListener(this);
                model_breakpoint.removeAllElements();
                list_breakpoint.addListSelectionListener(this);
                combo_hgTables.removeActionListener(this);
                model_hgTables.removeAllElements();
                combo_hgTables.addActionListener(this);
                return;
            }
            ncsa.evolutionhighway.datatype.Genome genome = (ncsa.evolutionhighway.datatype.Genome)obj;
            genome.rebuild(settings, null);
            canvas.setGenome(genome);
            model_chromosome.removeAllElements();
            gnu.trove.TIntArrayList selected = new TIntArrayList();
            java.util.Iterator chriter = genome.getChromosomeIterator();
            for(int index = 0; chriter.hasNext(); index++)
            {
                ncsa.evolutionhighway.datatype.Chromosome c = (ncsa.evolutionhighway.datatype.Chromosome)chriter.next();
                model_chromosome.addElement(c);
                if(c.getVisible())
                    selected.add(index);
            }

            list_chromosome.removeListSelectionListener(this);
            list_chromosome.setSelectedIndices(selected.toNativeArray());
            list_chromosome.addListSelectionListener(this);
            model_ancID.removeAllElements();
            for(java.util.Iterator anciter = genome.getAncestorIDIterator(); anciter.hasNext(); model_ancID.addElement(anciter.next()));
            javax.swing.DefaultListModel model = order.model();
            model.removeAllElements();
            model_synteny.removeAllElements();
            model_breakpoint.removeAllElements();
            java.lang.Object next;
            for(java.util.Iterator spciter = genome.getComparativeSpeciesIDIterator(); spciter.hasNext(); model_breakpoint.addElement(next))
            {
                next = spciter.next();
                model.addElement(next);
                model_synteny.addElement(next);
            }

            if (model_hgTables != null) model_hgTables.removeAllElements();
            //String str = settings.getCodeBase().toString()+hgTablesFile + java.io.File.separator + genome.getID();
			List glist = settings.getgenomeList();
			if (glist.contains(genome.getID()))
			{
				System.out.println("Checking for features");
            	System.out.println("genomeID "+genome.getID());
            	model_hgTables.addElement("(none)");
            	for (int i=0; i< settings.gettableList().size(); i++)
            	{
            		System.out.println(settings.gettableList().get(i).toString());
            		CharSequence str = "/"+genome.getID()+"/";
            		System.out.println(str);
            		if (settings.gettableList().get(i).toString().contains(str))
            		{
            			int val = settings.gettableList().get(i).toString().indexOf(str.toString());
            		    val += str.length();
            		    String feature = settings.gettableList().get(i).toString().substring(val);
            		    val = feature.indexOf("/");
            		    feature = feature.substring(0, val);
            		    System.out.println("found feature: "+feature);
            		    Boolean found = false;
            		    for (int j=0; j<model_hgTables.getSize(); j++)
            			{
            		    	System.out.println("comparing "+feature+" to "+model_hgTables.getElementAt(j));
            				if ((boolean)model_hgTables.getElementAt(j).equals(feature))
            				{
            					found = true;
            					break;
            				}
            			}
            			if (found == false)
            			{
            				model_hgTables.addElement(feature);
            				System.out.println("hgOptions "+feature);
            			}
            		}

            	/* trying to not use featureList structure
            	model_hgTables.addElement("(none)");
                for (int i=0; i<settings.getfeatureList().size(); i++) {
            		model_hgTables.addElement(settings.getfeatureList().get(i));
            		System.out.println("hgOptions "+i+" "+settings.getfeatureList().get(i));
            		*/

					/*String hgOptions[] = settings.getfeatureFiles();
					model_hgTables.addElement("(none)");
	                for(int i = 0; i < hgOptions.length; i++){
	                    model_hgTables.addElement(hgOptions[i]);
	                    System.out.println("hgOptions "+i+" "+hgOptions[i]);
	                }*/
            	}
            }
            //java.io.File refGenDir = new File(hgTablesFile + java.io.File.separator + genome.getID());
            //if(refGenDir.exists() && refGenDir.isDirectory()){
            //	System.out.println("Getting hgOptions");
              /*  java.io.File hgOptions[] = refGenDir.listFiles(new java.io.FileFilter() {

                    public boolean accept(java.io.File pathname)
                    {
                        if(!pathname.isDirectory())
                        {
                            return false;
                        } else
                        {
                            java.io.File chrCandidates[] = pathname.listFiles(new java.io.FileFilter() {

                                public boolean accept(java.io.File pathname)
                                {
                                    return !pathname.isDirectory() && pathname.getName().startsWith("chr");
                                }
                    //{
                        //super();
                    //}
                            }
);
                            return chrCandidates.length >= 1;
                        }
                    }
            //{
             //   super();
            //}
                }
);
                model_hgTables.addElement("(none)");
                for(int i = 0; i < hgOptions.length; i++){
                    model_hgTables.addElement(hgOptions[i].getName());
                    System.out.println("hgOptions "+i+" "+hgOptions[i].getName());
                }

            */
        } else
        if(src == list_chromosome)
        {
            for(int i = 0; i < model_chromosome.size(); i++)
            {
                ncsa.evolutionhighway.datatype.Chromosome c = (ncsa.evolutionhighway.datatype.Chromosome)model_chromosome.elementAt(i);
                c.setVisible(list_chromosome.isSelectedIndex(i));
            }

            java.lang.String activeAncID = null;
            if(list_ancID.getSelectedIndex() >= 0)
                activeAncID = list_ancID.getSelectedValue().toString();
            ((ncsa.evolutionhighway.datatype.Genome)list_genome.getSelectedValue()).rebuild(settings, activeAncID);
        } else
        if(src == list_ancID)
        {
            if(list_ancID.getSelectedIndex() < 0)
                ((ncsa.evolutionhighway.datatype.Genome)list_genome.getSelectedValue()).rebuild(settings, null);
            else
                ((ncsa.evolutionhighway.datatype.Genome)list_genome.getSelectedValue()).rebuild(settings, list_ancID.getSelectedValue().toString());
        } else
        if(src == list_breakpoint)
        {
            list_synteny.removeListSelectionListener(this);
            list_synteny.clearSelection();
            list_synteny.addListSelectionListener(this);
            ncsa.evolutionhighway.datatype.Genome g = canvas.getGenome();
            if(g == null)
            {
                msgAreaRef.setText("no genome selected");
                return;
            }
            java.lang.Object v[] = list_breakpoint.getSelectedValues();
            if(v.length == 0)
            {
                ncsa.evolutionhighway.datatype.Chromosome c;
                for(java.util.Iterator citer = g.getChromosomeIterator(); citer.hasNext(); c.setCompareMatches(new ArrayList(), null, -1D))
                {
                    c = (ncsa.evolutionhighway.datatype.Chromosome)citer.next();
                    c.clearVisibleOverride();
                }

                rebuild(new HashMap());
                return;
            }
            double threshold;
            if(compareBreakpointThreshold.getText().length() == 0)
                threshold = 1.7976931348623157E+308D;
            else
                try
                {
                    threshold = 1000000D * java.lang.Double.parseDouble(compareBreakpointThreshold.getText()) * 3.9999999999999998E-006D;
                }
                catch(java.lang.NumberFormatException nfe)
                {
                    msgAreaRef.setText("non-numeric threshold: " + compareBreakpointThreshold.getText());
                    return;
                }
            if(threshold <= 0.0D)
                threshold = 1.7976931348623157E+308D;
            ncsa.evolutionhighway.datatype.Chromosome c;
            for(java.util.Iterator cIter = g.getChromosomeIterator(); cIter.hasNext(); c.clearVisibleOverride())
                c = (ncsa.evolutionhighway.datatype.Chromosome)cIter.next();

            rebuild(new HashMap());
            // -la ncsa.d2k.modules.projects.gpape.evolution.datatype.Chromosome c;
            java.util.ArrayList combined;
            java.lang.StringBuffer desc;
            for(java.util.Iterator cIter = g.getChromosomeIterator(); cIter.hasNext(); c.setCompareMatches(combined, desc.toString(), -1D))
            {
                c = (ncsa.evolutionhighway.datatype.Chromosome)cIter.next();
                combined = new ArrayList();
                desc = new StringBuffer(v[0].toString());
                ncsa.evolutionhighway.datatype.ComparativeSpecies firstCS = c.getComparativeSpecies(v[0].toString());
                java.util.List firstBounds = firstCS.getBreakpointBounds();
                for(int i = 0; i < firstBounds.size(); i++)
                {
                    double d[] = (double[])(double[])firstBounds.get(i);
                    if(d[1] - d[0] < threshold)
                        combined.add(d);
                }

                firstCS = null;
                firstBounds = null;
                for(int i = 1; i < v.length; i++)
                {
                    ncsa.evolutionhighway.datatype.ComparativeSpecies nextCS = c.getComparativeSpecies(v[i].toString());
                    java.util.List nextBounds = nextCS.getBreakpointBounds();
                    java.util.ArrayList nextCombined = new ArrayList();
                    desc.append(",");
                    desc.append(v[i].toString());
                    for(int a = 0; a < combined.size(); a++)
                    {
                        for(int b = 0; b < nextBounds.size(); b++)
                        {
                            double leftD[] = (double[])(double[])combined.get(a);
                            double rightD[] = (double[])(double[])nextBounds.get(b);
                            if(rightD[1] - rightD[0] >= threshold)
                                continue;
                            // range of left contains range of right
                            if(leftD[0] <= rightD[0] && leftD[1] >= rightD[1])
                            {
                                nextCombined.add(new double[] {
//                                        rightD[0], rightD[1]
                                    leftD[0], leftD[1]
                                });
                                continue;
                            }
                            // range of left is contained by range of right
                            if(rightD[0] <= leftD[0] && rightD[1] >= leftD[1])
                            {
                                nextCombined.add(new double[] {
//                                        leftD[0], leftD[1]
                                    rightD[0], rightD[1]
                                });
                                continue;
                            }
                            //range of left starts above range of right but ends inside of range of right
                            if(leftD[0] < rightD[0] && leftD[1] > rightD[0] && leftD[1] < rightD[1])
                            {
                                nextCombined.add(new double[] {
//                                        rightD[0], leftD[1]
                                    leftD[0], rightD[1]
                                });
                                continue;
                            }
                            //range of right starts above range of left but ends inside of range of left
                            if(leftD[0] > rightD[0] && leftD[0] < rightD[1] && leftD[1] > rightD[1])
                                nextCombined.add(new double[] {
//                                        leftD[0], rightD[1]
                                    rightD[0], leftD[1]
                                });
                            //left is above right or right is above left
                            if(leftD[1] < rightD[0] || rightD[1] < leftD[0]) {
                                nextCombined.add(new double[] {
                                    leftD[0], leftD[1]
                                });
                                nextCombined.add(new double[] {
                                  rightD[0], rightD[1]
                              });
                            }
                        }

                    }

                    combined = nextCombined;
                }

                gnu.trove.TIntArrayList selectedIndices = new TIntArrayList();
                int indices[] = list_breakpoint.getSelectedIndices();
                for(int i = 0; i < indices.length; i++)
                    selectedIndices.add(indices[i]);

                for(int i = 0; i < model_breakpoint.getSize(); i++)
                {
                    if(selectedIndices.contains(i))
                        continue;
                    ncsa.evolutionhighway.datatype.ComparativeSpecies excCS = c.getComparativeSpecies(model_breakpoint.elementAt(i).toString());
                    java.util.List excBounds = excCS.getBreakpointBounds();
                    for(int a = 0; a < combined.size(); a++)
                    {
                        for(int b = 0; b < excBounds.size(); b++)
                        {
                            double leftD[] = (double[])(double[])combined.get(a);
                            double rightD[] = (double[])(double[])excBounds.get(b);
                            if(rightD[1] - rightD[0] >= threshold)
                                continue;
                            boolean exclude = false;
                          /*  if(leftD[0] <= rightD[0] && leftD[1] >= rightD[1])
                                exclude = true;
                            else
                            if(rightD[0] <= leftD[0] && rightD[1] >= leftD[1])
                                exclude = true;
                            else
                            if(leftD[0] < rightD[0] && leftD[1] > rightD[0] && leftD[1] < rightD[1])
                                exclude = true;
                            else
                            if(leftD[0] > rightD[0] && leftD[0] < rightD[1] && leftD[1] > rightD[1])
                                exclude = true;*/
                            //if(!exclude)
                              //  continue;
                            //combined.remove(a); //commenting this line out finds common among multiple species selection
                            a--;
                            break;
                        }

                    }

                }

            }

        } else
        if(src == list_synteny)
        {
            list_breakpoint.removeListSelectionListener(this);
            list_breakpoint.clearSelection();
            list_breakpoint.addListSelectionListener(this);
            ncsa.evolutionhighway.datatype.Genome g = canvas.getGenome();
            if(g == null)
            {
                msgAreaRef.setText("no genome selected");
                return;
            }
            java.lang.Object v[] = list_synteny.getSelectedValues();
            if(v.length == 0)
            {
                ncsa.evolutionhighway.datatype.Chromosome c;
                for(java.util.Iterator citer = g.getChromosomeIterator(); citer.hasNext(); c.clearVisibleOverride())
                    c = (ncsa.evolutionhighway.datatype.Chromosome)citer.next();

                rebuild(new HashMap());
            } else
            {
                for(java.util.Iterator citer = g.getChromosomeIterator(); citer.hasNext();)
                {
                    ncsa.evolutionhighway.datatype.Chromosome c = (ncsa.evolutionhighway.datatype.Chromosome)citer.next();
                    c.clearVisibleOverride();
                    int i = 0;
                    while(i < v.length)
                    {
                        c.setVisibleOverride(v[i].toString());
                        i++;
                    }
                }

                rebuild(new HashMap());
            }
            for(java.util.Iterator chromosomeIterator = g.getChromosomeIterator(); chromosomeIterator.hasNext();)
            {
                ncsa.evolutionhighway.datatype.Chromosome c = (ncsa.evolutionhighway.datatype.Chromosome)chromosomeIterator.next();
                if(v.length == 0)
                    c.setCompareMatches(new ArrayList(), null, -1D);
                else
                if(v.length == 1)
                {
                    java.lang.String spc_id = v[0].toString();
                    ncsa.evolutionhighway.datatype.ComparativeSpecies cs = c.getComparativeSpecies(spc_id);
                    java.util.List list = cs.getConservedSyntenyBounds();
                    c.setCompareMatches((java.util.ArrayList)(new ncsa.evolutionhighway.util.CompareNode.Leaf(list)).eval(), spc_id, -1D);
                } else
                {
                    ncsa.evolutionhighway.util.CompareNode.Branch active = new ncsa.evolutionhighway.util.CompareNode.Branch();
                    ncsa.evolutionhighway.util.CompareNode root = active;
                    java.lang.StringBuffer desc = new StringBuffer();
                    for(int i = 0; i < v.length; i++)
                    {
                        java.lang.String spc_id = v[i].toString();
                        ncsa.evolutionhighway.datatype.ComparativeSpecies cs = c.getComparativeSpecies(spc_id);
                        java.util.List list = cs.getConservedSyntenyBounds();
                        desc.append(spc_id);
                        if(i < v.length - 1)
                        {
                            ncsa.evolutionhighway.util.CompareNode.Leaf left = new ncsa.evolutionhighway.util.CompareNode.Leaf(list);
                            ncsa.evolutionhighway.util.CompareNode.Branch right = new ncsa.evolutionhighway.util.CompareNode.Branch();
                            active.setLeft(left);
                            active.setRight(right);
                            left.setParent(active);
                            right.setParent(active);
                            active = right;
                            desc.append(",");
                        } else
                        {
                            ncsa.evolutionhighway.util.CompareNode.Leaf l = new ncsa.evolutionhighway.util.CompareNode.Leaf(list);
                            active.setLeft(l);
                            active.setRight(l);
                            l.setParent(active);
                        }
                    }

                    c.setCompareMatches((java.util.ArrayList)root.eval(), desc.toString(), -1D);
                }
            }

        }
    }
}
