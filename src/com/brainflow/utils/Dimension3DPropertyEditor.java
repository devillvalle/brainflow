/*
 * Dimension3DPropertyEditor.java
 *
 * Created on February 5, 2003, 4:33 PM
 */

package com.brainflow.utils;

import java.beans.*;
import java.text.*;
import java.util.StringTokenizer;
/**
 *
 * @author  Bradley
 */
public class Dimension3DPropertyEditor extends PropertyEditorSupport {
    
    Dimension3D dim3d;
    
    /** Creates new Dimension3DPropertyEditor */
    public Dimension3DPropertyEditor() {
    }
    
    public String getAsText() {
        dim3d = (Dimension3D)getValue();
        NumberFormat nf = DecimalFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        return new String(nf.format(dim3d.x) + ", " + nf.format(dim3d.y) + ", " + nf.format(dim3d.z));
    }
    
    public void setAsText(String t) {
        StringTokenizer tokenizer = new StringTokenizer(t, " ,");
        double[] vals = new double[3];
        for (int i=0; i<3; i++) {
            if (tokenizer.hasMoreElements()) {
                String str = tokenizer.nextToken();
                if (str == null)
                    throw new IllegalArgumentException("Invalid Input");
                vals[i] = Double.parseDouble(str);
            }
            else {
                throw new IllegalArgumentException("Invalid input");
            }
                
        }
        
        dim3d.x = vals[0];
        dim3d.y = vals[1];
        dim3d.z = vals[2];
        
        setValue(dim3d);
    }
            
}
