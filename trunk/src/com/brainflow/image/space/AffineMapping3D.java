package com.brainflow.image.space;

import com.brainflow.utils.Point3D;
import com.brainflow.math.Vector3f;
import com.brainflow.math.Matrix4f;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 19, 2008
 * Time: 11:53:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class AffineMapping3D implements ImageMapping3D {

    private final Matrix4f mat;

    public AffineMapping3D(Matrix4f mat) {
        this.mat = mat;
    }

    public void gridToWorld(int i, int j, int k, Point3D out) {
        
    }

    public void gridToWorld(int i, int j, int k, Vector3f out) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Vector3f gridToWorld(Vector3f in) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Vector3f gridToWorld(float i, float j, float k) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Vector3f gridToWorld(int i, int j, int k) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void worldToGrid(Vector3f in, Vector3f out) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Vector3f worldToGrid(Vector3f in) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
