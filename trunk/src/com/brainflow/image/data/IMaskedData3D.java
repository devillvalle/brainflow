package com.brainflow.image.data;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 9, 2007
 * Time: 11:44:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMaskedData3D extends IImageData3D {



    public int cardinality();

    public int isTrue(int x, int y, int z);

    public int isTrue(int index);

    
    


}
