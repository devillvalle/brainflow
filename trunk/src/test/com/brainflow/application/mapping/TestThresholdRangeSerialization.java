package test.com.brainflow.application.mapping;

import com.brainflow.application.mapping.XStreamUtils;
import com.brainflow.display.ThresholdRange;
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
public class TestThresholdRangeSerialization {

    private static XStream xstream;

    private static ThresholdRange trange;

    private static String xmlString;

    @BeforeClass
    public static void unitSetup() {
        xstream = XStreamUtils.createDefaultXStream();
        trange = new ThresholdRange(-44, 44);


    } // unitSetup()


    @Test
    public void thresholdToXML() {
        xmlString = xstream.toXML(trange);
        assertNotNull(xmlString);
        System.out.println(xmlString);
    }

    @Test
    public void thresholdFromXML() {
        ThresholdRange recon = (ThresholdRange)xstream.fromXML(xmlString);
        assertEquals(recon.getMin(), trange.getMin(), .01);
        assertEquals(recon.getMax(), trange.getMax(), .01);

    }






}