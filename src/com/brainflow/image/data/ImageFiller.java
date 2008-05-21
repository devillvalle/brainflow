package com.brainflow.image.data;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace2D;
import test.Testable;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 25, 2006
 * Time: 11:30:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageFiller {

    private IImageSpace ispace;



    @Testable
    public BasicImageData2D fillImage(DataAccessor3D inputData, Anatomy3D displayAnatomy, int displayIndex) {

        ispace = inputData.getImageSpace();

        AnatomicalAxis xaxis = ispace.getAnatomicalAxis(Axis.X_AXIS);
        AnatomicalAxis yaxis = ispace.getAnatomicalAxis(Axis.Y_AXIS);
        AnatomicalAxis zaxis = ispace.getAnatomicalAxis(Axis.Z_AXIS);

        AnatomicalAxis axis1 = displayAnatomy.XAXIS;
        AnatomicalAxis axis2 = displayAnatomy.YAXIS;

        QuickIterator fastIterator;
        QuickIterator slowIterator;

        float[] values = null;

        // XYZ
        if (xaxis.sameAxis(axis1) && yaxis.sameAxis(axis2)) {
            fastIterator = makeIterator(xaxis, axis1);
            slowIterator = makeIterator(yaxis, axis2);
            values = fillXYZ(inputData, fastIterator, slowIterator, displayIndex);
            //values = fillXYZ(inputData, fastIterator, slowIterator, getFixedValue(zaxis, sliceIndex));
        }
        // YXZ
        else if (yaxis.sameAxis(axis1) & xaxis.sameAxis(axis2)) {
            fastIterator = makeIterator(yaxis, axis1);
            slowIterator = makeIterator(xaxis, axis2);
            values = fillYXZ(inputData, fastIterator, slowIterator, displayIndex);
            //values = fillYXZ(inputData, fastIterator, slowIterator, getFixedValue(zaxis, sliceIndex));

        }

        //YZX
        else if (yaxis.sameAxis(axis1) && zaxis.sameAxis(axis2)) {
            fastIterator = makeIterator(yaxis, axis1);
            slowIterator = makeIterator(zaxis, axis2);
            values = fillYZX(inputData, fastIterator, slowIterator, displayIndex);
            //values = fillYZX(inputData, fastIterator, slowIterator, getFixedValue(xaxis, sliceIndex));
        }
        //XZY
        else if (xaxis.sameAxis(axis1) && zaxis.sameAxis(axis2)) {
            fastIterator = makeIterator(xaxis, axis1);
            slowIterator = makeIterator(zaxis, axis2);
            values = fillXZY(inputData, fastIterator, slowIterator, getFixedValue(yaxis, displayIndex));
            //values = fillXZY(inputData, fastIterator, slowIterator, getFixedValue(yaxis, sliceIndex));
        }

        //ZYX
        else if (zaxis.sameAxis(axis1) && yaxis.sameAxis(axis2)) {
            fastIterator = makeIterator(zaxis, axis1);
            slowIterator = makeIterator(yaxis, axis2);
            values = fillZYX(inputData, fastIterator, slowIterator, getFixedValue(xaxis, displayIndex));
            //values = fillZYX(inputData, fastIterator, slowIterator, getFixedValue(xaxis, sliceIndex));
        }

        //ZXY
        else if (zaxis.sameAxis(axis1) & xaxis.sameAxis(axis2)) {
            fastIterator = makeIterator(zaxis, axis1);
            slowIterator = makeIterator(xaxis, axis2);
            values = fillZXY(inputData, fastIterator, slowIterator, getFixedValue(yaxis, displayIndex));
            //values = fillZXY(inputData, fastIterator, slowIterator, getFixedValue(yaxis, sliceIndex));

        } else{
            throw new IllegalArgumentException("illegal combination of axes: " + axis1 + " and " + axis2);
        }

       

        AxisRange arange1 = ispace.getImageAxis(axis1, true).getRange();
        AxisRange arange2 = ispace.getImageAxis(axis2, true).getRange();


        ImageAxis a1 = new ImageAxis(arange1.getBeginning().getValue(), arange1.getEnd().getValue(),
                axis1, ispace.getDimension(axis1));

        ImageAxis a2 = new ImageAxis(arange2.getBeginning().getValue(), arange2.getEnd().getValue(),
                axis2, ispace.getDimension(axis2));


        return new BasicImageData2D(new ImageSpace2D(a1, a2), values);
      

    }

    protected int getFixedValue(AnatomicalAxis axis1, int fpoint) {



        //ImageAxis iaxis = ispace.getImageAxis(axis1, true);
        //if (fpoint.getAnatomy() == axis1) {
        //    return iaxis.nearestSample(fpoint);
        //} else if (fpoint.getAnatomy() == axis1.getFlippedAxis()) {
        //    return iaxis.nearestSample(fpoint.mirrorPoint(iaxis));
        //} else throw new AssertionError();

        return 0 ;

    }

    private QuickIterator makeIterator(AnatomicalAxis axis1, AnatomicalAxis axis2) {
        if (axis1 == axis2) {
            return new Incrementor(0, ispace.getDimension(axis2));
        } else if (axis1 == axis2.getFlippedAxis()) {
            return new Decrementor(ispace.getDimension(axis2) - 1, -1);
        } else {
            throw new IllegalArgumentException("illegal axis");
        }

    }


    private float[] fillXYZ(DataAccessor3D data, QuickIterator fastIterator, QuickIterator slowIterator, int fixed) {
        int x, y, z;
        z = fixed;

        float[] op = new float[fastIterator.size() * slowIterator.size()];
        int i = 0;

        while (slowIterator.hasNext()) {
            y = slowIterator.next();
            while (fastIterator.hasNext()) {
                x = fastIterator.next();
                op[i] = (float)data.value(x, y, z);
                i++;
            }
            fastIterator.reset();
        }

        return op;

    }

    private float[] fillYXZ(DataAccessor3D data, QuickIterator fastIterator, QuickIterator slowIterator, int fixed) {
        int x, y, z;
        z = fixed;
        float[] op = new float[fastIterator.size() * slowIterator.size()];
        int i = 0;

        while (slowIterator.hasNext()) {
            x = slowIterator.next();

            while (fastIterator.hasNext()) {
                y = fastIterator.next();

                op[i] = (float)data.value(x, y, z);
                i++;
            }
            fastIterator.reset();
        }

        return op;

    }

    private float[] fillXZY(DataAccessor3D data, QuickIterator fastIterator, QuickIterator slowIterator, int fixed) {
        int x, y, z;
        y = fixed;
        float[] op = new float[fastIterator.size() * slowIterator.size()];
        int i = 0;

        while (slowIterator.hasNext()) {
            z = slowIterator.next();
            while (fastIterator.hasNext()) {
                x = fastIterator.next();
                op[i] = (float)data.value(x, y, z);
                i++;
            }
            fastIterator.reset();
        }

        return op;

    }

    private float[] fillYZX(DataAccessor3D data, QuickIterator fastIterator, QuickIterator slowIterator, int fixed) {
        int x, y, z;
        x = fixed;
        float[] op = new float[fastIterator.size() * slowIterator.size()];
        int i = 0;

        while (slowIterator.hasNext()) {
            z = slowIterator.next();
            while (fastIterator.hasNext()) {
                y = fastIterator.next();
                op[i] = (float)data.value(x, y, z);
                i++;
            }
            fastIterator.reset();
        }

        return op;

    }

    private float[] fillZYX(DataAccessor3D data, QuickIterator fastIterator, QuickIterator slowIterator, int fixed) {
        int x, y, z;
        x = fixed;
        float[] op = new float[fastIterator.size() * slowIterator.size()];
        int i = 0;

        while (slowIterator.hasNext()) {
            y = slowIterator.next();
            while (fastIterator.hasNext()) {
                z = fastIterator.next();

                op[i] = (float)data.value(x, y, z);
                i++;
            }
            fastIterator.reset();
        }

        return op;

    }

    private float[] fillZXY(DataAccessor3D data, QuickIterator fastIterator, QuickIterator slowIterator, int fixed) {
        int x, y, z;
        y = fixed;
        float[] op = new float[fastIterator.size() * slowIterator.size()];
        int i = 0;

        while (slowIterator.hasNext()) {
            x = slowIterator.next();
            while (fastIterator.hasNext()) {
                z = fastIterator.next();
                //System.out.println("x : " + x + " y: " + y + " z: " + z + "value : " + data.value(x, y, z));
                op[i] = (float)data.value(x, y, z);
                i++;
            }
            fastIterator.reset();
        }

        return op;

    }


    private static class Incrementor implements QuickIterator {

        private  int value;
        private final int startValue;
        private final int endValue;

        public Incrementor(int startValue, int endValue) {
            value = startValue;
            this.startValue = value;
            this.endValue = endValue;

        }

        public final int next() {
            return value++;
        }

        public final boolean hasNext() {
            return value < endValue;

        }

        public final void reset() {
            value = startValue;
        }

        public final int size() {
            return endValue - startValue;
        }
    }

    private static class Decrementor implements QuickIterator {

        private int value;
        private final int startValue;
        private final int endValue;

        public Decrementor(int startValue, int endValue) {
            value = startValue;
            this.startValue = value;
            this.endValue = endValue;

        }

        public final int next() {
            return value--;
        }

        public final boolean hasNext() {
            return value > endValue;

        }

        public final void reset() {
            value = startValue;
        }

        public final int size() {
            return startValue - endValue;
        }
    }


    


}
