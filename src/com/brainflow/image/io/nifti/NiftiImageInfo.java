package com.brainflow.image.io.nifti;

import com.brainflow.image.io.ImageInfo;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 15, 2006
 * Time: 10:22:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class NiftiImageInfo extends ImageInfo {

    public int qfac = 1;

    public double[][] qform;
    public double[][] sform;

    public double[] quaternion;


}
