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

public class XYZ_AxisSwap implements CoordinateSwap3D {

  public XYZ_AxisSwap() {
  }
  // swap of x, y and z axes
  // Axial -- Sagittal
  public Point3D swap(Point3D in, Point3D out) {
    out.setX(in.getY());
    out.setY(in.getZ());
    out.setZ(in.getX());
    return out;
  }

}