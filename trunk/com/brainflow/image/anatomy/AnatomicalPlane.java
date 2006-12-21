package com.brainflow.image.anatomy;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.IndexConverter2D;
import com.brainflow.image.IndexConverter1D;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Jun 30, 2003
 * Time: 12:35:21 PM
 * To change this template use Options | File Templates.
 */
public class AnatomicalPlane implements Anatomy {

    private static AnatomicalPlane[] instances = new AnatomicalPlane[24];

    public static final AnatomicalPlane SAGITTAL_AI = new AnatomicalPlane(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final AnatomicalPlane SAGITTAL_PI = new AnatomicalPlane(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final AnatomicalPlane SAGITTAL_PS = new AnatomicalPlane(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final AnatomicalPlane SAGITTAL_AS = new AnatomicalPlane(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final AnatomicalPlane SAGITTAL_IA = new AnatomicalPlane(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final AnatomicalPlane SAGITTAL_IP = new AnatomicalPlane(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final AnatomicalPlane SAGITTAL_SP = new AnatomicalPlane(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final AnatomicalPlane SAGITTAL_SA = new AnatomicalPlane(AnatomicalOrientation.SAGITTAL, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.ANTERIOR_POSTERIOR);

    public static final AnatomicalPlane CORONAL_LI = new AnatomicalPlane(AnatomicalOrientation.CORONAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final AnatomicalPlane CORONAL_RI = new AnatomicalPlane(AnatomicalOrientation.CORONAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.INFERIOR_SUPERIOR);
    public static final AnatomicalPlane CORONAL_RS = new AnatomicalPlane(AnatomicalOrientation.CORONAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final AnatomicalPlane CORONAL_LS = new AnatomicalPlane(AnatomicalOrientation.CORONAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.SUPERIOR_INFERIOR);
    public static final AnatomicalPlane CORONAL_IL = new AnatomicalPlane(AnatomicalOrientation.CORONAL, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final AnatomicalPlane CORONAL_IR = new AnatomicalPlane(AnatomicalOrientation.CORONAL, AnatomicalAxis.INFERIOR_SUPERIOR, AnatomicalAxis.RIGHT_LEFT);
    public static final AnatomicalPlane CORONAL_SR = new AnatomicalPlane(AnatomicalOrientation.CORONAL, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.RIGHT_LEFT);
    public static final AnatomicalPlane CORONAL_SL = new AnatomicalPlane(AnatomicalOrientation.CORONAL, AnatomicalAxis.SUPERIOR_INFERIOR, AnatomicalAxis.LEFT_RIGHT);


    public static final AnatomicalPlane AXIAL_LA = new AnatomicalPlane(AnatomicalOrientation.AXIAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final AnatomicalPlane AXIAL_RA = new AnatomicalPlane(AnatomicalOrientation.AXIAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.ANTERIOR_POSTERIOR);
    public static final AnatomicalPlane AXIAL_RP = new AnatomicalPlane(AnatomicalOrientation.AXIAL, AnatomicalAxis.RIGHT_LEFT, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final AnatomicalPlane AXIAL_LP = new AnatomicalPlane(AnatomicalOrientation.AXIAL, AnatomicalAxis.LEFT_RIGHT, AnatomicalAxis.POSTERIOR_ANTERIOR);
    public static final AnatomicalPlane AXIAL_AL = new AnatomicalPlane(AnatomicalOrientation.AXIAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final AnatomicalPlane AXIAL_AR = new AnatomicalPlane(AnatomicalOrientation.AXIAL, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.RIGHT_LEFT);
    public static final AnatomicalPlane AXIAL_PL = new AnatomicalPlane(AnatomicalOrientation.AXIAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.LEFT_RIGHT);
    public static final AnatomicalPlane AXIAL_PR = new AnatomicalPlane(AnatomicalOrientation.AXIAL, AnatomicalAxis.POSTERIOR_ANTERIOR, AnatomicalAxis.RIGHT_LEFT);



    private static int count=0;

    public final AnatomicalAxis XAXIS;
    public final AnatomicalAxis YAXIS;

    AnatomicalOrientation orientation;

    private AnatomicalPlane(AnatomicalOrientation _orientation, AnatomicalAxis xAxis, AnatomicalAxis yAxis) {
        orientation = _orientation;
        this.XAXIS = xAxis;
        this.YAXIS = yAxis;
        instances[count] = this;
        count++;

    }

    public static AnatomicalPlane matchAnatomy(AnatomicalDirection a1, AnatomicalDirection a2) {

        for (int i = 0; i < instances.length; i++) {
            AnatomicalPlane tmp = instances[i];
            if ((tmp.XAXIS.getMinDirection() == a1) && (tmp.YAXIS.getMinDirection() == a2)) {
                return tmp;
            }

        }
        throw new IllegalArgumentException("Axes do not correspond to valid anatomical volume ");

    }


    public static AnatomicalPlane matchAnatomy(AnatomicalAxis xaxis, AnatomicalAxis yaxis) {

        for (int i=0; i<instances.length; i++) {
            if ( (instances[i].XAXIS == xaxis) && (instances[i].YAXIS == yaxis) )
                return instances[i];
        }

        throw new IllegalArgumentException("Axes do not correspond to valid anatomical plane ");
    }

    public IndexConverter2D getIndexConverter(AnatomicalPlane other, int xdim, int ydim) {
        IndexConverter1D xcon = null;
        IndexConverter1D ycon = null;

        boolean swap = false;



        if (other.XAXIS == XAXIS) {
            xcon = new AnatomicalAxis.IndexDoNothing();
        }
        else if (other.XAXIS == XAXIS.getFlippedAxis()) {
            xcon = new AnatomicalAxis.IndexFlipper(xdim);
        }

        else if ( other.XAXIS == YAXIS) {
            ycon = new AnatomicalAxis.IndexDoNothing();
            swap=true;
        }

        else if ( other.XAXIS == YAXIS.getFlippedAxis()) {
            ycon = new AnatomicalAxis.IndexFlipper(ydim);
            swap=true;
        }

        else { throw new IllegalArgumentException("Cannot convert between orthogonal planes");  }

        if (other.YAXIS == YAXIS) {
            ycon = new AnatomicalAxis.IndexDoNothing();
        }

        else if (other.YAXIS == YAXIS.getFlippedAxis()) {
            ycon = new AnatomicalAxis.IndexFlipper(ydim);
        }

        else if (other.YAXIS == XAXIS) {
            xcon = new AnatomicalAxis.IndexDoNothing();
            swap=true;
        }

        else if ( other.YAXIS == XAXIS.getFlippedAxis()) {
            xcon = new AnatomicalAxis.IndexFlipper(xdim);
            swap=true;
        }

        else { throw new IllegalArgumentException("Cannot convert between orthogonal planes"); };

        if (swap) return new AnatomicalPlane.SwappingIndexConverter2D(xcon, ycon);
        else return new AnatomicalPlane.StandardIndexConverter2D(xcon, ycon);


    }

    public AnatomicalPlane getAxisSwappedPlane() {
        return matchAnatomy(YAXIS, XAXIS);
    }

    public boolean isSwappedVersion(AnatomicalPlane other) {
        if (other.XAXIS.sameAxis(YAXIS) && (other.YAXIS.sameAxis(XAXIS)) ) {
            return true;
        }

        return false;

    }


    public AnatomicalAxis[] getAnatomicalAxes() {
        return new AnatomicalAxis[] { XAXIS, YAXIS };
    }

    public AnatomicalOrientation getOrientation() {
        return orientation;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Axis 1: " + XAXIS + " ");
        sb.append("Axis 2: " + YAXIS);

        return sb.toString();

    }


    public static class StandardIndexConverter2D implements IndexConverter2D {

        IndexConverter1D one;
        IndexConverter1D two;

        public StandardIndexConverter2D(IndexConverter1D _one, IndexConverter1D _two) {
            one = _one;
            two = _two;
        }

        public final int[] convertXY(int x, int y) {
            return new int[] { one.convert(x), two.convert(y) };
        }

         public final int[] convertXY(int x, int y, int[] out) {
            out[0] = one.convert(x);
            out[1] = two.convert(y);
            return out;
        }


    }

    public static class SwappingIndexConverter2D implements IndexConverter2D {

        IndexConverter1D one;
        IndexConverter1D two;

        public SwappingIndexConverter2D(IndexConverter1D _one, IndexConverter1D _two) {
            one = _one;
            two = _two;
        }

        public final int[] convertXY(int x, int y) {
            return new int[] { one.convert(y), two.convert(x) };

        }

        public final int[] convertXY(int x, int y, int[] out) {
            out[0] = one.convert(y);
            out[1] = two.convert(x);
            return out;
        }


    }


}
