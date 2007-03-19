package com.brainflow.display;

import com.jgoodies.binding.beans.Model;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 3, 2006
 * Time: 9:51:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class InterpolationMethod extends Model {


    public static final String INTERPOLATION_PROPERTY = "interpolation";


    public InterpolationMethod(InterpolationHint interpolation) {
        this.interpolation = interpolation;
    }

    private InterpolationHint interpolation = InterpolationHint.CUBIC;


    public InterpolationHint getInterpolation() {
        return interpolation;
    }

    public void setInterpolation(InterpolationHint interpolation) {
        InterpolationHint old = this.interpolation;
        this.interpolation = interpolation;
        
        firePropertyChange(INTERPOLATION_PROPERTY, old, interpolation);

    }
}
