package com.brainflow.colormap;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Area;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 27, 2007
 * Time: 11:23:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThermoColorMap extends JComponent {

    private IColorMap model = new LinearColorMap(0, 255, ColorTable.SPECTRUM);


    private int thermoWidth = 50;
    private int capHeight = 40;



    public ThermoColorMap() {
    }

    public ThermoColorMap(IColorMap model) {
        this.model = model;
    }

    private IColorMap getModel() {
        return model;
    }

    public int getThermoWidth() {
        return thermoWidth;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);



        Paint p = createGradientPaint();
        RoundRectangle2D rect = new RoundRectangle2D.Double(10,0, getThermoWidth(), getHeight(), 120, 120);
        Ellipse2D topCap =  new Ellipse2D.Double(0,0, getThermoWidth(), capHeight*2);
        //Ellipse2D bottomCap =  new Ellipse2D.Double(0,getHeight()- capHeight, getThermoWidth(), getHeight());

        //Area clipArea = new Area(rect);
        //clipArea.add(new Area(topCap));
        //clipArea.add(new Area(bottomCap));

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2.setPaint(Color.RED);
        //g2.fill(topCap);

        g2.setPaint(p);
        //g2.setClip(clipArea);
        g2.fill(rect);


    }

    private float[] getFractions() {
        IColorMap model = getModel();
        float[] frac = new float[model.getMapSize()];


        double cRange = model.getMaximumValue() - model.getMinimumValue();

        ColorInterval c1 = model.getInterval(0);
        ColorInterval cn = model.getInterval(model.getMapSize()-1);

        frac[0] = .1f;
        frac[model.getMapSize()-1] = .9f;

        for (int i=1; i<model.getMapSize()-1; i++) {

            ColorInterval ci = model.getInterval(i);
            double max = ci.getMaximum();
            double diff = max - model.getMinimumValue();
            float f = (float) (diff / cRange);
            f = (float)(f*.8 + .1);
            System.out.println("index : " + i);
            System.out.println("f : " + f);
            frac[i] = f;
            
        }

        return frac;

    }

    private Color[] getColors() {
        IColorMap model = getModel();
        Color[] clrs = new Color[model.getMapSize()];
        ListIterator<ColorInterval> iter = model.iterator();
        int i = 0;
        while (iter.hasNext()) {
            ColorInterval ci = iter.next();
            clrs[i] = ci.getColor();
            i++;
        }

        return clrs;


    }

    private LinearGradientPaint createGradientPaint() {

       
        int height = getHeight();

        LinearGradientPaint paint = new LinearGradientPaint(0f, 0f,
                (float)0, (float)height, getFractions(), getColors());

        return paint;

    }

    public Dimension getPreferredSize() {
        return new Dimension(getThermoWidth(), 400);
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.add(new ThermoColorMap());
        jf.pack();
        jf.setVisible(true);

    }


}
