package com.brainflow.application.mapping;

import com.brainflow.application.ILoadableImage;
import com.brainflow.application.ImageIODescriptor;
import com.brainflow.application.SoftLoadableImage;
import com.brainflow.application.toplevel.ImageIOManager;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.local.LocalFile;

import java.io.FileOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 11, 2006
 * Time: 4:00:23 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestXStream {


    SoftLoadableImage limg;

    public TestXStream() {
        try {
            ImageIOManager.getInstance().initialize();


            FileObject fobj1 = VFS.getManager().resolveFile("/home/surge/FAgeXDiag.hdr");
            FileObject fobj2 = VFS.getManager().resolveFile("/home/surge/FAgeXDiag.img");

            ImageIODescriptor descriptor = ImageIOManager.getInstance().getDescriptor(fobj1);
            limg = descriptor.createLoadableImage(fobj1, fobj2);
            System.out.println(limg.getDataFile().getName());
            XStream xstream = new XStream(new DomDriver());
            xstream.alias("brain-data", ILoadableImage.class);
            xstream.alias("brain-data", SoftLoadableImage.class);
            xstream.alias("file-reference", FileObject.class);
            xstream.alias("file-reference", LocalFile.class);
            xstream.registerConverter(new ILoadableImageConverter());
            xstream.registerConverter(new FileObjectConverter());

            xstream.toXML(limg, new FileOutputStream("/home/surge/fobj.xml"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new TestXStream();
    }


}
