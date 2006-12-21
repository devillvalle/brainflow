package com.brainflow.image.io;
import java.io.*;
import java.net.URL;
import com.brainflow.core.*;
import org.apache.commons.vfs.FileObject;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public interface ImageInfoReader {

  public ImageInfo readInfo(File f) throws BrainflowException;
  public ImageInfo readInfo(FileObject fobj) throws BrainflowException;
  public ImageInfo readInfo(URL url) throws BrainflowException;

}