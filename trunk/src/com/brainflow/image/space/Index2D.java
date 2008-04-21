package com.brainflow.image.space;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 19, 2008
 * Time: 10:59:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class Index2D extends Index {

    public int i, j;


    public Index2D(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public final int dim() {
        return 2;
    }

    //public final int flatten(int dim1, int dim2) {
    //

    //}






}
