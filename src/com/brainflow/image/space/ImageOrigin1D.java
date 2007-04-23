package com.brainflow.image.space;

import com.brainflow.image.anatomy.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 25, 2006
 * Time: 5:50:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageOrigin1D implements IImageOrigin {


    private AnatomicalPoint1D origin;

    private AnatomicalDirection[] dirs;

    public ImageOrigin1D(AnatomicalDirection a1, double x) {

        origin = new AnatomicalPoint1D(AnatomicalAxis.matchAnatomy(a1), x);

        dirs = new AnatomicalDirection[3];
        dirs[0] = a1;


    }

    public AnatomicalDirection[] getOriginalDirection() {
        return dirs;
    }

    public AnatomicalPoint getOrigin() {
        return origin;
    }
}
