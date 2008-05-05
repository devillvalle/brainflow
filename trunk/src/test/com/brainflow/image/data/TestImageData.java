package test.com.brainflow.image.data;

import com.brainflow.image.data.ImageData;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.iterators.ImageIterator;
import org.junit.*;
        import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 10, 2008
 * Time: 9:14:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestImageData {

    public TestImageData() {
    } // constructor

    @BeforeClass
    public static void unitSetup() {
    } // unitSetup()

    @AfterClass
    public static void unitCleanup() {
    } // unitCleanup()

    @Before
    public void methodSetup() {
    } // methodSetup()

    @After
    public void methodCleanup() {
    } // methodCleanup()

    @Test
    public void testCreateConstantData() {
        IImageData data = ImageData.createConstantData(32, new ImageSpace3D(new ImageAxis(0,10, AnatomicalAxis.LEFT_RIGHT,10),
                new ImageAxis(0,10, AnatomicalAxis.ANTERIOR_POSTERIOR,10),
                new ImageAxis(0,10, AnatomicalAxis.INFERIOR_SUPERIOR,10)));

        assertEquals(data.value(0), 32, .00001);
        assertEquals(data.value(10), 32, .00001);
        Assert.assertNotNull(data.iterator());

        ImageIterator iter = data.iterator();
        while (iter.hasNext()) {
            assertEquals(iter.next(), 32, .0001);
        }


    } // testCreateConstantData()
}
