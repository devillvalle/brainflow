package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.AnatomicalVolume;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 17, 2004
 * Time: 5:22:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TriplanarImageView extends ImageView {


    private int divisor = 4;

    private IImagePlot[] plots;
    private ImagePane[] panes;
    private int selectedIndex = 0;

    public TriplanarImageView(IImageDisplayModel dset) {
        super(dset);
        setBorder(BorderFactory.createRaisedBevelBorder());
        layoutGrid(null);
    }

    public void scheduleRepaint(DisplayChangeEvent e) {
        repaint();
    }

    public Point getCrosshairLocation(IImagePlot plot) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public AnatomicalPoint3D getAnatomicalLocation(Component source, Point screenPoint) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TriplanarImageView(IImageDisplayModel dset, Dimension panelDim) {
        super(dset);
        layoutGrid(panelDim);
    }


    private void layoutGrid(Dimension panelDim) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        AnatomicalVolume leadingDisplay = AnatomicalVolume.getCanonicalAxial();
        IImageDisplayModel dset = getImageDisplayModel();
        double[] bestDim = null;
        if (panelDim == null)
            bestDim = new double[]{dset.getImageAxis(leadingDisplay.XAXIS).getRange().getInterval(),
                    dset.getImageAxis(leadingDisplay.YAXIS).getRange().getInterval()};
        else {
            bestDim = new double[2];
            bestDim[0] = panelDim.getWidth();
            bestDim[1] = panelDim.getHeight();
        }


        plots = new IImagePlot[3];
        panes = new ImagePane[3];

        AnatomicalVolume[] av = leadingDisplay.getCanonicalOrthogonal();
        for (int i = 0; i < 3; i++) {
            //plots[i] = ImagePlotFactory.createDefaultImagePlot(dset, dset.getImageAxis(leadingDisplay.XAXIS).getRange(),
            //        dset.getImageAxis(leadingDisplay.YAXIS).getRange(), av[i]);

            //panes[i] = new ImagePane(plots[i], String.valueOf(i));
            //add(panes[i]);
        }
    }

    public IImagePlot whichPlot(Point p) {
        Component c = getComponentAt(p);
        Container ct = SwingUtilities.getAncestorOfClass(ImagePane.class, c);
        if (ct != null & (ct instanceof ImagePane)) {
            return ((ImagePane) ct).getImagePlot();
        }

        return null;
    }

    public ImagePane whichPane(int x, int y) {
        Component c = getComponentAt(x, y);
        if (c instanceof ImagePane)
            return (ImagePane) c;

        Container ct = SwingUtilities.getAncestorOfClass(ImagePane.class, c);
        if (ct != null & (ct instanceof ImagePane)) {
            return (ImagePane) ct;
        }

        return null;
    }

    public List<IImagePlot> getPlots() {
        List<IImagePlot> lst = new ArrayList<IImagePlot>();
        for (IImagePlot ip : plots) {
            lst.add(ip);
        }

        return lst;

    }

    public IImagePlot[] getActivatedPlots() {
        return plots;
    }


    public IImagePlot[] getOtherPlots(IImagePlot sel) {
        if (sel == plots[0]) {
            return new IImagePlot[]{plots[1], plots[2]};
        } else if (sel == plots[1]) {
            return new IImagePlot[]{plots[0], plots[2]};
        } else if (sel == plots[2]) {
            return new IImagePlot[]{plots[0], plots[1]};
        } else {
            // never should even get here.
            assert false;
        }

        return null;


    }


}
