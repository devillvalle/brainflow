package com.brainflow.image.anatomy;

import com.brainflow.utils.*;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.space.Axis;
import com.brainflow.image.*;
import com.brainflow.image.space.Plane;

import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class AnatomicalOrientation implements java.io.Serializable {


    private static final String __SAGITTAL = "Sagittal";
    private static final String __CORONAL = "Coronal";
    private static final String __AXIAL = "Axial";

    public static final AnatomicalOrientation SAGITTAL = AnatomicalOrientation.createSagittalOrientation();
    public static final AnatomicalOrientation CORONAL = AnatomicalOrientation.createCoronalOrientation();
    public static final AnatomicalOrientation AXIAL = AnatomicalOrientation.createAxialOrientation();

    private static AnatomicalOrientation[] instances = { SAGITTAL, CORONAL, AXIAL };



    private String orientationLabel = "NULL";


    public static CoordinateSwap3D yz = new YZ_AxisSwap();
    public static CoordinateSwap3D xyz = new XYZ_AxisSwap();
    public static CoordinateSwap3D xz = new XZ_AxisSwap();
    public static CoordinateSwap3D identitySwap = new IdentitySwap();


    private static HashMap swapMap = new HashMap();
    private static HashMap sliceMap = new HashMap();
    private static HashMap fixAxisMap = new HashMap();

    static {

        swapMap.put(__AXIAL + __SAGITTAL, xz);
        swapMap.put(__SAGITTAL + __AXIAL, xz);
        swapMap.put(__AXIAL + __CORONAL, yz);
        swapMap.put(__CORONAL + __AXIAL, yz);
        swapMap.put(__AXIAL + __AXIAL, identitySwap);
        swapMap.put(__CORONAL + __SAGITTAL, xz);
        swapMap.put(__SAGITTAL + __CORONAL, xz);
        swapMap.put(__SAGITTAL + __SAGITTAL, identitySwap);
        swapMap.put(__CORONAL + __CORONAL, identitySwap);

        sliceMap.put(__AXIAL + __SAGITTAL, Plane.YZ_PLANE);
        sliceMap.put(__SAGITTAL + __AXIAL, Plane.XZ_PLANE);
        sliceMap.put(__AXIAL + __CORONAL, Plane.XZ_PLANE);
        sliceMap.put(__CORONAL + __AXIAL, Plane.XZ_PLANE);
        sliceMap.put(__AXIAL + __AXIAL, Plane.XY_PLANE);
        sliceMap.put(__CORONAL + __SAGITTAL, Plane.YZ_PLANE);
        sliceMap.put(__SAGITTAL + __CORONAL, Plane.YZ_PLANE);
        sliceMap.put(__SAGITTAL + __SAGITTAL, Plane.XY_PLANE);
        sliceMap.put(__CORONAL + __CORONAL, Plane.XY_PLANE);

        fixAxisMap.put(__AXIAL + __SAGITTAL, Axis.X_AXIS);
        fixAxisMap.put(__SAGITTAL + __AXIAL, Axis.Y_AXIS);
        fixAxisMap.put(__AXIAL + __CORONAL, Axis.Y_AXIS);
        fixAxisMap.put(__CORONAL + __AXIAL, Axis.Y_AXIS);
        fixAxisMap.put(__CORONAL + __SAGITTAL, Axis.X_AXIS);
        fixAxisMap.put(__SAGITTAL + __CORONAL, Axis.X_AXIS);
        fixAxisMap.put(__SAGITTAL + __SAGITTAL, Axis.Z_AXIS);
        fixAxisMap.put(__CORONAL + __CORONAL, Axis.Z_AXIS);
        fixAxisMap.put(__AXIAL + __AXIAL, Axis.Z_AXIS);


    }  

    private AnatomicalOrientation() {

    }

    public static void main(String[] args) {

    }

    public static AnatomicalOrientation[] getInstances() {
        return AnatomicalOrientation.instances;
    }

    public static AnatomicalOrientation lookupByLabel(String label) {
        for (int i = 0; i < instances.length; i++) {
            if (instances[i].orientationLabel.equalsIgnoreCase(label))
                return instances[i];
        }

        return null;
    }

    public static Point3D swapAxes(AnatomicalOrientation from, AnatomicalOrientation to, Point3D pt) {
        String str = from.orientationLabel + to.orientationLabel;
        CoordinateSwap3D sc = (CoordinateSwap3D) swapMap.get(str);
        if (str == null)
            sc = identitySwap;
        return sc.swap(pt, new Point3D());
    }

    public static CoordinateSwap3D getAxisSwapper(AnatomicalOrientation from, AnatomicalOrientation to) {
        String str = from.orientationLabel + to.orientationLabel;
        CoordinateSwap3D sc = (CoordinateSwap3D) swapMap.get(str);
        if (str == null)
            sc = identitySwap;
        return sc;
    }

    public static AnatomicalOrientation[] getOrthogonalOrientations(AnatomicalOrientation orient) {
        AnatomicalOrientation[] other = new AnatomicalOrientation[2];
        if (orient == AnatomicalOrientation.SAGITTAL) {
            other[0] = AnatomicalOrientation.AXIAL;
            other[1] = AnatomicalOrientation.CORONAL;
        } else if (orient == AnatomicalOrientation.AXIAL) {
            other[0] = AnatomicalOrientation.SAGITTAL;
            other[1] = AnatomicalOrientation.CORONAL;
        } else if (orient == AnatomicalOrientation.CORONAL) {
            other[0] = AnatomicalOrientation.AXIAL;
            other[1] = AnatomicalOrientation.SAGITTAL;
        }

        return other;


    }

    public Axis getFixedAxisValue(AnatomicalOrientation to, Point3D cutPoint) {
        String str = orientationLabel + to.orientationLabel;
        Axis axis = (Axis) fixAxisMap.get(str);

        return axis;


    }

    public static Axis getFixedAxis(AnatomicalOrientation from, AnatomicalOrientation to) {
        String str = from.orientationLabel + to.orientationLabel;
        Axis axis = (Axis) fixAxisMap.get(str);
        return axis;

    }

    public static Plane getCutPlane(AnatomicalOrientation from, AnatomicalOrientation to) {
        String str = from.orientationLabel + to.orientationLabel;
        return (Plane) sliceMap.get(str);
    }

    public Plane getCutPlane(AnatomicalOrientation to) {
        String str = orientationLabel + to.orientationLabel;
        return (Plane)sliceMap.get(str);
    }

    

  

    



    private static AnatomicalOrientation createAxialOrientation() {
        AnatomicalOrientation o = new AnatomicalOrientation();
        o.orientationLabel = __AXIAL;
        //o.anatomies = AnatomicalVolume.getAxialFamily();
        return o;
    }

    private static AnatomicalOrientation createCoronalOrientation() {
        AnatomicalOrientation o = new AnatomicalOrientation();
        o.orientationLabel = __CORONAL;
        //o.anatomies = AnatomicalVolume.getCoronalFamily();
        return o;
    }

    private static AnatomicalOrientation createSagittalOrientation() {
        AnatomicalOrientation o = new AnatomicalOrientation();
        o.orientationLabel = __SAGITTAL;
        //o.anatomies = AnatomicalVolume.getSagittalFamily();
        return o;

    }

    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (!(other instanceof AnatomicalOrientation))
            return false;

        AnatomicalOrientation ao = (AnatomicalOrientation) other;
        if (ao.orientationLabel != orientationLabel)
            return false;
        else
            return true;
    }

    public String getOrientationLabel() {
        return orientationLabel;
    }

    public String toString() {
        return orientationLabel;
    }


}