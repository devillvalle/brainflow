package com.brainflow.image.data;

import com.brainflow.application.BrainflowException;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.io.BrainIO;
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
    public BasicImageData2D fillImage(DataAccessor3D inputData, AnatomicalAxis axis1,
                                      AnatomicalAxis axis2, AnatomicalPoint1D fixedPoint) {

        ispace = inputData.getImageSpace();

        AnatomicalAxis xaxis = ispace.getAnatomicalAxis(Axis.X_AXIS);
        AnatomicalAxis yaxis = ispace.getAnatomicalAxis(Axis.Y_AXIS);
        AnatomicalAxis zaxis = ispace.getAnatomicalAxis(Axis.Z_AXIS);

        QuickIterator fastIterator;
        QuickIterator slowIterator;

        float[] values = null;

        // XYZ
        if (xaxis.sameAxis(axis1) && yaxis.sameAxis(axis2)) {
            fastIterator = makeIterator(xaxis, axis1);
            slowIterator = makeIterator(yaxis, axis2);
            values = fillXYZ(inputData, fastIterator, slowIterator, getFixedValue(zaxis, fixedPoint));
        }
        // YXZ
        else if (yaxis.sameAxis(axis1) & xaxis.sameAxis(axis2)) {
            fastIterator = makeIterator(yaxis, axis1);
            slowIterator = makeIterator(xaxis, axis2);
            values = fillYXZ(inputData, fastIterator, slowIterator, getFixedValue(zaxis, fixedPoint));

        }

        //YZX
        else if (yaxis.sameAxis(axis1) && zaxis.sameAxis(axis2)) {
            fastIterator = makeIterator(yaxis, axis1);
            slowIterator = makeIterator(zaxis, axis2);
            values = fillYZX(inputData, fastIterator, slowIterator, getFixedValue(xaxis, fixedPoint));
        }
        //XZY
        else if (xaxis.sameAxis(axis1) && zaxis.sameAxis(axis2)) {
            fastIterator = makeIterator(xaxis, axis1);
            slowIterator = makeIterator(zaxis, axis2);
            values = fillXZY(inputData, fastIterator, slowIterator, getFixedValue(yaxis, fixedPoint));
        }

        //ZYX
        else if (zaxis.sameAxis(axis1) && yaxis.sameAxis(axis2)) {
            fastIterator = makeIterator(zaxis, axis1);
            slowIterator = makeIterator(yaxis, axis2);
            values = fillZYX(inputData, fastIterator, slowIterator, getFixedValue(xaxis, fixedPoint));
        }

        //ZXY
        else if (zaxis.sameAxis(axis1) & xaxis.sameAxis(axis2)) {
            fastIterator = makeIterator(zaxis, axis1);
            slowIterator = makeIterator(xaxis, axis2);
            values = fillZXY(inputData, fastIterator, slowIterator, getFixedValue(yaxis, fixedPoint));

        } else{
            throw new IllegalArgumentException("illegal combination of axes: " + axis1 + " and " + axis2);
        }

       

        AxisRange arange1 = ispace.getImageAxis(axis1, true).getRange();
        AxisRange arange2 = ispace.getImageAxis(axis2, true).getRange();


        ImageAxis a1 = new ImageAxis(arange1.getBeginning().getX(), arange1.getEnd().getX(),
                axis1, ispace.getDimension(axis1));

        ImageAxis a2 = new ImageAxis(arange2.getBeginning().getX(), arange2.getEnd().getX(),
                axis2, ispace.getDimension(axis2));


        return new BasicImageData2D(new ImageSpace2D(a1, a2), values);
      

    }

    protected int getFixedValue(AnatomicalAxis axis1, AnatomicalPoint1D fpoint) {

        ImageAxis iaxis = ispace.getImageAxis(axis1, true);
        if (fpoint.getAnatomy() == axis1) {
            return iaxis.nearestSample(fpoint);
        } else if (fpoint.getAnatomy() == axis1.getFlippedAxis()) {
            return iaxis.nearestSample(fpoint.mirrorPoint(iaxis));
        } else throw new AssertionError();

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


    public static void main(String[] args) {
        try {


            IImageData3D data = (IImageData3D) BrainIO.readAnalyzeImage("c:/code/icbm/icbm452_atlas_probability_white.img");
            ImageFiller filler = new ImageFiller();
            BasicImageData2D slice = filler.fillImage(data, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR,
                    new AnatomicalPoint1D(AnatomicalAxis.LEFT_RIGHT, 50));

            int count = 0;
            while (true) {
                //JAI.create("filestore", slice.snapShot(), "c:/sliceASL.png", "png");
                slice = filler.fillImage(data, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR,
                        new AnatomicalPoint1D(AnatomicalAxis.LEFT_RIGHT, 50));
                //JAI.create("filestore", slice.snapShot(), "c:/sliceAIL.png", "png");
                slice = filler.fillImage(data, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.INFERIOR_SUPERIOR,
                        new AnatomicalPoint1D(AnatomicalAxis.RIGHT_LEFT, 50));
                //JAI.create("filestore", slice.snapShot(), "c:/sliceAIR.png", "png");
                slice = filler.fillImage(data, AnatomicalAxis.ANTERIOR_POSTERIOR, AnatomicalAxis.SUPERIOR_INFERIOR,
                        new AnatomicalPoint1D(AnatomicalAxis.RIGHT_LEFT, 50));
                //JAI.create("filestore", slice.snapShot(), "c:/sliceAIR.png", "png");
                count++;
                System.out.println(count);
            }


        } catch (BrainflowException e) {
            e.printStackTrace();
        }
    }


}
