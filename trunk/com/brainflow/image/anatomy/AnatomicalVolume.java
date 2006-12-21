package com.brainflow.image.anatomy;

import com.brainflow.utils.Point3D;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.Anatomy;
import org.apache.commons.lang.math.NumberUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Jul 1, 2003
 * Time: 9:16:40 AM
 * To change this template use Options | File Templates.
 */
public class AnatomicalVolume implements Anatomy {

    private static AnatomicalVolume[] instances = new AnatomicalVolume[24];




    public static final AnatomicalVolume AXIAL_LPI = new AnatomicalVolume(AnatomicalOrientation.AXIAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final AnatomicalVolume AXIAL_RPI = new AnatomicalVolume(AnatomicalOrientation.AXIAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final AnatomicalVolume AXIAL_LAI = new AnatomicalVolume(AnatomicalOrientation.AXIAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final AnatomicalVolume AXIAL_RAI = new AnatomicalVolume(AnatomicalOrientation.AXIAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final AnatomicalVolume AXIAL_LAS = new AnatomicalVolume(AnatomicalOrientation.AXIAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final AnatomicalVolume AXIAL_RAS = new AnatomicalVolume(AnatomicalOrientation.AXIAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final AnatomicalVolume AXIAL_LPS = new AnatomicalVolume(AnatomicalOrientation.AXIAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final AnatomicalVolume AXIAL_RPS = new AnatomicalVolume(AnatomicalOrientation.AXIAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR);

    public static final AnatomicalVolume SAGITTAL_AIL = new AnatomicalVolume(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final AnatomicalVolume SAGITTAL_PIL = new AnatomicalVolume(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final AnatomicalVolume SAGITTAL_ASL = new AnatomicalVolume(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final AnatomicalVolume SAGITTAL_PSL = new AnatomicalVolume(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final AnatomicalVolume SAGITTAL_AIR = new AnatomicalVolume(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.RIGHT_LEFT);
    public static final AnatomicalVolume SAGITTAL_PIR = new AnatomicalVolume(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.RIGHT_LEFT);
    public static final AnatomicalVolume SAGITTAL_ASR = new AnatomicalVolume(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.RIGHT_LEFT);
    public static final AnatomicalVolume SAGITTAL_PSR = new AnatomicalVolume(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.RIGHT_LEFT);

    public static final AnatomicalVolume CORONAL_LIA = new AnatomicalVolume(AnatomicalOrientation.CORONAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final AnatomicalVolume CORONAL_RIA = new AnatomicalVolume(AnatomicalOrientation.CORONAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final AnatomicalVolume CORONAL_LSA = new AnatomicalVolume(AnatomicalOrientation.CORONAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final AnatomicalVolume CORONAL_RSA = new AnatomicalVolume(AnatomicalOrientation.CORONAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final AnatomicalVolume CORONAL_LIP = new AnatomicalVolume(AnatomicalOrientation.CORONAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final AnatomicalVolume CORONAL_RIP = new AnatomicalVolume(AnatomicalOrientation.CORONAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final AnatomicalVolume CORONAL_LSP = new AnatomicalVolume(AnatomicalOrientation.CORONAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final AnatomicalVolume CORONAL_RSP = new AnatomicalVolume(AnatomicalOrientation.CORONAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.POSTERIOR_ANTERIOR);


    public static final AnatomicalVolume referenceAnatomy = AXIAL_LPI;

    private AnatomicalOrientation orientation;

    public final AnatomicalAxis XAXIS;
    public final AnatomicalAxis YAXIS;
    public final AnatomicalAxis ZAXIS;

    public final AnatomicalPlane XY_PLANE;
    public final AnatomicalPlane XZ_PLANE;
    public final AnatomicalPlane YZ_PLANE;

    private static int count = 0;


    private AnatomicalVolume(AnatomicalOrientation _orientation, AnatomicalAxis _xAxis, AnatomicalAxis _yAxis, AnatomicalAxis _zAxis) {
        orientation = _orientation;
        this.XAXIS = _xAxis;
        this.YAXIS = _yAxis;
        this.ZAXIS = _zAxis;

        XY_PLANE = AnatomicalPlane.matchAnatomy(XAXIS, YAXIS);
        XZ_PLANE = AnatomicalPlane.matchAnatomy(XAXIS, ZAXIS);
        YZ_PLANE = AnatomicalPlane.matchAnatomy(YAXIS, ZAXIS);

        instances[count] = this;
        count++;
    }

    public AnatomicalAxis[] getAnatomicalAxes() {
        return new AnatomicalAxis[] { XAXIS, YAXIS, ZAXIS };
    }



    public static double findExtreme(double[] edgepts, AnatomicalAxis axis, AnatomicalVolume reference) {
        AnatomicalAxis refAxis = reference.matchAxis(axis);
        assert refAxis != null;

        if (refAxis == axis) {
            //same axis, choose lowest value
            return NumberUtils.min(edgepts);
        }
        else {
            return NumberUtils.max(edgepts);
        }

    }

    public AnatomicalAxis matchAxis(AnatomicalAxis axis) {
        if (axis.sameAxis(XAXIS)) return XAXIS;
        if (axis.sameAxis(YAXIS)) return YAXIS;
        if (axis.sameAxis(ZAXIS)) return ZAXIS;

        return null;

    }

    public static AnatomicalVolume getCanonicalSagittal() {
        return AnatomicalVolume.SAGITTAL_ASL;
    }

     public static AnatomicalVolume getCanonicalCoronal() {
        return AnatomicalVolume.CORONAL_LSA;
    }

    public static AnatomicalVolume getCanonicalAxial() {
        return AnatomicalVolume.AXIAL_LAI;

    }



    public AnatomicalVolume[] getCanonicalOrthogonal() {
      if (isAxial()) {
        return new AnatomicalVolume[] {this, AnatomicalVolume.SAGITTAL_ASL, AnatomicalVolume.CORONAL_LSA};
      }
      if (isSagittal()) {
        return new AnatomicalVolume[] {this, AnatomicalVolume.AXIAL_LAI, AnatomicalVolume.CORONAL_LSA};
      }
      if (isCoronal()) {
        return new AnatomicalVolume[] {this, AnatomicalVolume.AXIAL_LAI, AnatomicalVolume.SAGITTAL_ASL};
      }
      else {
        // never here
        throw new RuntimeException("AnatomicalVolume.getCanonicalOrthogonal(...) : reached ureachable else");
      }

    }

    public boolean isAxial() {
        if (orientation == AnatomicalOrientation.AXIAL)
            return true;
        return false;
    }

    public boolean isSagittal() {
            if (orientation == AnatomicalOrientation.SAGITTAL)
                return true;
            return false;
    }

    public boolean isCoronal() {
            if (orientation == AnatomicalOrientation.CORONAL)
                return true;
            return false;
    }


    public AnatomicalOrientation getOrientation() {
        return orientation;
    }

    public static AnatomicalVolume matchAnatomy(AnatomicalDirection a1, AnatomicalDirection a2, AnatomicalDirection a3) {

        for (int i=0; i<instances.length; i++) {
            AnatomicalVolume tmp = instances[i];
            if ( (tmp.XAXIS.getMinDirection() == a1) && (tmp.YAXIS.getMinDirection() == a2) && (tmp.ZAXIS.getMinDirection() == a3) ) {
                return tmp;
            }

        }

        
        throw new IllegalArgumentException("Axes do not correspond to valid anatomical volume ");

    }

    public static AnatomicalVolume matchAnatomy(AnatomicalAxis a1, AnatomicalAxis a2, AnatomicalAxis a3) {

        for (int i=0; i<instances.length; i++) {
            AnatomicalVolume tmp = instances[i];
            if ( (tmp.XAXIS == a1) && (tmp.YAXIS == a2) && (tmp.ZAXIS == a3) ) {
                return tmp;
            }

        }

        throw new IllegalArgumentException("Axes do not correspond to valid anatomical volume ");

    }





    public static AnatomicalVolume[] getAxialFamily() {
        AnatomicalVolume[] ret = new AnatomicalVolume[8];
        int count=0;
        for (int i=0; i<instances.length; i++) {
            if (instances[i].getOrientation() == AnatomicalOrientation.AXIAL) {
                ret[count] = instances[i];
                count++;
            }
        }

        return ret;
    }

    public static AnatomicalVolume[] getSagittalFamily() {
        AnatomicalVolume[] ret = new AnatomicalVolume[8];
        int count=0;
        for (int i=0; i<instances.length; i++) {
            if (instances[i].getOrientation() == AnatomicalOrientation.SAGITTAL) {
                ret[count] = instances[i];
                count++;
            }
        }

        return ret;
    }

    public static AnatomicalVolume[] getCoronalFamily() {
        AnatomicalVolume[] ret = new AnatomicalVolume[8];
        int count=0;
        for (int i=0; i<instances.length; i++) {
            if (instances[i].getOrientation() == AnatomicalOrientation.CORONAL) {
                ret[count] = instances[i];
                count++;
            }
        }

        return ret;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Orientation: " + orientation.getOrientationLabel() + "\n");
        sb.append("X Axis: \n");
        sb.append("\t" + XAXIS + "\n");
        sb.append("Y Axis: \n");
        sb.append("\t" + YAXIS + "\n");
        sb.append("Z Axis: \n");
        sb.append("\t" + ZAXIS + "\n");
        return sb.toString();

    }



}
