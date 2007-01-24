/*
* BrainFrame.java
*
* Created on April 23, 2003, 2:14 PM
*/

package com.brainflow.application;

import com.jidesoft.docking.DefaultDockableHolder;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;


/**
 * @author Bradley
 */


public class BrainFrame extends DefaultDockableHolder {


    private static Logger log = Logger.getLogger(BrainFrame.class.getName());


    public BrainFrame() {

    }

    private void initLookAndFeel() {


    }


    public static void main(String[] args) {
        //com.incors.plaf.alloy.AlloyLookAndFeel.setProperty("alloy.licenseCode", "v#ej_technologies#uwbjzx#e6pck8");
        //com.incors.plaf.alloy.AlloyLookAndFeel.setProperty("alloy.isLookAndFeelFrameDecoration", "true");
        //com.incors.plaf.alloy.AlloyLookAndFeel alloylookandfeel = new com.incors.plaf.alloy.AlloyLookAndFeel();
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFrame", "7.YTcWgxxjx1xjSnUqG:U1ldgGetfRn1");


        try {
            UIManager.setLookAndFeel(new com.jidesoft.plaf.eclipse.EclipseWindowsLookAndFeel());
            //UIManager.setLookAndFeel(new com.jgoodies.looks.plastic.Plastic3DLookAndFeel());
            //LookAndFeelFactory.installJideExtension();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }


        BrainFrame bf = new BrainFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        bf.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight() - 50);
        bf.setVisible(true);

    }


}

