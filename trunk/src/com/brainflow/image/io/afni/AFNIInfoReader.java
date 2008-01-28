package com.brainflow.image.io.afni;

import com.brainflow.application.BrainflowException;
import com.brainflow.image.io.ImageInfoReader;
import com.brainflow.image.io.ImageInfo;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 12, 2007
 * Time: 1:21:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class AFNIInfoReader implements ImageInfoReader {

    public static String getHeaderName(String name) {
        if (name.endsWith(".BRIK")) {
            name = name.substring(0, name.length() - 4);
            return name + "HEAD";
        }
        if (name.endsWith(".BRIK")) {
            return name;
        }
        if (name.endsWith(".BRIK.gz")) {
            return name;
        }

        return name + ".HEAD";
    }

    public static String getImageName(String name) {
        if (name.endsWith(".hdr")) {
            name = name.substring(0, name.length() - 4);
            return name + "BRIK";
        }
        if (name.endsWith(".BRIK")) {
            return name;
        }

        if (name.endsWith(".BRIK.gz")) {
            return name;
        }

        if (name.endsWith(".HEAD.gz")) {
            name = name.substring(0, name.length() - 6);
            return name + "BRIK.gz";
        }

        return name + ".BRIK";
    }


    public List<AFNIImageInfo> readInfo(InputStream stream) throws BrainflowException {

        List<AFNIImageInfo> ret;

        try {
            ret = readHeader(stream);

        } catch (IOException e) {
            throw new BrainflowException(e);
        }

        return ret;


    }

    public List<AFNIImageInfo> readInfo(FileObject fobj) throws BrainflowException {
        List<AFNIImageInfo> ret;

        try {

            ret = readInfo(fobj.getURL().openStream());
            FileObject dataFile = VFS.getManager().resolveFile(fobj.getParent(), AFNIInfoReader.getImageName(fobj.getName().getBaseName()));
            for (ImageInfo ii : ret) {
                ii.setDataFile(dataFile);
            }

        } catch (FileSystemException e) {
            throw new BrainflowException(e);
        } catch(IOException e) {
            throw new BrainflowException(e);

        }

        return ret;
    }

    public List<AFNIImageInfo> readInfo(File f) throws BrainflowException {
        List<AFNIImageInfo> ret;
        try {
            InputStream istream = new FileInputStream(f);
            ret = readHeader(istream);
            FileObject dataFile = VFS.getManager().resolveFile(f.getParentFile(), AFNIInfoReader.getImageName(f.getName()));
            for (ImageInfo ii : ret) {
                ii.setDataFile(dataFile);
            }

        } catch (IOException e) {
            throw new BrainflowException(e);
        }

        return ret;


    }


    private List<AFNIImageInfo> readHeader(InputStream istream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));

        AFNIImageInfo info = new AFNIImageInfo();

        AFNIAttribute ret = null;
        skipNewLines(reader);
        do {
            ret = parseElement(reader);
            if (ret != null) {
                info.putAttribute(ret.getKey(), ret);
            }

        } while (ret != null);


        return Arrays.asList(info);


    }

    private void skipNewLines(BufferedReader reader) throws IOException {
        String str = null;
        boolean marked = false;
        while ((str = reader.readLine()).equals("")) {
            reader.mark(500);
            marked = true;
        }


        if (marked) {
            reader.reset();
        }
    }

    private AFNIAttribute parseElement(BufferedReader reader) throws IOException {

        String typeStr = reader.readLine();

        if (typeStr == null) return null;

        String nameStr = reader.readLine();
        String countStr = reader.readLine();

        StringBuffer sb = new StringBuffer();

        String line = null;
        do {
            line = reader.readLine();
            sb.append(line);
        } while (line != null && !line.equals(""));

        AFNIAttribute.AFNI_ATTRIBUTE_TYPE type = AFNIAttribute.parseType(typeStr.replaceFirst("-", "_"));

        return AFNIAttribute.createAttribute(type, AFNIAttributeKey.valueOf(AFNIAttribute.parseName(nameStr)),
                AFNIAttribute.parseCount(countStr), sb.toString());

    }

    enum AFNI_TYPE {
        string_attribute,
        float_attribute,
        integer_attribute,
        double_attribute;

    }


    public static void main(String[] args) {
        File f = null;
        try {
            URL url = ClassLoader.getSystemResource("resources/data/motion-reg2+orig.HEAD");
            ImageInfoReader reader = new AFNIInfoReader();

            List<? extends ImageInfo> ilist = reader.readInfo(url.openStream());

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
