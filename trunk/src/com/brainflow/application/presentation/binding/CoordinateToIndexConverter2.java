package com.brainflow.application.presentation.binding;

import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace3D;
import com.brainflow.math.Index3D;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.RProperty;
import net.java.dev.properties.WProperty;
import net.java.dev.properties.container.ObservableWrapper;
import net.java.dev.properties.container.BeanContainer;

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
        BeanContainer.bind(this);
        axis = _axis;
        space = _space;

    }

    private AnatomicalPoint3D getValue() {
        RProperty<AnatomicalPoint3D> prop = (RProperty<AnatomicalPoint3D>) getProperty();
        return prop.get();
    }

    private Index3D getGridValue() {
        AnatomicalPoint3D ap = getValue();
        float[] gpt = space.worldToGrid((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
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

        Index3D voxel = getGridValue();

        if (axis == Axis.X_AXIS) {
             voxel = new Index3D(i, voxel.i2(), voxel.i3());
        } else if (axis == Axis.Y_AXIS) {
            voxel = new Index3D(voxel.i1(), i, voxel.i3());
        } else if (axis == Axis.Z_AXIS) {
            voxel = new Index3D(voxel.i1(), voxel.i2(), i);
        } else {
            throw new AssertionError("illegal image axis : " + axis);
        }


        float[] ret = space.gridToWorld(voxel.i1(),  voxel.i2(), voxel.i3());
        AnatomicalPoint3D nap = new AnatomicalPoint3D(getValue().getAnatomy(), ret[0], ret[1], ret[2]);

        WProperty<AnatomicalPoint3D> wprop = (WProperty<AnatomicalPoint3D>) getProperty();
        wprop.set(nap);
    }


}