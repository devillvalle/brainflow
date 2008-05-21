package test.com.brainflow.application.mapping;

import com.brainflow.application.mapping.XStreamUtils;
import com.brainflow.core.layer.ImageLayerProperties;
import com.brainflow.core.ClipRange;
import com.brainflow.display.*;
import com.brainflow.utils.Range;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
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
public class TestLayerPropertiesSerialization {

    private static XStream xstream;

    private static ImageLayerProperties properties;
    private static String xmlString;

    @BeforeClass
    public static void unitSetup() {
        xstream = XStreamUtils.createDefaultXStream();
        properties = new ImageLayerProperties(new Range(0,255));
        Annotations.configureAliases(xstream, Opacity.class);
        Annotations.configureAliases(xstream, Visibility.class);
        Annotations.configureAliases(xstream, InterpolationMethod.class);
        Annotations.configureAliases(xstream, InterpolationType.class);
        Annotations.configureAliases(xstream, SmoothingRadius.class);
        Annotations.configureAliases(xstream, ImageLayerProperties.class);
        Annotations.configureAliases(xstream, ClipRange.class);



    } // unitSetup()


    @Test
    public void thresholdToXML() {
        xmlString = xstream.toXML(properties);
        assertNotNull(xmlString);
        System.out.println(xmlString);
    }

    @Test
    public void thresholdFromXML() {
        //ThresholdRange recon = (ThresholdRange)xstream.fromXML(xmlString);
        //assertEquals(recon.getMin(), trange.getMin(), .01);
        //assertEquals(recon.getMax(), trange.getMax(), .01);

    }






}