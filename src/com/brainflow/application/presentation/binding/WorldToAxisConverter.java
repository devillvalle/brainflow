package com.brainflow.application.presentation.binding;

import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace3D;
import com.brainflow.math.Index3D;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.RProperty;
import net.java.dev.properties.WProperty;
import net.java.dev.properties.Property;
import net.java.dev.properties.container.ObservableWrapper;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Nov 16, 2007
 * Time: 6:01:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorldToAxisConverter extends ObservableWrapper.ReadWrite<Double> {

    private Axis axis;

    private IImageSpace3D space;

    public WorldToAxisConverter(BaseProperty<AnatomicalPoint3D> property, IImageSpace3D _space, Axis _axis) {
        super(property);
        axis = _axis;
        space = _space;

    }

    private AnatomicalPoint3D getValue() {
        RProperty<AnatomicalPoint3D> prop = (RProperty<AnatomicalPoint3D>) getProperty();
        return prop.get();
    }



    @Override
    public Double get() {

        AnatomicalPoint3D ap = getValue();

        double ret;
        if (axis == Axis.X_AXIS) {
            ret = space.worldToGridX((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
        } else if (axis == Axis.Y_AXIS) {
            ret = space.worldToGridY((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
        } else if (axis == Axis.Z_AXIS) {
            ret = space.worldToGridZ((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
        } else {
            throw new AssertionError("illegal image axis : " + axis);
        }

        return (double)Math.round(ret);
    }

    @Override
    public void set(Double val) {
        Property<AnatomicalPoint3D> wprop = (Property<AnatomicalPoint3D>) getProperty();
        AnatomicalPoint3D old = wprop.get();
        if (axis == Axis.X_AXIS) {
            wprop.set(new AnatomicalPoint3D(old.getAnatomy(), val, old.getY(), old.getZ()));
        } else if (axis == Axis.Y_AXIS) {
            wprop.set(new AnatomicalPoint3D(old.getAnatomy(), old.getX(), val, old.getZ()));

        } else if (axis == Axis.Z_AXIS) {
            wprop.set(new AnatomicalPoint3D(old.getAnatomy(), old.getX(), old.getY(), val));
        } else {
            throw new AssertionError("illegal image axis : " + axis);
        }

        
    }


}