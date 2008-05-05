package test

import org.junit.Test
import org.junit.runner.JUnitCore
import org.junit.*;
import static org.junit.Assert.assertEquals
import com.brainflow.image.io.NiftiInfoReader
import com.brainflow.image.io.NiftiImageInfo
import org.apache.commons.vfs.VFS
import com.brainflow.image.space.AffineMapping3D
import com.brainflow.math.Vector3f
import com.brainflow.math.Matrix4f;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 22, 2008
 * Time: 9:39:22 AM
 * To change this template use File | Settings | File Templates.
 */
class TestLoadQForm {

    def qform

    def info

    def mapping


    @Before void readQForm() {
        def imagename = "resources/data/mean-BRB-EPI-001.nii"

        def reader = new NiftiInfoReader()
        info = reader.readInfo(getFileObject(imagename)).get(0) as NiftiImageInfo
        qform = info.qform

        def spacing = info.getSpacing()
        def offset = info.getOrigin()

      
        mapping = new AffineMapping3D(new Vector3f(offset.getX() as float, offset.getY() as float, offset.getZ() as float),
                   new Vector3f(spacing.getDim(0) as float, spacing.getDim(1) as float, spacing.getDim(2) as float),
                  info.getAnatomy());


        
        
    }

    @Test void mappingEqualsQForm() {
        assert mapping.getMatrix() == qform;
    }

    @Test void multQform() {
        def x = qform.mult3f(0,0,0)

        assert x == [110.74219, -110.74219, -46.749985] as float[]
    }

    @Test void makeImageSpace() {
        def space = info.createImageSpace()
   
        assert mapping == space.getMapping()
    }


    def getURL(iname) {
        ClassLoader.getSystemResource iname
    }

    def getFileObject(iname) {
        def url = getURL(iname)
        VFS.getManager().resolveFile url.getPath();

    }

}