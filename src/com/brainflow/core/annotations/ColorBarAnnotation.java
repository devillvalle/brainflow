package com.brainflow.core.annotations;

import com.brainflow.colormap.AbstractColorBar;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.IImagePlot;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 6, 2007
 * Time: 2:01:03 PM
 */
public class ColorBarAnnotation extends AbstractAnnotation {

    public static final String ORIENTATION_PROPERTY = "orientation";

    private int selectedLayer = -1;

    private IImageDisplayModel model;

    private int orientation = SwingUtilities.VERTICAL;

    private int position = SwingUtilities.RIGHT;


    private int maximumWidth = 30;

    private int maximumHeight = 800;

    private float maximumPercentWidth = .2f;

    private float maximumPercentHeight = .8f;

    private int xmargin = 20;

    private int ymargin = 20;


    private AbstractColorBar colorBar = null;

    private BufferedImage colorBarImage = null;


    public ColorBarAnnotation(IImageDisplayModel _model) {
        model = _model;


    }

    public IAnnotation safeCopy() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void draw(Graphics2D g2d, Rectangle2D plotArea, IImagePlot plot) {
        if (!isVisible())  return;
        
        int selectedLayer = model.getSelectedIndex();

        if (colorBar == null) {
            colorBar = model.getImageLayer(selectedLayer).getImageLayerParameters().getColorMap().getParameter().createColorBar();
            colorBar.setOrientation(SwingUtilities.VERTICAL);
            colorBarImage = colorBar.getImage();
        }


        Rectangle2D cbounds = g2d.getClipBounds();

        int roomx = (int) cbounds.getWidth() - xmargin;
        int roomy = (int) cbounds.getHeight() - ymargin;

        int width = Math.min(roomx, maximumWidth);
        width = (int) Math.min(width, maximumPercentWidth * cbounds.getWidth());

        int height = Math.min(roomy, maximumHeight);
        height = (int) Math.min(height, maximumPercentHeight * cbounds.getHeight());

        int xmin = (int) cbounds.getWidth() - width - xmargin;
        int ymin = ymargin;

        AffineTransform xform = AffineTransform.getTranslateInstance(xmin, ymin);
        xform.scale((float) width / (float) colorBarImage.getWidth(), (float) height / (float) colorBarImage.getHeight());
        g2d.drawRenderedImage(colorBarImage, xform);

    }


    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        int old = getOrientation();
        this.orientation = orientation;

        support.firePropertyChange(ColorBarAnnotation.ORIENTATION_PROPERTY, old, getOrientation());
    }
}
