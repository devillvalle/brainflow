package com.brainflow.utils;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 29, 2006
 * Time: 9:01:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilledSelectableBorder extends AbstractBorder implements ISelectableBorder, IResizeableBorder, ITitledBorder {

    private FilledBorder fillBorder;
    private Component component;


    private Color selectedColor = new Color(15, 95, 245);
    private Color preselectedColor = new Color(5, 45, 130);
    private Color deselectedColor = new Color(12, 18, 14);

    private float selectedAlpha = .9f;
    private float deselectedAlpha = .2f;
    private float preselectedAlpha = .5f;

    private boolean preSelected = false;
    private boolean selected = false;

    private Rectangle bounds = new Rectangle(0, 0, 0, 0);

    private Rectangle N_Resize_Zone = new Rectangle(0, 0, 0, 0);
    private Rectangle S_Resize_Zone;
    private Rectangle E_Resize_Zone;
    private Rectangle W_Resize_Zone;
    private Rectangle NE_Resize_Zone;
    private Rectangle NW_Resize_Zone;
    private Rectangle SE_Resize_Zone;
    private Rectangle SW_Resize_Zone;

    private StringGenerator titleGenerator;


    public FilledSelectableBorder(Component _component) {
        component = _component;
        fillBorder = new FilledBorder();
        fillBorder.setBorderColor(deselectedColor);
        titleGenerator = new ConstantStringGenerator("");

        selectedColor = UIManager.getColor("DockableFrame.activeTitleBackground");
        deselectedColor = UIManager.getColor("DockableFrame.activeTitleBackground");
        preselectedColor = UIManager.getColor("DockableFrame.activeTitleBackground").darker();
        //UIManager.getColor("InternalFrame.activeBorderColor");
    }


    public void setSelected(boolean b) {
        selected = b;
        if (b) {
            fillBorder.setAlpha(selectedAlpha);
            fillBorder.setBorderColor(selectedColor);
        } else {
            fillBorder.setAlpha(deselectedAlpha);
            fillBorder.setBorderColor(deselectedColor);

        }

    }

    private void updateBounds(Component c, int x, int y, int width, int height) {
        if ((x != bounds.x) || (y != bounds.y)
                || (width != bounds.width) || (height != bounds.height)) {
            bounds = new Rectangle(x, y, width, height);

            Insets insets = getBorderInsets(c);

            N_Resize_Zone = new Rectangle(x + insets.left, y - 1, width - insets.left - insets.right, 7);
            S_Resize_Zone = new Rectangle(x + insets.left, y + height - insets.bottom, width - insets.left - insets.right, insets.bottom + 2);
            E_Resize_Zone = new Rectangle(x + width - insets.right, y + insets.top, insets.right + 1, height - insets.top - insets.bottom);
            W_Resize_Zone = new Rectangle(x - 1, y + insets.top, insets.left + 1, height - insets.top - insets.bottom);

            NW_Resize_Zone = new Rectangle(x - 5, y - 5, 12, 12);
            NE_Resize_Zone = new Rectangle(x + width - 5, y - 5, 12, 12);

            SW_Resize_Zone = new Rectangle(x - 5, y + height - 5, 12, 12);
            SE_Resize_Zone = new Rectangle(x + width - 5, y + height - 5, 12, 12);


        }
    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        updateBounds(c, x, y, width, height);
        fillBorder.setTitle(titleGenerator.getString());
        fillBorder.paintBorder(c, g, x, y, width, height);
    }

    public Insets getBorderInsets(Component c) {
        return fillBorder.getBorderInsets(c);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        return fillBorder.getBorderInsets(c, insets);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public boolean isBorderOpaque() {
        return fillBorder.isBorderOpaque();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Rectangle getInteriorRectangle(Component c, int x, int y, int width, int height) {
        return fillBorder.getInteriorRectangle(c, x, y, width, height);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int getBaseline(Component c, int width, int height) {

        return fillBorder.getBaseline(c, width, height);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Component.BaselineResizeBehavior getBaselineResizeBehavior(Component c) {
        return fillBorder.getBaselineResizeBehavior(c);
    }

    public void setPreSelected(boolean b) {
        preSelected = b;
        if (b) {
            fillBorder.setAlpha(preselectedAlpha);
            fillBorder.setBorderColor(preselectedColor);
        } else {
            fillBorder.setAlpha(deselectedAlpha);
            fillBorder.setBorderColor(deselectedColor);

        }
    }

    public boolean isPreSelected() {
        return preSelected;
    }

    public boolean isSelected() {
        return preSelected;
    }


    private boolean overNorthEast(Point p) {
        if (NE_Resize_Zone.contains(p)) {

            return true;
        }

        return false;


    }

    private boolean overNorthWest(Point p) {
        if (NW_Resize_Zone.contains(p)) {

            return true;
        }

        return false;


    }

    private boolean overSouthWest(Point p) {
        if (SW_Resize_Zone.contains(p)) {

            return true;
        }

        return false;


    }

    private boolean overSouthEast(Point p) {
        if (SE_Resize_Zone.contains(p)) {

            return true;
        }

        return false;


    }

    private boolean overLeftSide(Point p) {
        if (W_Resize_Zone.contains(p)) {
            return true;
        }

        return false;
    }

    private boolean overRightSide(Point p) {
        if (E_Resize_Zone.contains(p)) {
            return true;
        }

        return false;

    }

    private boolean overBottom(Point p) {
        if (S_Resize_Zone.contains(p)) {
            return true;
        }

        return false;

    }

    private boolean overTop(Point p) {
        if (N_Resize_Zone.contains(p)) {
            return true;
        }

        return false;

    }


    public boolean onHandle(Point p) {
        if (overNorthEast(p) ||
                overNorthWest(p) ||
                overSouthEast(p) ||
                overSouthWest(p) ||
                overTop(p) ||
                overBottom(p) ||
                overLeftSide(p) ||
                overRightSide(p)) {
            return true;
        }

        return false;


    }

    public HANDLE_LOCATION getHandle(Point p) {
        if (overNorthWest(p)) {
            return IResizeableBorder.HANDLE_LOCATION.TOP_LEFT;
        } else if (overNorthEast(p)) {
            return IResizeableBorder.HANDLE_LOCATION.TOP_RIGHT;
        } else if (overSouthWest(p)) {
            return IResizeableBorder.HANDLE_LOCATION.BOTTOM_LEFT;
        } else if (overSouthEast(p)) {
            return IResizeableBorder.HANDLE_LOCATION.BOTTOM_RIGHT;
        } else if (overTop(p)) {
            return IResizeableBorder.HANDLE_LOCATION.TOP_MIDDLE;
        } else if (overBottom(p)) {
            return IResizeableBorder.HANDLE_LOCATION.BOTTOM_MIDDLE;
        } else if (overLeftSide(p)) {
            return IResizeableBorder.HANDLE_LOCATION.SIDE_LEFT;
        } else if (overRightSide(p)) {
            return IResizeableBorder.HANDLE_LOCATION.SIDE_RIGHT;
        } else {
            return IResizeableBorder.HANDLE_LOCATION.NULL;
        }
    }

    public String getTitle() {
        return titleGenerator.getString();
    }

    public void setTitleGenerator(StringGenerator _titleGenerator) {
        titleGenerator = _titleGenerator;
    }


}
