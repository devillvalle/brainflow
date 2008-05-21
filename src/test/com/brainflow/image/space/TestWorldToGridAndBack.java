package test.com.brainflow.image.space;

import com.brainflow.application.BrainflowException;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.space.IImageSpace3D;
import com.brainflow.image.space.ImageSpace3D;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import test.TUtils;
import test.TestUtils;

import java.util.Arrays;


/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 17, 2008
 * Time: 12:06:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestWorldToGridAndBack {

    IImageSpace3D space;

    IImageSpace3D space2;


    @Before
    public void methodSetup() {
        ImageAxis xaxis = new ImageAxis(0, 20, AnatomicalAxis.LEFT_RIGHT, 10);
        ImageAxis yaxis = new ImageAxis(0, 20, AnatomicalAxis.POSTERIOR_ANTERIOR, 10);
        ImageAxis zaxis = new ImageAxis(0, 20, AnatomicalAxis.INFERIOR_SUPERIOR, 10);
        space = new ImageSpace3D(xaxis, yaxis, zaxis);

        try {
            space2 = (IImageSpace3D)TestUtils.quickDataSource("icbm452_atlas_probability_gray.hdr").load().getImageSpace();
        } catch (BrainflowException e ) {
            fail();
        }
    }

    @Test
    public void testIndexToWorld() {

        TUtils.assertArrayEquals(space.indexToWorld(0,0,0), new float[] { 1,1,1}, .001f);
        TUtils.assertArrayEquals(space.indexToWorld(9,9,9), new float[] { 20-1,20-1,20-1}, .001f);


    }

    @Test
    public void testGridToWorld() {
        TUtils.assertArrayEquals(space.gridToWorld(0,0,0), new float[] { 0,0,0}, .001f);
        TUtils.assertArrayEquals(space.gridToWorld(20,20,20), new float[] { 40,40,40}, .001f);

    }

    @Test
    public void testWorldToGrid() {
        TUtils.assertArrayEquals(space.worldToGrid(0,0,0), new float[] { 0,0,0}, .001f);
        TUtils.assertArrayEquals(space.worldToGrid(40,40,40), new float[] { 20,20,20}, .001f);
        System.out.println(Arrays.toString(space.worldToGrid(40,40,40)));
    }

    @Test
    public void testCentroid() {
        System.out.println("centroid = " + space.getCentroid());
        assertEquals(space.getCentroid(), new AnatomicalPoint3D(Anatomy3D.REFERENCE_ANATOMY, 10, 10, 10));
    }

    @Test
    public void testAnalyzeSpace() {

        AnatomicalPoint3D centroid = (AnatomicalPoint3D)space2.getCentroid();
        float[] gridpos = space2.worldToGrid((float)centroid.getX(), (float)centroid.getY(), (float)centroid.getZ());
        float[] worldpos = space2.gridToWorld(gridpos);

        TUtils.assertArrayEquals(worldpos, new float[] { (float)centroid.getX(), (float)centroid.getY(), (float)centroid.getZ() }, .01f);

        System.out.println("centroid : " + centroid);
        System.out.println("gridpos : " + Arrays.toString(gridpos));
        System.out.println("worldpos : " + Arrays.toString(worldpos));


    }







}
