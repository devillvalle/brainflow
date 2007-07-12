package com.brainflow.colormap;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.data.RGBAImage;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.utils.IRange;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Mar 10, 2005
 * Time: 12:45:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IColorMap {

    public static final int MAXIMUM_INTERVALS = 1000;

    public int getMapSize();

    public ColorInterval getInterval(int index);

    public Color getColor(double value);

    

    public double getMaximumValue();

    public double getMinimumValue();

    public ListIterator<ColorInterval> iterator();

    public RGBAImage getRGBAImage(IImageData2D data);

    public AbstractColorBar createColorBar();

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

}
