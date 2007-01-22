package com.brainflow.application;

import com.brainflow.core.BrainflowException;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.utils.ProgressListener;
import org.apache.commons.vfs.FileObject;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 20, 2005
 * Time: 2:04:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class MemoryImage implements ILoadableImage {

    private IImageData data;

    public MemoryImage(IImageData _data) {
        data = _data;
    }

    public ImageInfo getImageInfo() {
        return data.getImageInfo();
    }

    public String getStem() {
        return "memory_image";
    }

    public FileObject getDataFile() {
        throw new UnsupportedOperationException("MemoryImage does not have associated data file");
    }

    public FileObject getHeaderFile() {
        throw new UnsupportedOperationException("MemoryImage does not have associated header file");
    }

    public String getFileFormat() {
        throw new UnsupportedOperationException("MemoryImage does not have a file format");
    }

    public IImageData getData() {
        return data;
    }

    public IImageData load(ProgressListener plistener) throws BrainflowException {
        plistener.finished();
        return data;
    }

    public IImageData load() throws BrainflowException {
        return data;
    }

    public int getUniqueID() {
        // garbage/...
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void releaseData() {
        // does nothing
    }


}