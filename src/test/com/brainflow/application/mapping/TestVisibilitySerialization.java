package test.com.brainflow.application.mapping;

import com.brainflow.application.mapping.XStreamUtils;
import com.brainflow.display.Opacity;
import com.brainflow.display.Visibility;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 14, 2007
 * Time: 7:56:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestVisibilitySerialization {

    private static XStream xstream;

    private static Visibility visible;

    private static String xmlString;

    @BeforeClass
    public static void unitSetup() {
        xstream = XStreamUtils.createDefaultXStream();
        visible = new Visibility(null, true);
        visible.setVisible(false);
        Annotations.configureAliases(xstream, Visibility.class);
    } // unitSetup()


    @Test
    public void opacityToXML() {
        xmlString = xstream.toXML(visible);
        assertNotNull(xmlString);
        System.out.println(xmlString);
    }

    @Test
    public void opacityFromXML() {
        Visibility recon = (Visibility)xstream.fromXML(xmlString);
        assertEquals(recon, visible);

    }






}