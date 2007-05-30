package com.brainflow.image.operations;

import com.brainflow.image.io.analyze.AnalyzeIO;
import com.brainflow.image.io.analyze.AnalyzeInfoReader;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.data.MaskedData3D;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.BinaryImageData3D;
import com.brainflow.display.ThresholdRange;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 28, 2007
 * Time: 12:34:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestBinaryOperations {

    public static void main(String[] args) {

        try {
            URL url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_temporal.hdr");
            IImageData data = AnalyzeIO.readAnalyzeImage(url);

            MaskedData3D mask1 = new MaskedData3D((IImageData3D)data, new ThresholdRange(-50, 16000));
            System.out.println("mask1 cardinality : " + mask1.cardinality());

            BinaryImageData3D bdat = new BinaryImageData3D(mask1);
            System.out.println("bdat cardinality : " + bdat.cardinality());


            url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_gray.hdr");
            data = AnalyzeIO.readAnalyzeImage(url);
            MaskedData3D mask2 = new MaskedData3D((IImageData3D)data, new ThresholdRange(-1,16000));

            System.out.println("mask2 cardinality : " + mask2.cardinality());


            BinaryImageData3D bdat3 = bdat.AND(new BinaryImageData3D(mask2));

            System.out.println("bdat3 cardinality : " + bdat3.cardinality());


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        
    }


}
