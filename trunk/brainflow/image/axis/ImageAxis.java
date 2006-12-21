package com.brainflow.image.axis;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalDirection;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Jan 30, 2004
 * Time: 11:30:47 AM
 * To change this template use Options | File Templates.
 */
public class ImageAxis {

    protected AnatomicalAxis axis;
    protected int samples;

    protected AxisRange range;
    protected int[] sampleArray;
    protected double spacing;


    public ImageAxis() {
        axis = AnatomicalAxis.LEFT_RIGHT;
        samples = 1;
        sampleArray = new int[1];
        spacing = 1;
    }

    public ImageAxis(ImageAxis src) {
        range = src.getRange();
        samples = src.samples;
        axis = src.axis;
        spacing = src.spacing;

    }

    public ImageAxis(AxisRange range, double spacing) {
        this.range = range;
        this.axis = range.getAnatomicalAxis();
        this.samples = (int) (range.getInterval() / spacing);
        this.spacing = spacing;

    }


    public ImageAxis(double begin, double end, AnatomicalAxis _axis, int _samples) {
        range = new AxisRange(_axis, begin, end);
        samples = _samples;
        axis = _axis;
        spacing = range.getInterval() / samples;
    }

    public ImageAxis flip() {
        return new ImageAxis(range.getBeginning().getX(), range.getEnd().getX(), axis.getFlippedAxis(), samples);
    }


    public ImageAxis anchorAxis(AnatomicalDirection adir, double value) {
        if (adir == axis.getMaxDirection()) {
            return new ImageAxis(new AxisRange(getAnatomicalAxis(), value - range.getInterval(), value), spacing);
        } else if (adir == axis.getMinDirection()) {
            return new ImageAxis(new AxisRange(getAnatomicalAxis(), value, value + range.getInterval()), spacing);
        } else {
            throw new ImageAxis.IncompatibleAxisException("cannot anchor to axis direction: " + adir + " for axis " + getAnatomicalAxis());
        }
    }

    public ImageAxis matchAxis(AnatomicalAxis aaxis) {
        if (aaxis == getAnatomicalAxis()) {
            return this;
        } else if (aaxis == getAnatomicalAxis().getFlippedAxis()) {
            return flip();
        }

        throw new ImageAxis.IncompatibleAxisException("cannot match axes: " + aaxis + " and " + getAnatomicalAxis());

    }

    public ImageAxis matchAxis(ImageAxis src) {
        if (src.getAnatomicalAxis() == getAnatomicalAxis()) {
            return src;
        } else if (src.getAnatomicalAxis() == getAnatomicalAxis().getFlippedAxis()) {
            return flip();
        }

        throw new ImageAxis.IncompatibleAxisException("cannot match axes: " + src.getAnatomicalAxis() + " and " + getAnatomicalAxis());
    }


    public double getExtent() {
        return range.getInterval();
    }

    public AnatomicalPoint1D getEdgePoint(AnatomicalDirection adir) {
        if (adir != axis.getMinDirection() && adir != axis.getMaxDirection()) {
            throw new IncompatibleAxisException("ImageAxis.getEndPoint: supplied Axis Direction " + adir + " is incorrect");
        }

        return range.getEdgePoint(adir);

    }

    public AxisRange getRange() {
        return range;
    }

    public AnatomicalAxis getAnatomicalAxis() {
        return axis;
    }

    public int getNumSamples() {
        return samples;
    }

    public final int nearestSample(double pt) {
        int idx = (int) (Math.abs(pt - range.getMinimum()) / spacing);
        idx = Math.max(idx, 0);
        idx = Math.min(idx, getNumSamples() - 1);
        return getSamples()[idx];
    }

    public final int nearestSample(AnatomicalPoint1D pt) {
        assert pt.getAnatomy().sameAxis(axis);


        if (pt.getAnatomy() == axis.getFlippedAxis()) {
            pt = pt.mirrorPoint(this);
        }

        int idx = (int) (Math.abs(pt.getX() - range.getMinimum()) / spacing);
        idx = Math.max(idx, 0);
        idx = Math.min(idx, getNumSamples() - 1);
        return getSamples()[idx];
    }

    public double valueOf(AnatomicalPoint1D sample) {
        assert sample.getAnatomy().sameAxis(axis);

        if (sample.getAnatomy() == axis.getFlippedAxis()) {
            sample = sample.mirrorPoint(this);
        }

        double relpos = sample.getX() * spacing + spacing / 2f;
        return relpos + range.getMinimum();
    }


    public double valueOf(int sample) {
        double relpos = sample * spacing + spacing / 2f;
        return relpos + range.getMinimum();
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
        return range.contains(pt);
    }


    public final double fractionalSample(double pt) {
        return ((pt - range.getMinimum()) / spacing);
    }


    public double taxi(double current, double step, AnatomicalDirection adir) {
        assert (adir == axis.getMaxDirection()) || (adir == axis.getMinDirection());

        double ret = 0;
        if (adir == axis.getMaxDirection()) {
            ret = current + step;
        } else if (adir == axis.getMinDirection()) {
            ret = current - step;
        }

        return ret;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("anatomical axis", axis)
                .append("axis range", range)
                .append("samples", samples)
                .append("spacing", spacing).toString();

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
