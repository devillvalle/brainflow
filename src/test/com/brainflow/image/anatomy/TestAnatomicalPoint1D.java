package test.com.brainflow.image.anatomy;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.axis.CoordinateAxis;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 20, 2008
 * Time: 11:47:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestAnatomicalPoint1D {

    CoordinateAxis subAxis;

    CoordinateAxis bigAxis;

    @Before
    public void methodSetup() {
        subAxis = new CoordinateAxis(new AxisRange(AnatomicalAxis.RIGHT_LEFT, 0, 100));
        bigAxis = new CoordinateAxis(new AxisRange(AnatomicalAxis.LEFT_RIGHT, -100, 100));

    }

    @Test
    public void testConvert1() {


        AnatomicalPoint1D pt1 = new AnatomicalPoint1D(subAxis, 92);
        AnatomicalPoint1D pt2 = pt1.convertTo(bigAxis);
        System.out.println("conversion 1" + pt2);
        assertEquals(pt2.getValue(), -92, .001);

        CoordinateAxis biggerAxis = new CoordinateAxis(new AxisRange(AnatomicalAxis.LEFT_RIGHT, -200, 200));
        AnatomicalPoint1D pt3 = pt1.convertTo(biggerAxis);
        assertEquals(pt3.getValue(), -92, .001);

        biggerAxis = new CoordinateAxis(new AxisRange(AnatomicalAxis.LEFT_RIGHT, -300, 300));
        AnatomicalPoint1D pt4 = pt1.convertTo(biggerAxis);
        assertEquals(pt4.getValue(), -92, .001);



    }

}
