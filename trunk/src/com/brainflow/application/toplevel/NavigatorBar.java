package com.brainflow.application.toplevel;

import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import java.awt.*;

import org.jvnet.substance.skin.SubstanceSaharaLookAndFeel;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 26, 2007
 * Time: 10:26:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class NavigatorBar {

    JToolBar toolBar;

    public NavigatorBar() {
        toolBar = new JToolBar();
        toolBar.setLayout(new JideBoxLayout(toolBar));
        ImageIcon pageBack = new ImageIcon(getClass().getClassLoader().getResource("resources/icons/control_rewind_blue.png"));
        ImageIcon pageForward = new ImageIcon(getClass().getClassLoader().getResource("resources/icons/control_fastforward_blue.png"));
        ImageIcon forward = new ImageIcon(getClass().getClassLoader().getResource("resources/icons/control_play_blue.png"));
        ImageIcon back = new ImageIcon(getClass().getClassLoader().getResource("resources/icons/control_back_blue.png"));


        JTextField sliceField = new JTextField("34 of 68");

        toolBar.add(new JButton(pageBack), JideBoxLayout.FIX);
        toolBar.add(new JButton(back), JideBoxLayout.FIX);
        //toolBar.add(new JSlider(0,100), JideBoxLayout.FIX);
        toolBar.add(sliceField, JideBoxLayout.VARY);
        toolBar.add(new JButton(forward), JideBoxLayout.FIX);
        toolBar.add(new JButton(pageForward), JideBoxLayout.FIX);

    }

    public JToolBar getToolBar() {
        return toolBar;
    }


    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new SubstanceSaharaLookAndFeel());
        JFrame frame = new JFrame();
        frame.setSize(800,800);
        frame.add(new NavigatorBar().getToolBar(), BorderLayout.NORTH);
        frame.setVisible(true);
    }
}
