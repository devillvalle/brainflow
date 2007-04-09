package com.brainflow.core;

import com.brainflow.modes.*;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 27, 2004
 * Time: 11:35:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageCanvas2 extends JComponent implements InternalFrameListener {


    private ImageCanvasModel canvasModel = new ImageCanvasModel();


    private JDesktopPane desktopPane = new JDesktopPane();

    private Logger log = Logger.getLogger(ImageCanvas2.class.getName());

    private List<ImageViewInteractor> interactors = new ArrayList<ImageViewInteractor>();



    public ImageCanvas2() {
        super();
        setLayout(new BorderLayout());

        add(desktopPane, "Center");
        addInteractor(new CrosshairInteractor());
        addInteractor(new MouseWheelInteractor());
        addInteractor(new PanningInteractor());
    }



    public ImageCanvasModel getImageCanvasModel() {
        return canvasModel;
    }

    public void addInteractor(ImageViewInteractor interactor) {
        interactor.setView(getSelectedView());
        interactors.add(interactor);
    }

    public void removeInteractor(ImageViewInteractor interactor) {
        interactor.setView(null);
        interactors.remove(interactor);
    }


    public ImageView whichSelectedView(Point p) {
        Component c = desktopPane.findComponentAt(p);
        if (c instanceof ImageView) {
            return (ImageView) c;
        }

        Container ct = SwingUtilities.getAncestorOfClass(ImageView.class, c);

        if (ct != null & ct instanceof ImageView) {
            ImageView iv = (ImageView) ct;
            if (iv == canvasModel.getSelectedView())
                return iv;
        }

        return null;


    }

    public ImageView whichView(Component source, Point p) {
        p = SwingUtilities.convertPoint(source, p, desktopPane);
        Component c = desktopPane.findComponentAt(p);

        if (c instanceof ImageView) {
            return findParentView((ImageView) c);
        }

        Container ct = SwingUtilities.getAncestorOfClass(ImageView.class, c);

        if (ct != null & ct instanceof ImageView) {
            return findParentView((ImageView) ct);
        }

        return null;

    }

    private ImageView findParentView(ImageView view) {
        ImageView ret = view;

        while (view.getParent() instanceof ImageView) {
            ret = (ImageView) view.getParent();
            view = ret;
        }

        return ret;

    }

    public Component whichComponent(Point p) {

        return desktopPane.findComponentAt(p);


    }


    public IImagePlot whichPlot(Point p) {
        Component c = desktopPane.findComponentAt(p);
        Container ct = SwingUtilities.getAncestorOfClass(ImagePane.class, c);
        if (ct != null & ct instanceof ImageView) {
            ImageView iview = (ImageView) ct;
            Point relPoint = SwingUtilities.convertPoint(desktopPane, p, iview);
            return iview.whichPlot(relPoint);
        }

        return null;


    }

    public java.util.List<IImagePlot> getPlotList() {
        java.util.List<IImagePlot> plots = new ArrayList<IImagePlot>();
        for (ImageView view : canvasModel.getImageViews()) {
            plots.addAll(view.getPlots());
        }

        return plots;
    }

    public boolean isSelectedView(ImageView view) {
        if (view == canvasModel.getSelectedView())
            return true;
        else
            return false;
    }




    public java.util.List<ImageView> getViews() {
        return canvasModel.getImageViews();
    }


    public void setSelectedView(ImageView view) {
        canvasModel.setSelectedView(view);
    }

    public ImageView getSelectedView() {
        return canvasModel.getSelectedView();
    }


    public void removeImageView(ImageView view) {
      
        canvasModel.removeImageView(view);
        JInternalFrame[] frames = desktopPane.getAllFrames();
        for (int i=0; i<frames.length; i++) {
            if (frames[i].getContentPane() == view) {
                desktopPane.remove(frames[i]);
            }
        }


    }

    public void addImageView(ImageView view) {
      
        view.setSize(view.getPreferredSize());
        JInternalFrame jframe = new JInternalFrame("view", true, true, true, true);

        jframe.setContentPane(view);
        jframe.setSize(view.getSize());
        jframe.setVisible(true);
        jframe.addInternalFrameListener(this);
        //desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);


        canvasModel.addImageView(view);
        desktopPane.add(jframe);


    }



    public void moveToFront(ImageView view) {
        /// do nothing for now
    }





    private void updateSelection(ImageView view) {
        canvasModel.setSelectedView(view);
        for (ImageViewInteractor interactor : interactors) {
            interactor.setView(view);

        }
    }

    


    public void internalFrameDeactivated(InternalFrameEvent e) {


    }

    public void internalFrameOpened(InternalFrameEvent e) {

    }

    public void internalFrameClosing(InternalFrameEvent e) {

    }

    public void internalFrameClosed(InternalFrameEvent e) {

    }

    public void internalFrameIconified(InternalFrameEvent e) {

    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
       
    }

    public void internalFrameActivated(InternalFrameEvent e) {
       JInternalFrame frame = e.getInternalFrame();
       Component c = frame.getContentPane();
        if (c instanceof ImageView) {
            ImageView sel = (ImageView)c;
            updateSelection(sel);

        } else {
            updateSelection(null);
        }
    }
}
