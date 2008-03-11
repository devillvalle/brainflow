package test.com.brainflow.application.mapping;

import com.brainflow.application.mapping.XStreamUtils;
import com.brainflow.display.InterpolationMethod;
import com.brainflow.display.InterpolationType;
import com.brainflow.display.Opacity;
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
public class TestInterpolationMethodSerialization {

    private static XStream xstream;

    private static InterpolationMethod interp;

    private static String xmlString;

    @BeforeClass
    public static void unitSetup() {
        xstream = XStreamUtils.createDefaultXStream();
        interp = new InterpolationMethod(InterpolationType.LINEAR);

        Annotations.configureAliases(xstream, InterpolationMethod.class);
    } // unitSetup()


    @Test
    public void opacityToXML() {
        xmlString = xstream.toXML(interp);
        assertNotNull(xmlString);
        System.out.println(xmlString);
    }

    @Test
    public void opacityFromXML() {
        InterpolationMethod recon = (InterpolationMethod)xstream.fromXML(xmlString);
        assertEquals(recon, interp);

    }






}