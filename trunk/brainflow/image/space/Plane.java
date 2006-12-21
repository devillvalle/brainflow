package com.brainflow.image.space;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class Plane {

  public static final Plane XY_PLANE = new Plane("XY Plane", 0);
  public static final Plane YZ_PLANE = new Plane("YZ Plane", 1);
  public static final Plane XZ_PLANE = new Plane("XZ Plane", 2);

  private String label;
  private int id;

  private Plane(String _label, int _id) {
    label = _label;
    id = _id;
  }

  public int getId() {
    return id;
  }

  public String getLabel() {
    return label;
  }

  public boolean equals(Object other) {
    if ( !(other instanceof Plane) )
      return false;
    if ( ((Plane)other).id == id )
      return true;
    return false;
  }
  
  public String toString() {
      return getLabel();
  }


}