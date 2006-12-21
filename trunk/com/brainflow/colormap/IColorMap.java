package com.brainflow.colormap;

import com.brainflow.image.data.IImageData;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Mar 10, 2005
 * Time: 12:45:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IColorMap {

    public static final int MAXIMUM_INTERVALS = 256;

    public int getMapSize();

    public ColorInterval getInterval(int index);

    public Color getColor(double value);

    public void setMapSize(int size);

    public double getMaximumValue();

    public double getMinimumValue();

    public Iterator<ColorInterval> iterator();

    public byte[][] getRGBAComponents(IImageData data);

    public byte[] getInterleavedRGBAComponents(IImageData data);

    public AbstractColorBar createColorBar();

    public boolean isScaleable();

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

}
