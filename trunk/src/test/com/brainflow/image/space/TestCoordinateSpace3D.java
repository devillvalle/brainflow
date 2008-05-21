package test.com.brainflow.image.space;

import org.junit.Before;
import org.junit.Test;
import com.brainflow.image.space.ICoordinateSpace3D;
import com.brainflow.image.space.CoordinateSpace3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import static junit.framework.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 20, 2008
 * Time: 9:48:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestCoordinateSpace3D {

    ICoordinateSpace3D space1;


    @Before
    public void methodSetup() {
        space1 = new CoordinateSpace3D(Anatomy3D.AXIAL_LPI);

    }

    @Test
    public void testCentroid() {
        AnatomicalPoint3D ap = space1.getCentroid();
   
        assertEquals(ap.getX(), 0, .0001);
        assertEquals(ap.getY(), 0, .0001);
        assertEquals(ap.getZ(), 0, .0001);

    }
}
