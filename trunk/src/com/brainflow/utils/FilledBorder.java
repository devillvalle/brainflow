package com.brainflow.utils;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 29, 2006
 * Time: 6:44:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilledBorder extends AbstractBorder {


    private Paint lightOutline = new Color(160, 160, 160);
    private Paint darkOutline = new Color(40, 40, 40);


    private Color startColor = new Color(0, 20, 0);
    private Color endColor = new Color(0, 120, 30);


    private int titleHeight = 30;
    private Insets insets = new Insets(titleHeight, 2, 2, 2);
    private GradientPaint fillPaint;

    private float alpha = .7f;

    private int gradientOrientation = SwingConstants.VERTICAL;

    private String title = "";


    public FilledBorder() {

    }

    private GradientPaint makeGradientPaint(int length) {
        if (gradientOrientation == SwingConstants.VERTICAL) {
            fillPaint = new GradientPaint(0, 0, startColor, 0, length - 2, endColor);
        } else if (gradientOrientation == SwingConstants.HORIZONTAL) {
            fillPaint = new GradientPaint(0, 0, startColor, length - 2, 0, endColor);
        }

        return fillPaint;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }


    public Color getBorderColor() {
        return endColor;
    }

    public void setBorderColor(Color color) {
        this.endColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
        this.startColor = new Color((int) Math.min(1.5 * color.getRed(), 255)
                , (int) Math.min(1.5 * color.getGreen(), 255), (int) (Math.min(1.5 * color.getBlue(), 255)),
                (int) Math.min(1/2 * color.getAlpha(), 255));
    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        Graphics2D g2 = (Graphics2D) g;
        Rectangle bounds = new Rectangle(x, y, width, height);
        g2.clip(bounds);

        Paint oldPaint = g2.getPaint();


        g2.setPaint(makeGradientPaint(titleHeight));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Composite oldComposite = g2.getComposite();

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.fillRect(x, y, width, titleHeight);
        g2.setPaint(endColor);

        // left border
        g2.fillRect(x, y + titleHeight, insets.left, height - titleHeight - insets.bottom);

        //bottom border
        g2.fillRect(x, y + height - insets.bottom, width, insets.bottom);

        //right border
        g2.fillRect(x + width - insets.right, y + titleHeight, insets.right, height - titleHeight - insets.bottom);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // top highlight
        g2.setPaint(lightOutline);

        //outer light
        g2.drawLine(x, y, x + width, y);
        g2.drawLine(x, y, x, y + height);

        //inner light
        g2.drawLine(x + width - insets.right, y + insets.top, x + width - insets.right, y + height - insets.bottom);
        g2.drawLine(x + insets.left, y + height - insets.bottom, x + width - insets.right, y + height - insets.bottom);

        g2.setPaint(darkOutline);

        //outer dark
        g2.drawLine(x, y + height, x + width, y + height);
        g2.drawLine(x + width, y, x + width, y + height);

        //inner dark
        g2.drawLine(x + insets.left, y + insets.top, x + width - insets.right, y + insets.top);
        g2.drawLine(x + insets.left, y + insets.top, x + insets.left, y + height - insets.bottom);


        g2.setColor(Color.WHITE);
        g2.drawString(title, x + 12, y + insets.top / 2 + 5);

        g2.setPaint(oldPaint);
        g2.setComposite(oldComposite);
    }


    public Insets getBorderInsets(Component c) {
        return insets;
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        return insets;
    }

    public static void main(String[] args) {

    }


}
