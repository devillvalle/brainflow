package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.jgoodies.binding.list.SelectionInList;

import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 20, 2004
 * Time: 9:57:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class IconImageView extends ImageView implements Icon {


    private IImagePlot iplot;

    private final int ICON_WIDTH = 127;
    private final int ICON_HEIGHT = 128;

    //private BasicImageLayerRenderer imageRenderer = new BasicImageLayerRenderer();

    private JLabel labelView;

    private Logger log = Logger.getLogger(IconImageView.class.getName());

    public IconImageView(IImageDisplayModel dset) {
        super(dset);
        log.info("creating image plot");

        // iplot = ImagePlotFactory.createDefaultImagePlot(dset,
        //        dset.getImageAxis(AnatomicalVolume.getCanonicalAxial().XAXIS).getRange(),
        //        dset.getImageAxis(AnatomicalVolume.getCanonicalAxial().YAXIS).getRange(), AnatomicalVolume.getCanonicalAxial());


        labelView = new JLabel(this);
        setLayout(new BorderLayout());
        add(labelView, BorderLayout.CENTER);


    }

    public void scheduleRepaint(DisplayChangeEvent e) {
        repaint();
    }


    public SelectionInList getPlotSelection() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public IImagePlot getSelectedPlot() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public RenderedImage captureImage() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AnatomicalPoint3D getAnatomicalLocation(Component source, Point screenPoint) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Point getCrosshairLocation(IImagePlot plot) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        System.out.println("painting icon ...");
        System.out.println("x = " + x);
        System.out.println("y = " + y);

        Graphics2D g2 = (Graphics2D) g;
        //imageRenderer.drawLayers(g2, getImageDisplayModel(), iplot, new Rectangle2D.Double(x, y, ICON_WIDTH, ICON_HEIGHT));

    }


    public int getIconWidth() {
        return ICON_WIDTH;
    }

    public int getIconHeight() {
        return ICON_HEIGHT;
    }

    public IImagePlot whichPlot(Point p) {
        // check if in bounds first ...
        return iplot;
    }

    public List<IImagePlot> getPlots() {
        List<IImagePlot> ret = new ArrayList<IImagePlot>();
        ret.add(iplot);
        return ret;
    }


    public void advance() {

    }

    public void retreat() {

    }

    public Dimension getPreferredSize() {
        return new Dimension(ICON_WIDTH, ICON_HEIGHT);
    }


}
