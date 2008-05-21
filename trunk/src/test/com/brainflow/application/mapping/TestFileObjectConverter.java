package test.com.brainflow.application.mapping;

import test.TestUtils;
import com.brainflow.application.mapping.XStreamUtils;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.AbstractFileObject;
import org.junit.*;
import static org.junit.Assert.*;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 12, 2007
 * Time: 11:45:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestFileObjectConverter {

    private static FileObject localFile;


    private static XStream xstream;

    private static String xmlString;

    public TestFileObjectConverter() {
    } // constructor

    @BeforeClass
    public static void unitSetup() {
      xstream = XStreamUtils.createDefaultXStream();
    } // unitSetup()

    @AfterClass
    public static void unitCleanup() {
        xstream = null;
        xmlString = null;
    }

    @Before
    public void methodSetup() {
        try {
            URL url = TestUtils.getDataURL("icbm_atlas_probability_white.hdr");
            localFile = VFS.getManager().resolveFile(url.getPath());


        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @After
    public void methodCleanup() {
    } // methodCleanup()

    @Test
    public void testMarshal() {

        xmlString = xstream.toXML(localFile);
        System.out.println("xmlString : " + xmlString);
        assertNotNull(xmlString);
    } // testMarshal()
    
    @Test
    public void testUnmarshal() {

        AbstractFileObject reconstituted = (AbstractFileObject) xstream.fromXML(xmlString);
        System.out.println("unmarshalled file : " + reconstituted);

        assertEquals(localFile, reconstituted);
    }


}
