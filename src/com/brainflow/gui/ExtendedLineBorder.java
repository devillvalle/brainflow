package com.brainflow.gui;

import javax.swing.border.LineBorder;
import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Feb 27, 2004
 * Time: 2:35:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedLineBorder extends AbstractBorder {

    private Paint linePaint;



    protected int thickness=2;
    protected boolean roundedCorners;

    public ExtendedLineBorder(Paint _linePaint) {
        linePaint = _linePaint;

    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        Paint oldPaint = g2d.getPaint();
        int i;

        g2d.setPaint(linePaint);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        for (i = 0; i < thickness; i++) {
            if (!roundedCorners)
                g2d.drawRect(x + i, y + i, width - i - i - 1, height - i - i - 1);
            else
                g2d.drawRoundRect(x + i, y + i, width - i - i - 1, height - i - i - 1, thickness, thickness);
        }

        g2d.setPaint(oldPaint);
    }


    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }


    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = thickness;
        return insets;
    }


    public Paint getlinePaint() {
        return linePaint;
    }

    public void setThickness(int _thickness) {
        thickness = _thickness;

    }


    public int getThickness() {
        return thickness;
    }


    public boolean getRoundedCorners() {
        return roundedCorners;
    }


    public boolean isBorderOpaque() {
        return !roundedCorners;

    }


}
