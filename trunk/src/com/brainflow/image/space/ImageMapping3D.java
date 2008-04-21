package com.brainflow.image.space;

import com.brainflow.utils.Point3D;
import com.brainflow.math.Vector3f;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 19, 2008
 * Time: 11:10:07 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageMapping3D {


    public void gridToWorld(int i, int j, int k, Vector3f out);

    public Vector3f gridToWorld(int i, int j, int k);

    public Vector3f gridToWorld(float i, float j, float k);

    public Vector3f gridToWorld(Vector3f in);

    public void worldToGrid(Vector3f in, Vector3f out);

    public Vector3f worldToGrid(Vector3f in);

    
}
