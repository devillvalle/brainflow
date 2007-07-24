package com.brainflow.image.io.afni;

import com.brainflow.application.BrainflowException;
import com.brainflow.image.io.ImageInfoReader;
import org.apache.commons.vfs.FileObject;

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

    private AFNIImageInfo info = new AFNIImageInfo();

    public AFNIImageInfo readInfo(URL url) throws BrainflowException {
        try {
            readHeader(url.openStream());
        } catch (IOException e) {
            throw new BrainflowException(e);
        }

        return info;
    }

    public AFNIImageInfo readInfo(FileObject fobj) throws BrainflowException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public AFNIImageInfo readInfo(File f) throws BrainflowException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    private void readHeader(InputStream istream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));

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


    }

    private void skipNewLines(BufferedReader reader) throws IOException {
        String str = null;
        boolean marked = false;
        while ((str = reader.readLine()).equals("")) {
            reader.mark(500);
            System.out.println("skipping line: " + str);
            marked = true;
        }

        System.out.println("returning to mark: " + str);

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
