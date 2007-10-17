package com.brainflow.application;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.io.ImageInfoReader;
import com.brainflow.image.io.ImageReader;
import com.brainflow.utils.ProgressListener;
import org.apache.commons.vfs.FileObject;

import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 1:32:12 PM
 * To change this template use File | Settings | File Templates.
 */


public class SoftImageDataSource implements IImageDataSource {

    final static Logger log = Logger.getLogger(SoftImageDataSource.class.getCanonicalName());

    private ImageIODescriptor descriptor;

    private FileObject header;

    private FileObject dataFile;

    private SoftReference<IImageData> dataRef = new SoftReference<IImageData>(null);

    private BufferedImage previewImage;


    private ImageInfo imageInfo = null;



    public SoftImageDataSource(ImageIODescriptor _descriptor, FileObject _header, FileObject _data) {
        descriptor = _descriptor;
        //assert descriptor.getHeaderName(_data) == _header.getName().getBaseName();
        dataFile = _data;
        header = _header;
    }

    public SoftImageDataSource(ImageIODescriptor _descriptor, FileObject _header) {
        descriptor = _descriptor;
        header = _header;

    }

    public void releaseData() {
        dataRef.enqueue();
    }

    public boolean isLoaded() {
        if (dataRef.get() == null) {
            return false;
        }

        return true;
    }

    public IImageData getData() {
        if (dataRef.get() == null) {
            try {
                load();
            } catch (BrainflowException e) {
                log.severe("failed to load " + getDataFile().getName().getPath());
                throw new RuntimeException(e);
            }
        }

        return dataRef.get();
    }

    public String getStem() {
        return descriptor.getStem(header.getName().getBaseName());
    }

    public FileObject getDataFile() {
        return dataFile;
    }

    public FileObject getHeaderFile() {
        return header;
    }

    public String getFileFormat() {
        return descriptor.getFormatName();
    }

    public ImageInfo getImageInfo() {
        if (imageInfo == null) {
            try {
                ImageInfoReader reader = (ImageInfoReader) descriptor.getHeaderReader().newInstance();
                imageInfo = reader.readInfo(getHeaderFile());


            } catch (BrainflowException e) {
                //e.printStackTrace();
                throw new RuntimeException(e);
            } catch (InstantiationException e2) {
                //e2.printStackTrace();
                throw new RuntimeException(e2);
            } catch (IllegalAccessException e3) {
                //e3.printStackTrace();
                throw new RuntimeException(e3);
            }
        }

        return imageInfo;
    }


    public BufferedImage getPreview() {
        if (previewImage == null) {

        }

        return null;
    }


    public IImageData load(ProgressListener plistener) throws BrainflowException {
        try {


            imageInfo = getImageInfo();

            if (imageInfo.getImageFile() == null) {
                imageInfo.setImageFile(getDataFile());
            }

            ImageReader ireader = (ImageReader) descriptor.getDataReader().newInstance();

            IImageData data = ireader.readImage(imageInfo, plistener);
            data.setIdentifier(getUniqueID());
            data.setImageLabel(getStem());
            dataRef = new SoftReference(data);
        } catch (IllegalAccessException e) {
            log.warning("Error caught in BasicImageData.load()");
            throw new BrainflowException(e);
        } catch (InstantiationException e) {
            log.warning("Error caught in BasicImageData.load()");
            throw new BrainflowException(e);
        }

        return dataRef.get();

    }


    public IImageData load() throws BrainflowException {
        try {

            imageInfo = getImageInfo();
            if (imageInfo.getImageFile() == null) {
                imageInfo.setImageFile(getDataFile());
            }

            ImageReader ireader = (ImageReader) descriptor.getDataReader().newInstance();
            IImageData data = ireader.readImage(imageInfo, new ProgressListener() {

                public void setValue(int val) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void setMinimum(int val) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void setMaximum(int val) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void setString(String message) {
                    log.info(message);
                }

                public void setIndeterminate(boolean b) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void finished() {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });
            data.setIdentifier(getUniqueID());
            data.setImageLabel(getStem());
            dataRef = new SoftReference(data);

        } catch (IllegalAccessException e) {
            log.warning("Error caught in BasicImageData.load()");
            throw new BrainflowException(e);
        } catch (InstantiationException e) {
            log.warning("Error caught in BasicImageData.load()");
            throw new BrainflowException(e);
        }

        return (IImageData) dataRef.get();
    }

    public int getUniqueID() {
        return hashCode();
    }

    public String toString() {
        return header.getName().getBaseName();

    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoftImageDataSource that = (SoftImageDataSource) o;

        if (!dataFile.getName().getPath().equals(that.dataFile.getName().getPath())) return false;
        if (!header.getName().getPath().equals(that.header.getName().getPath())) return false;
        if (!getFileFormat().equals(that.getFileFormat())) return false;

        return true;
    }


    public int hashCode() {
        int result;
        result = header.getName().getPath().hashCode();
        result = 31 * result + dataFile.getName().getPath().hashCode();
        result = 17 * result + getFileFormat().hashCode();
        return result;
    }


}
