package test.com.brainflow.core;

import com.brainflow.core.CursorPosition;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.space.IImageSpace3D;
import com.brainflow.image.space.ImageSpace3D;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 20, 2008
 * Time: 2:19:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestCursorPosition {

    CursorPosition pos;

    IImageSpace3D refSpace;

    @Before
    public void methodSetup() {

        ImageAxis xaxis = new ImageAxis(new AxisRange(AnatomicalAxis.RIGHT_LEFT, -100,100), 1);
        ImageAxis yaxis = new ImageAxis(new AxisRange(AnatomicalAxis.POSTERIOR_ANTERIOR, -200,200), 1);
        ImageAxis zaxis = new ImageAxis(new AxisRange(AnatomicalAxis.INFERIOR_SUPERIOR, -300,300), 1);

        refSpace = new ImageSpace3D(xaxis, yaxis, zaxis);
        pos = new CursorPosition(refSpace, 12, 12, 12);


    }

    @Test
    public void convertToWorld() {
        assertEquals(pos.getWorldPosition(), new AnatomicalPoint3D(Anatomy3D.REFERENCE_ANATOMY, -12, 12,12));



    }
}
