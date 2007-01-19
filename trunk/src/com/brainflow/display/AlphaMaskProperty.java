package com.brainflow.display;

import com.brainflow.core.ImageLayer;
import com.jgoodies.binding.beans.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 2, 2006
 * Time: 7:36:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class AlphaMaskProperty extends Model {

    private List<ImageLayer> alphaMaskSet;

    public static final String ALPHA_MASK_PROPERTY = "alphaMaskSet";

    public AlphaMaskProperty() {
        alphaMaskSet = new ArrayList<ImageLayer>();
    }

    public void addAlphaMask(ImageLayer layer) {
        List<ImageLayer> old = new ArrayList<ImageLayer>(alphaMaskSet);
        alphaMaskSet.add(layer);
        this.firePropertyChange(ALPHA_MASK_PROPERTY, old, alphaMaskSet);
    }

    public void removeAlphaMask(ImageLayer layer) {
        List<ImageLayer> old = new ArrayList<ImageLayer>(alphaMaskSet);
        alphaMaskSet.remove(layer);
        this.firePropertyChange(ALPHA_MASK_PROPERTY, old, alphaMaskSet);
    }


    public Collection<ImageLayer> getAlphaMaskSet() {
        return Collections.unmodifiableCollection(alphaMaskSet);
    }


}
