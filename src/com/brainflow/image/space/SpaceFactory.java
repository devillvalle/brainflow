package com.brainflow.image.space;

import com.brainflow.image.iterators.XYZIterator;
import com.brainflow.image.LinearSet1D;
import com.brainflow.image.LinearSet3D;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.axis.AxisRange;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 12, 2004
 * Time: 2:15:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpaceFactory {


    public static IImageSpace createImageSpace(ImageAxis ... axes) {
        assert axes.length <= 3;

        if (axes.length > 3) {
            throw new IllegalArgumentException("Cannot create an IImageSpace with more than three axes");
        }

        if (axes.length == 1) {
            //return new ImageSpace1D();
            throw new UnsupportedOperationException();
        }
        else if (axes.length == 2) {
            return new ImageSpace2D(axes[0], axes[1]);
        }
        else if (axes.length == 3) {
            return new ImageSpace3D(axes[0], axes[1], axes[2]);
        }

        else throw new RuntimeException();

    }


    public static XYZIterator createXYZiterator(ImageSpace3D space) {
        AxisRange a1 = space.getImageAxis(Axis.X_AXIS).getRange();
        AxisRange a2 = space.getImageAxis(Axis.Y_AXIS).getRange();
        AxisRange a3 = space.getImageAxis(Axis.Z_AXIS).getRange();


        LinearSet1D xset = new LinearSet1D(a1.getBeginning().getX(), a1.getEnd().getX(), space.getDimension(Axis.X_AXIS));
        LinearSet1D yset = new LinearSet1D(a2.getBeginning().getX(), a2.getEnd().getX(), space.getDimension(Axis.Y_AXIS));
        LinearSet1D zset = new LinearSet1D(a3.getBeginning().getX(), a3.getEnd().getX(), space.getDimension(Axis.Z_AXIS));
        LinearSet3D pset = new LinearSet3D(xset, yset, zset);
        return pset.iterator();


    }
}