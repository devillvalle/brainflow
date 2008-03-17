package com.brainflow.image.io;

import com.brainflow.image.io.ImageInfo;
import com.brainflow.math.Matrix4f;
import com.brainflow.math.Vector3f;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 15, 2006
 * Time: 10:22:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class NiftiImageInfo extends ImageInfo {

    public int qfac = 1;

    public Matrix4f qform;

    public Matrix4f sform;

    public Vector3f quaternion;

    public Vector3f qoffset;

    public static Matrix4f quaternionToMatrix(float qb, float qc, float qd,
                                              float qx, float qy, float qz,
                                              float dx, float dy, float dz, float qfac) {

        Matrix4f R = new Matrix4f();
        double a, b = qb, c = qc, d = qd, xd, yd, zd;

        /* last row is always [ 0 0 0 1 ] */

        //R.m[3][0]=R.m[3][1]=R.m[3][2] = 0.0 ; R.m[3][3]= 1.0 ;
        R.m30 = R.m31 = R.m32 = 0;
        R.m33 = 1f;

        /* compute a parameter from b,c,d */

        a = 1.0 - (b * b + c * c + d * d);
        if (a < 1.e-7) {                   /* special case */
            a = 1.0 / Math.sqrt(b * b + c * c + d * d);
            b *= a;
            c *= a;
            d *= a;        /* normalize (b,c,d) vector */
            a = 0.0;                        /* a = 0 ==> 180 degree rotation */
        } else {
            a = Math.sqrt(a);                     /* angle = 2*arccos(a) */
        }

        /* load rotation matrix, including scaling factors for voxel sizes */

        xd = (dx > 0.0) ? dx : 1.0;       /* make sure are positive */
        yd = (dy > 0.0) ? dy : 1.0;
        zd = (dz > 0.0) ? dz : 1.0;

        if (qfac < 0.0) zd = -zd;         /* left handedness? */

        R.m00 = (float) ((a * a + b * b - c * c - d * d) * xd);
        R.m01 = (float) (2.0 * (b * c - a * d) * yd);
        R.m02 = (float) (2.0 * (b * d + a * c) * zd);
        R.m10 = (float) (2.0 * (b * c + a * d) * xd);
        R.m11 = (float) ((a * a + c * c - b * b - d * d) * yd);
        R.m12 = (float) (2.0 * (c * d - a * b) * zd);
        R.m20 = (float) (2.0 * (b * d - a * c) * xd);
        R.m21 = (float) (2.0 * (c * d + a * b) * yd);
        R.m22 = (float) ((a * a + d * d - c * c - b * b) * zd);

        /* load offsets */

        R.m03 = qx;
        R.m13 = qy;
        R.m23 = qz;

        return R;
    }


    public String toString() {
        return super.toString() + "\n"  + "NiftiImageInfo{" +
                "qfac=" + qfac +
                ", qform=" + qform +
                ", sform=" + sform +
                ", quaternion=" + quaternion +
                ", qoffset=" + qoffset +
                '}';
    }
}



