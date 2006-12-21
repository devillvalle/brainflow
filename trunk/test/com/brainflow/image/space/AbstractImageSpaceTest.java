package com.brainflow.image.space;

import org.testng.annotations.*;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalDirection;

import java.util.Arrays;

/**
 * AbstractImageSpace Tester.
 *
 * @author Brad Buchsbaum
 * @version $Revision$, $Date$
 * @created February 25, 2006
 * @since 1.0
 */
public class AbstractImageSpaceTest {


    IImageSpace space1;
    IImageSpace space2;


    @Configuration(beforeTestClass = true)
    public void setUp() {
        space1 = SpaceFactory.createImageSpace(new ImageAxis(0,100, AnatomicalAxis.LEFT_RIGHT, 50),
                                               new ImageAxis(0,100, AnatomicalAxis.ANTERIOR_POSTERIOR,50),
                                               new ImageAxis(0,100, AnatomicalAxis.INFERIOR_SUPERIOR,50));

        space2 = SpaceFactory.createImageSpace(new ImageAxis(-100,100, AnatomicalAxis.LEFT_RIGHT, 100),
                                               new ImageAxis(-100,200, AnatomicalAxis.ANTERIOR_POSTERIOR,50),
                                               new ImageAxis(-100,300, AnatomicalAxis.INFERIOR_SUPERIOR,25));

    }



    @Test
    public void testAnchorAxis() {
        assert space1.getImageAxis(Axis.X_AXIS).anchorAxis(AnatomicalDirection.RIGHT, 0).getRange().getMaximum() == 0;
        assert space1.getImageAxis(Axis.X_AXIS).anchorAxis(AnatomicalDirection.RIGHT, 0).getRange().getMinimum() == -100;
        assert space1.getImageAxis(Axis.X_AXIS).anchorAxis(AnatomicalDirection.LEFT, 0).getRange().getMaximum() == 100;
        assert space1.getImageAxis(Axis.X_AXIS).anchorAxis(AnatomicalDirection.LEFT, 0).getRange().getMinimum() == 0;

        assert true;
    }

    @Test
    public void testGetDimensionVector() {
        int[] dims = space1.getDimensionVector();
        assert dims[0] == 50;
        assert dims[1] == 50;
        assert dims[2] == 50;
        assert true;
    }

    @Test
    public void testGetDimension() {
        assert space1.getDimension(Axis.X_AXIS) == 50;
    }

    @Test
    public void testGetSpacing() {
        assert space1.getSpacing(Axis.X_AXIS) == 2;
    }

    @Test
    public void testGetExtent() {
        assert space1.getExtent(Axis.X_AXIS) == 100;
    }

    @Test
    public void testGetSpacing1() {
        assert space1.getSpacing(AnatomicalAxis.LEFT_RIGHT) == 2;
    }

    @Test
    public void testGetExtent1() {
        assert space1.getExtent(AnatomicalAxis.LEFT_RIGHT) == 100;
    }


    @Test
    public void testGetAnatomicalAxis() {
        assert space1.getAnatomicalAxis(Axis.X_AXIS) == AnatomicalAxis.LEFT_RIGHT;
        assert space1.getAnatomicalAxis(Axis.Y_AXIS) == AnatomicalAxis.ANTERIOR_POSTERIOR;
        assert space1.getAnatomicalAxis(Axis.Z_AXIS) == AnatomicalAxis.INFERIOR_SUPERIOR;
        assert space1.getAnatomicalAxis(Axis.Z_AXIS) != AnatomicalAxis.SUPERIOR_INFERIOR;

    }

    @Test
    public void testFindAxis() {
        assert space1.findAxis(AnatomicalAxis.LEFT_RIGHT) == Axis.X_AXIS;
        assert space1.findAxis(AnatomicalAxis.ANTERIOR_POSTERIOR) == Axis.Y_AXIS;
        assert space1.findAxis(AnatomicalAxis.POSTERIOR_ANTERIOR) == Axis.Y_AXIS;
        assert space1.findAxis(AnatomicalAxis.INFERIOR_SUPERIOR) != Axis.Y_AXIS;

    }

    @Test
    public void testSameAxes() {
        assert space1.sameAxes(space2);
        IImageSpace space3 = SpaceFactory.createImageSpace(new ImageAxis(-100,100, AnatomicalAxis.LEFT_RIGHT, 100),
                                                       new ImageAxis(-100,200, AnatomicalAxis.POSTERIOR_ANTERIOR,50),
                                                       new ImageAxis(-100,300, AnatomicalAxis.INFERIOR_SUPERIOR,25));

        assert !space1.sameAxes(space3);

    }

    @Test
    public void testUnion() {



        IImageSpace unionSpace = space1.union(space2);

        System.out.println("union xaxis: " + unionSpace.getImageAxis(Axis.X_AXIS));
        System.out.println("union yaxis: " + unionSpace.getImageAxis(Axis.Y_AXIS));
        System.out.println("union zaxis: " + unionSpace.getImageAxis(Axis.Z_AXIS));


        assert unionSpace.getImageAxis(Axis.X_AXIS).getSpacing() ==  Math.min(space1.getSpacing(Axis.X_AXIS), space2.getSpacing(Axis.X_AXIS));
        assert unionSpace.getImageAxis(Axis.Y_AXIS).getSpacing() ==  Math.min(space1.getSpacing(Axis.Y_AXIS), space2.getSpacing(Axis.Y_AXIS));
        assert unionSpace.getImageAxis(Axis.Z_AXIS).getSpacing() ==  Math.min(space1.getSpacing(Axis.Z_AXIS), space2.getSpacing(Axis.Z_AXIS));

        assert unionSpace.getImageAxis(Axis.X_AXIS).getRange().getMinimum() ==  Math.min(space1.getImageAxis(Axis.X_AXIS).getRange().getMinimum(),
                                                                                           space2.getImageAxis(Axis.X_AXIS).getRange().getMinimum());

        assert unionSpace.getImageAxis(Axis.Y_AXIS).getRange().getMinimum() ==  Math.min(space1.getImageAxis(Axis.Y_AXIS).getRange().getMinimum(),
                                                                                           space2.getImageAxis(Axis.Y_AXIS).getRange().getMinimum());

        assert unionSpace.getImageAxis(Axis.Z_AXIS).getRange().getMinimum() ==  Math.min(space1.getImageAxis(Axis.Z_AXIS).getRange().getMinimum(),
                                                                                           space2.getImageAxis(Axis.Z_AXIS).getRange().getMinimum());


        assert unionSpace.getImageAxis(Axis.X_AXIS).getRange().getMaximum() ==  Math.max(space1.getImageAxis(Axis.X_AXIS).getRange().getMaximum(),
                                                                                           space2.getImageAxis(Axis.X_AXIS).getRange().getMaximum());

        assert unionSpace.getImageAxis(Axis.Y_AXIS).getRange().getMaximum() ==  Math.max(space1.getImageAxis(Axis.Y_AXIS).getRange().getMaximum(),
                                                                                           space2.getImageAxis(Axis.Y_AXIS).getRange().getMaximum());

        assert unionSpace.getImageAxis(Axis.Z_AXIS).getRange().getMaximum() ==  Math.max(space1.getImageAxis(Axis.Z_AXIS).getRange().getMaximum(),
                                                                                           space2.getImageAxis(Axis.Z_AXIS).getRange().getMaximum());

    }


    @Test
    public void testGetNumDimensions() {
        assert space1.getNumDimensions() == 3;
    }

    @Test
    public void testGetNumSamples() {
        assert space1.getNumSamples() == 50*50*50;
    }

}
