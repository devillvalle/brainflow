package com.brainflow.image.axis;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalDirection;
import com.brainflow.image.anatomy.AnatomicalPoint1D;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Jan 30, 2004
 * Time: 11:30:47 AM
 * To change this template use Options | File Templates.
 */
public class ImageAxis extends CoordinateAxis {


    protected int samples;

    protected int[] sampleArray;

    protected double spacing;


    public ImageAxis() {
        super(AnatomicalAxis.LEFT_RIGHT);
        samples = 1;
        sampleArray = new int[1];
        spacing = 1;
    }

    public ImageAxis(ImageAxis src) {
        super(src.getAxis(), src.getRange());

        setAxis(src.getAxis());
        samples = src.samples;

        spacing = src.spacing;

    }

    public ImageAxis(AxisRange range, double spacing) {
        super(range.getAnatomicalAxis(), range);
        this.samples = (int) (range.getInterval() / spacing);
        this.spacing = spacing;

    }


    public ImageAxis(double begin, double end, AnatomicalAxis _axis, int _samples) {
        super(_axis, new AxisRange(_axis, begin, end));
        samples = _samples;
        spacing = getRange().getInterval() / samples;
    }

    public ImageAxis flip() {
        return new ImageAxis(getRange().getBeginning().getX(), getRange().getEnd().getX(), getAxis().getFlippedAxis(), samples);
    }


    public ImageAxis anchorAxis(AnatomicalDirection adir, double value) {
        if (adir == getAxis().getMaxDirection()) {
            return new ImageAxis(new AxisRange(getAxis(), value - getRange().getInterval(), value), spacing);
        } else if (adir == getAxis().getMinDirection()) {
            return new ImageAxis(new AxisRange(getAxis(), value, value + getRange().getInterval()), spacing);
        } else {
            throw new ImageAxis.IncompatibleAxisException("cannot anchor to axis direction: " + adir + " for axis " + getAxis());
        }
    }

    public ImageAxis matchAxis(AnatomicalAxis aaxis) {
        if (aaxis == getAxis()) {
            return this;
        } else if (aaxis == getAxis().getFlippedAxis()) {
            return flip();
        }

        throw new ImageAxis.IncompatibleAxisException("cannot match axes: " + aaxis + " and " + getAxis());

    }

    public ImageAxis matchAxis(ImageAxis src) {
        if (src.getAxis() == getAxis()) {
            return src;
        } else if (src.getAxis() == getAxis().getFlippedAxis()) {
            return flip();
        }

        throw new ImageAxis.IncompatibleAxisException("cannot match axes: " + src.getAxis() + " and " + getAxis());
    }






    public int getNumSamples() {
        return samples;
    }

    public final int nearestSample(double pt) {
        int idx = (int) (Math.abs(pt - getRange().getMinimum()) / spacing);
        idx = Math.max(idx, 0);
        idx = Math.min(idx, getNumSamples() - 1);
        return getSamples()[idx];
    }

    public final int nearestSample(AnatomicalPoint1D pt) {
        assert pt.getAnatomy().sameAxis(getAxis());


        if (pt.getAnatomy() == getAxis().getFlippedAxis()) {
            pt = pt.mirrorPoint(this);
        }

        int idx = (int) (Math.abs(pt.getX() - getRange().getMinimum()) / spacing);
        idx = Math.max(idx, 0);
        idx = Math.min(idx, getNumSamples() - 1);
        return getSamples()[idx];
    }

    public double valueOf(AnatomicalPoint1D sample) {
        assert sample.getAnatomy().sameAxis(getAxis());

        if (sample.getAnatomy() == getAxis().getFlippedAxis()) {
            sample = sample.mirrorPoint(this);
        }

        double relpos = sample.getX() * spacing + spacing / 2f;
        return relpos + getRange().getMinimum();
    }


    public AnatomicalPoint1D valueOf(int sample) {
        double relpos = sample * spacing + spacing / 2f;
        return new AnatomicalPoint1D(getAxis(), relpos + getRange().getMinimum());
    }

    public int[] getSampleArray() {
        if (sampleArray == null) {
            sampleArray = new int[samples];
            for (int i = 0; i < samples; i++) {
                sampleArray[i] = i;
            }
        }

        return sampleArray.clone();

    }

    protected int[] getSamples() {
        if (sampleArray == null) {
            sampleArray = new int[samples];
            for (int i = 0; i < samples; i++) {
                sampleArray[i] = i;
            }
        }

        return sampleArray;
    }

    public double getSpacing() {
        return spacing;
    }

    public boolean contains(double pt) {
        return getRange().contains(pt);
    }


    public final double fractionalSample(double pt) {
        return ((pt - getRange().getMinimum()) / spacing);
    }


    public double taxi(double current, double step, AnatomicalDirection adir) {
        assert (adir == getAxis().getMaxDirection()) || (adir == getAxis().getMinDirection());

        double ret = 0;
        if (adir == getAxis().getMaxDirection()) {
            ret = current + step;
        } else if (adir == getAxis().getMinDirection()) {
            ret = current - step;
        }

        return ret;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("anatomical axis: " + getAxis().toString())
                .append("axis range: " + getRange().toString())
                .append("samples: " + samples)
                .append("spacing: " + spacing);

        return sb.toString();

    }

    /*private class FlippedImageAxis extends ImageAxis {

       public FlippedImageAxis(ImageAxis unflipped) {
           range = unflipped.getRange().flip();
           samples = unflipped.getNumSamples();
           spacing = unflipped.getSpacing();
           axis = unflipped.getAnatomicalAxis().getFlippedAxis();
           sampleArray = unflipped.getSampleArray();
           reverseSamples();
       }

       private void reverseSamples() {
           int[] nsamples = new int[sampleArray.length];
           for (int i = sampleArray.length - 1; i > -1; i--) {
               nsamples[sampleArray.length - i - 1] = sampleArray[i];
           }

           sampleArray = nsamples;
       }

       public int[] getSampleArray() {
           return sampleArray.clone();
       }

       protected int[] getSamples() {
           return sampleArray;
       }

   } */


    public static class IncompatibleAxisException extends IllegalArgumentException {
        public IncompatibleAxisException() {
            super();
        }

        public IncompatibleAxisException(String message) {
            super(message);
        }

    }


    public static void main(String[] args) {
        int[] i1 = new int[]{1, 2, 3};
        int[] i2 = i1.clone();
        i2[0] = 55;


        System.out.println("" + i1[0]);
        System.out.println("" + i2[0]);

    }


}
