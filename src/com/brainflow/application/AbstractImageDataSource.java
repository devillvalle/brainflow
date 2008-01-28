package com.brainflow.application;

import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.io.ImageInfoReader;
import org.apache.commons.vfs.FileObject;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 26, 2008
 * Time: 4:53:10 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractImageDataSource implements IImageDataSource {


    private static final BufferedImage BLANK = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);

    private ImageIODescriptor descriptor;

    private FileObject header;

    private FileObject dataFile;

    private BufferedImage previewImage = BLANK;

    private ImageInfo imageInfo = null;

    private int index = 0;


    public AbstractImageDataSource(ImageIODescriptor _descriptor, ImageInfo _info) {
        this(_descriptor, _info, 0);
    }

    public AbstractImageDataSource(ImageIODescriptor _descriptor, ImageInfo _info, int _index) {
        imageInfo = _info;
        descriptor = _descriptor;

        //todo ensure ImageInfo matches descriptor
        dataFile = imageInfo.getDataFile();
        header = imageInfo.getHeaderFile();
        index = _index;
    }

    public AbstractImageDataSource(ImageIODescriptor _descriptor, FileObject _header, FileObject _data) {
        descriptor = _descriptor;
        dataFile = _data;
        header = _header;
    }

    public AbstractImageDataSource(ImageIODescriptor _descriptor, FileObject _header) {
        descriptor = _descriptor;
        header = _header;
    }

    public ImageInfo readImageInfo() {
        if (imageInfo == null) fetchImageInfo();
        return imageInfo;
    }

    protected void fetchImageInfo() {

        try {
            ImageInfoReader reader = (ImageInfoReader) getDescriptor().getHeaderReader().newInstance();
            List<? extends ImageInfo> info = reader.readInfo(getHeaderFile());
            imageInfo = info.get(index);

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


    protected ImageInfo getImageInfo() {
        return imageInfo;
    }

  
    public int getImageIndex() {
        return index;
    }

    public FileObject getDataFile() {
        return dataFile;
    }

    public String getFileFormat() {
        return descriptor.getFormatName();
    }

    public FileObject getHeaderFile() {
        return header;
    }



    public ImageIODescriptor getDescriptor() {
        return descriptor;
    }

    public BufferedImage getPreview() {
        return previewImage;
    }

    public String getStem() {
        return descriptor.getStem(header.getName().getBaseName());
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

        IImageDataSource that = (IImageDataSource) o;

        if (!dataFile.getName().getPath().equals(that.getDataFile().getName().getPath())) return false;
        if (!header.getName().getPath().equals(that.getHeaderFile().getName().getPath())) return false;
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
