package com.brainflow.image.space;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 19, 2008
 * Time: 11:08:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class Index3D extends Index{

    public int i, j, k;


    public Index3D(int i, int j, int k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public final int dim() {
        return 3;
    }
}
