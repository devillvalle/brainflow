package test.com.brainflow.image.anatomy;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.axis.CoordinateAxis;
import com.brainflow.image.space.CoordinateSpace3D;
import com.brainflow.image.space.ICoordinateSpace3D;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 20, 2008
 * Time: 11:03:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestAnatomicalPoint3D {

    AnatomicalPoint3D pt1;

    ICoordinateSpace3D space1;

    ICoordinateSpace3D space2;

    ICoordinateSpace3D space3;


    @Before
    public void methodSetup() {

        space1 = new CoordinateSpace3D(
                new CoordinateAxis(new AxisRange(AnatomicalAxis.RIGHT_LEFT, 0, 100)),
                new CoordinateAxis(new AxisRange(AnatomicalAxis.ANTERIOR_POSTERIOR, 0, 100)),
                new CoordinateAxis(new AxisRange(AnatomicalAxis.INFERIOR_SUPERIOR, 0, 100)));

        space2 = new CoordinateSpace3D(
                new CoordinateAxis(new AxisRange(AnatomicalAxis.LEFT_RIGHT, -100, 100)),
                new CoordinateAxis(new AxisRange(AnatomicalAxis.POSTERIOR_ANTERIOR, -100, 100)),
                new CoordinateAxis(new AxisRange(AnatomicalAxis.INFERIOR_SUPERIOR, -100, 100)));

        space3 = new CoordinateSpace3D(
                new CoordinateAxis(new AxisRange(AnatomicalAxis.RIGHT_LEFT, -100, 100)),
                new CoordinateAxis(new AxisRange(AnatomicalAxis.ANTERIOR_POSTERIOR, -100, 100)),
                new CoordinateAxis(new AxisRange(AnatomicalAxis.SUPERIOR_INFERIOR, -100, 100)));

        pt1 = new AnatomicalPoint3D(space1, 92, 50, 50);


    }

    @Test
    public void convertPoint() {
        AnatomicalPoint3D pt2 = pt1.convertTo(space2);
        assertEquals(pt2.getX(), -92, .01);
        assertEquals(pt2.getY(), -50, .01);
        assertEquals(pt2.getZ(), 50, .01);

        AnatomicalPoint3D pt3 = new AnatomicalPoint3D(space2, 25, 25, 25);
        AnatomicalPoint3D pt4 = pt3.convertTo(space3);

        assertEquals(pt4.getX(), -25, .01);
        assertEquals(pt4.getY(), -25, .01);
        assertEquals(pt4.getZ(), -25, .01);



    }
}
