package com.brainflow.image.io.afni;

import com.brainflow.application.BrainflowException;
import com.brainflow.image.io.ImageInfoReader;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import java.io.*;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 12, 2007
 * Time: 1:21:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class AFNIInfoReader implements ImageInfoReader {


    public AFNIImageInfo readInfo(URL url) throws BrainflowException {
        try {
            AFNIImageInfo info = readHeader(url.openStream());
            return info;
        } catch (IOException e) {
            throw new BrainflowException(e);
        }


    }

    public AFNIImageInfo readInfo(FileObject fobj) throws BrainflowException {
        try {
            AFNIImageInfo info = readInfo(fobj.getURL());
            return info;
        } catch (FileSystemException e) {
            throw new BrainflowException(e);
        }
    }

    public AFNIImageInfo readInfo(File f) throws BrainflowException {
        try {
            InputStream istream = new FileInputStream(f);
            AFNIImageInfo info = readHeader(istream);
            return info;
        } catch (IOException e) {
            throw new BrainflowException(e);
        }


    }


    private AFNIImageInfo readHeader(InputStream istream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));

        AFNIImageInfo info = new AFNIImageInfo();

        AFNIAttribute ret = null;
        skipNewLines(reader);
        do {
            ret = parseElement(reader);
            if (ret != null) {
                info.putAttribute(ret.getKey(), ret);
                System.out.println(ret.toString());
            }

        } while (ret != null);

        System.out.println("info : " + info);
        return info;


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
            URL url = ClassLoader.getSystemResource("resources/data/avg152T1_brainRPI+orig.HEAD");
            AFNIInfoReader reader = new AFNIInfoReader();
            reader.readInfo(url);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
