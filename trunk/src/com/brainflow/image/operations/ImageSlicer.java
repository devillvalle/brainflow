package com.brainflow.image.operations;

import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.data.BasicImageData2D;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.ImageFiller;
import com.brainflow.image.space.IImageSpace;

import java.util.logging.Logger;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class ImageSlicer {

    private Logger log = Logger.getLogger("com.brainflow.image");

    private IImageData3D image;

    private Anatomy3D displayAnatomy = Anatomy3D.AXIAL_LAI;


    public ImageSlicer(IImageData3D _image) {
        image = _image;

    }


    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }

    public void setDisplayAnatomy(Anatomy3D displayAnatomy) {
        this.displayAnatomy = displayAnatomy;
    }


    public BasicImageData2D getSlice(Anatomy3D displayAnatomy, AnatomicalPoint1D displaySlice) {

        // retrieves the ImageAxis for the image in data space
        // these axes may well be "flipped" with respect to the display space
        //ImageAxis a1 = space.getImageAxis(displayAnatomy.XAXIS, true);
        //ImageAxis a2 = space.getImageAxis(displayAnatomy.YAXIS, true);
        //ImageAxis a3 = space.getImageAxis(displayAnatomy.ZAXIS, true);


        ImageFiller filler = new ImageFiller();
        return filler.fillImage(image, displayAnatomy.XAXIS, displayAnatomy.YAXIS, displaySlice);

        /*int fixedSample = a3.nearestSample(slice);
        PlaneArray dataPlane = new PlaneArray(a1, a2);
        PlaneArray probePlane = dataPlane.matchPlane(displayAnatomy.XY_PLANE);
        // match axis may have to check for flipped version
        return getData(probePlane, dataPlane, space.findAxis(displayAnatomy.ZAXIS), fixedSample);
        */


    }

    /*protected BasicImageData2D getData(PlaneArray probe, PlaneArray data, Axis fixed, int idx) {
        BasicImageData2D outdata = null;
        int[] xsamples = data.getXSamples();
        int[] ysamples = data.getYSamples();
        int[] xprobe = probe.getXSamples();
        int[] yprobe = probe.getYSamples();

        IImageSpace space = new ImageSpace2D(new ImageAxis(probe.getXImageAxis()), new ImageAxis(probe.getYImageAxis()));
        //AxisRange xRange = probe.getXImageAxis().getRange();
        //AxisRange yRange = probe.getYImageAxis().getRange();

        outdata = new BasicImageData2D(space, image.getDataType());
        // surely this can be simplified with iterators???
        assert (fixed == Axis.Z_AXIS || fixed == Axis.X_AXIS || fixed == Axis.Y_AXIS);
        if (fixed == Axis.Z_AXIS) {
            if (probe.getAnatomicalPlane().isSwappedVersion(data.getAnatomicalPlane())) {
                for (int y = 0; y < ysamples.length; y++) {
                    for (int x = 0; x < xsamples.length; x++) {
                        outdata.setValue(xprobe[y], yprobe[x], image.getValue(xsamples[x], ysamples[y], idx));
                    }
                }
            } else {
                for (int y = 0; y < ysamples.length; y++) {
                    for (int x = 0; x < xsamples.length; x++) {
                        outdata.setValue(xprobe[x], yprobe[y], image.getValue(xsamples[x], ysamples[y], idx));
                    }
                }
            }
        } else if (fixed == Axis.Y_AXIS) {
            if (probe.getAnatomicalPlane().isSwappedVersion(data.getAnatomicalPlane())) {
                for (int y = 0; y < ysamples.length; y++) {
                    for (int x = 0; x < xsamples.length; x++) {
                        outdata.setValue(xprobe[y], yprobe[x], image.getValue(xsamples[x], idx, ysamples[y]));
                    }
                }
            } else {
                for (int y = 0; y < ysamples.length; y++) {
                    for (int x = 0; x < xsamples.length; x++) {
                        outdata.setValue(xprobe[x], yprobe[y], image.getValue(xsamples[x], idx, ysamples[y]));
                    }
                }
            }
        } else if (fixed == Axis.X_AXIS) {
            if (probe.getAnatomicalPlane().isSwappedVersion(data.getAnatomicalPlane())) {
                for (int y = 0; y < ysamples.length; y++) {
                    for (int x = 0; x < xsamples.length; x++) {
                        outdata.setValue(xprobe[y], yprobe[x], image.getValue(idx, xsamples[x], ysamples[y]));
                    }
                }
            } else {
                for (int y = 0; y < ysamples.length; y++) {
                    for (int x = 0; x < xsamples.length; x++) {
                        outdata.setValue(xprobe[x], yprobe[y], image.getValue(idx, xsamples[x], ysamples[y]));
                    }
                }
            }
        }

        return outdata;
    } */

}


