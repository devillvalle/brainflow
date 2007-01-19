package com.brainflow.image.io;

import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.data.BasicImageData;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.utils.DataType;
import com.brainflow.utils.Dimension3D;
import com.brainflow.utils.Point3D;
import org.apache.commons.vfs.FileObject;

import java.nio.ByteOrder;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class ImageInfo implements java.io.Serializable {

    private Dimension3D arrayDim = new Dimension3D<Integer>(0, 0, 0);

    private Dimension3D realDim = new Dimension3D<Double>(0.0, 0.0, 0.0);

    private Dimension3D spacing = new Dimension3D<Double>(0.0, 0.0, 0.0);

    private Dimension3D voxelOffset = new Dimension3D<Integer>(0, 0, 0);

    private Point3D origin = new Point3D();

    private int numImages = 1;

    private int byteOffset = 0;

    private int dimensionality = 3;

    // SPM Default anatomy is LPI (Neurological)

    public static AnatomicalVolume DEFAULT_ANATOMY = AnatomicalVolume.AXIAL_LPI;

    private AnatomicalVolume anatomy = AnatomicalVolume.AXIAL_LPI;

    private DataType dataType = DataType.BYTE;

    private ByteOrder endian = ByteOrder.nativeOrder();

    private double scaleFactor = 1.0;

    private double intercept = 0;

    private FileObject imageFile;

    private FileObject headerFile;

    public ImageInfo() {
    }

    public ImageInfo(ImageInfo info) {
        imageFile = info.imageFile;
        headerFile = info.headerFile;
        endian = info.endian;
        scaleFactor = info.scaleFactor;
        dataType = info.dataType;
        anatomy = info.anatomy;
        dimensionality = info.dimensionality;
        byteOffset = info.byteOffset;
        numImages = info.numImages;
        origin = new Point3D(info.origin);
        voxelOffset = new Dimension3D<Integer>(info.voxelOffset);
        realDim = new Dimension3D<Double>(info.realDim);
        spacing = new Dimension3D<Double>(info.spacing);
        arrayDim = new Dimension3D<Integer>(info.arrayDim);
    }


    public ImageInfo(IImageData data) {
        IImageSpace space = data.getImageSpace();
        int[] dimensions = space.getDimensionVector();

        assert dimensions.length >= 3;

        setAnatomy((AnatomicalVolume) space.getAnatomy());

        setArrayDim(new Dimension3D<Integer>(dimensions[0], dimensions[1], dimensions[2]));
        setDataType(data.getDataType());
        setDimensionality(space.getNumDimensions());

        setRealDim(new Dimension3D<Double>(space.getImageAxis(Axis.X_AXIS).getRange().getInterval(),
                space.getImageAxis(Axis.Y_AXIS).getRange().getInterval(),
                space.getImageAxis(Axis.Z_AXIS).getRange().getInterval()));


        if (space.getNumDimensions() == 4) {
            this.setNumImages(space.getDimension(Axis.T_AXIS));
        }


        setSpacing(new Dimension3D<Double>(space.getSpacing(Axis.X_AXIS),
                space.getSpacing(Axis.Y_AXIS),
                space.getSpacing(Axis.Z_AXIS)));

        //imageFile = VFS.getManager().


    }


    public IImageSpace createImageSpace() {
        ImageAxis xaxis = new ImageAxis(-realDim.x.doubleValue() / 2, realDim.x.doubleValue() / 2, anatomy.XAXIS, (int) arrayDim.x.doubleValue());
        ImageAxis yaxis = new ImageAxis(-realDim.y.doubleValue() / 2, realDim.y.doubleValue() / 2, anatomy.YAXIS, (int) arrayDim.y.doubleValue());
        ImageAxis zaxis = new ImageAxis(-realDim.z.doubleValue() / 2, realDim.z.doubleValue() / 2, anatomy.ZAXIS, (int) arrayDim.z.doubleValue());
        ImageSpace3D space3d = new ImageSpace3D(xaxis, yaxis, zaxis);
        return space3d;
    }

    public void setImageFile(FileObject fobj) {
        imageFile = fobj;
    }

    public FileObject getImageFile() {
        return imageFile;
    }

    public ByteOrder getEndian() {
        return endian;
    }

    public Dimension3D getArrayDim() {
        return arrayDim;
    }

    public DataType getDataType() {
        return dataType;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public void calculateRealDim() {
        double x = arrayDim.x.intValue() * spacing.x.doubleValue();
        double y = arrayDim.y.intValue() * spacing.y.doubleValue();
        double z = arrayDim.z.intValue() * spacing.z.doubleValue();

        realDim = new Dimension3D<Double>(x, y, z);
    }

    public Dimension3D getRealDim() {
        return realDim;
    }

    public Anatomy getAnatomy() {
        return anatomy;
    }

    public double getIntercept() {
        return intercept;
    }

    public void setIntercept(double intercept) {
        this.intercept = intercept;
    }


    public void setNumImages(int _numTimePoints) {
        numImages = _numTimePoints;
    }

    public int getNumImages() {
        return numImages;
    }

    public void setVoxelOffset(Dimension3D _voxelOffset) {
        voxelOffset = _voxelOffset;
    }

    public Dimension3D getVoxelOffset() {
        return voxelOffset;
    }

    public void setDimensionality(int _dimensionality) {
        dimensionality = _dimensionality;
    }

    public int getDimensionality() {
        return dimensionality;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public void setAnatomy(AnatomicalVolume _anatomy) {
        anatomy = _anatomy;
    }

    public void setArrayDim(Dimension3D arrayDim) {
        this.arrayDim = arrayDim;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setByteOffset(int byteOffset) {
        this.byteOffset = byteOffset;
    }

    public void setRealDim(Dimension3D realDim) {
        this.realDim = realDim;
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public Point3D getOrigin() {
        return origin;
    }

    public void setOrigin(Point3D origin) {
        this.origin = origin;
    }

    public Dimension3D getSpacing() {
        return spacing;
    }

    public void setSpacing(Dimension3D spacing) {
        this.spacing = spacing;
    }

    public void setEndian(ByteOrder _endian) {
        endian = _endian;
    }

    public String toString() {
        return "ImageInfo{" +
                "arrayDim=" + arrayDim +
                ", realDim=" + realDim +
                ", spacing=" + spacing +
                ", voxelOffset=" + voxelOffset +
                ", origin=" + origin +
                ", numImages=" + numImages +
                ", byteOffset=" + byteOffset +
                ", dimensionality=" + dimensionality +
                ", anatomy=" + anatomy +
                ", dataType=" + dataType +
                ", endian=" + endian +
                ", scaleFactor=" + scaleFactor +
                ", intercept=" + intercept +
                ", imageFile=" + imageFile +
                ", headerFile=" + headerFile +
                '}';
    }


}