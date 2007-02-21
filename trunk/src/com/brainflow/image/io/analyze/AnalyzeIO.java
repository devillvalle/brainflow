package com.brainflow.image.io.analyze;


import com.brainflow.application.BrainflowException;
import com.brainflow.image.data.BasicImageData;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.BasicImageReader;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.io.nifti.NiftiInfoReader;
import com.brainflow.utils.DataType;
import com.brainflow.utils.Dimension3D;
import com.brainflow.utils.ProgressListener;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class AnalyzeIO {

    private static Logger log = Logger.getLogger(AnalyzeIO.class.getCanonicalName());


    public AnalyzeIO() {
    }


    public static IImageData readNiftiImage(String fname) throws BrainflowException {

        NiftiInfoReader reader = new NiftiInfoReader();

        ImageInfo info = reader.readInfo(new File(fname));

        BasicImageReader ireader = new BasicImageReader(info);
        IImageData data = ireader.readImage(info, new ProgressListener() {
            public void setValue(int val) {

            }

            public void setMinimum(int val) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void setMaximum(int val) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void setString(String message) {
                log.info("Progress: " + message);
            }

            public void setIndeterminate(boolean b) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void finished() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });


        return data;
    }


    public static IImageData readAnalyzeImage(String fname) throws BrainflowException {
        log.info("AnalyzeIO.readAnalyzeImage " + fname);
        AnalyzeInfoReader reader = new AnalyzeInfoReader();

        ImageInfo info = reader.readInfo(new File(fname));
        log.info("AnalyzeInfo: " + info);

        BasicImageReader ireader = new BasicImageReader(info);
        IImageData data = ireader.readImage(info, new ProgressListener() {
            public void setValue(int val) {

            }

            public void setMinimum(int val) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void setMaximum(int val) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void setString(String message) {
                log.info("Progress: " + message);
            }

            public void setIndeterminate(boolean b) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void finished() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });


        return data;
    }

    public static IImageData readAnalyzeImage(ImageInfo info, int timeNum) throws BrainflowException {
        info.setDimensionality(3);

        BasicImageReader ireader = new BasicImageReader(info);
        Dimension3D dim3d = info.getArrayDim();
        ireader.setByteOffset((int) (timeNum * dim3d.x.intValue() * dim3d.y.intValue() * dim3d.z.intValue()));


        IImageData data = null;

        data = ireader.readImage(info, new ProgressListener() {
            public void setValue(int val) {

            }

            public void setMinimum(int val) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void setMaximum(int val) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void setString(String message) {
                log.severe("Progress: " + message);
            }

            public void setIndeterminate(boolean b) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void finished() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        data.setImageLabel(info.getImageFile().getName().getBaseName());

        return data;
    }

    public static void appendAnalyzeImage(int imgNum, ImageInfo info, BasicImageData data) throws BrainflowException {
        FileImageOutputStream ostream = null;
        String fname = info.getImageFile().getName().getPath();

        Dimension3D dim3d = info.getArrayDim();
        int pos = (int) (imgNum * info.getDataType().getBytesPerUnit() * dim3d.x.intValue() * dim3d.y.intValue() * dim3d.z.intValue());


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

            log.info("AnalyzeIO.writeAnalyzeImage: data type is " + data.getDataType());
            log.info("AnalyzeIO.writeAnalyzeImage: is big endian?  " + info.getEndian());
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