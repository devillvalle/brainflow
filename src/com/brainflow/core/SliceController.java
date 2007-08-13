package com.brainflow.core;

import com.brainflow.image.anatomy.AnatomicalPoint1D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 7, 2007
 * Time: 2:31:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SliceController {


    public AnatomicalPoint1D getSlice();

    public void setSlice(AnatomicalPoint1D slice);

    public void nextSlice();

    public void previousSlice();

    public void pageBack();

    public void pageForward();


}
