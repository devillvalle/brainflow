package com.brainflow.application.presentation;

import com.brainflow.application.ILoadableImage;
import com.brainflow.application.MemoryImage;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.BrainflowException;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.SimpleImageView;
import com.brainflow.display.ImageLayerParameters;
import com.brainflow.image.io.AnalyzeIO;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
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
    private Insets insets = new Insets(titleHeight, 7, 7, 7);
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
        this.endColor = color;
        this.startColor = new Color((int) (.4 * color.getRed()), (int) (.4 * color.getGreen()), (int) (.4 * color.getBlue()));
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
        try {
            ImageDisplayModel dset = new ImageDisplayModel("snapper");
            ILoadableImage il1 = new MemoryImage(AnalyzeIO.readAnalyzeImage("c:/code/icbm/icbm452_atlas_probability_white.img"));
            LinearColorMap lmap = new LinearColorMap(0, 18000, ColorTable.GRAYSCALE);
            dset.addLayer(new ImageLayer(il1.getData(), new ImageLayerParameters(lmap)));

            SimpleImageView view1 = new SimpleImageView(dset);
            view1.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), new FilledBorder()));

            JFrame jf = new JFrame();
            jf.getContentPane().setBackground(Color.DARK_GRAY);
            jf.add(view1);
            jf.pack();
            jf.setVisible(true);

        } catch (BrainflowException e) {
            e.printStackTrace();
        }
    }


}
