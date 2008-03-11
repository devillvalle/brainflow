package test.com.brainflow.application.mapping;

import com.brainflow.application.*;
import com.brainflow.application.mapping.XStreamUtils;
import com.brainflow.application.toplevel.ImageIOManager;
import com.brainflow.image.io.SoftImageDataSource;
import com.brainflow.image.io.IImageDataSource;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 13, 2007
 * Time: 4:30:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestImageDataSourceConverter {

    private static XStream xstream;

    private static String xmlString;

    private static FileObject headerFile;

    private static FileObject dataFile;

    private static IImageDataSource dataSource;

    @BeforeClass
    public static void unitSetup() {
      xstream = XStreamUtils.createDefaultXStream();
      try {
          ImageIOManager.getInstance().initialize();
      } catch(Exception e)  {
          e.printStackTrace();
          fail();
      }
    } // unitSetup()

    @AfterClass
    public static void unitCleanup() {
        xstream = null;

    }

    @Before
    public void methodSetup() {
        try {
            URL url = TestUtils.getDataURL("icbm452_atlas_probability_white.hdr");
            headerFile = VFS.getManager().resolveFile(url.getPath());

            url = TestUtils.getDataURL("icbm452_atlas_probability_white.img");
            dataFile = VFS.getManager().resolveFile(url.getPath());

            ImageIODescriptor descriptor = ImageIOManager.getInstance().getDescriptor(headerFile);
            dataSource = new SoftImageDataSource(descriptor, headerFile, dataFile);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testMarshal() {

        xmlString = xstream.toXML(dataSource);

        assertNotNull(xmlString);
    } // testMarshal()

    @Test
    public void testUnmarshal() {

        IImageDataSource reconstituted = (IImageDataSource) xstream.fromXML(xmlString);


        assertEquals(dataSource, reconstituted);
    }


}
