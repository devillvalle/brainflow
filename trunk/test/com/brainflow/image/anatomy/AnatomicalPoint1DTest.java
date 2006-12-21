package com.brainflow.image.anatomy;

import org.testng.annotations.*;
import com.brainflow.image.axis.ImageAxis;

/**
 * AnatomicalPoint1D Tester.
 *
 * @author Brad Buchsbaum
 * @version $Revision$, $Date$
 * @created February 26, 2006
 * @since 1.0
 */
public class AnatomicalPoint1DTest {

    ImageAxis axis1 = new ImageAxis(0,100, AnatomicalAxis.LEFT_RIGHT, 100);
    ImageAxis axis2 = new ImageAxis(0,100, AnatomicalAxis.RIGHT_LEFT, 100);
    ImageAxis axis3 = new ImageAxis(-100,300, AnatomicalAxis.RIGHT_LEFT, 100);
    ImageAxis axis4 = new ImageAxis(-100,300, AnatomicalAxis.ANTERIOR_POSTERIOR, 100);

    @Configuration(beforeTestClass = true)
    public void setUp() {

    }


    @Test
    public void testMirrorPoint() {
        AnatomicalPoint1D point1 = new AnatomicalPoint1D(axis1.getAnatomicalAxis(), 40);
        assert point1.mirrorPoint(axis1).getX() == 40;
        assert point1.mirrorPoint(axis2).getX() == 60;

        assert true;
    }


}
