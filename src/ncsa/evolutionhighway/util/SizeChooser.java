// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: fullnames fieldsfirst 
// Source File Name:   SizeChooser.java

package ncsa.evolutionhighway.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.accessibility.AccessibleContext;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class SizeChooser extends javax.swing.JPanel
    implements java.awt.event.ActionListener
{

    private javax.swing.JButton button_ok;
    private javax.swing.JButton button_cancel;
    private javax.swing.JTextField field_width;
    private javax.swing.JTextField field_height;
    private javax.swing.JDialog dialog;
    private java.awt.Insets insets_0;
    private java.awt.Insets insets_5;
    private int window_status;
    private int w;
    private int h;

    public SizeChooser()
    {
        insets_0 = new Insets(0, 0, 0, 0);
        insets_5 = new Insets(5, 5, 5, 5);
        window_status = -1;
        w = 640;
        h = 480;
        field_width = new JTextField(java.lang.Integer.toString(w), 10);
        field_height = new JTextField(java.lang.Integer.toString(h), 10);
        button_ok = new JButton("OK");
        button_ok.addActionListener(this);
        button_cancel = new JButton("Cancel");
        button_cancel.addActionListener(this);
        java.awt.GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        javax.swing.JLabel label_width = new JLabel("width: ");
        javax.swing.JLabel label_height = new JLabel("height: ");
        layout.addLayoutComponent(label_width, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 0, insets_5, 0, 0));
        add(label_width);
        layout.addLayoutComponent(field_width, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 10, 2, insets_5, 0, 0));
        add(field_width);
        layout.addLayoutComponent(label_height, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 10, 0, insets_5, 0, 0));
        add(label_height);
        layout.addLayoutComponent(field_height, new GridBagConstraints(1, 1, 1, 1, 1.0D, 0.0D, 10, 2, insets_5, 0, 0));
        add(field_height);
        javax.swing.JLabel filler = new JLabel();
        layout.addLayoutComponent(filler, new GridBagConstraints(0, 2, 2, 1, 1.0D, 1.0D, 10, 1, insets_0, 0, 0));
        add(filler);
        javax.swing.JSeparator separator = new JSeparator();
        layout.addLayoutComponent(separator, new GridBagConstraints(0, 3, 2, 1, 1.0D, 0.0D, 10, 2, insets_0, 0, 0));
        add(separator);
        javax.swing.JPanel buttonPanel = new JPanel();
        java.awt.GridBagLayout buttonLayout = new GridBagLayout();
        buttonPanel.setLayout(buttonLayout);
        javax.swing.JLabel buttonFiller = new JLabel();
        buttonLayout.addLayoutComponent(buttonFiller, new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, 10, 2, insets_0, 0, 0));
        buttonPanel.add(buttonFiller);
        buttonLayout.addLayoutComponent(button_ok, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 10, 0, insets_0, 0, 0));
        buttonPanel.add(button_ok);
        buttonLayout.addLayoutComponent(button_cancel, new GridBagConstraints(2, 0, 1, 1, 0.0D, 0.0D, 10, 0, insets_0, 0, 0));
        buttonPanel.add(button_cancel);
        layout.addLayoutComponent(buttonPanel, new GridBagConstraints(0, 4, 2, 1, 1.0D, 0.0D, 10, 2, insets_5, 0, 0));
        add(buttonPanel);
    }

    public void actionPerformed(java.awt.event.ActionEvent e)
    {
        java.lang.Object src = e.getSource();
        if(src == button_ok)
        {
            window_status = 1;
            w = java.lang.Integer.parseInt(field_width.getText());
            h = java.lang.Integer.parseInt(field_height.getText());
            dialog.dispose();
        } else
        if(src == button_cancel)
        {
            window_status = -1;
            dialog.dispose();
        }
    }

    protected javax.swing.JDialog createDialog(java.awt.Component parent)
        throws java.awt.HeadlessException
    {
        java.awt.Frame frame = (parent instanceof java.awt.Frame) ? (java.awt.Frame)parent : (java.awt.Frame)javax.swing.SwingUtilities.getAncestorOfClass(java.awt.Frame.class, parent);
        java.lang.String title = "Choose output size";
        getAccessibleContext().setAccessibleDescription(title);
        dialog = new JDialog(frame, title, true);
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

    public int getChosenHeight()
    {
        return h;
    }

    public int getChosenWidth()
    {
        return w;
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
}
