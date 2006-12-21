package com.brainflow.image.anatomy;

import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.ImageSpace3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 22, 2006
 * Time: 12:31:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class AnatomicalPointOnGrid {


    private AnatomicalVolume displayAnatomy;
    private ImageSpace3D voxelSpace;
    private AnatomicalPoint3D displayPoint;
    private AnatomicalPoint3D dataVoxel;


    public AnatomicalPointOnGrid(AnatomicalPoint3D dPoint, ImageSpace3D voxelSpace) {
        this.displayAnatomy = dPoint.getAnatomy();
        this.voxelSpace = voxelSpace;

        dataVoxel = new AnatomicalPoint3D((AnatomicalVolume) voxelSpace.getAnatomy(), 0, 0, 0);

        displayPoint = new AnatomicalPoint3D(dPoint.getAnatomy(), dPoint.getX(),
                dPoint.getY(), dPoint.getZ());

    }

    public AnatomicalPointOnGrid(AnatomicalVolume displayAnatomy, ImageSpace3D voxelSpace) {
        this.displayAnatomy = displayAnatomy;
        this.voxelSpace = voxelSpace;

        dataVoxel = new AnatomicalPoint3D((AnatomicalVolume) voxelSpace.getAnatomy(), 0, 0, 0);
        displayPoint = new AnatomicalPoint3D(displayPoint.getAnatomy(), 0, 0, 0);

    }

    public AnatomicalVolume getDisplayAnatomy() {
        return displayAnatomy;
    }


    public ImageSpace3D getVoxelSpace() {
        return voxelSpace;
    }

    public void setVoxelSpace(ImageSpace3D voxelSpace) {
        this.voxelSpace = voxelSpace;
    }


    private void recomputePoint() {

        ImageAxis display_xaxis = voxelSpace.getImageAxis(displayAnatomy.XAXIS, true);
        ImageAxis display_yaxis = voxelSpace.getImageAxis(displayAnatomy.YAXIS, true);
        ImageAxis display_zaxis = voxelSpace.getImageAxis(displayAnatomy.ZAXIS, true);

        double x = display_xaxis.valueOf(dataVoxel.getValue(displayAnatomy.XAXIS));
        double y = display_yaxis.valueOf(dataVoxel.getValue(displayAnatomy.YAXIS));
        double z = display_zaxis.valueOf(dataVoxel.getValue(displayAnatomy.ZAXIS));

        displayPoint.setX(x);
        displayPoint.setY(y);
        displayPoint.setZ(z);


    }


    private void recomputeVoxel() {
        ImageAxis voxel_xaxis = voxelSpace.getImageAxis(Axis.X_AXIS);
        ImageAxis voxel_yaxis = voxelSpace.getImageAxis(Axis.Y_AXIS);
        ImageAxis voxel_zaxis = voxelSpace.getImageAxis(Axis.Z_AXIS);

        int x = voxel_xaxis.nearestSample(displayPoint.getValue(voxel_xaxis.getAnatomicalAxis()));
        int y = voxel_yaxis.nearestSample(displayPoint.getValue(voxel_yaxis.getAnatomicalAxis()));
        int z = voxel_zaxis.nearestSample(displayPoint.getValue(voxel_zaxis.getAnatomicalAxis()));

        dataVoxel.setX(x);
        dataVoxel.setY(y);
        dataVoxel.setZ(z);

    }


    public void setDisplayPoint(AnatomicalPoint3D point) {
        this.displayPoint = point;
        this.displayAnatomy = displayPoint.getAnatomy();

        recomputeVoxel();
    }

    public AnatomicalPoint3D getDisplayPoint() {
        return displayPoint;
    }

    public void setVoxelX(int _x) {
        dataVoxel.setX(_x);
        recomputePoint();

    }

    public void setVoxelY(int _y) {
        dataVoxel.setY(_y);
        recomputePoint();
    }

    public void setVoxelZ(int _z) {
        dataVoxel.setZ(_z);
        recomputePoint();
    }

    public int getVoxelX() {
        return (int) dataVoxel.getX();
    }

    public int getVoxelY() {
        return (int) dataVoxel.getY();
    }

    public int getVoxelZ() {
        return (int) dataVoxel.getZ();
    }

    public void setDataVoxel(AnatomicalPoint3D voxel) {
        this.dataVoxel = voxel;
        recomputePoint();
    }

    public AnatomicalPoint3D getDataVoxel() {
        return dataVoxel;
    }


    public static void main(String[] args) {
        ImageSpace3D vspace = new ImageSpace3D(new ImageAxis(-50, 50, AnatomicalAxis.LEFT_RIGHT, 300),
                new ImageAxis(-50, 50, AnatomicalAxis.ANTERIOR_POSTERIOR, 300),
                new ImageAxis(-50, 50, AnatomicalAxis.INFERIOR_SUPERIOR, 300));

        ImageSpace3D dspace = new ImageSpace3D(new ImageAxis(-50, 50, AnatomicalAxis.RIGHT_LEFT, 200),
                new ImageAxis(-50, 50, AnatomicalAxis.ANTERIOR_POSTERIOR, 200),
                new ImageAxis(-50, 50, AnatomicalAxis.INFERIOR_SUPERIOR, 200));


        AnatomicalPointOnGrid gpoint = new AnatomicalPointOnGrid(AnatomicalVolume.AXIAL_LPS, vspace);


    }


}
