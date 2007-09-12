package com.brainflow.application.presentation;

import com.brainflow.application.IImageDataSource;
import com.brainflow.application.actions.ActionContext;
import com.brainflow.application.presentation.forms.LoadableImageCell2;
import com.brainflow.application.toplevel.LoadableImageManager;
import com.brainflow.image.space.Axis;
import com.jidesoft.swing.StyleRange;
import com.jidesoft.swing.StyledLabel;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.BasicAction;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 14, 2006
 * Time: 11:59:47 PM
 * To change this template use File | Settings | File Templates.
 */


public class LoadableImageListView extends JList {

    static Color listForeground, listBackground,
            listSelectionForeground, listSelectionBackground;

    private LoadableImageCell2 cellPrototype;
    private DefaultListModel listModel = new DefaultListModel();

    private LoadableImageManager manager;

    /*private Map<IImageDataSource, ImageIcon> imap = new HashMap<IImageDataSource, ImageIcon>();

    public static final int ICON_WIDTH = 40;
    public static final int ICON_HEIGHT = 40;

    */

    private Map context = new HashMap();
    private BasicAction removeAction;


    static {
        UIDefaults uid = UIManager.getLookAndFeel().getDefaults();
        listForeground = uid.getColor("List.foreground");
        listBackground = uid.getColor("List.background");
        listSelectionForeground = uid.getColor("List.selectionForeground");
        listSelectionBackground = uid.getColor("List.selectionBackground");
    }

    public LoadableImageListView(LoadableImageManager _manager) {
        manager = _manager;

        buildPrototypeCell();
        setCellRenderer(new LoadableImageCellRenderer());
        setModel(listModel);

        this.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {

                IImageDataSource limg = (IImageDataSource) LoadableImageListView.this.getSelectedValue();
                context.put(ActionContext.SELECTED_LOADABLE_IMAGE, limg);
            }
        });


        addMouseListener(new MouseAdapter() {


            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JPopupMenu popup = createPopup();
                    popup.show(LoadableImageListView.this, e.getX(), e.getY());
                } else {

                }

            }
        });
        initMenu();

    }


    private void initMenu() {
        removeAction = (BasicAction) ActionManager.getInstance().createAction("Unload");
        removeAction.setContext(context);

    }

    private JPopupMenu createPopup() {
        JPopupMenu menu = new JPopupMenu();
        menu.add(removeAction);
        return menu;
    }


    protected void buildPrototypeCell() {
        cellPrototype = new LoadableImageCell2();
        opacify(cellPrototype);

    }

    protected void opacify(Container prototype) {
        Component[] comps = prototype.getComponents();
        for (Component c : comps) {
            if (c instanceof JComponent) {
                ((JComponent) c).setOpaque(true);
            }
        }

    }

    class LoadableImageCellRenderer implements ListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            IImageDataSource limg = (IImageDataSource) value;

            //ImageIcon icon = imap.get(limg);

            /*if (icon == null) {
                ImageDisplayModel dmodel = new ImageDisplayModel("" + limg.getUniqueID());

                ImageLayerProperties parms = new ImageLayerProperties(new LinearColorMap(limg.getData().getMinValue(), limg.getData().getMaxValue(), ColorTable.GRAYSCALE));
                ImageLayer layer = new ImageLayer3D(limg, parms);

                dmodel.addLayer(layer);

                AnatomicalPoint1D point = dmodel.getImageAxis(Anatomy3D.getCanonicalAxial().ZAXIS).getRange().getCenter();
                SnapShooter shooter = new SnapShooter(dmodel, Anatomy3D.getCanonicalAxial());
                RenderedImage rimg = shooter.shoot(point.getX());
                float sx = (float) ICON_WIDTH / (float) rimg.getWidth();
                float sy = (float) ICON_HEIGHT / (float) rimg.getHeight();
                ParameterBlock pb = new ParameterBlock();

                pb.add(sx);
                pb.add(sy);
                pb.addSource(rimg);

                rimg = JAI.create("scale", pb);

                RenderedImageAdapter rapter = new RenderedImageAdapter(rimg);
                icon = new ImageIcon(rapter.getAsBufferedImage());


                imap.put(limg, icon);
            } */

            //cellPrototype.getIconLabel().setIcon(icon);
            cellPrototype.getFilenameLabel().setText(limg.getStem());
            StyledLabel infoLabel = (StyledLabel) cellPrototype.getInfoLabel();
            infoLabel.setStyleRange(new StyleRange(0, infoLabel.getText().length(), Font.ITALIC, Color.GRAY));

            //Font font = infoLabel.getFont().deriveFont(Font.ITALIC);
            //infoLabel.setForeground(Color.LIGHT_GRAY);
            //infoLabel.setFont(font);

            int d1 = limg.getData().getDimension(Axis.X_AXIS);
            int d2 = limg.getData().getDimension(Axis.Y_AXIS);
            int d3 = limg.getData().getDimension(Axis.Z_AXIS);

            String dimstr = d1 + "zero" + d2 + "zero" + d3;
            infoLabel.setText(dimstr);
            setColorsForSelectionState(cellPrototype, isSelected);

            return cellPrototype;
        }

        private void setColorsForSelectionState(Container prototype, boolean isSelected) {
            Component[] comps = prototype.getComponents();

            if (isSelected) {
                prototype.setBackground(listSelectionBackground);
                prototype.setForeground(listSelectionForeground);
            } else {
                prototype.setBackground(listBackground);
                prototype.setForeground(listForeground);

            }

            for (Component c : comps) {
                if (isSelected) {
                    c.setForeground(listSelectionForeground);
                    c.setBackground(listSelectionBackground);
                } else {
                    c.setForeground(listForeground);
                    c.setBackground(listBackground);
                }
            }

        }
    }
}
