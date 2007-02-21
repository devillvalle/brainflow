package com.brainflow.image.io;

import com.brainflow.application.BrainflowException;
import org.apache.commons.vfs.FileObject;

import java.io.File;
import java.net.URL;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public interface ImageInfoReader {

    public ImageInfo readInfo(File f) throws BrainflowException;

    public ImageInfo readInfo(FileObject fobj) throws BrainflowException;

    public ImageInfo readInfo(URL url) throws BrainflowException;

}