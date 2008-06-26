package com.brainflow.image.io;


import com.brainflow.application.BrainflowException;
import com.brainflow.image.data.BasicImageData;
import com.brainflow.image.data.IImageData;

import com.brainflow.utils.DataType;
import com.brainflow.utils.IDimension;
import com.brainflow.utils.ProgressAdapter;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.logging.Logger;
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

public class BrainIO {

    private static Logger log = Logger.getLogger(BrainIO.class.getCanonicalName());


    public BrainIO() {
    }


    public static IImageData readNiftiImage(URL header) throws BrainflowException {

        IImageData data;

        try {
            NiftiInfoReader reader = new NiftiInfoReader();


            List<NiftiImageInfo> info = reader.readInfo(VFS.getManager().resolveFile(header.toString()));

            BasicImageReader ireader = new BasicImageReader(info.get(0));
            data = ireader.readImage(info.get(0), new ProgressAdapter());

        } catch (FileSystemException e) {
            throw new BrainflowException(e);
        }

        return data;
    }


    public static IImageData readNiftiImage(String fname) throws BrainflowException {

        try {
            URL url = new File(fname).toURI().toURL();
            return readNiftiImage(url);
        } catch (MalformedURLException e) {
            throw new BrainflowException(e);
        }


    }


    public static IImageData readAnalyzeImage(URL header) throws BrainflowException {
        IImageData data;
        try {
            AnalyzeInfoReader reader = new AnalyzeInfoReader();
            FileSystemManager fsManager = VFS.getManager();
            FileObject fobj = fsManager.resolveFile(header.getPath());
            List<? extends ImageInfo> info = reader.readInfo(fobj);
            BasicImageReader ireader = new BasicImageReader(info.get(0));
            data = ireader.readImage(info.get(0), new ProgressAdapter());

        } catch (FileSystemException e) {
            throw new BrainflowException(e.getMessage(), e);
        }


        return data;
    }


    public static IImageData readAnalyzeImage(String fname) throws BrainflowException {
        AnalyzeInfoReader reader = new AnalyzeInfoReader();

        List<ImageInfo> info = reader.readInfo(new File(fname));

        BasicImageReader ireader = new BasicImageReader(info.get(0));
        IImageData data = ireader.readImage(info.get(0), new ProgressAdapter());


        return data;
    }

    public static IImageData readAnalyzeImage(ImageInfo info, int timeNum) throws BrainflowException {
        info.setDimensionality(3);

        BasicImageReader ireader = new BasicImageReader(info);
        IDimension dim3d = info.getArrayDim();
        ireader.setByteOffset((timeNum * dim3d.getDim(0).intValue() * dim3d.getDim(1).intValue() * dim3d.getDim(2).intValue()));


        IImageData data = null;

        data = ireader.readImage(info, new ProgressAdapter());

        //data.setImageLabel(info.getDataFile().getName().getBaseName());

        return data;
    }

    public static void appendAnalyzeImage(int imgNum, ImageInfo info, BasicImageData data) throws BrainflowException {
        FileImageOutputStream ostream = null;
        String fname = info.getDataFile().getName().getPath();

        IDimension dim3d = info.getArrayDim();
        int pos = (int) (imgNum * info.getDataType().getBytesPerUnit() * dim3d.getDim(0).intValue() * dim3d.getDim(1).intValue() * dim3d.getDim(2).intValue());


        try {

            String imgName = AnalyzeInfoReader.getImageName(fname);
            File opfile = new File(imgName);
            ostream = new FileImageOutputStream(opfile);

            if (pos != ostream.length()) {
                log.severe("Error appending image to 4D file, aborting");
                throw new BrainflowException("file length: " + pos + " does not match stream postion " + ostream.length());
            }

            ostream.seek(ostream.length());
            ostream.setByteOrder(info.getEndian());


            DataType dtype = data.getDataType();
            Object storage = data.getStorage();

            if (dtype == DataType.BYTE) {
                ostream.write((byte[]) storage, 0, ((byte[]) storage).length);
            } else if (dtype == DataType.SHORT) {
                ostream.writeShorts((short[]) storage, 0, ((short[]) storage).length);
            } else if (dtype == DataType.INTEGER) {
                ostream.writeInts((int[]) storage, 0, ((int[]) storage).length);
            } else if (dtype == DataType.FLOAT) {
                ostream.writeFloats((float[]) storage, 0, ((float[]) storage).length);
            } else if (dtype == DataType.DOUBLE) {
                ostream.writeDoubles((double[]) storage, 0, ((double[]) storage).length);
            } else
                throw new IllegalArgumentException("Data Type : " + dtype + " not supported");

        } catch (FileNotFoundException e1) {
            throw new BrainflowException(e1);
        } catch (IOException e2) {
            throw new BrainflowException(e2);
        } finally {
            try {
                ostream.close();
            } catch (IOException e) {
            }
        }
    }

    public static void writeAnalyzeImage(String fname, BasicImageData data) throws BrainflowException {

        FileImageOutputStream ostream = null;

        try {
            AnalyzeInfoWriter writer = new AnalyzeInfoWriter();
            ImageInfo info = new ImageInfo(data);
            String hdrName = AnalyzeInfoReader.getHeaderName(fname);
            writer.writeInfo(new File(hdrName), info);


            String imgName = AnalyzeInfoReader.getImageName(fname);

            File opfile = new File(imgName);


            ostream = new FileImageOutputStream(opfile);

            assert ostream != null;


            ostream.setByteOrder(info.getEndian());

            DataType dtype = data.getDataType();
            Object storage = data.getStorage();


            assert storage != null;


            if (dtype == DataType.BYTE) {
                ostream.write((byte[]) storage, 0, ((byte[]) storage).length);
            } else if (dtype == DataType.SHORT) {
                ostream.writeShorts((short[]) storage, 0, ((short[]) storage).length);
            } else if (dtype == DataType.INTEGER) {
                ostream.writeInts((int[]) storage, 0, ((int[]) storage).length);
            } else if (dtype == DataType.FLOAT) {
                ostream.writeFloats((float[]) storage, 0, ((float[]) storage).length);
            } else if (dtype == DataType.DOUBLE) {
                ostream.writeDoubles((double[]) storage, 0, ((double[]) storage).length);
            } else
                throw new IllegalArgumentException("Data Type : " + dtype + " not supported");

        } catch (FileNotFoundException e1) {
            throw new BrainflowException(e1);
        } catch (IOException e2) {
            throw new BrainflowException(e2);
        } finally {
            try {
                ostream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}