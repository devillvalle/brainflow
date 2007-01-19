package com.brainflow.application;

import com.brainflow.application.services.LoadableImageProgressEvent;
import com.brainflow.core.BrainflowException;
import com.brainflow.image.data.BasicImageData;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.io.ImageInfoReader;
import com.brainflow.image.io.ImageReader;
import com.brainflow.utils.ProgressListener;
import org.apache.commons.vfs.FileObject;
import org.bushe.swing.event.EventBus;

import java.lang.ref.SoftReference;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 1:32:12 PM
 * To change this template use File | Settings | File Templates.
 */


public class SoftLoadableImage implements ILoadableImage {

    final static Logger log = Logger.getLogger(SoftLoadableImage.class.getCanonicalName());

    private ImageIODescriptor descriptor;
    private FileObject header;
    private FileObject dataFile;
    private SoftReference dataRef = new SoftReference(null);


    private ImageInfo imageInfo = null;

    public SoftLoadableImage(ImageIODescriptor _descriptor, FileObject _header, FileObject _data) {
        descriptor = _descriptor;
        //assert descriptor.getHeaderName(_data) == _header.getName().getBaseName();
        dataFile = _data;
        header = _header;
    }

    public SoftLoadableImage(ImageIODescriptor _descriptor, FileObject _header) {
        descriptor = _descriptor;
        header = _header;

    }

    public void releaseData() {
        dataRef.enqueue();
    }

    public BasicImageData getData() {
        if (dataRef.get() == null) {
            try {
                load();
            } catch (BrainflowException e) {
                log.severe("failed to load " + getDataFile().getName().getPath());
                // need to deal with exception reasonably
                //
            }
        }

        return (BasicImageData) dataRef.get();
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
                e.printStackTrace();
            } catch (InstantiationException e2) {
                e2.printStackTrace();
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
            }
        }

        return imageInfo;
    }

    public BasicImageData load(ProgressListener plistener) throws BrainflowException {
        try {

            ImageInfoReader reader = (ImageInfoReader) descriptor.getHeaderReader().newInstance();
            imageInfo = reader.readInfo(getHeaderFile());
            imageInfo.setImageFile(getDataFile());
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

        return (BasicImageData) dataRef.get();

    }

    public BasicImageData load() throws BrainflowException {
        try {
            ImageInfoReader reader = (ImageInfoReader) descriptor.getHeaderReader().newInstance();
            imageInfo = reader.readInfo(getHeaderFile());
            imageInfo.setImageFile(getDataFile());
            ImageReader ireader = (ImageReader) descriptor.getDataReader().newInstance();
            IImageData data = ireader.readImage(imageInfo, new LoadableImageProgressListener(0, 100));
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

        return (BasicImageData) dataRef.get();
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

        SoftLoadableImage that = (SoftLoadableImage) o;

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

    class LoadableImageProgressListener implements ProgressListener {
        int min;
        int max;

        int value;

        String message = "";

        public LoadableImageProgressListener(int _min, int _max) {
            min = _min;
            max = _max;
        }

        public void setValue(int val) {
            value = val;
            EventBus.publish(new LoadableImageProgressEvent(SoftLoadableImage.this, (int) ((float) (value - min) / (float) (max - min) * 100f), message));

        }

        public void setMinimum(int val) {
            min = val;
        }

        public void setMaximum(int val) {
            max = val;
        }

        public void setString(String _message) {
            message = _message;
            EventBus.publish(new LoadableImageProgressEvent(SoftLoadableImage.this, (int) ((float) (value - min) / (float) (max - min) * 100f), message));

        }

        public void setIndeterminate(boolean b) {
        }

        public void finished() {
            value = max;
            EventBus.publish(new LoadableImageProgressEvent(SoftLoadableImage.this, (int) ((float) (value - min) / (float) (max - min) * 100f), message));

        }

    }


}
