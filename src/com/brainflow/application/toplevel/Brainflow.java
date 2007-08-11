package com.brainflow.application.toplevel;

import com.brainflow.application.*;
import com.brainflow.application.actions.*;
import com.brainflow.application.presentation.*;
import com.brainflow.application.services.ImageViewCursorEvent;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.*;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.data.IImageData;
import com.brainflow.utils.Range;
import com.brainflow.utils.StaticTimer;
import com.jidesoft.action.CommandMenuBar;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.document.DocumentComponent;
import com.jidesoft.document.DocumentPane;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.StatusBar;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.JideTabbedPane;
import com.pietschy.command.CommandContainer;
import com.pietschy.command.GuiCommands;
import com.pietschy.command.configuration.ParseException;
import com.pietschy.command.group.CommandGroup;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ActionUIFactory;
import org.bushe.swing.action.BasicAction;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;
import org.xml.sax.SAXException;

import javax.swing.*;
import java.awt.*;
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


    private final BrainflowContext applicationContext = new BrainflowContext();

    private final static Logger log = Logger.getLogger(Brainflow.class.getCanonicalName());

    private BrainFrame brainFrame = null;

    private MountFileSystemAction mountFileSystemAction;

    private SavePNGAction savePNGAction;

    private RecentPathMenu pathMenu = new RecentPathMenu();

    private DocumentPane documentPane = new DocumentPane();

    private ImageFileExplorer loadingDock = null;


    private StatusBar statusBar = new StatusBar();

    private CursorCoordinates cursorCoordinates = new CursorCoordinates();

    private CrosshairCoordinates crosshairCoordinates = new CrosshairCoordinates();

    private SelectedViewStatus viewStatus = new SelectedViewStatus();

    private CommandContainer commandContainer;


    private static final String JIDE_FACTORY = "jide_factory";

    protected Brainflow() {
        // Exists only to thwart instantiation.
    }

    public static Brainflow getInstance() {
        return (Brainflow) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.toplevel.Brainflow");
    }

    public static void main(String[] args) {

        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");

        try {
            //SyntheticaLookAndFeel lf = new SyntheticaStandardLookAndFeel();

            //UIManager.setLookAndFeel(new Plastic3DLookAndFeel());


            UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceMistSilverLookAndFeel());
            //UIManager.setLookAndFeel(lf);
            //LookAndFeelFactory.installDefaultLookAndFeel();
            LookAndFeelFactory.installJideExtension(LookAndFeelFactory.XERTO_STYLE);
            //LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2003_STYLE);
        } catch (Exception e) {
            Logger.getAnonymousLogger().severe("Error Loading LookAndFeel, exiting");
            e.printStackTrace();
            System.exit(-1);

        }
        final Brainflow bflow = getInstance();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    bflow.launch();
                } catch (Throwable e) {
                    Logger.getAnonymousLogger().severe("Error Launching Brainflow, exiting");
                    e.printStackTrace();
                    System.exit(-1);
                }

            }
        });

    }


    public JFrame getApplicationFrame() {
        return brainFrame;
    }


    private void splashMessage(String message) {
        Graphics2D g = SplashScreen.getSplashScreen().createGraphics();
        //g.setComposite(AlphaComposite.Clear);
        //g.fillRect(20,430,100,460);
        //g.setPaintMode();
        g.setColor(Color.WHITE);
        g.setFont(new Font("helvetica", Font.PLAIN, 16));
        g.drawString(message, 20, 430);
        SplashScreen.getSplashScreen().update();

    }


    public void launch() throws Throwable {
        final SplashScreen splash = SplashScreen.getSplashScreen();

        if (splash == null) {
            //throw new RuntimeException("Cannot create splash screen");

        }

        //JFrame.setDefaultLookAndFeelDecorated(true);

        brainFrame = new BrainFrame();

        ImageCanvasManager.getInstance().createCanvas();

        JideActionUIFactory jideFactory = new JideActionUIFactory(ActionManager.getInstance());

        ActionUIFactory.setInstance(Brainflow.JIDE_FACTORY, jideFactory);

        StaticTimer.start();
        loadCommands();
        bindContainer();
        StaticTimer.end("loading and binding took: ");

        StaticTimer.start();
        initImageIO();
        StaticTimer.end("init image io took: ");


        StaticTimer.start();
        initializeActions();
        StaticTimer.end("init actions took: ");

        StaticTimer.start();
        intializeKeyActions();
        StaticTimer.end("init key actions took: ");

        StaticTimer.start();
        initializeMenu();
        StaticTimer.end("init menu took: ");

        StaticTimer.start();
        initializeStatusBar();
        StaticTimer.end("init status took: ");

        StaticTimer.start();
        initializeToolBar();
        StaticTimer.end("init toolbar took: ");

        StaticTimer.start();
        initializeWorkspace();
        StaticTimer.end("init workspace took: ");

        StaticTimer.start();
        initializeResources();
        StaticTimer.end("init resources took: ");


    }


    private void loadCommands() {
        try {

            GuiCommands.load("resources/commands/ImageViewCommands");
            GuiCommands.load("resources/commands/BrainFlowCommands");


        } catch (ParseException e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public CommandContainer getCommandContainer() {
        return commandContainer;
    }

    private void bindContainer() {
        commandContainer = new CommandContainer();
        commandContainer.bind(brainFrame);


        CommandGroup imageViewGroup = new CommandGroup("image-view-menu");


        imageViewGroup.bind(brainFrame);


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
        pathMenu.getMenu().setName("Mount Recent");
        fileMenu.add(pathMenu.getMenu());
        fileMenu.add(savePNGAction);

        Action action = ActionManager.getInstance().getAction("main-save-colorbar");
        fileMenu.add(action);

        BasicAction exit = (BasicAction) ActionManager.getInstance().getAction("main-exit");
        fileMenu.add(exit);


        brainFrame.setJMenuBar(menuBar);
        brainFrame.getJMenuBar().add(fileMenu);


        ActionList navList = ActionManager.getInstance().getActionList("navigation-menu");
        brainFrame.getJMenuBar().add(ActionUIFactory.getInstance(JIDE_FACTORY).createMenu(navList));

        ActionList annotationList = ActionManager.getInstance().getActionList("annotation-menu");
        brainFrame.getJMenuBar().add(ActionUIFactory.getInstance(JIDE_FACTORY).createMenu(annotationList));

        //ActionList debugList = ActionManager.getInstance().getActionList("debug-menu");
        //brainFrame.getJMenuBar().add(ActionUIFactory.getInstance(JIDE_FACTORY).createMenu(debugList));

        brainFrame.getJMenuBar().add(DockWindowManager.getInstance().getDockMenu());


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

        //ActionList globalList = ActionManager.getInstance().getActionList("view-menu");
        //ActionAttributes attr = new ActionAttributes();
        //attr.putValue(ActionManager.TOOLBAR_SHOWS_TEXT, true);

        //CommandBar mainToolbar = ((JideActionUIFactory) ActionUIFactory.getInstance(JIDE_FACTORY)).createCommandBar(globalList);

        /// hack from jidesoft for synthetica lookandfeel
        //mainToolbar.setPaintBackground(false);
        //mainToolbar.setOpaque(false);
        //////////////////////////////////////////////////

        //JideSplitButton sliderDirection = new JideSplitButton("0  ");
        //sliderDirection.add(new JRadioButtonMenuItem("Z Axis"));
        //sliderDirection.add(new JRadioButtonMenuItem("X Axis"));
        //sliderDirection.add(new JRadioButtonMenuItem("Y Axis"));

        //JLabel sliceLabel = new JLabel("0 ");

        //mainToolbar.addSeparator();

        //ImageViewSliderPresenter sliderPresenter = new ImageViewSliderPresenter();
        //PropertyAdapter adapter = new PropertyAdapter(sliceLabel, "text");
        //sliderPresenter.setValueLabel(adapter);
        //mainToolbar.add(sliderPresenter.getComponent());
        //mainToolbar.add(sliderDirection);

        //mainToolbar.addSeparator();
        //Action crossAction = ActionManager.getInstance().getAction("toggle-cross");

        //AbstractButton crossToggle = ((JideActionUIFactory) ActionUIFactory.getInstance(JIDE_FACTORY)).
        //        createJideButton(crossAction);
        //mainToolbar.add(crossToggle);

        //Action axisLabelAction = ActionManager.getInstance().getAction("toggle-axislabel");

        //AbstractButton axisLabelToggle = ((JideActionUIFactory) ActionUIFactory.getInstance(JIDE_FACTORY)).
        //        createJideButton(axisLabelAction);
        //mainToolbar.add(axisLabelToggle);

        //Action colorbarAction = ActionManager.getInstance().getAction("toggle-colorbar");
        //AbstractButton colorbarToggle = ((JideActionUIFactory) ActionUIFactory.getInstance(JIDE_FACTORY)).
        //        createButton(colorbarAction);

        //mainToolbar.add(colorbarToggle);

        CommandGroup mainToolbarGroup = new CommandGroup("main-toolbar");
        mainToolbarGroup.bind(getApplicationFrame());

        OpenImageCommand openImageCommand = new OpenImageCommand();
        openImageCommand.bind(getApplicationFrame());


        CreateAxialViewCommand axialCommand = new CreateAxialViewCommand();
        axialCommand.bind(getApplicationFrame());
        axialCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        CreateSagittalViewCommand sagittalCommand = new CreateSagittalViewCommand();
        sagittalCommand.bind(getApplicationFrame());
        sagittalCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        CreateCoronalViewCommand coronalCommand = new CreateCoronalViewCommand();
        coronalCommand.bind(getApplicationFrame());
        coronalCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        IncreaseContrastCommand increaseContrastCommand = new IncreaseContrastCommand();
        increaseContrastCommand.bind(getApplicationFrame());
        increaseContrastCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        DecreaseContrastCommand decreaseContrastCommand = new DecreaseContrastCommand();
        decreaseContrastCommand.bind(getApplicationFrame());
        decreaseContrastCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        JToolBar mainToolbar = mainToolbarGroup.createToolBar();
        //ActionCommand increaseContrastCommand = new IncreaseContrastCommand();
        //increaseContrastCommand.bind(brainFrame);
        //mainToolbar.add(increaseContrastCommand.getActionAdapter());

        //ActionCommand decreaseContrastCommand = new DecreaseContrastCommand();
        //decreaseContrastCommand.bind(brainFrame);
        //mainToolbar.add(decreaseContrastCommand.getActionAdapter());


        brainFrame.getContentPane().add(mainToolbar, BorderLayout.NORTH);


    }


    private void initializeWorkspace() throws Exception {
        log.info("initializing workspace");
        brainFrame.getDockingManager().getWorkspace().setLayout(new BorderLayout());
        brainFrame.getDockingManager().getWorkspace().add(documentPane, "Center");

        ImageCanvas2 canvas = ImageCanvasManager.getInstance().getSelectedCanvas();
        canvas.setRequestFocusEnabled(true);
        canvas.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

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
        //cbar.setImageCanvas(canvas);
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

            DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("File Manager",
                    "resources/icons/fldr_obj.gif",
                    DockContext.STATE_FRAMEDOCKED,
                    DockContext.DOCK_SIDE_WEST);

            //explorer.getComponent().setPreferredSize(new Dimension(400,200));
            dframe.setPreferredSize(new Dimension(275, 200));

            //JScrollPane jsp = new JScrollPane(explorer.getComponent());
            //jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            dframe.getContentPane().add(explorer.getComponent());
            brainFrame.getDockingManager().addFrame(dframe);

        } catch (FileSystemException e) {
            // this would be more or less fatal, no?
            log.log(Level.SEVERE, e.getMessage(), e);
        }

    }

    private void initProjectView() throws IOException {
        ProjectTreeView projectTreeView = new ProjectTreeView(ProjectManager.getInstance().getActiveProject());
        DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("Project",
                "resources/icons/folder_page.png",
                DockContext.STATE_FRAMEDOCKED,
                DockContext.DOCK_SIDE_WEST, 1);

        dframe.getContentPane().add(new JScrollPane(projectTreeView.getComponent()));
        dframe.setPreferredSize(new Dimension(275, 200));

        brainFrame.getDockingManager().addFrame(dframe);
    }

    private void initLoadableImageTableView() throws IOException {

        LoadableImageTableView loadView = new LoadableImageTableView();
        DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("Loaded Images",
                "resources/icons/det_pane_hide.gif",
                DockContext.STATE_FRAMEDOCKED,
                DockContext.DOCK_SIDE_WEST,
                1);

        dframe.getContentPane().add(new JScrollPane(loadView.getComponent()));
        dframe.setPreferredSize(new Dimension(275, 200));
        brainFrame.getDockingManager().addFrame(dframe);

    }

    private void initEventBusMonitor() {
        DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("Event Monitor",
                "resources/icons/console_view.gif",
                DockContext.STATE_AUTOHIDE,
                DockContext.DOCK_SIDE_SOUTH,
                1);


        dframe.getContentPane().add(new JScrollPane(new EventBusMonitor().getComponent()));

        dframe.setPreferredSize(new Dimension(800, 200));
        brainFrame.getDockingManager().addFrame(dframe);

    }


    private void initControlPanel() {

        JideTabbedPane tabbedPane = new JideTabbedPane();

        DockableFrame dframe = new DockableFrame("Controls", new ImageIcon(getClass().getClassLoader().getResource("resources/icons/types.gif")));


        ColorAdjustmentControl colorAdjustmentControl = new ColorAdjustmentControl();
        CoordinateControls coordinateControls = new CoordinateControls();

        ColorMapTablePresenter tablePresenter = new ColorMapTablePresenter();

        MaskTablePresenter maskPresenter = new MaskTablePresenter();

        tabbedPane.addTab("Adjustment", new JScrollPane(colorAdjustmentControl.getComponent()));
        tabbedPane.addTab("Color Table", tablePresenter.getComponent());
        tabbedPane.addTab("Mask Table", maskPresenter.getComponent());
        tabbedPane.addTab("Coordinates", new JScrollPane(coordinateControls.getComponent()));


        dframe.getContentPane().add(tabbedPane);
        dframe.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        dframe.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        dframe.getContext().setInitIndex(1);
        dframe.setPreferredSize(new Dimension(300, 500));
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
            }


        }

    }

    public void loadAndDisplay(final ILoadableImage limg) {

        if (limg != null) {
            register(limg);


            ImageProgressDialog id = new ImageProgressDialog(limg, ImageCanvasManager.getInstance().getSelectedCanvas()) {

                protected void done() {
                    IImageDisplayModel displayModel = ProjectManager.getInstance().addToActiveProject(limg);

                    ImageView iview = ImageViewFactory.createAxialView(displayModel);
                    iview.setTransferHandler(new ImageViewTransferHandler());
                    ImageCanvasManager.getInstance().getSelectedCanvas().addImageView(iview);
                    getDialog().setVisible(false);


                }
            };


            JDialog dialog = id.getDialog();
            dialog.setVisible(true);

            id.execute();


        }

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

                    //todo data range should be a property of ImageLayerProperties, not IColorMap
                    ImageLayerProperties params = new ImageLayerProperties(new Range(data.getMinValue(), data.getMaxValue()));
                    params.getColorMap().setProperty(new LinearColorMap(data.getMinValue(), data.getMaxValue(), ResourceManager.getInstance().getDefaultColorMap()));

                    ImageLayer layer = new ImageLayer3D(limg, params);

                    dset.addLayer(layer);
                }
            }
        }

    }

    public ImageView getSelectedView() {
        return ImageCanvasManager.getInstance().getSelectedCanvas().getSelectedView();
    }

    public ImageCanvas2 getSelectedCanvas() {
        return ImageCanvasManager.getInstance().getSelectedCanvas();

    }


    private void addLayerWithFadeIn(ILoadableImage limg, ImageView view) {
        IImageDisplayModel dset = view.getModel();

        //final LinearColorMap cmap = (LinearColorMap) params.getColorMap().getProperty();
        //cmap.setAlphaMultiplier(0.0);

        //FadeAction fade = new FadeAction(cmap);
        //final Timer timer = new javax.swing.Timer(25, fade);
        //timer.setCoalesce(true);
        //fade.setTimer(timer);
        ImageLayerProperties parms = new ImageLayerProperties(new LinearColorMap(limg.getData().getMinValue(), limg.getData().getMaxValue(),
                ResourceManager.getInstance().getDefaultColorMap()));
        ImageLayer3D layer = new ImageLayer3D(limg, parms);


        dset.addLayer(layer);
        //timer.start();

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


            ImageCanvas2 canvas = ImageCanvasManager.getInstance().
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
            AbstractLayer layer = view.getModel().getLayer(view.getModel().getSelectedIndex());
            double value = layer.getValue(gpoint);


            IColorMap cmap = layer.getImageLayerProperties().getColorMap().getProperty();

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