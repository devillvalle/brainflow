package com.brainflow.image.io.afni;

import com.brainflow.core.BrainflowException;
import com.brainflow.image.io.ImageInfoReader;
import com.brainflow.image.io.ImageInfo;

import java.net.URL;
import java.io.*;
import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;

import org.apache.commons.vfs.FileObject;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 12, 2007
 * Time: 1:21:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class AFNIInfoReader implements ImageInfoReader {



    public ImageInfo readInfo(URL url) throws BrainflowException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ImageInfo readInfo(FileObject fobj) throws BrainflowException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ImageInfo readInfo(File f) throws BrainflowException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }




    private void skipNewLines(BufferedReader reader) throws IOException {
        String str = null;
        boolean marked = false;
        while ( (str = reader.readLine()).equals("")) {
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

        return AFNIAttribute.createAttribute(type, AFNIAttribute.parseName(nameStr), 
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
            f = new File("C:/javacode/googlecode/brainflow/test/data/ANOVA_lag+orig.HEAD");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            AFNIInfoReader areader = new AFNIInfoReader();
            Object ret = null;
            areader.skipNewLines(reader);
            do {
                ret = areader.parseElement(reader);
              
                if (ret != null) {
                    System.out.println(ret);
                }
            } while (ret != null);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
