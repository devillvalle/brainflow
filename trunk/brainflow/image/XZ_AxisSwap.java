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

public class XZ_AxisSwap implements CoordinateSwap3D {

  public XZ_AxisSwap() {
  }

  // Coronal -- Sagittal
  public Point3D swap(Point3D in, Point3D out) {
    out.x = in.z;
    out.y = in.y;
    out.z = in.x;
    return out;
  }
}