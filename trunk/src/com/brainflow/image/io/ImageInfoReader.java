package com.brainflow.image.io;

import com.brainflow.application.BrainflowException;
import org.apache.commons.vfs.FileObject;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
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

public interface ImageInfoReader {

    public List<? extends ImageInfo> readInfo(File f) throws BrainflowException;

    public List<? extends ImageInfo> readInfo(FileObject fobj) throws BrainflowException;

    public List<? extends ImageInfo> readInfo(InputStream stream) throws BrainflowException;



}