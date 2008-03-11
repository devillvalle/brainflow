package test.com.brainflow.application.mapping;

import com.brainflow.application.mapping.XStreamUtils;
import com.brainflow.display.Opacity;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import org.apache.commons.vfs.VFS;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 14, 2007
 * Time: 7:56:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestOpacitySerialization {

    private static XStream xstream;

    private static Opacity opacity;

    private static String xmlString;

    @BeforeClass
    public static void unitSetup() {
        xstream = XStreamUtils.createDefaultXStream();
        opacity = new Opacity();
        opacity.setOpacity(.5);
        Annotations.configureAliases(xstream, Opacity.class);
    } // unitSetup()


    @Test
    public void opacityToXML() {
        xmlString = xstream.toXML(opacity);
        assertNotNull(xmlString);
        System.out.println(xmlString);
    }

    @Test
    public void opacityFromXML() {
        Opacity recon = (Opacity)xstream.fromXML(xmlString);
        assertEquals(recon, opacity);

    }






}
