package com.brainflow.application;

import java.util.logging.Logger;
import org.jdom.Element;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;

import com.brainflow.core.BrainflowException;
import com.brainflow.utils.FileObjectFilter;

import java.io.FileFilter;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 1:11:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageIODescriptor {

    private final String headerExtension;
    private final String dataExtension;
    private Class dataReader;
    private Class headerReader;

    private String formatName;
    private FOF fof = new FOF();

    Logger log = Logger.getLogger(ImageIODescriptor.class.getName());




    private ImageIODescriptor(Element formatElement) throws BrainflowException {
        Element headerElement = formatElement.getChild("Header");
        Element dataElement = formatElement.getChild("Data");

        formatName = formatElement.getAttributeValue("name");

        headerExtension = headerElement.getAttributeValue("extension");
        dataExtension = dataElement.getAttributeValue("extension");

        String headerReaderClassName = headerElement.getAttributeValue("reader");
        String dataReaderClassName = dataElement.getAttributeValue("reader");


        try {
            dataReader = Class.forName(dataReaderClassName);
            headerReader = Class.forName(headerReaderClassName);

        } catch (ClassNotFoundException e) {
            throw new BrainflowException("ImageIODescriptor() : couldn't load class " + dataReaderClassName, e);
        }
    }



    public SoftLoadableImage[] findLoadableImages(FileObject[] fobjs) {

        List<SoftLoadableImage> limglist = new ArrayList<SoftLoadableImage>();

        for (int i=0; i<fobjs.length; i++) {
            if (isHeaderMatch(fobjs[i])) {
                try {

                    FileObject dobj = VFS.getManager().resolveFile(fobjs[i].getParent(), getDataName(fobjs[i]));

                    if (dobj != null && dobj.exists()) {

                        limglist.add(new SoftLoadableImage(this, fobjs[i], dobj));
                    }

                } catch (FileSystemException e) {
                    log.warning("failed to resolve data file for image header: " + fobjs[i]);

                }

            }
        }

        SoftLoadableImage[] ret = new SoftLoadableImage[limglist.size()];
        limglist.toArray(ret);
        return ret;

    }



    public FileObject findDataMatch(FileObject header, FileObject[] candidates) {
        assert isHeaderMatch(header);

        for (int i=0; i<candidates.length; i++) {
            String dataName = getDataName(header);
            String candyName = candidates[i].getName().getBaseName();


            if (dataName.equals(candyName)) {
                return candidates[i];
            }
        }

        return null;

    }
    public SoftLoadableImage createLoadableImage(FileObject header, FileObject data) {
        assert isHeaderMatch(header) && isDataMatch(data);
        SoftLoadableImage limg = new SoftLoadableImage(this, header, data);
        return limg;
    }


    public String getDataName(FileObject headerFile) {
        assert isHeaderMatch(headerFile);
        String stem = getStem(headerFile.getName().getBaseName());
        String dataName = stem + dataExtension;
        return dataName;
    }

    public String getHeaderName(FileObject dataFile) {
        assert isDataMatch(dataFile);
        String stem = getStem(dataFile.getName().getBaseName());
        String headerName = stem + headerExtension;
        return headerName;

    }

    public boolean isHeaderName(String hname) {
        if (hname.endsWith(headerExtension)) {
            return true;
        }

        return false;

    }

    public boolean isDataName(String dname) {
        if (dname.endsWith(dataExtension)) {
            return true;
        }

        return false;

    }



    public boolean isHeaderMatch(FileObject fobj) {
        if (fobj.getName().getPath().endsWith(headerExtension)) {
            return true;
        }

        // other checks would be appropriate (i.e. is header size = 348?)
        return false;
    }

    public String getStem(String fname) {
        String ret = null;

        if (fname.endsWith(headerExtension)) {
            ret = fname.substring(0, fname.length() - headerExtension.length());
        }

        else if (fname.endsWith(dataExtension)) {
            ret = fname.substring(0, fname.length() - dataExtension.length());
        }

        return ret;
    }



    public boolean isDataMatch(FileObject fobj) {
        if (fobj.getName().getPath().endsWith(dataExtension)) {
            return true;
        }

        return false;

    }

    public String getHeaderExtension() {
        return headerExtension;
    }

    public String getDataExtension() {
        return dataExtension;
    }

    public Class getDataReader() {
        return dataReader;
    }

    public Class getHeaderReader() {
        return headerReader;
    }

    public String getFormatName() {
        return formatName;
    }

    public static ImageIODescriptor loadFromXML(Element formatElement) throws BrainflowException {
        return new ImageIODescriptor(formatElement);
    }

    public FileObjectFilter createFileObjectFilter() {
        return fof;
    }

    class FOF implements FileObjectFilter {
        // matches headers only!
        public boolean accept(FileObject fobj) {
            return isHeaderMatch(fobj);
        }
    }



}
