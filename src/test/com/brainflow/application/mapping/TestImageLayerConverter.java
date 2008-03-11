package test.com.brainflow.application.mapping;

import com.brainflow.image.io.IImageDataSource;
import com.brainflow.application.ImageIODescriptor;
import com.brainflow.image.io.SoftImageDataSource;
import com.brainflow.application.TestUtils;
import com.brainflow.application.mapping.XStreamUtils;
import com.brainflow.application.toplevel.ImageIOManager;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageLayer3D;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.junit.*;
import static org.junit.Assert.*;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 12, 2007
 * Time: 11:37:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestImageLayerConverter {

    private static XStream xstream;

    private static String xmlString;

    private static FileObject headerFile;

    private static FileObject dataFile;

    private static IImageDataSource dataSource;

    private static ImageLayer layer;

    public TestImageLayerConverter() {
    } // constructor

    @BeforeClass
    public static void unitSetup() {
        xstream = XStreamUtils.createDefaultXStream();
        xstream.setMode(XStream.ID_REFERENCES);
        try {
            ImageIOManager.getInstance().initialize();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    } // unitSetup()

    @AfterClass
    public static void unitCleanup() {
        dataFile = null;
        dataSource = null;
        layer = null;
        xmlString = null;
        xstream = null;
    } // unitCleanup()

    @Before
    public void methodSetup() {
        System.out.println("method setup");
        try {
            URL url = TestUtils.getDataURL("icbm452_atlas_probability_white.hdr");
            headerFile = VFS.getManager().resolveFile(url.getPath());

            url = TestUtils.getDataURL("icbm452_atlas_probability_white.img");
            dataFile = VFS.getManager().resolveFile(url.getPath());

            ImageIODescriptor descriptor = ImageIOManager.getInstance().getDescriptor(headerFile);
            dataSource = new SoftImageDataSource(descriptor, headerFile, dataFile);

            layer = new ImageLayer3D(dataSource);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        System.out.println("end method setup");

    } // methodSetup()


    @Test
    public void testMarshal() {
        xmlString = xstream.toXML(layer);
        assertNotNull(xmlString);
    } // testMarshal()

    @Test
    public void testUnmarshal() {
        ImageLayer reconstituted = (ImageLayer) xstream.fromXML(xmlString);
        assertEquals(reconstituted, layer);


    } // testUnmarshal()


    @Test
    public void testCanConvert() {
        fail(); // @todo - implement
    } // testCanConvert()
}
