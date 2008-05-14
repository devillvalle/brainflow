package test.com.brainflow;


import org.junit.*;
import static org.junit.Assert.*;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.io.IImageDataSource;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.application.TestUtils;
import com.brainflow.application.BrainflowException;
import com.brainflow.core.ImageLayer3D;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.SliceRenderer;

import java.util.Arrays;


/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 10, 2008
 * Time: 12:06:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestImageOverlay {


    IImageData3D background;

    IImageData3D foreground;

    ImageLayer3D foreLayer;

    ImageLayer3D backLayer;


    @Before
    public void methodSetup() {
        IImageDataSource source1 = TestUtils.quickDataSource("BRB-20071214-09-t1_mprage-001.nii");
        IImageDataSource source2 = TestUtils.quickDataSource("mean-BRB-EPI-001.nii");

        try {
            background = (IImageData3D)source1.load();
            foreground = (IImageData3D)source2.load();

            foreLayer = new ImageLayer3D(foreground);
            backLayer = new ImageLayer3D(background);
        } catch(BrainflowException e) {
            fail();
        }


    } // unitSetup()





    @Test
    public void testImageSpaceAfterLayerAdded() {
        System.out.println("foreground anatomy : " + foreground.getAnatomy());
        System.out.println("background anatomy : " + background.getAnatomy());

        IImageDisplayModel model = new ImageDisplayModel("testmodel");
        model.addLayer(backLayer);

        IImageSpace space1 = model.getImageSpace();
        System.out.println("model ref space : " + space1);
        model.addLayer(foreLayer);

        IImageSpace space2 = model.getImageSpace();
        assertEquals(space1, space2);

        SliceRenderer backRenderer = backLayer.getSliceRenderer(model.getImageSpace(), new AnatomicalPoint1D(AnatomicalAxis.INFERIOR_SUPERIOR, 0));

        SliceRenderer foreRenderer = foreLayer.getSliceRenderer(model.getImageSpace(), new AnatomicalPoint1D(AnatomicalAxis.INFERIOR_SUPERIOR, 0));

        System.out.println("background : (0,0,0) : " + Arrays.toString(background.getImageSpace().gridToWorld(new int[] {0,0,0})));
        System.out.println("background : (240,256,160) : " + Arrays.toString(background.getImageSpace().gridToWorld(new int[] {239,255,159})));




    }


}
