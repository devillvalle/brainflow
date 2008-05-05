package test

import org.junit.Before
import org.junit.Test
import com.brainflow.image.io.BrainIO
import com.brainflow.image.data.IImageData3D
import com.brainflow.image.interpolation.TrilinearInterpolator

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 22, 2008
 * Time: 12:55:38 PM
 * To change this template use File | Settings | File Templates.
 */
class TestInterpolation {

    def IImageData3D image

    def iname = "resources/data/mean-BRB-EPI-001.nii"


    def interp

    @Before void loadImage() {
        def url = ClassLoader.getSystemResource(iname)
        image = BrainIO.readNiftiImage(url)


        interp = new TrilinearInterpolator()

        println image
    }

    @Test void printValue() {
        println image.value(22,22,12)
        println image.getImageSpace()
        println image.worldValue(0,0,0, interp)



    }


}

