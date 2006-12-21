package com.brainflow.image;
import com.brainflow.utils.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class YZ_AxisSwap implements CoordinateSwap3D {

  public YZ_AxisSwap() {
  }

  public Point3D swap(Point3D in, Point3D out) {
    out.x = in.x;
    out.y = in.z;
    out.z = in.y;
    return out;
  }
}