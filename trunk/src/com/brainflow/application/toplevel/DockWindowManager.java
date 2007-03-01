package com.brainflow.application.toplevel;

import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.event.DockableFrameListener;
import com.jidesoft.docking.event.DockableFrameEvent;
import com.jidesoft.swing.JideMenu;
import com.brainflow.application.actions.ActivateDockableFrameAction;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ActionUIFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 27, 2007
 * Time: 10:09:21 PM
 * To change this template use File | Settings | File Templates.
 */

class DockWindowManager implements DockableFrameListener {

    private HashMap<String, DockableFrame> windowMap = new HashMap<String, DockableFrame>();

    private JMenu dockMenu = new JideMenu("Window");


    public DockWindowManager() {
        dockMenu.setMnemonic('W');
    }

    public static DockWindowManager getInstance() {
        return (DockWindowManager) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.toplevel.DockWindowManager");
    }

    public DockableFrame createDockableFrame(String title, String iconLocation, int state, int side) {

        DockableFrame dframe = new DockableFrame(title,
                new ImageIcon(getClass().getClassLoader().getResource(iconLocation)));


        dframe.getContext().setInitMode(state);
        dframe.getContext().setInitSide(side);
        dframe.getContext().setInitIndex(0);

        dockMenu.add(ActionUIFactory.getInstance().createMenuItem(new ActivateDockableFrameAction(dframe)));
             

        windowMap.put(title, dframe);
        return dframe;

    }

    public DockableFrame createDockableFrame(String title, String iconLocation, int state, int side, int index) {

        DockableFrame dframe = new DockableFrame(title,
                new ImageIcon(getClass().getClassLoader().getResource(iconLocation)));


        dframe.getContext().setInitMode(state);
        dframe.getContext().setInitSide(side);
        dframe.getContext().setInitIndex(index);

        dframe.addDockableFrameListener(this);
       

        windowMap.put(title, dframe);
       
        dockMenu.add(ActionUIFactory.getInstance().createMenuItem(new ActivateDockableFrameAction(dframe)));
       
        return dframe;

    }


    public JMenu getDockMenu() {
        return dockMenu;
    }

    public DockableFrame getDockableFrame(String title) {
        return windowMap.get(title);
    }


    public void dockableFrameAdded(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameRemoved(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameShown(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameHidden(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameDocked(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameFloating(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameAutohidden(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameAutohideShowing(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameActivated(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameDeactivated(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameTabShown(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameTabHidden(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameMaximized(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dockableFrameRestored(DockableFrameEvent dockableFrameEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
