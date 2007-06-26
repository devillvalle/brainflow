package com.brainflow.image.space;

import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.utils.Index3D;
import com.brainflow.utils.Point3D;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 18, 2004
 * Time: 9:00:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageSpace3D extends AbstractImageSpace {


    private IImageOrigin origin;


    public ImageSpace3D(ICoordinateSpace cspace) {
        // todo how can coordinate space be argument here, without sampling grid info?
        Anatomy3D anat = (Anatomy3D)cspace.getAnatomy();
        Anatomy3D check = Anatomy3D.matchAnatomy(anat.XAXIS, anat.YAXIS, anat.ZAXIS);

        assert check != null;

        setAnatomy(check);

        createImageAxes(3);

        initAxis(new ImageAxis(cspace.getImageAxis(Axis.X_AXIS).getRange(), 1), Axis.X_AXIS);
        initAxis(new ImageAxis(cspace.getImageAxis(Axis.Y_AXIS).getRange(), 1), Axis.Y_AXIS);
        initAxis(new ImageAxis(cspace.getImageAxis(Axis.Z_AXIS).getRange(), 1), Axis.Z_AXIS);

        origin = cspace.getImageOrigin();

    }

    public ImageSpace3D(ImageAxis xaxis, ImageAxis yaxis, ImageAxis zaxis) {
        Anatomy3D check = Anatomy3D.matchAnatomy(xaxis.getAnatomicalAxis(), yaxis.getAnatomicalAxis(), zaxis.getAnatomicalAxis());
        assert check != null;

        setAnatomy(check);

        createImageAxes(3);

        initAxis(xaxis, Axis.X_AXIS);
        initAxis(yaxis, Axis.Y_AXIS);
        initAxis(zaxis, Axis.Z_AXIS);

        origin = new ImageOrigin3D(xaxis.getAnatomicalAxis().getMinDirection(), yaxis.getAnatomicalAxis().getMinDirection(),
                zaxis.getAnatomicalAxis().getMinDirection(), xaxis.getRange().getBeginning().getX(),
                yaxis.getRange().getBeginning().getX(), zaxis.getRange().getBeginning().getX());

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

        return new AnatomicalPoint3D((Anatomy3D) getAnatomy(), x.getX(), y.getX(), z.getX());


    }


    public ImageSpace3D(ImageSpace3D space) {
        setAnatomy(space.getAnatomy());
        createImageAxes(3);
        initAxis(space.getImageAxis(Axis.X_AXIS), Axis.X_AXIS);
        initAxis(space.getImageAxis(Axis.Y_AXIS), Axis.Y_AXIS);
        initAxis(space.getImageAxis(Axis.Z_AXIS), Axis.Z_AXIS);
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

        double x = xaxis.convertValue(this.getAnatomicalAxis(Axis.X_AXIS), otherPoint3D.getX());
        double y = yaxis.convertValue(this.getAnatomicalAxis(Axis.Y_AXIS), otherPoint3D.getY());
        double z = zaxis.convertValue(this.getAnatomicalAxis(Axis.Z_AXIS), otherPoint3D.getZ());

        return new AnatomicalPoint3D((Anatomy3D) getAnatomy(), x, y, z);


    } */


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("anatomy: " + getAnatomy().toString())
                .append("/n")
                .append("x axis: " + getImageAxis(Axis.X_AXIS))
                .append("/n")
                .append("y axis: " + getImageAxis(Axis.Y_AXIS))
                .append("/n")
                .append("z axis: " + getImageAxis(Axis.Z_AXIS));

        return sb.toString();


    }

}
