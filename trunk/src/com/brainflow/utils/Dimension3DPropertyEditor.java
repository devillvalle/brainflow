/*
 * Dimension3DPropertyEditor.java
 *
 * Created on February 5, 2003, 4:33 PM
 */

package com.brainflow.utils;

import java.beans.PropertyEditorSupport;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.StringTokenizer;

/**
 * @author Bradley
 */
public class Dimension3DPropertyEditor extends PropertyEditorSupport {

    Dimension3D dim3d;

    /**
     * Creates new Dimension3DPropertyEditor
     */
    public Dimension3DPropertyEditor() {
    }

    public String getAsText() {
        dim3d = (Dimension3D) getValue();
        NumberFormat nf = DecimalFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        return new String(nf.format(dim3d.zero) + ", " + nf.format(dim3d.one) + ", " + nf.format(dim3d.two));
    }

    public void setAsText(String t) {
        StringTokenizer tokenizer = new StringTokenizer(t, " ,");
        double[] vals = new double[3];
        for (int i = 0; i < 3; i++) {
            if (tokenizer.hasMoreElements()) {
                String str = tokenizer.nextToken();
                if (str == null)
                    throw new IllegalArgumentException("Invalid Input");
                vals[i] = Double.parseDouble(str);
            } else {
                throw new IllegalArgumentException("Invalid input");
            }

        }

        dim3d.zero = vals[0];
        dim3d.one = vals[1];
        dim3d.two = vals[2];

        setValue(dim3d);
    }

}
