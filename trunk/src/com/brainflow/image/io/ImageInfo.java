package com.brainflow.image.io;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.utils.*;
import org.apache.commons.vfs.FileObject;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

    private IDimension arrayDim = new Dimension3D<Integer>(0, 0, 0);

    private IDimension spacing = new Dimension3D<Double>(0.0, 0.0, 0.0);

    private Dimension3D voxelOffset = new Dimension3D<Integer>(0, 0, 0);

    private Point3D origin = new Point3D();

    private int numImages = 1;

    private int byteOffset = 0;

    private int dimensionality = 3;

    // SPM Default anatomy is LPI (Neurological)

    public static Anatomy3D DEFAULT_ANATOMY = Anatomy3D.AXIAL_LPI;

    private Anatomy3D anatomy = Anatomy3D.AXIAL_LPI;

    private DataType dataType = DataType.BYTE;

    private ByteOrder endian = ByteOrder.nativeOrder();

    private List<Double> scaleFactors = new ArrayList<Double>(Arrays.asList(new Double(1)));

    private List<Double> intercepts = new ArrayList<Double>(Arrays.asList(new Double(0)));

    private FileObject imageFile;

    private FileObject headerFile;

    public ImageInfo() {

    }

    public ImageInfo(ImageInfo info) {
        imageFile = info.imageFile;
        headerFile = info.headerFile;
        endian = info.endian;
        scaleFactors = info.scaleFactors;
        dataType = info.dataType;
        anatomy = info.anatomy;
        dimensionality = info.dimensionality;
        byteOffset = info.byteOffset;
        numImages = info.numImages;
        origin = new Point3D(info.origin);
        voxelOffset = new Dimension3D<Integer>(info.voxelOffset);

        spacing = info.spacing;
        arrayDim = info.arrayDim;
    }


    public ImageInfo(IImageData data) {
        IImageSpace space = data.getImageSpace();
        int[] dimensions = space.getDimensionVector();

        assert dimensions.length >= 3;

        setAnatomy((Anatomy3D) space.getAnatomy());

        setArrayDim(new Dimension3D<Integer>(dimensions[0], dimensions[1], dimensions[2]));
        setDataType(data.getDataType());
        setDimensionality(space.getNumDimensions());


        if (space.getNumDimensions() == 4) {
            setNumImages(space.getDimension(Axis.T_AXIS));
            double[] sf = new double[space.getDimension(Axis.T_AXIS)];
            Arrays.fill(sf, 1);

            double[] intercept = new double[space.getDimension(Axis.T_AXIS)];
            Arrays.fill(intercept, 0);


            setScaleFactors(makeNumericList(sf));
            setIntercepts(makeNumericList(intercept));


        }


        setSpacing(new Dimension3D<Double>(space.getSpacing(Axis.X_AXIS),
                space.getSpacing(Axis.Y_AXIS),
                space.getSpacing(Axis.Z_AXIS)));

        //imageFile = VFS.getManager().


    }

    private List<Double> makeNumericList(double[] vals) {
        List<Double> lst = new ArrayList<Double>(vals.length);
        for (int i = 0; i < vals.length; i++) {
            lst.add(vals[i]);
        }

        return lst;
    }


    public IImageSpace createImageSpace() {
        ImageAxis[] iaxes = new ImageAxis[3];
        AnatomicalAxis[] aaxes = anatomy.getAnatomicalAxes();

        IDimension realDim = calculateRealDim();
        for (int i = 0; i < iaxes.length; i++) {
            iaxes[i] = new ImageAxis(-realDim.getDim(i).doubleValue() / 2, realDim.getDim(i).doubleValue() / 2,
                    aaxes[i], (int) arrayDim.getDim(i).doubleValue());
        }

        ImageSpace3D space3d = new ImageSpace3D(iaxes[0], iaxes[1], iaxes[2]);
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

    public IDimension getArrayDim() {
        return arrayDim;
    }

    public DataType getDataType() {
        return dataType;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public IDimension calculateRealDim() {
        Double[] realVals = new Double[arrayDim.numDim()];
        for (int i = 0; i < realVals.length; i++) {
            realVals[i] = arrayDim.getDim(i).intValue() * spacing.getDim(i).doubleValue();
        }
        return DimensionFactory.create(realVals);
    }


    public Anatomy getAnatomy() {
        return anatomy;
    }

    public double getIntercept(int i) {
        return intercepts.get(i);
    }

    public void setIntercepts(List<Double> interceptList) {
        if (interceptList.size() != getNumImages()) {
            throw new IllegalArgumentException("number of intercept values must equal number of total images");
        }
    }


    public void setNumImages(int _numImages) {
        numImages = _numImages;
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

    public double getScaleFactor(int imageNum) {
        return scaleFactors.get(imageNum);
    }

    public void setAnatomy(Anatomy3D _anatomy) {
        anatomy = _anatomy;
    }

    public void setArrayDim(IDimension arrayDim) {
        this.arrayDim = arrayDim;
        calculateRealDim();
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;

    }

    public void setByteOffset(int byteOffset) {
        this.byteOffset = byteOffset;
    }


    public void setScaleFactors(List<Double> scaleFactorList) {
        if (scaleFactorList.size() != getNumImages()) {
            throw new IllegalArgumentException("number of intercept values must equal number of total images");
        }

        scaleFactors = scaleFactorList;
    }


    public Point3D getOrigin() {
        return origin;
    }

    public void setOrigin(Point3D origin) {
        this.origin = origin;
    }

    public IDimension getSpacing() {
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
                ", spacing=" + spacing +
                ", voxelOffset=" + voxelOffset +
                ", origin=" + origin +
                ", numImages=" + numImages +
                ", byteOffset=" + byteOffset +
                ", dimensionality=" + dimensionality +
                ", anatomy=" + anatomy +
                ", dataType=" + dataType +
                ", endian=" + endian +
                ", scaleFactor=" + scaleFactors +
                ", intercept=" + intercepts +
                ", imageFile=" + imageFile +
                ", headerFile=" + headerFile +
                '}';
    }


}