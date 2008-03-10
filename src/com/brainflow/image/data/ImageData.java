package com.brainflow.image.data;

import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.utils.Index3D;
import com.brainflow.utils.DataType;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 9, 2008
 * Time: 9:10:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageData {


    public static IImageData3D createConstantData(final double value, final ImageSpace3D space) {
        return new AbstractImageData3D(space, DataType.DOUBLE) {
            public double getValue(int index) {
                return value;
            }

            public void setValue(int idx, double val) {
                throw new UnsupportedOperationException();
            }

            public double minValue() {
                return value;
            }

            public double maxValue() {
                return value;
            }

            public ImageIterator iterator() {
                return new BasicImageData3D.Iterator3D(this);
            }

            public double getRealValue(double realx, double realy, double realz, InterpolationFunction3D interp) {
                return value;
            }

            public double getValue(double x, double y, double z, InterpolationFunction3D interp) {
                return value;
            }

            public double getValue(int x, int y, int z) {
                return value;
            }

            public void setValue(int x, int y, int z, double val) {
                throw new UnsupportedOperationException();
            }
        };



    }
}
