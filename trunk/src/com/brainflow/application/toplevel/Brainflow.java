package com.brainflow.application.toplevel;

import com.brainflow.application.*;
import com.brainflow.application.actions.*;
import com.brainflow.application.presentation.*;
import com.brainflow.application.services.ImageViewMousePointerEvent;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap2;
import com.brainflow.core.*;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.IImageDataSource;
import com.brainflow.utils.Range;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.document.DocumentComponent;
import com.jidesoft.document.DocumentPane;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.plaf.basic.ThemePainter;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.StatusBar;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.dialog.JideOptionPane;
import com.pietschy.command.CommandContainer;
import com.pietschy.command.GuiCommands;
import com.pietschy.command.face.Face;
import com.pietschy.command.configuration.ParseException;
import com.pietschy.command.group.CommandGroup;
import com.pietschy.command.group.ExpansionPointBuilder;
import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleGroup;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.AWTEventListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: Nov 24, 2004
 * Time: 9:00:59 AM
 * To change this template use File | Settings | File Templates.
 */

public class Brainflow {

    static {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");
    }


    private final BrainflowContext applicationContext = new BrainflowContext();

    private final static Logger log = Logger.getLogger(Brainflow.class.getCanonicalName());

    private BrainFrame brainFrame = null;


    private RecentPathMenu pathMenu = new RecentPathMenu();

    private DocumentPane documentPane = new DocumentPane();

    private ImageFileExplorer loadingDock = null;

    private StatusBar statusBar;


    private SelectedViewStatus viewStatus;

    private CommandContainer commandContainer;

    private ExecutorService threadService = Executors.newCachedThreadPool();

    protected Brainflow() {
        // Exists only to thwart instantiation.
    }

    public static Brainflow getInstance() {
        return (Brainflow) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.toplevel.Brainflow");
    }


    public static void main(String[] args) {


        final Brainflow bflow = getInstance();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    log.info("Launching Brainflow ...");
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

    private void reportTime(long startTime, String message) {
        long curTime = System.currentTimeMillis();
        long duration = curTime - startTime;

        log.info(message + " : " + duration);
    }

    public void launch() throws Throwable {


        try {

            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            System.out.println(UIManager.getLookAndFeel());

            LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2003_STYLE);
        } catch (UnsupportedLookAndFeelException e) {
            log.severe("could not load look and feel");
        }

        //final SplashScreen splash = SplashScreen.getSplashScreen();

        //JFrame.setDefaultLookAndFeelDecorated(true);

        long startTime = System.currentTimeMillis();


        brainFrame = new BrainFrame();
        statusBar = new StatusBar();
        reportTime(startTime, "created brainframe");
        brainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        log.info("initializing BrainCanvasManager ...");
        BrainCanvasManager.getInstance().createCanvas();

        log.info("initializing resources ...");
        FutureTask<Boolean> initResources = new FutureTask<Boolean>(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return initializeResources();
            }
        });

        threadService.execute(initResources);


        log.info("loading commands ...");
        FutureTask<Boolean> loadCommandsTask = new FutureTask<Boolean>(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return loadCommands();
            }
        });

        threadService.execute(loadCommandsTask);


        log.info("initializing IO ...");
        FutureTask<Boolean> initIOTask = new FutureTask<Boolean>(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return initImageIO();
            }
        });

        threadService.execute(initIOTask);


        log.info("initializing status bar ...");
        initializeStatusBar();
        reportTime(startTime, "initialized status bar");


        log.info("initializing work space ...");
        initializeWorkspace();
        reportTime(startTime, "initialized work space");

        loadCommandsTask.get();
        reportTime(startTime, "loaded commands");

        log.info("binding container ...");
        bindContainer();
        reportTime(startTime, "bound container");


        log.info("initializing tool bar ...");
        initializeToolBar();
        reportTime(startTime, "initialized tool bar");


        log.info("initializing menu ...");
        initializeMenu();
        reportTime(startTime, "initialized menu");


        initIOTask.get();
        reportTime(startTime, "initialized IO");

        initResources.get();
        reportTime(startTime, "initialized resources");

        initExceptionHandler();


    }

    private void initExceptionHandler() {
        LookAndFeelFactory.UIDefaultsCustomizer uiDefaultsCustomizer = new LookAndFeelFactory.UIDefaultsCustomizer() {
            public void customize(UIDefaults defaults) {
                ThemePainter painter = (ThemePainter) UIDefaultsLookup.get("Theme.painter");
                defaults.put("OptionPaneUI", "com.jidesoft.plaf.basic.BasicJideOptionPaneUI");

                defaults.put("OptionPane.showBanner", Boolean.TRUE); // show banner or not. default is true
                //defaults.put("OptionPane.bannerIcon", JideIconsFactory.getImageIcon(JideIconsFactory.JIDE50));
                defaults.put("OptionPane.bannerFontSize", 13);
                defaults.put("OptionPane.bannerFontStyle", Font.BOLD);
                defaults.put("OptionPane.bannerMaxCharsPerLine", 60);
                defaults.put("OptionPane.bannerForeground", painter != null ? painter.getOptionPaneBannerForeground() : null);  // you should adjust this if banner background is not the default gradient paint
                defaults.put("OptionPane.bannerBorder", null); // use default border

                // set both bannerBackgroundDk and // set both bannerBackgroundLt to null if you don't want gradient
                defaults.put("OptionPane.bannerBackgroundDk", painter != null ? painter.getOptionPaneBannerDk() : null);
                defaults.put("OptionPane.bannerBackgroundLt", painter != null ? painter.getOptionPaneBannerLt() : null);
                defaults.put("OptionPane.bannerBackgroundDirection", Boolean.TRUE); // default is true

                // optionally, you can set a Paint object for BannerPanel. If so, the three UIDefaults related to banner background above will be ignored.
                defaults.put("OptionPane.bannerBackgroundPaint", null);

                defaults.put("OptionPane.buttonAreaBorder", BorderFactory.createEmptyBorder(6, 6, 6, 6));
                defaults.put("OptionPane.buttonOrientation", SwingConstants.RIGHT);
            }
        };
        uiDefaultsCustomizer.customize(UIManager.getDefaults());

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                JideOptionPane optionPane = new JideOptionPane("Click \"Details\" button to see more information ... ", JOptionPane.ERROR_MESSAGE, JideOptionPane.CLOSE_OPTION);
                optionPane.setTitle("An " + e.getClass().getName() + " occurred in Brainflow : " + e.getMessage());


                JTextArea textArea = new JTextArea();
                StringWriter sw = new StringWriter();
                PrintWriter out = new PrintWriter(sw);
                e.printStackTrace(out);
                // Add string to end of text area
                textArea.append(sw.toString());
                textArea.setRows(10);
                optionPane.setDetails(new JScrollPane(textArea));
                JDialog dialog = optionPane.createDialog(brainFrame, "Warning");
                dialog.setResizable(true);
                dialog.pack();
                dialog.setVisible(true);
            }
        });

    }


    private boolean loadCommands() {
        try {

            GuiCommands.load("resources/commands/ImageViewCommands");
            GuiCommands.load("resources/commands/BrainFlowCommands");


        } catch (ParseException e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }

        return true;
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

    private void initializeLogging() {

    }


    private void initializeMenu() {
        log.info("initializing Menu");


        GoToVoxelCommand gotoVoxelCommand = new GoToVoxelCommand();
        gotoVoxelCommand.bind(getApplicationFrame());
        gotoVoxelCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        CommandGroup fileMenuGroup = new CommandGroup("file-menu");
        fileMenuGroup.bind(getApplicationFrame());

        CommandGroup viewMenuGroup = new CommandGroup("view-menu");
        viewMenuGroup.bind(getApplicationFrame());

        CommandGroup gotoMenuGroup = new CommandGroup("goto-menu");
        gotoMenuGroup.bind(getApplicationFrame());

        JMenuBar menuBar = new JMenuBar();

        menuBar.add(fileMenuGroup.createMenuItem());
        menuBar.add(viewMenuGroup.createMenuItem());
        menuBar.add(gotoMenuGroup.createMenuItem());

        brainFrame.setJMenuBar(menuBar);

        MountFileSystemCommand mountFileSystemCommand = new MountFileSystemCommand();
        mountFileSystemCommand.bind(getApplicationFrame());

        ExitApplicationCommand exitCommand = new ExitApplicationCommand();
        exitCommand.bind(getApplicationFrame());

        ExpansionPointBuilder builder = fileMenuGroup.getExpansionPointBuilder();
        builder.add(pathMenu.getCommandGroup());
        builder.applyChanges();

        brainFrame.getJMenuBar().add(DockWindowManager.getInstance().getDockMenu());


    }

    private void initializeStatusBar() {
        viewStatus = new SelectedViewStatus();
        log.info("initialzing status bar");
        statusBar.setAutoAddSeparator(false);

        statusBar.add(viewStatus.getComponent(), JideBoxLayout.FIX);
        statusBar.add(new com.jidesoft.status.StatusBarSeparator(), JideBoxLayout.FIX);

        LabelStatusBarItem crossLabel = new LabelStatusBarItem();
        crossLabel.setText("Cross: ");
        statusBar.add(crossLabel, JideBoxLayout.FIX);

        CursorCoordinates cursorCoordinates = new CursorCoordinates();

        CrosshairCoordinates crosshairCoordinates = new CrosshairCoordinates();

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
        //statusBar.add(new com.jidesoft.status.ProgressStatusBarItem(), JideBoxLayout.FIX);

        //statusBar.add(new com.jidesoft.status.MemoryStatusBarItem());
        brainFrame.getContentPane().add(statusBar, "South");


    }

    private void initializeToolBar() {


        CommandGroup mainToolbarGroup = new CommandGroup("main-toolbar");
        mainToolbarGroup.bind(getApplicationFrame());


        ToggleGroup interpToggleGroup = new ToggleGroup("toggle-interp-group");
        interpToggleGroup.bind(getApplicationFrame());

        OpenImageCommand openImageCommand = new OpenImageCommand();
        openImageCommand.bind(getApplicationFrame());

        SnapshotCommand snapshotCommand = new SnapshotCommand();
        snapshotCommand.bind(getApplicationFrame());

        CreateAxialViewCommand axialCommand = new CreateAxialViewCommand();
        axialCommand.bind(getApplicationFrame());
        axialCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        CreateSagittalViewCommand sagittalCommand = new CreateSagittalViewCommand();
        sagittalCommand.bind(getApplicationFrame());
        sagittalCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        CreateCoronalViewCommand coronalCommand = new CreateCoronalViewCommand();
        coronalCommand.bind(getApplicationFrame());
        coronalCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        CreateVerticalOrthogonalCommand vertCommand = new CreateVerticalOrthogonalCommand();
        vertCommand.bind(getApplicationFrame());
        vertCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        CreateHorizontalOrthogonalCommand horizCommand = new CreateHorizontalOrthogonalCommand();
        horizCommand.bind(getApplicationFrame());
        horizCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        CreateTriangularOrthogonalCommand triCommand = new CreateTriangularOrthogonalCommand();
        triCommand.bind(getApplicationFrame());
        triCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        CommandGroup orthoGroup = new CommandGroup("ortho-view-group");
        orthoGroup.bind(getApplicationFrame());


        final NextSliceCommand nextSliceCommand = new NextSliceCommand();
        nextSliceCommand.bind(getApplicationFrame());
        nextSliceCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        final PreviousSliceCommand previousSliceCommand = new PreviousSliceCommand();
        previousSliceCommand.bind(getApplicationFrame());
        previousSliceCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent event) {
                if (event.getID() == KeyEvent.KEY_PRESSED) {
                    KeyEvent ke = (KeyEvent) event;
                    if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                        previousSliceCommand.execute();
                    } else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                        nextSliceCommand.execute();
                    }

                }

            }
        }, AWTEvent.KEY_EVENT_MASK);


        PageBackSliceCommand pageBackSliceCommand = new PageBackSliceCommand();
        pageBackSliceCommand.bind(getApplicationFrame());
        pageBackSliceCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        PageForwardSliceCommand pageForwardSliceCommand = new PageForwardSliceCommand();
        pageForwardSliceCommand.bind(getApplicationFrame());
        pageForwardSliceCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        IncreaseContrastCommand increaseContrastCommand = new IncreaseContrastCommand();
        increaseContrastCommand.bind(getApplicationFrame());
        increaseContrastCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        DecreaseContrastCommand decreaseContrastCommand = new DecreaseContrastCommand();
        decreaseContrastCommand.bind(getApplicationFrame());
        decreaseContrastCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        ToggleCommand nearest = new NearestInterpolationToggleCommand();
        nearest.bind(getApplicationFrame());
        decreaseContrastCommand.installShortCut(documentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        ToggleCommand linear = new LinearInterpolationToggleCommand();
        linear.bind(getApplicationFrame());

        ToggleCommand cubic = new CubicInterpolationToggleCommand();
        cubic.bind(getApplicationFrame());

        ToggleCommand toggleAxisLabelCommand = new ToggleAxisLabelCommand();
        toggleAxisLabelCommand.bind(getApplicationFrame());


        JToolBar mainToolbar = mainToolbarGroup.createToolBar();
        //ActionCommand increaseContrastCommand = new IncreaseContrastCommand();
        //increaseContrastCommand.bind(brainFrame);
        //mainToolbar.add(increaseContrastCommand.getActionAdapter());

        //ActionCommand decreaseContrastCommand = new DecreaseContrastCommand();
        //decreaseContrastCommand.bind(brainFrame);
        //mainToolbar.add(decreaseContrastCommand.getActionAdapter());


        brainFrame.getContentPane().add(mainToolbar, BorderLayout.NORTH);

        //InputMap map = documentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        //for (KeyStroke ks : map.keys()) {
        //    System.out.println("key : " + ks);
        //}


    }


    private void initializeWorkspace() throws Exception {
        log.info("initializing workspace");
        brainFrame.getDockingManager().getWorkspace().setLayout(new BorderLayout());
        brainFrame.getDockingManager().getWorkspace().add(documentPane, "Center");

        BrainCanvas canvas = BrainCanvasManager.getInstance().getSelectedCanvas();
        canvas.setRequestFocusEnabled(true);
        canvas.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        applicationContext.putValue(ActionContext.SELECTED_CANVAS, canvas);


        documentPane.setTabPlacement(DocumentPane.BOTTOM);
        documentPane.openDocument(new DocumentComponent(new JScrollPane(canvas), "Canvas-1"));
        documentPane.setActiveDocument("Canvas-1");

        log.info("initializing loading dock");
        initLoadingDock();
        log.info("initializing project view");
        initProjectView();
        log.info("initializing image table view");
        initLoadableImageTableView();
        log.info("initializing control panel");
        initControlPanel();
        log.info("initializing event monitor");
        initEventBusMonitor();
        log.info("initializing log monitor");
        initLogMonitor();


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
            BrainCanvasManager.getInstance().getSelectedCanvas().setTransferHandler(handler);

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

    private void initLogMonitor() {
        DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("Log Monitor",
                "resources/icons/console_view.gif",
                DockContext.STATE_AUTOHIDE,
                DockContext.DOCK_SIDE_SOUTH,
                1);


        LogMonitor monitor = new LogMonitor();
        monitor.setLevel(Level.FINEST);
        LogManager.getLogManager().getLogger("").addHandler(monitor);
        dframe.getContentPane().add(new JScrollPane(monitor.getComponent()));

        dframe.setPreferredSize(new Dimension(800, 200));
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

        DockableFrame dframe = DockWindowManager.getInstance().createDockableFrame("Tool Box",
                "resources/icons/types.gif",
                DockContext.STATE_FRAMEDOCKED,
                DockContext.DOCK_SIDE_EAST);


        ColorAdjustmentControl colorAdjustmentControl = new ColorAdjustmentControl();

        CoordinateControls coordinateControls = new CoordinateControls();

        LayerInfoControl layerInfoControl = new LayerInfoControl();


        //ColorMapTablePresenter tablePresenter = new ColorMapTablePresenter();


        //MaskTablePresenter maskPresenter = new MaskTablePresenter();

        tabbedPane.addTab("Adjustment", new JScrollPane(colorAdjustmentControl.getComponent()));
        tabbedPane.addTab("Layer Info", new JScrollPane(layerInfoControl.getComponent()));
        //tabbedPane.addTab("Color Table", tablePresenter.getComponent());
        //tabbedPane.addTab("Mask Table", maskPresenter.getComponent());
        tabbedPane.addTab("Coordinates", new JScrollPane(coordinateControls.getComponent()));


        dframe.getContentPane().add(tabbedPane);

        dframe.setPreferredSize(new Dimension(300, 500));
        brainFrame.getDockingManager().addFrame(dframe);

    }


    private boolean initImageIO() {
        log.info("initializing imageio");
        try {
            ImageIOManager.getInstance().initialize();
        } catch (BrainflowException e) {
            log.severe("Could not initialize IO facilities, aborting");
            throw new RuntimeException(e);
        }

        return true;
    }

    private boolean initializeResources() {
        log.info("initializing resources");
        ResourceManager.getInstance().getColorMaps();
        return true;
    }


    private void register(IImageDataSource limg) {
        DataSourceManager manager = DataSourceManager.getInstance();
        boolean alreadyRegistered = manager.isRegistered(limg);

        if (alreadyRegistered) {
            StringBuffer sb = new StringBuffer();
            sb.append("Image " + limg.getDataFile().getName().getBaseName());
            sb.append(" has already been loaded, would you like to reload from disk?");
            Integer ret = JOptionPane.showConfirmDialog(brainFrame, sb.toString(), "Image Already Loaded", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            log.info("return value is: " + ret);

            if (ret == JOptionPane.YES_OPTION) {
                limg.releaseData();
            }
        } else {
            manager.register(limg);
        }

    }

    private IImageDataSource specialHandling(IImageDataSource dataSource) {


        if (dataSource.getFileFormat().equals("Analyze7.5")) {
            JPanel panel = new JPanel();
            JLabel messageLabel = new JLabel("Please select correct image orientation from menu: ");
            java.util.List<Anatomy3D> choices = Anatomy3D.getInstanceList();
            JComboBox choiceBox = new JComboBox(choices.toArray());

            //todo hackery alert
            Anatomy anatomy = (Anatomy) dataSource.getImageInfo().getAnatomy();
            choiceBox.setSelectedItem(anatomy);

            FormLayout layout = new FormLayout("4dlu, l:p, p:g, 4dlu", "6dlu, p, 10dlu, p, 6dlu");
            CellConstraints cc = new CellConstraints();
            panel.setLayout(layout);
            panel.add(messageLabel, cc.xyw(2, 2, 2));
            panel.add(choiceBox, cc.xyw(2, 4, 2));

            JOptionPane.showMessageDialog(brainFrame, panel, "Analyze 7.5 image format ...", JOptionPane.WARNING_MESSAGE);
            Anatomy selectedAnatomy = (Anatomy) choiceBox.getSelectedItem();
            if (selectedAnatomy != anatomy) {
                //todo hackery alert
                dataSource.getImageInfo().setAnatomy((Anatomy3D) selectedAnatomy);
                dataSource.releaseData();
            }
        }

        return dataSource;

    }

    public void loadAndDisplay(final IImageDataSource dataSource) {

        if (dataSource != null) {
            final IImageDataSource checkedDataSource = specialHandling(dataSource);
            register(dataSource);

            ImageProgressDialog id = DataSourceManager.getInstance().createProgressDialog(checkedDataSource);

            JDialog dialog = id.getDialog();
            dialog.setVisible(true);

            id.execute();


        }

    }

    public void loadAndDisplay(IImageDataSource limg, ImageView view) {
        if (limg != null) {

            register(limg);
            IImageData data = null;
            data = limg.getData();
            if (data != null) {

                IImageDisplayModel dset = view.getModel();

                //todo data range should be a property of ImageLayerProperties, not IColorMap
                ImageLayerProperties params = new ImageLayerProperties(new Range(data.minValue(), data.maxValue()));

                //todo check data caches min and max values
                params.colorMap.set(new LinearColorMap2(data.minValue(), data.maxValue(), ResourceManager.getInstance().getDefaultColorMap()));

                ImageLayer layer = new ImageLayer3D(limg, params);

                dset.addLayer(layer);
                // todo hack alert
                //layer.getImageLayerProperties().visible.set(true);

            }
        }
    }


    public ImageView getSelectedView() {
        return BrainCanvasManager.getInstance().getSelectedCanvas().getSelectedView();
    }

    public BrainCanvas getSelectedCanvas() {
        return BrainCanvasManager.getInstance().getSelectedCanvas();

    }


    public IImageDataSource[] getSelectedLoadableImages() {
        IImageDataSource[] limg = loadingDock.requestLoadableImages();
        return limg;

    }


    class ValueStatusItem extends LabelStatusBarItem implements EventSubscriber {

        private Map<Color, ImageIcon> colorMap = new HashMap<Color, ImageIcon>();
        private NumberFormat format = NumberFormat.getNumberInstance();

        public ValueStatusItem() {
            EventBus.subscribeExactly(ImageViewMousePointerEvent.class, this);
            setIcon(ColorTable.createImageIcon(Color.GRAY, 40, 15));
            setText("Value :");
        }

        public void onEvent(Object evt) {

            ImageViewMousePointerEvent event = (ImageViewMousePointerEvent) evt;

            //todo only publish events when cursorPos is over valid view
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
            AbstractLayer layer = view.getSelectedLayer();
            double value = layer.getValue(gpoint);


            IColorMap cmap = layer.getImageLayerProperties().getColorMap();
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
            int i = view.getSelectedLayerIndex();
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
