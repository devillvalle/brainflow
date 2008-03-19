package test.com.brainflow.image.data;

import com.brainflow.image.data.ImageFiller;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.application.TestUtils;
import org.junit.*;
        import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 18, 2008
 * Time: 12:01:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestImageFiller {

    IImageData3D data;
    ImageFiller filler;

    public TestImageFiller() {
    } // constructor

    @BeforeClass
    public static void unitSetup() {
    } // unitSetup()

    @AfterClass
    public static void unitCleanup() {
    } // unitCleanup()

    @Before
    public void methodSetup() {
        data = (IImageData3D)TestUtils.quickDataSource("BRB-20071214-09-t1_mprage-001.nii").getData();
        filler = new ImageFiller();
    } // methodSetup()

    @After
    public void methodCleanup() {
    } // methodCleanup()

    @Test
    public void testFillImage() {

        //IImageData2D data = filler.fillImage(data, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis)

    } // testFillImage()
}
