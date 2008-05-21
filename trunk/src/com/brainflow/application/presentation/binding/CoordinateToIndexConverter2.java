package com.brainflow.application.presentation.binding;

import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace3D;
import com.brainflow.math.Index3D;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.RProperty;
import net.java.dev.properties.WProperty;
import net.java.dev.properties.container.ObservableWrapper;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Nov 16, 2007
 * Time: 6:01:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateToIndexConverter2 extends ObservableWrapper.ReadWrite<Integer> {

    private Axis axis;

    private IImageSpace3D space;

    public CoordinateToIndexConverter2(BaseProperty<AnatomicalPoint3D> property, IImageSpace3D _space, Axis _axis) {
        super(property);
        axis = _axis;
        space = _space;

    }

    private AnatomicalPoint3D getValue() {
        RProperty<AnatomicalPoint3D> prop = (RProperty<AnatomicalPoint3D>) getProperty();
        return prop.get();
    }

    private Index3D getGridValue() {
        AnatomicalPoint3D ap = getValue();
        System.out.println("coordinate value : " + ap);
        float[] gpt = space.worldToGrid((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
        System.out.println("grid value : " + Arrays.toString(gpt));
        return new Index3D(Math.round(gpt[0]), Math.round(gpt[1]), Math.round(gpt[2]));

    }

    @Override
    public Integer get() {
        AnatomicalPoint3D ap = getValue();

        float ret;
        if (axis == Axis.X_AXIS) {
            ret = space.worldToGridX((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
        } else if (axis == Axis.Y_AXIS) {
            ret = space.worldToGridY((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
        } else if (axis == Axis.Z_AXIS) {
            ret = space.worldToGridZ((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
        } else {
            throw new AssertionError("illegal image axis : " + axis);
        }

        return Math.round(ret);
    }

    @Override
    public void set(Integer i) {
        System.out.println("setting integer value : " + i);
        
        Index3D voxel = getGridValue();
        System.out.println("current grid value " + voxel);

        if (axis == Axis.X_AXIS) {
            System.out.println("x axis");
            voxel = new Index3D(i, voxel.i2(), voxel.i3());

        } else if (axis == Axis.Y_AXIS) {
            System.out.println("y axis");
            voxel = new Index3D(voxel.i1(), i, voxel.i3());
        } else if (axis == Axis.Z_AXIS) {
            System.out.println("z axis");
            voxel = new Index3D(voxel.i1(), voxel.i2(), i);
        } else {
            throw new AssertionError("illegal image axis : " + axis);
        }

        System.out.println("new voxel is " + voxel);

        float[] ret = space.gridToWorld(voxel.i1(),  voxel.i2(), voxel.i3());
        AnatomicalPoint3D nap = new AnatomicalPoint3D(getValue().getAnatomy(), ret[0], ret[1], ret[2]);

        System.out.println("new point : " + nap);
        WProperty<AnatomicalPoint3D> wprop = (WProperty<AnatomicalPoint3D>) getProperty();
        wprop.set(nap);
    }


}