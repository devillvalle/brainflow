package com.brainflow.utils;

import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 2, 2006
 * Time: 12:00:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectableBorder extends AbstractBorder implements ISelectableBorder, IResizeableBorder {

    private int innerMargin = 4;
    private int outerMargin = 4;

    private EmptyBorder margin1 = new EmptyBorder(innerMargin, innerMargin, innerMargin, innerMargin);

    private EmptyBorder margin2 = new EmptyBorder(outerMargin, outerMargin, outerMargin, outerMargin);

    private HandledBorder handledBorder;

    private CompoundBorder border;

    private Component component;


    public SelectableBorder(Component component) {
        handledBorder = new HandledBorder();
        handledBorder.setSelected(false);
        border = new CompoundBorder(margin1, new CompoundBorder(handledBorder, margin2));
        this.component = component;


    }

    public boolean onHandle(Point p) {
        return handledBorder.onHandle(p);
    }

    public IResizeableBorder.HANDLE_LOCATION getHandle(Point p) {
        return handledBorder.getHandle(p);
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        border.paintBorder(c, g, x, y, width, height);
    }

    public Insets getBorderInsets(Component c) {
        return border.getBorderInsets(c);
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        return border.getBorderInsets(c, insets);
    }

    public boolean isBorderOpaque() {
        return border.isBorderOpaque();
    }

    public Rectangle getInteriorRectangle(Component c, int x, int y, int width, int height) {
        return border.getInteriorRectangle(c, x, y, width, height);
    }

    public void setSelected(boolean b) {
        handledBorder.setSelected(b);
    }

    public boolean isSelected() {
        return handledBorder.isSelected();
    }

    public boolean isPreSelected() {
        return handledBorder.isPreSelected();
    }

    public void setPreSelected(boolean b) {
        handledBorder.setPreSelected(b);
    }

    public int hashCode() {
        return border.hashCode();
    }

    public String toString() {
        return border.toString();
    }


    public boolean equals(Object obj) {
        return border.equals(obj);
    }


}
