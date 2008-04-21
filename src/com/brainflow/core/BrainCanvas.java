package com.brainflow.core;

import com.brainflow.image.space.IImageSpace;
import com.brainflow.modes.CrosshairInteractor;
import com.brainflow.modes.ImageViewInteractor;
import com.brainflow.modes.MouseWheelInteractor;
import com.brainflow.modes.PanningInteractor;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListDataEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 27, 2004
 * Time: 11:35:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrainCanvas extends JComponent implements InternalFrameListener {

    private static final Logger log = Logger.getLogger(BrainCanvas.class.getName());

    private BrainCanvasModel canvasModel = new BrainCanvasModel();


    private JDesktopPane desktopPane = new JDesktopPane();

    private List<ImageViewInteractor> interactors = new ArrayList<ImageViewInteractor>();

    private DisplayModelListener modelListener = new DisplayModelListener();


    public BrainCanvas() {
        super();
        setLayout(new BorderLayout());

        add(desktopPane, "Center");
        desktopPane.setOpaque(false);
        addInteractor(new CrosshairInteractor());
        addInteractor(new MouseWheelInteractor());
        addInteractor(new PanningInteractor());
    }


    public BrainCanvasModel getImageCanvasModel() {
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
        if (ct != null && ct instanceof ImageView) {
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
        return view == canvasModel.getSelectedView();
    }


    public List<ImageView> getViews(IImageDisplayModel model) {
        List<ImageView> views = getViews();
        List<ImageView> ret = new ArrayList<ImageView>();

        for (ImageView view : views) {
            if (view.getModel() == model) {
                ret.add(view);
            }
        }

        return ret;


    }

    public List<ImageView> getViews() {
        return canvasModel.getImageViews();
    }


    public void setSelectedView(ImageView view) {
        canvasModel.setSelectedView(view);
    }

    public ImageView getSelectedView() {
        return canvasModel.getSelectedView();
    }

    private void renameViews() {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        for (JInternalFrame frame : frames) {
            if (frame.getContentPane() instanceof ImageView) {
                ImageView view = (ImageView)frame.getContentPane();
                putTitle(frame, view);
            }

        }

    }


    public void removeImageView(ImageView view) {
        view.getModel().removeImageDisplayModelListener(modelListener);
        canvasModel.removeImageView(view);
        JInternalFrame[] frames = desktopPane.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            if (frames[i].getContentPane() == view) {
                desktopPane.remove(frames[i]);
            }
        }

        renameViews();
        desktopPane.repaint();


    }

    private void putTitle(JInternalFrame frame, ImageView view) {

        view.identifier.set("View [" + (canvasModel.indexOf(view) + 1) + "]");
        frame.setTitle(view.identifier.get());

    }


    public void addImageView(ImageView view) {

        view.setSize(view.getPreferredSize());
        JInternalFrame jframe = new JInternalFrame("view", true, true, true, true);

        view.getModel().addImageDisplayModelListener(modelListener);


        jframe.setContentPane(view);
        jframe.setSize(view.getSize());
        Dimension d = desktopPane.getSize();
        Point p = desktopPane.getLocation();
        jframe.setLocation((int) (p.x + .25 * d.width), (int) (p.y + .25 * d.height));

        jframe.setVisible(true);
        jframe.addInternalFrameListener(this);

        //desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);


        canvasModel.addImageView(view);
        putTitle(jframe, view);

        

        desktopPane.add(jframe);
        jframe.moveToFront();


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

       if (e.getInternalFrame().getContentPane() instanceof ImageView) {
           ImageView view = (ImageView)e.getInternalFrame().getContentPane();
           removeImageView(view);
           //e.getInternalFrame().dispose();
           System.out.println("number of internal frames ? " + desktopPane.getAllFrames().length);
       }
    }


    public void internalFrameIconified(InternalFrameEvent e) {

    }

    public void internalFrameDeiconified(InternalFrameEvent e) {

    }

    public void internalFrameActivated(InternalFrameEvent e) {
        JInternalFrame frame = e.getInternalFrame();

        Component c = frame.getContentPane();
        if (c instanceof ImageView) {
            ImageView sel = (ImageView) c;
            updateSelection(sel);

        } else {
            updateSelection(null);
        }
    }


    class DisplayModelListener implements ImageDisplayModelListener {
        public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void intervalAdded(ListDataEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void intervalRemoved(ListDataEvent e) {
            IImageDisplayModel model = (IImageDisplayModel) e.getSource();
            if (model.getNumLayers() == 0) {
                List<ImageView> views = getViews(model);
                for (ImageView view : views) {
                    removeImageView(view);
                }
            }
        }

        public void contentsChanged(ListDataEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

}

