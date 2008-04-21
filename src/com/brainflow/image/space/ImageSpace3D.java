package com.brainflow.image.space;

import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.utils.*;
import com.brainflow.math.Matrix4f;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 18, 2004
 * Time: 9:00:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageSpace3D extends AbstractImageSpace {


    private IImageOrigin origin;

    private Matrix4f transform;

    public ImageSpace3D(ICoordinateSpace cspace) {
        this(new ImageAxis(cspace.getImageAxis(Axis.X_AXIS).getRange(), 1),
                new ImageAxis(cspace.getImageAxis(Axis.Y_AXIS).getRange(), 1),
                new ImageAxis(cspace.getImageAxis(Axis.Z_AXIS).getRange(), 1));

    }

    public ImageSpace3D(ImageSpace3D space) {

        this(space.getImageAxis(Axis.X_AXIS),
             space.getImageAxis(Axis.Y_AXIS),
             space.getImageAxis(Axis.Z_AXIS));
    }


    public ImageSpace3D(ImageAxis xaxis, ImageAxis yaxis, ImageAxis zaxis) {
        Anatomy3D check = Anatomy3D.matchAnatomy(xaxis.getAnatomicalAxis(), yaxis.getAnatomicalAxis(), zaxis.getAnatomicalAxis());
        if (check == null) {
            throw new IllegalArgumentException("could not initialize axes from supplied ImageAxes : " + xaxis + " : " + yaxis + ": " + zaxis);
        }


        setAnatomy(check);

        createImageAxes(3);

        initAxis(xaxis, Axis.X_AXIS);
        initAxis(yaxis, Axis.Y_AXIS);
        initAxis(zaxis, Axis.Z_AXIS);

        origin = new ImageOrigin3D(xaxis.getAnatomicalAxis().getMinDirection(), yaxis.getAnatomicalAxis().getMinDirection(),
                zaxis.getAnatomicalAxis().getMinDirection(), xaxis.getRange().getBeginning().getX(),
                yaxis.getRange().getBeginning().getX(), zaxis.getRange().getBeginning().getX());

        transform = getAnatomy().getReferenceTransform();

    }

    public Anatomy3D getAnatomy() {
        return (Anatomy3D) super.getAnatomy();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ImageSpace3D(ImageAxis xaxis, ImageAxis yaxis, ImageAxis zaxis, Matrix4f transform) {
        Anatomy3D check = Anatomy3D.matchAnatomy(xaxis.getAnatomicalAxis(), yaxis.getAnatomicalAxis(), zaxis.getAnatomicalAxis());
        if (check == null) {
            throw new IllegalArgumentException("could not initialize axes from supplied ImageAxes : " + xaxis + " : " + yaxis + ": " + zaxis);
        }


        setAnatomy(check);

        createImageAxes(3);

        initAxis(xaxis, Axis.X_AXIS);
        initAxis(yaxis, Axis.Y_AXIS);
        initAxis(zaxis, Axis.Z_AXIS);

        origin = new ImageOrigin3D(xaxis.getAnatomicalAxis().getMinDirection(), yaxis.getAnatomicalAxis().getMinDirection(),
                zaxis.getAnatomicalAxis().getMinDirection(), xaxis.getRange().getBeginning().getX(),
                yaxis.getRange().getBeginning().getX(), zaxis.getRange().getBeginning().getX());

    }


    public IDimension<Integer> getDimension() {
        return new Dimension3D<Integer>(getDimension(Axis.X_AXIS), getDimension(Axis.Y_AXIS), getDimension(Axis.Z_AXIS));
    }


    public IImageOrigin getImageOrigin() {
        return origin;
    }

    public AnatomicalPoint3D getCentroid() {

        ImageAxis a1 = getImageAxis(Axis.X_AXIS);
        ImageAxis a2 = getImageAxis(Axis.Y_AXIS);
        ImageAxis a3 = getImageAxis(Axis.Z_AXIS);

        AnatomicalPoint1D x = a1.getRange().getCenter();
        AnatomicalPoint1D y = a2.getRange().getCenter();
        AnatomicalPoint1D z = a3.getRange().getCenter();

        return new AnatomicalPoint3D(getAnatomy(), x.getX(), y.getX(), z.getX());
    }

    public Matrix4f getVoxelToWorldTransform() {
        throw new UnsupportedOperationException();
    }

    public Matrix4f getWorldToVoxelTransform() {
        throw new UnsupportedOperationException();
    }


    public Index3D pointToVoxel(Point3D pt) {
        int x = getImageAxis(Axis.X_AXIS).nearestSample(pt.getX());
        int y = getImageAxis(Axis.Y_AXIS).nearestSample(pt.getY());
        int z = getImageAxis(Axis.Z_AXIS).nearestSample(pt.getZ());
        return new Index3D(x, y, z);
    }

    /*public AnatomicalPoint3D convertPoint(AnatomicalPoint3D otherPoint3D) {
        Anatomy3D avol = otherPoint3D.getAnatomy();

        AnatomicalAxis xaxis = avol.findAxis(getAnatomicalAxis(Axis.X_AXIS));
        AnatomicalAxis yaxis = avol.findAxis(getAnatomicalAxis(Axis.Y_AXIS));
        AnatomicalAxis zaxis = avol.findAxis(getAnatomicalAxis(Axis.Z_AXIS));

        double zero = xaxis.convertValue(this.getAnatomicalAxis(Axis.X_AXIS), otherPoint3D.getX());
        double zero = yaxis.convertValue(this.getAnatomicalAxis(Axis.Y_AXIS), otherPoint3D.getY());
        double one = zaxis.convertValue(this.getAnatomicalAxis(Axis.Z_AXIS), otherPoint3D.getZ());

        return new AnatomicalPoint3D((Anatomy3D) getAnatomy(), zero, zero, one);


    } */


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("anatomy: " + getAnatomy().toString())
                .append("/n")
                .append("zero axis: " + getImageAxis(Axis.X_AXIS))
                .append("/n")
                .append("zero axis: " + getImageAxis(Axis.Y_AXIS))
                .append("/n")
                .append("one axis: " + getImageAxis(Axis.Z_AXIS));

        return sb.toString();


    }

}
