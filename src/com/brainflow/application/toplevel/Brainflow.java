package com.brainflow.application.toplevel;

import com.brainflow.application.*;
import com.brainflow.application.actions.*;
import com.brainflow.application.presentation.*;
import com.brainflow.application.services.ImageViewCursorEvent;
import com.brainflow.application.services.LoadableImageProgressEvent;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.*;
import com.brainflow.display.ImageLayerParameters;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.interpolation.NearestNeighborInterpolator;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jidesoft.action.CommandBar;
import com.jidesoft.action.CommandMenuBar;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.document.DocumentComponent;
import com.jidesoft.document.DocumentPane;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.StatusBar;
import com.jidesoft.swing.*;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.bushe.swing.action.*;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;
import org.xml.sax.SAXException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: Nov 24, 2004
 * Time: 9:00:59 AM
 * To change this template use File | Settings | File Templates.
 */

public class Brainflow {




    private final BrainFlowContext applicationContext = new BrainFlowContext();

    private final static Logger log = Logger.getLogger(Brainflow.class.getCanonicalName());

    private BrainFrame brainFrame = null;

    private MountFileSystemAction mountFileSystemAction;

    private SavePNGAction savePNGAction;



    private RecentDirectoryMenu directoryMenu = new RecentDirectoryMenu();

    // MENU

    // TOOLBAR

    //CoordinateBar coordinateBar = new CoordinateBar();

    // TOOLBAR

    // MAIN WINDOW

    DocumentPane documentPane = new DocumentPane();

    JideSplitPane westSplitPane = new JideSplitPane(SwingUtilities.VERTICAL);

    ImageFileExplorer loadingDock = null;

    // STATUS BAR

    StatusBar statusBar = new StatusBar();

    // STATUS BAR

    CursorCoordinates cursorCoordinates = new CursorCoordinates();

    CrosshairCoordinates crosshairCoordinates = new CrosshairCoordinates();

    SelectedViewStatus viewStatus = new SelectedViewStatus();

    protected Brainflow() {
        // Exists only to thwart instantiation.
    }

    public static Brainflow getInstance() {
        return (Brainflow) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.toplevel.Brainflow");
    }

    public static void main(String[] args) {

        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");
        //com.jgoodies.looks.plastic.Plastic3DLookAndFeel plastic = new Plastic3DLookAndFeel();

        try {

            //org.jvnet.substance.SubstanceLookAndFeel lf = new SubstanceLookAndFeel();
            //SubstanceLookAndFeel.setSkin(new BusinessBlueSteelSkin());

            //SyntheticaLookAndFeel lf = new SyntheticaStandardLookAndFeel();
            SyntheticaLookAndFeel lf = new de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel();
            //A03LookAndFeel lf = new apprising.api.swing.plaf.a03.A03LookAndFeel();
            UIManager.setLookAndFeel(lf);
            //LookAndFeelFactory.installDefaultLookAndFeelAndExtension();


            LookAndFeelFactory.installJideExtension(LookAndFeelFactory.XERTO_STYLE);

            //ColorUIResource clr = (ColorUIResource)UIManager.get("DockableFrame.activeTitleBackground");
            //System.out.println("color is " + clr);
            //UIManager.put("CollapsiblePane.background", clr.brighter());

        } catch (Exception e) {
            Logger.getAnonymousLogger().severe("Could not load Look and Feel, aborting");
            e.printStackTrace();
            System.exit(-1);
        }

        Brainflow bflow = getInstance();
        bflow.launch();
    }


    public JFrame getApplicationFrame() {
        return brainFrame;
    }


    public void launch() {
        brainFrame = new BrainFrame();
        ImageCanvasManager.getInstance().createCanvas();


        log.info("launching Brainflow version 0.1");


        initImageIO();
        initializeActions();
        intializeKeyActions();
        initializeMenu();
        initializeStatusBar();
        initializeToolBar();
        initializeWorkspace();
        initializeResources();


    }


    private void initializeActions() {
        applicationContext.putValue(ActionContext.APPLICATION_FRAME, brainFrame);
        try {
            ActionManager.getInstance().register(getClass().getClassLoader().
                    getResource("resources/config/brainflow-actions.xml"));
        } catch (SAXException e1) {
            //todo log
            e1.printStackTrace();
        } catch (IOException e2) {
            //todo log
            e2.printStackTrace();
        }


        log.info("initializing Actions");

        mountFileSystemAction = new MountFileSystemAction();
        savePNGAction = new SavePNGAction(brainFrame);

    }


    private void intializeKeyActions() {
        ImageCanvas canvas = ImageCanvasManager.getInstance().getSelectedCanvas();


    }

    private void initializeMenu() {
        log.info("initializing Menu");


        CommandMenuBar menuBar = new CommandMenuBar();
        //
        menuBar.setOpaque(false);
        menuBar.setPaintBackground(false);
        //

        JideMenu fileMenu = new JideMenu("File");

        fileMenu.add(mountFileSystemAction);
        directoryMenu.getMenu().setName("Mount Recent");
        fileMenu.add(directoryMenu.getMenu());
        fileMenu.add(savePNGAction);

        Action action = ActionManager.getInstance().getAction("main-save-colorbar");
        fileMenu.add(action);

        BasicAction exit = (BasicAction) ActionManager.getInstance().getAction("main-exit");
        fileMenu.add(exit);



        brainFrame.setJMenuBar(menuBar);
        brainFrame.getJMenuBar().add(fileMenu);


        ActionList viewList = ActionManager.getInstance().getActionList("view-menu");
        brainFrame.getJMenuBar().add(ActionUIFactory.getInstance().createMenu(viewList));

        ActionList navList = ActionManager.getInstance().getActionList("navigation-menu");
        brainFrame.getJMenuBar().add(ActionUIFactory.getInstance().createMenu(navList));

        ActionList appearanceList = ActionManager.getInstance().getActionList("appearance-menu");
        brainFrame.getJMenuBar().add(ActionUIFactory.getInstance().createMenu(appearanceList));

        ActionList debugList = ActionManager.getInstance().getActionList("debug-menu");
        brainFrame.getJMenuBar().add(ActionUIFactory.getInstance().createMenu(debugList));




    }

    private void initializeStatusBar() {
        log.info("initialzing status bar");
        statusBar.setAutoAddSeparator(false);

        statusBar.add(viewStatus.getComponent(), JideBoxLayout.FIX);
        statusBar.add(new com.jidesoft.status.StatusBarSeparator(), JideBoxLayout.FIX);

        LabelStatusBarItem crossLabel = new LabelStatusBarItem();
        crossLabel.setText("Cross: ");
        statusBar.add(crossLabel, JideBoxLayout.FIX);

        statusBar.add(crosshairCoordinates.getXaxisLabel(), JideBoxLayout.FIX);
        statusBar.add(crosshairCoordinates.getYaxisLabel(), JideBoxLayout.FIX);
        statusBar.add(crosshairCoordinates.getZaxisLabel(), JideBoxLayout.FIX);
        statusBar.addSeparator();

        LabelStatusBarItem cursorLabel = new LabelStatusBarItem();
        cursorLabel.setText("Cursor: ");
        statusBar.add(cursorLabel, JideBoxLayout.FIX);

        statusBar.add(cursorCoordinates.getXaxisLabel(), JideBoxLayout.FIX);
        statusBar.add(cursorCoordinates.getYaxisLabel(), JideBoxLayout.FIX);
        statusBar.add(cursorCoordinates.getZaxisLabel(), JideBoxLayout.FIX);

        statusBar.addSeparator();

        statusBar.add(new ValueStatusItem(), JideBoxLayout.FIX);

        statusBar.add(new LabelStatusBarItem(), JideBoxLayout.VARY);
        statusBar.add(new com.jidesoft.status.ProgressStatusBarItem(), JideBoxLayout.FIX);

        statusBar.add(new com.jidesoft.status.MemoryStatusBarItem());
        brainFrame.getContentPane().add(statusBar, "South");


    }

    private void initializeToolBar() {

        JideActionUIFactory jideFactory = new JideActionUIFactory(ActionManager.getInstance());

        ActionUIFactory.setInstance("jidefactory", jideFactory);

        ActionList globalList = ActionManager.getInstance().getActionList("view-menu");
        ActionAttributes attr = new ActionAttributes();
        attr.putValue(ActionManager.TOOLBAR_SHOWS_TEXT, true);


        CommandBar mainToolbar = ((JideActionUIFactory) ActionUIFactory.getInstance("jidefactory")).createCommandBar(globalList);

        /// hack from jidesoft for synthetica lookandfeel
        mainToolbar.setPaintBackground(false);
        mainToolbar.setOpaque(false);
        //////////////////////////////////////////////////


        //JideSplitButton sliderDirection = new JideSplitButton("0  ");
        //sliderDirection.add(new JRadioButtonMenuItem("Z Axis"));
        //sliderDirection.add(new JRadioButtonMenuItem("X Axis"));
        //sliderDirection.add(new JRadioButtonMenuItem("Y Axis"));

        mainToolbar.addSeparator();

        ImageViewSliderPresenter sliderPresenter = new ImageViewSliderPresenter();
        //PropertyAdapter adapter = new PropertyAdapter(sliderDirection, "text");
        //sliderPresenter.setValueLabel(adapter);
        mainToolbar.add(sliderPresenter.getComponent());
        //mainToolbar.add(sliderDirection);


        mainToolbar.addSeparator();
        Action crossAction = ActionManager.getInstance().getAction("toggle-cross");
        AbstractButton crossToggle = ((JideActionUIFactory) ActionUIFactory.getInstance("jidefactory")).
                createJideButton(crossAction);
        mainToolbar.add(crossToggle);

        Action axisLabelAction = ActionManager.getInstance().getAction("toggle-axislabel");
        AbstractButton axisLabelToggle = ((JideActionUIFactory) ActionUIFactory.getInstance("jidefactory")).
                createJideButton(axisLabelAction);
        mainToolbar.add(axisLabelToggle);

        Action colorbarAction = ActionManager.getInstance().getAction("toggle-colorbar");
        AbstractButton colorbarToggle = ((JideActionUIFactory) ActionUIFactory.getInstance("jidefactory")).
                createJideButton(colorbarAction);
        mainToolbar.add(colorbarToggle);

        brainFrame.getContentPane().add(mainToolbar, BorderLayout.NORTH);
        ActionManager.mapKeystrokeForAction(documentPane, ActionManager.getInstance().getAction("view-close"));
        ActionManager.mapKeystrokeForAction(documentPane, ActionManager.getInstance().getAction("view-axial"));
        ActionManager.mapKeystrokeForAction(documentPane, ActionManager.getInstance().getAction("view-coronal"));
        ActionManager.mapKeystrokeForAction(documentPane, ActionManager.getInstance().getAction("view-sagittal"));
        ActionManager.mapKeystrokeForAction(documentPane, ActionManager.getInstance().getAction("view-orthogonal"));

        ActionManager.mapKeystrokeForAction(documentPane, ActionManager.getInstance().getAction("set-smoothing"));


    }


    private void initializeWorkspace() {
        log.info("initializing workspace");
        brainFrame.getDockingManager().getWorkspace().setLayout(new BorderLayout());
        brainFrame.getDockingManager().getWorkspace().add(documentPane, "Center");

        ImageCanvas canvas = ImageCanvasManager.getInstance().getSelectedCanvas();
        canvas.setRequestFocusEnabled(true);
        canvas.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        applicationContext.putValue(ActionContext.SELECTED_CANVAS, canvas);


        documentPane.setTabPlacement(DocumentPane.BOTTOM);
        documentPane.openDocument(new DocumentComponent(new JScrollPane(canvas), "Canvas-1"));
        documentPane.setActiveDocument("Canvas-1");


        initLoadingDock();
        initProjectView();
        initLoadableImageTableView();
        initControlPanel();
        initEventBusMonitor();


        brainFrame.getDockingManager().beginLoadLayoutData();
        brainFrame.getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_EAST_WEST_SOUTH_NORTH);
        brainFrame.getDockingManager().loadLayoutData();

        brainFrame.toFront();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        brainFrame.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight() - 50);
        brainFrame.setVisible(true);


        CanvasBar cbar = new CanvasBar();

        /// TODO add automatic updating of canvas to Canvas Bar via EventBus
        cbar.setImageCanvas(canvas);
        ////////////////////////////////////////////////////////////////////
        canvas.add(cbar.getComponent(), BorderLayout.NORTH);


    }


    private void initLoadingDock() {
        try {


            loadingDock = new ImageFileExplorer(VFS.getManager().resolveFile(DirectoryManager.getInstance().getCurrentLocalDirectory().getAbsolutePath()));
            ImageCanvasTransferHandler handler = new ImageCanvasTransferHandler();
            loadingDock.setDragEnabled(true);
            loadingDock.setTransferHandler(handler);
            ImageCanvasManager.getInstance().getSelectedCanvas().setTransferHandler(handler);

            DirectoryManager.getInstance().addFileSystemEventListener(new FileSystemEventListener() {
                public void eventOccurred(FileSystemEvent event) {
                    final FileObject fobj = event.getFileObject();

                    if (fobj != null) {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                loadingDock.addFileRoot(fobj);
                            }
                        });

                    }

                }
            });

            SearchableImageFileExplorer explorer = new SearchableImageFileExplorer(loadingDock);


            DockableFrame dframe = new DockableFrame("File Manager", new ImageIcon(getClass().getClassLoader().getResource("resources/icons/fldr_obj.gif")));
            dframe.setPreferredSize(new Dimension(275, 200));

            //JScrollPane jsp = new JScrollPane(explorer.getComponent());
            //jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            dframe.getContentPane().add(explorer.getComponent());

            dframe.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
            dframe.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
            dframe.getContext().setInitIndex(0);

            brainFrame.getDockingManager().addFrame(dframe);
        } catch (FileSystemException e) {
            // this would be more or less fatal, no?
            log.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    private void initProjectView() {
        ProjectView projectView = new ProjectView(ProjectManager.getInstance().getActiveProject());
        DockableFrame dframe = new DockableFrame("Project");
        dframe.getContentPane().add(new JScrollPane(projectView.getComponent()));
        dframe.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        dframe.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        dframe.getContext().setInitIndex(1);
        dframe.setPreferredSize(new Dimension(275, 200));
        brainFrame.getDockingManager().addFrame(dframe);
    }

    private void initLoadableImageTableView() {

        LoadableImageTableView loadView = new LoadableImageTableView();

        DockableFrame dframe = new DockableFrame("Loaded Images");
        dframe.getContentPane().add(new JScrollPane(loadView.getComponent()));
        dframe.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        dframe.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        dframe.getContext().setInitIndex(1);
        dframe.setPreferredSize(new Dimension(275, 200));
        brainFrame.getDockingManager().addFrame(dframe);

    }

    private void initEventBusMonitor() {
        DockableFrame dframe = new DockableFrame("Event Monitor");
        dframe.getContentPane().add(new JScrollPane(new EventBusMonitor().getComponent()));
        dframe.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        dframe.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        dframe.getContext().setInitIndex(1);
        dframe.setPreferredSize(new Dimension(800, 200));
        brainFrame.getDockingManager().addFrame(dframe);

    }


    private void initControlPanel() {

        JideTabbedPane tabbedPane = new JideTabbedPane();

        DockableFrame dframe = new DockableFrame("Controls", new ImageIcon(getClass().getClassLoader().getResource("resources/icons/types.gif")));


        ColorAdjustmentControl colorAdjustmentControl = new ColorAdjustmentControl();
        MaskControl maskControl = new MaskControl();
        //colorAdjustmentControl.getComponent().setPreferredSize(new Dimension(250,500));
        CoordinateControls coordinateControls = new CoordinateControls();
        //coordinateControls.getComponent().setPreferredSize(new Dimension(250,500));

        //Box layerAdjustmentPanel = new Box(BoxLayout.Y_AXIS);
        //layerAdjustmentPanel.add(new ImageDisplayTableView().getComponent());

        ColorMapTablePresenter tablePresenter = new ColorMapTablePresenter();

        tabbedPane.addTab("Adjustment", new JScrollPane(colorAdjustmentControl.getComponent()));
        tabbedPane.addTab("Color Table", tablePresenter.getComponent());
        //tabbedPane.addTab("Mask", maskControl.getComponent());
        tabbedPane.addTab("Coordinates", new JScrollPane(coordinateControls.getComponent()));


        dframe.getContentPane().add(tabbedPane);
        //dframe.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        dframe.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        dframe.getContext().setInitIndex(1);
        dframe.setPreferredSize(new Dimension(285, 500));
        brainFrame.getDockingManager().addFrame(dframe);

    }


    private void initImageIO() {
        log.info("initializing imageio");
        try {
            ImageIOManager.getInstance().initialize();
        } catch (BrainflowException e) {
            log.severe("Could not initialize IO facilities, aborting");
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.severe("Could not initialize IO facilities, aborting");
            throw new RuntimeException(e);
        }

    }

    private void initializeResources() {
        log.info("initializing resources");
        ResourceManager.getInstance().getColorMaps();

    }


    private void register(ILoadableImage limg) {
        LoadableImageManager manager = LoadableImageManager.getInstance();
        boolean alreadyRegistered = manager.isRegistered(limg);

        if (alreadyRegistered) {
            StringBuffer sb = new StringBuffer();
            sb.append("Image " + limg.getDataFile().getName().getBaseName());
            sb.append(" has already been loaded, would you like to reload from disk?");
            Integer ret = (Integer) JOptionPane.showConfirmDialog(brainFrame, sb.toString(), "Image Already Loaded", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            log.info("return value is: " + ret);

            if (ret == JOptionPane.YES_OPTION) {
                limg.releaseData();
                loadImage(limg);


            }


        } else {
            loadImage(limg);
            manager.registerLoadableImage(limg);
        }

        return;


    }

    public void loadAndDisplay(ILoadableImage limg, ImageView view, boolean fadeIn) {
        if (limg != null) {
            register(limg);
            IImageData data = null;
            data = limg.getData();
            if (data != null) {
                if (fadeIn) {
                    addLayerWithFadeIn(limg, view);
                } else {
                    IImageDisplayModel dset = view.getModel();
                    ImageLayerParameters params = new ImageLayerParameters();
                    params.setColorMap(new LinearColorMap(data.getMinValue(), data.getMaxValue(), ResourceManager.getInstance().getDefaultColorMap()));

                    ImageLayer layer = new ImageLayer3D(limg, params);

                    dset.addLayer(layer);
                }
            }
        }

    }


    private void addLayerWithFadeIn(ILoadableImage limg, ImageView view) {
        IImageDisplayModel dset = view.getModel();

        //final LinearColorMap cmap = (LinearColorMap) params.getColorMap().getProperty();
        //cmap.setAlphaMultiplier(0.0);

        //FadeAction fade = new FadeAction(cmap);
        //final Timer timer = new javax.swing.Timer(25, fade);
        //timer.setCoalesce(true);
        //fade.setTimer(timer);
        ImageLayerParameters parms = new ImageLayerParameters(new LinearColorMap(limg.getData().getMinValue(), limg.getData().getMaxValue(),
                ResourceManager.getInstance().getDefaultColorMap()));
        ImageLayer3D layer = new ImageLayer3D(limg, parms);


        dset.addLayer(layer);
        //timer.start();

    }

    class FadeAction implements ActionListener {

        private Timer timer;
        private LinearColorMap cmap;

        public FadeAction(LinearColorMap _cmap) {
            cmap = _cmap;
        }

        private void setTimer(Timer _timer) {
            timer = _timer;

        }

        public void actionPerformed(ActionEvent e) {
            double alpha = cmap.getAlphaMultiplier();
            if ((alpha >= 1) && (timer != null)) {
                timer.stop();
            } else {
                cmap.setAlphaMultiplier(Math.min(1, alpha + .08));
            }
        }
    }

    private void loadImage(final ILoadableImage limg) {

        final ProgressDialog pdialog = new ProgressDialog(LoadableImageProgressEvent.class, "Loading Image ...", "Reading File from disk");
        final JDialog dialog = pdialog.createDialog();
        dialog.setResizable(true);
        dialog.pack();

        final SwingWorker worker = new SwingWorker() {
            public Object doInBackground() {
                try {
                    limg.load();
                } catch (BrainflowException bfe) {
                    log.severe("Error load image " + limg.getHeaderFile().getName());
                    throw new RuntimeException(bfe);
                }
                return limg.getData();
            }


            protected void done() {
                dialog.setVisible(false);
            }
        };


        worker.execute();
        dialog.setVisible(true);


    }

    public void loadAndDisplay(ILoadableImage limg) {

        if (limg != null) {
            register(limg);

            IImageDisplayModel displayModel = ProjectManager.getInstance().addToActiveProject(limg);


            ImageView iview = ImageViewFactory.createAxialView(displayModel);
            iview.setTransferHandler(new ImageViewTransferHandler());
            ImageCanvasManager.getInstance().getSelectedCanvas().addImageView(iview);
        }

    }

    public SoftLoadableImage[] getSelectedLoadableImages() {
        SoftLoadableImage[] limg = loadingDock.requestLoadableImages();
        return limg;

    }

    public static JPopupMenu createColorMapPopup() {
        Map<String, IndexColorModel> maps = ResourceManager.getInstance().getColorMaps();
        JPopupMenu popup = new JPopupMenu("color maps");
        Iterator<String> iter = maps.keySet().iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            IndexColorModel icm = maps.get(name);
            SelectColorMapAction action = new SelectColorMapAction(name,
                    ColorTable.createImageIcon(icm, 40, 12), icm);


            ImageCanvas canvas = ImageCanvasManager.getInstance().
                    getSelectedCanvas();

            Map map = new HashMap();
            map.put(ActionContext.SELECTED_IMAGE_VIEW, ImageCanvasManager.getInstance().
                    getSelectedCanvas().getSelectedView());
            action.setContext(map);
            popup.add(action);


        }

        return popup;
    }

    class ValueStatusItem extends LabelStatusBarItem implements EventSubscriber {

        private Map<Color, ImageIcon> colorMap = new HashMap<Color, ImageIcon>();
        private NumberFormat format = NumberFormat.getNumberInstance();

        public ValueStatusItem() {
            EventBus.subscribeExactly(ImageViewCursorEvent.class, this);
            setIcon(ColorTable.createImageIcon(Color.GRAY, 40, 15));
            setText("Value :");
        }

        public void onEvent(Object evt) {

            ImageViewCursorEvent event = (ImageViewCursorEvent) evt;

            //todo only publish events when cursor is over valid view
            ImageView view = event.getImageView();

            if (view == null) {
                return;
            }
            if (view.getModel().getSelectedIndex() < 0) {
                // no layer selected
                return;
            }

            if (event.getLocation() == null) {
                // not clear why this should ever be the case. But it is.
                return;
            }


            AnatomicalPoint3D gpoint = event.getLocation();
            ImageLayer layer = view.getModel().getImageLayer(view.getModel().getSelectedIndex());


            IImageData3D idata = (IImageData3D) layer.getImageData();
            IImageSpace space = idata.getImageSpace();
            space.getAnatomicalAxis(Axis.X_AXIS);


            double x = gpoint.getValue(space.getAnatomicalAxis(Axis.X_AXIS)).getX();
            double y = gpoint.getValue(space.getAnatomicalAxis(Axis.Y_AXIS)).getX();
            double z = gpoint.getValue(space.getAnatomicalAxis(Axis.Z_AXIS)).getX();

            double value = idata.getRealValue(x, y, z, new NearestNeighborInterpolator());

            IColorMap cmap = layer.getImageLayerParameters().getColorMap().getProperty();

            Color c = null;
            try {
                c = cmap.getColor(value);
            } catch (Exception e) {

                e.printStackTrace();
            }
            ImageIcon icon = colorMap.get(c);
            if (icon == null) {
                icon = ColorTable.createImageIcon(c, 40, 15);
                colorMap.put(c, icon);
                if (colorMap.size() > 256) {
                    colorMap.clear();

                }
            }

            setText("Value: " + format.format(value));
            this.setIcon(icon);

        }
    }


    class SelectedViewStatus extends ImageViewPresenter {


        private LabelStatusBarItem anatomyLabel;

        public SelectedViewStatus() {
            anatomyLabel = new LabelStatusBarItem();
            anatomyLabel.setText("Layer: None");
            anatomyLabel.setMinimumSize(new Dimension(100, 0));
        }


        public void allViewsDeselected() {
            anatomyLabel.setText("Layer: None");
            anatomyLabel.setEnabled(false);

        }

        public void viewSelected(ImageView view) {
            int i = view.getSelectedIndex();
            if (i >= 0) {
                anatomyLabel.setText("Layer: " + view.getModel().getLayerName(i));
                anatomyLabel.setEnabled(true);
            } else {
                anatomyLabel.setText("Layer: None Selected");
                anatomyLabel.setEnabled(false);

            }
        }

        public JComponent getComponent() {
            return anatomyLabel;
        }
    }


}
