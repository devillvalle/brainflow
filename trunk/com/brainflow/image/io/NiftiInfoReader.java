package com.brainflow.image.io;

import com.brainflow.core.BrainflowException;
import com.brainflow.utils.DataType;
import com.brainflow.utils.Dimension3D;
import com.brainflow.utils.Point3D;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 1, 2006
 * Time: 3:41:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class NiftiInfoReader implements ImageInfoReader {

    public ImageInfo readInfo(File f) throws BrainflowException {
        ImageInfo ret = null;

        try {
            ret = readHeader(f.getName(), new FileInputStream(f));
            // todo check to see if valid nifti file extension
            ret.setImageFile(VFS.getManager().resolveFile(f.getParentFile(), f.getName()));
        } catch (Exception e) {
            //log.warning("Exception caught in AnalyzeInfoReader.readInfo ");
            throw new BrainflowException(e);
        }


        return ret;

    }

    public ImageInfo readInfo(FileObject fobj) throws BrainflowException {
        ImageInfo ret = null;

        try {
            InputStream istream = fobj.getContent().getInputStream();

            ret = readHeader(fobj.getName().getBaseName(), istream);
            ret.setImageFile(VFS.getManager().resolveFile(fobj.getParent(), AnalyzeInfoReader.getImageName(fobj.getName().getBaseName())));

        } catch (Exception e) {
            throw new BrainflowException(e);
        }

        return ret;


    }

    public ImageInfo readInfo(URL url) throws BrainflowException {
        try {
            return readHeader(url.toString(), url.openStream());
        } catch (IOException e) {
            throw new BrainflowException("Error reading image url", e);
        }
    }

    private void fillPixDim(float[] pixdim, NiftiImageInfo info) {
        info.qfac = (int) pixdim[0];
        info.setSpacing(new Dimension3D<Double>((double) pixdim[1], (double) pixdim[2], (double) pixdim[3]));

    }


    private void fillDataType(short datatype, NiftiImageInfo info) throws BrainflowException {

        DataType dtype = null;
        switch (datatype) {
            case Nifti1Dataset.NIFTI_TYPE_UINT8:
                dtype = DataType.UBYTE;
            case Nifti1Dataset.NIFTI_TYPE_INT8:
                dtype = DataType.BYTE;
                break;
            case Nifti1Dataset.NIFTI_TYPE_INT16:
                dtype = DataType.SHORT;
                break;
            case Nifti1Dataset.NIFTI_TYPE_INT32:
                dtype = DataType.INTEGER;
                break;
            case Nifti1Dataset.NIFTI_TYPE_FLOAT32:
                dtype = DataType.FLOAT;
                break;
            case Nifti1Dataset.NIFTI_TYPE_FLOAT64:
                dtype = DataType.DOUBLE;
                break;
            case Nifti1Dataset.NIFTI_TYPE_INT64:
                dtype = DataType.LONG;
                break;
            case Nifti1Dataset.NIFTI_TYPE_UINT16:
                throw new BrainflowException("Do not support NIFTI_TYPE_UINT16 datatype");
            case Nifti1Dataset.NIFTI_TYPE_UINT32:
                throw new BrainflowException("Do not support NIFTI_TYPE_UINT32 datatype");
            case Nifti1Dataset.NIFTI_TYPE_UINT64:
                throw new BrainflowException("Do not support NIFTI_TYPE_UINT64 datatype");
            case Nifti1Dataset.NIFTI_TYPE_RGB24:
                throw new BrainflowException("Do not support NIFTI_TYPE_RGB24 datatype");
            default:
                throw new BrainflowException("Do not support NIFTI_TYPE " + datatype);

        }

        info.setDataType(dtype);


    }

    private void fillImageDim(short[] dim, NiftiImageInfo info) throws BrainflowException {
        int numDims = dim[0];
        if (dim[0] > 2 && dim[0] < 5) {
            info.setDimensionality(dim[0]);
        } else {
            throw new BrainflowException("Nifti images with fewer than 3 or more than 4 dimensions are not supported." + dim[0]);
        }

        info.setArrayDim(new Dimension3D<Integer>((int) dim[1], (int) dim[2], (int) dim[3]));
        info.setDimensionality(numDims);
        if (numDims > 3) {
            info.setNumImages(dim[4]);
        }


    }


    private NiftiImageInfo readHeader(String name, InputStream stream) throws IOException, BrainflowException {
        NiftiImageInfo info = new NiftiImageInfo();

        try {
            Nifti1Dataset nifti = new Nifti1Dataset(name);
            nifti.readHeader(stream);
            short[] dim = nifti.dim;

            Arrays.toString(dim);
            fillImageDim(dim, info);
            fillDataType(nifti.datatype, info);
            float[] pixdim = nifti.pixdim;
            fillPixDim(pixdim, info);
            System.out.println(Arrays.toString(pixdim));

            // difficulty. requires qform support //////////
            info.setAnatomy(ImageInfo.DEFAULT_ANATOMY);
            /////////////////////////////////////////

            info.calculateRealDim();

            info.setScaleFactor(nifti.scl_slope);
            info.setIntercept(nifti.scl_inter);
            info.setByteOffset((int) nifti.vox_offset);
            info.setOrigin(new Point3D(nifti.qoffset[0], nifti.qoffset[1], nifti.qoffset[2]));
            if (nifti.big_endian) {
                info.setEndian(ByteOrder.BIG_ENDIAN);
            } else {
                info.setEndian(ByteOrder.LITTLE_ENDIAN);
            }


            return info;


        } catch (IOException e) {
            throw e;
        }


    }


    private NiftiImageInfo readHeader(String name) throws IOException, BrainflowException {
        NiftiImageInfo info = new NiftiImageInfo();

        try {
            Nifti1Dataset nifti = new Nifti1Dataset(name);
            nifti.readHeader();
            short[] dim = nifti.dim;
            fillImageDim(dim, info);
            fillDataType(nifti.datatype, info);
            float[] pixdim = nifti.pixdim;
            fillPixDim(pixdim, info);
            System.out.println(Arrays.toString(pixdim));

            // difficulty. requires qform support //////////
            info.setAnatomy(ImageInfo.DEFAULT_ANATOMY);
            /////////////////////////////////////////

            info.calculateRealDim();

            info.setScaleFactor(nifti.scl_slope);
            info.setIntercept(nifti.scl_inter);
            info.setByteOffset((int) nifti.vox_offset);
            info.setOrigin(new Point3D(nifti.qoffset[0], nifti.qoffset[1], nifti.qoffset[2]));
            if (nifti.big_endian) {
                info.setEndian(ByteOrder.BIG_ENDIAN);
            } else {
                info.setEndian(ByteOrder.LITTLE_ENDIAN);
            }


            return info;


        } catch (IOException e) {
            throw e;
        }


    }


    public static void main(String[] args) {
        try {

            NiftiInfoReader reader = new NiftiInfoReader();

            System.out.println("exists? " + VFS.getManager().resolveFile("F:/data/anyback/sbhighres_mean.nii").exists());

            FileObject fobj = VFS.getManager().resolveFile("F:/data/anyback/sbhighres_mean.nii");
            System.out.println("size = " + fobj.getContent().getSize());

            NiftiImageInfo info = reader.readHeader("sbhighres_mean.nii", fobj.getContent().getInputStream());
            //System.out.println(info);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
