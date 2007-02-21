package com.brainflow.image.io.nifti;

import com.brainflow.application.BrainflowException;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.io.ImageInfoReader;
import com.brainflow.utils.DataType;
import com.brainflow.utils.Dimension3D;
import com.brainflow.utils.Point3D;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.util.RandomAccessMode;

import java.io.*;
import java.net.URL;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 1, 2006
 * Time: 3:41:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class NiftiInfoReader implements ImageInfoReader {

    private static final Logger log = Logger.getLogger(NiftiInfoReader.class.getName());

    public static String getHeaderName(String name) {
        if (name.endsWith(".img")) {
            name = name.substring(0, name.length() - 3);
            return name + "hdr";
        }
        if (name.endsWith(".nii")) {
            return name;
        }
        if (name.endsWith(".nii.gz")) {
            return name;
        }

        return name + ".nii";
    }

    public static String getImageName(String name) {
        if (name.endsWith(".hdr")) {
            name = name.substring(0, name.length() - 3);
            return name + "img";
        }
        if (name.endsWith(".nii")) {
            return name;
        }

        if (name.endsWith(".nii.gz")) {
            return name;
        }

        return name + ".nii";
    }


    private InputStream getInputStream(FileObject obj) throws IOException {
        try {

            log.info("getting input stream for file object : " + obj.getName());
            if (obj.getName().getBaseName().endsWith(".gz")) {
                String uri = obj.getName().getURI();

                log.info("resolving zipped file : " + "gz:" + uri);
                FileObject gzfile = VFS.getManager().resolveFile("gz:" + uri);
                FileObject[] children = gzfile.getChildren();
                if (children == null || children.length == 0) {
                    throw new IOException("Error reading gzipped file object, URI : " + uri);
                }

                return children[0].getContent().getInputStream();
            } else {
                return obj.getContent().getRandomAccessContent(RandomAccessMode.READ).getInputStream();
            }

        } catch (FileSystemException e) {
            throw new IOException(e);
        }

    }

    private InputStream getInputStream(File f) throws IOException {
        if (f.getName().endsWith(".gz")) {
            return new BufferedInputStream(new GZIPInputStream(new FileInputStream(f), 2048));
        } else {
            return new BufferedInputStream(new FileInputStream(f), 2048);
        }

    }

    public ImageInfo readInfo(File f) throws BrainflowException {
        ImageInfo ret = null;

        try {

            String headerName = NiftiInfoReader.getHeaderName(f.getName());
            ret = readHeader(headerName, getInputStream(f));
            // todo check to see if valid nifti file extension
            ret.setImageFile(resolveFileObject(NiftiInfoReader.getImageName(f.getAbsolutePath())));
        } catch (Exception e) {
            //log.warning("Exception caught in AnalyzeInfoReader.readInfo ");
            throw new BrainflowException(e);
        }


        return ret;

    }

    private FileObject resolveFileObject(String f) throws FileNotFoundException {
        try {
            if (f.endsWith(".gz")) {
                FileObject gzfile = VFS.getManager().resolveFile("gz:" + f);
                FileObject[] children = gzfile.getChildren();
                if (children == null || children.length == 0) {
                    throw new FileNotFoundException("Error finding gzipped file : " + f);
                }

                log.info("resolved gzipped file : " + children[0]);
                log.info("file content type : " + children[0].getFileSystem());
                return children[0];
            } else {
                return VFS.getManager().resolveFile(f);
            }
        } catch (FileSystemException e) {
            throw new FileNotFoundException(e.getMessage());
        }

    }

    private FileObject resolveFileObject(File f) throws FileNotFoundException {
        return resolveFileObject(f.getAbsolutePath());
    }

    public ImageInfo readInfo(FileObject fobj) throws BrainflowException {
        ImageInfo ret = null;

        try {

            String headerName = NiftiInfoReader.getHeaderName(fobj.getName().getBaseName());
            ret = readHeader(headerName, getInputStream(fobj));
            ret.setImageFile(resolveFileObject(fobj.getName().getURI()));

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

            String niftigz1 = "c:/javacode/googlecode/brainflow/test/data/corr_with_zperf_index.nii.gz";
            NiftiInfoReader reader = new NiftiInfoReader();
            System.out.println(reader.readInfo(VFS.getManager().resolveFile(niftigz1)).toString());


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
