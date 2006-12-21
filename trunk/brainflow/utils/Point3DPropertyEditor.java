/*
 * Point3DPropertyEditor.java
 *
 * Created on February 10, 2003, 5:43 PM
 */

package com.brainflow.utils;
import java.beans.*;
import java.text.*;
import java.util.StringTokenizer;

/**
 *
 * @author  Bradley
 */
public class Point3DPropertyEditor extends PropertyEditorSupport {
    
    Point3D p3d;
    
    /** Creates a new instance of Point3DPropertyEditor */
    
    public Point3DPropertyEditor() {
    }
    
    public String getAsText() {
        p3d = (Point3D)getValue();
        NumberFormat nf = DecimalFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        return new String(nf.format(p3d.getX()) + ", " + nf.format(p3d.getY()) + ", " + nf.format(p3d.getZ()));
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
        
        p3d.setX(vals[0]);
        p3d.setY(vals[1]);
        p3d.setZ(vals[2]);
        
        setValue(p3d);
    }
    
}
