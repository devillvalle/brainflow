package com.brainflow.application.presentation;

import com.visutools.nav.bislider.*;
import com.brainflow.colormap.LinearColorMap2;
import com.brainflow.colormap.ColorTable;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 5, 2008
 * Time: 8:08:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class BiSliderTest extends JPanel {

    BiSlider slider;

    LinearColorMap2 colorMap = new LinearColorMap2(0, 277, ColorTable.GRAYSCALE);


    public BiSliderTest() {

        slider = new BiSlider();
        slider.setMinimumValue(0);
        slider.setMaximumValue(255);
        slider.setSegmentSize(3);
        //slider.setDecimalFormater(new DecimalFormat("###.#"));
        slider.getDecimalFormater().setMinimumIntegerDigits(4);
        //slider.setUniformSegment(true);

        slider.addContentPainterListener(new ContentPainterListener() {
            public void paint(ContentPainterEvent event) {
                Graphics2D Graphics2 = (Graphics2D) event.getGraphics();
                Rectangle Rect1 = event.getRectangle();
                Rectangle Rect2 = event.getBoundingRectangle();

                //System.out.println("index : " + event.getSegmentIndex());
                double min = event.getMinimum();
                double max = event.getMaximum();

                Color clr = colorMap.getColor((max + min) / 2);
                //if (event.getColor() != null) {
                    Graphics2.setColor(clr);
                    Graphics2.setPaint(new GradientPaint(Rect2.x, Rect2.y, clr.brighter(),
                            Rect2.x + Rect2.width, Rect2.y + Rect2.height, clr.darker()));
                    Graphics2.fillRect(Rect1.x, Rect1.y, Rect1.width, Rect1.height);
                //}
            }
        });

        slider.addBiSliderListener(new BiSliderListener() {
            public void newColors(BiSliderEvent event) {
                
            }

            public void newSegments(BiSliderEvent event) {
                System.out.println("new segments");
            }

            public void newMaxValue(BiSliderEvent event) {
                System.out.println("new max value " + slider.getMaximumValue());
                colorMap = colorMap.newClipRange(colorMap.getLowClip(), colorMap.getHighClip(), slider.getMinimumValue(), slider.getMaximumValue());
                
            }

            public void newMinValue(BiSliderEvent event) {
                System.out.println("new min");
            }

            public void newValues(BiSliderEvent event) {
                colorMap = colorMap.newClipRange(event.getMinimum(), event.getMaximum());
                

            }
        });

        setLayout(new BorderLayout());

        add(slider, BorderLayout.CENTER);
    }



    public static void main(String[] args) throws Exception {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");
        UIManager.setLookAndFeel(new WindowsLookAndFeel());

        LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2003_STYLE);
        JFrame jf = new JFrame();
        jf.add(new BiSliderTest(), BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);
    }
}
