package com.brainflow.image.space;

import com.brainflow.image.anatomy.*;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.utils.*;
import com.brainflow.utils.Index3D;
import com.brainflow.math.Matrix4f;
import com.brainflow.math.Vector3f;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 18, 2004
 * Time: 9:00:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageSpace3D extends AbstractImageSpace {


    private IImageOrigin origin;

    private ImageMapping3D mapping;

    private final int planeSize;

    private final int dim0;


    public ImageSpace3D(ICoordinateSpace cspace) {
        this(new ImageAxis(cspace.getImageAxis(Axis.X_AXIS).getRange(), 1),
                new ImageAxis(cspace.getImageAxis(Axis.Y_AXIS).getRange(), 1),
                new ImageAxis(cspace.getImageAxis(Axis.Z_AXIS).getRange(), 1), null);

        //todo total hack


    }

    public ImageSpace3D(ImageSpace3D space) {

        this(space.getImageAxis(Axis.X_AXIS),
                space.getImageAxis(Axis.Y_AXIS),
                space.getImageAxis(Axis.Z_AXIS), null);
    }


    public ImageSpace3D(ImageAxis xaxis, ImageAxis yaxis, ImageAxis zaxis) {
        this(xaxis, yaxis, zaxis, null);

    }

    public ImageSpace3D(ImageAxis xaxis, ImageAxis yaxis, ImageAxis zaxis, ImageMapping3D mapping) {
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

        if (mapping == null) {
            AnatomicalPoint pt = origin.getOrigin();
            this.mapping = new AffineMapping3D(new Vector3f((float) pt.getValue(Axis.X_AXIS.getId()),
                    (float) pt.getValue(Axis.Y_AXIS.getId()),
                    (float) pt.getValue(Axis.Z_AXIS.getId())),
                    new Vector3f((float) xaxis.getSpacing(), (float) yaxis.getSpacing(), (float) zaxis.getSpacing()), check);
        } else {
            this.mapping = mapping;
        }

        planeSize = getDimension(Axis.X_AXIS) * getDimension(Axis.Y_AXIS);
        dim0 = getDimension(Axis.X_AXIS);


    }


    @Override
    public Anatomy3D getAnatomy() {
        return (Anatomy3D) super.getAnatomy();    //To change body of overridden methods use File | Settings | File Templates.
    }


    public ImageMapping3D getMapping() {
        return mapping;
    }


    public Dimension3D<Integer> getDimension() {
        return new Dimension3D<Integer>(getDimension(Axis.X_AXIS), getDimension(Axis.Y_AXIS), getDimension(Axis.Z_AXIS));
    }


    public IImageOrigin getImageOrigin() {
        return origin;
    }

    public final Index3D indexToGrid(int idx, Index3D voxel) {
        voxel.setZ(idx / planeSize);
        int remainder = (idx % planeSize);
        voxel.setY(remainder / getDimension(Axis.X_AXIS));
        voxel.setX(remainder % getDimension(Axis.X_AXIS));

        return voxel;
    }

    public final int indexToGridX(int idx) {
        int remainder = (idx % planeSize);
        return remainder % getDimension(Axis.X_AXIS);

    }

    public final int indexToGridY(int idx) {
        int remainder = (idx % planeSize);
        return remainder / getDimension(Axis.X_AXIS);
    }

    public final int indexToGridZ(int idx) {
        return idx / planeSize;
    }

    public float worldToGridX(float x, float y, float z) {
        return mapping.worldToGridX(x, y, z);
    }

    public float worldToGridY(float x, float y, float z) {
        return mapping.worldToGridY(x, y, z);
    }

    public float worldToGridZ(float x, float y, float z) {
        return mapping.worldToGridZ(x, y, z);
    }

    public float gridToWorldX(float x, float y, float z) {
        return mapping.gridToWorldX(x, y, z);
    }

    public float gridToWorldY(float x, float y, float z) {
        return mapping.gridToWorldY(x, y, z);
    }

    public float gridToWorldZ(float x, float y, float z) {
        return mapping.gridToWorldZ(x, y, z);
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


    public Index3D pointToVoxel(Point3D pt) {
        int x = getImageAxis(Axis.X_AXIS).nearestSample(pt.getX());
        int y = getImageAxis(Axis.Y_AXIS).nearestSample(pt.getY());
        int z = getImageAxis(Axis.Z_AXIS).nearestSample(pt.getZ());
        return new Index3D(x, y, z);
    }

    public String toString() {
        return "ImageSpace3D{" +
                "x axis=" + this.getImageAxis(Axis.X_AXIS) +
                "y axis=" + this.getImageAxis(Axis.Y_AXIS) +
                "z axis=" + this.getImageAxis(Axis.Z_AXIS) +
                "origin=" + origin +
                ", mapping=" + mapping +
                '}';
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


}
