package com.brainflow.image.data;

import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.image.interpolation.InterpolationFunction2D;
import com.brainflow.image.iterators.ImageIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 1:10:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageData2D extends IImageData {


    public int indexOf(int x, int y);

    public double getValue(double x, double y, InterpolationFunction2D interp);

    public double getRealValue(double realx, double realy, InterpolationFunction2D interp);

    public double getValue(int x, int y);

    public float getFloat(int x, int y);

    public int getInt(int x, int y);

    public void setValue(int x, int y, double val);

    public void setFloat(int x, int y, float val);

    public void setInt(int x, int y, int val);

    public ImageIterator iterator();

    public ImageSpace2D getImageSpace();
}
