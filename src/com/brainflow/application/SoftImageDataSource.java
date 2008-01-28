package com.brainflow.application;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.io.ImageInfoReader;
import com.brainflow.image.io.ImageReader;
import com.brainflow.utils.ProgressListener;
import org.apache.commons.vfs.FileObject;

import java.lang.ref.SoftReference;
import java.util.logging.Logger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 1:32:12 PM
 * To change this template use File | Settings | File Templates.
 */


public class SoftImageDataSource extends AbstractImageDataSource {

    final static Logger log = Logger.getLogger(SoftImageDataSource.class.getCanonicalName());


    private SoftReference<IImageData> dataRef = new SoftReference<IImageData>(null);


    private ImageInfo imageInfo = null;


    public SoftImageDataSource(ImageIODescriptor _descriptor, FileObject _header, FileObject _data) {
        super(_descriptor, _header, _data);
    }

    public SoftImageDataSource(ImageIODescriptor _descriptor, FileObject _header) {
        super(_descriptor, _header);

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

    public ImageInfo readImageInfo() {
        if (imageInfo == null) fetchImageInfo();
        return imageInfo;
    }


    //duplicate code (with ImageDataSource)

    protected void fetchImageInfo() {
        if (imageInfo == null) {
            try {
                ImageInfoReader reader = (ImageInfoReader) getDescriptor().getHeaderReader().newInstance();
                List<? extends ImageInfo> info = reader.readInfo(getHeaderFile());
                imageInfo = info.get(getImageIndex());

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



    public IImageData load(ProgressListener plistener) throws BrainflowException {
        try {


            imageInfo = readImageInfo();

            if (imageInfo.getDataFile() == null) {
                imageInfo.setDataFile(getDataFile());
            }

            ImageReader ireader = (ImageReader) getDescriptor().getDataReader().newInstance();

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

            imageInfo = readImageInfo();
            if (imageInfo.getDataFile() == null) {
                imageInfo.setDataFile(getDataFile());
            }

            ImageReader ireader = (ImageReader) getDescriptor().getDataReader().newInstance();
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



    


}
