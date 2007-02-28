package com.brainflow.application.toplevel;

import com.brainflow.application.presentation.ImageFileExplorer;
import com.brainflow.application.presentation.SearchableImageFileExplorer;
import com.brainflow.application.ImageCanvasTransferHandler;
import com.brainflow.application.FileSystemEventListener;
import com.brainflow.application.FileSystemEvent;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockContext;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 27, 2007
 * Time: 10:09:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ToolWindowManager {




    public static ToolWindowManager getInstance() {
        return (ToolWindowManager) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.toplevel.ToolWindowManager");
    }


   



}
