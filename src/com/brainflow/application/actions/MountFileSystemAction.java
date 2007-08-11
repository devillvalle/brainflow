package com.brainflow.application.actions;

import com.brainflow.application.toplevel.Brainflow;
import com.brainflow.application.toplevel.DirectoryManager;
import com.jidesoft.swing.FolderChooser;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.VFS;
import org.bushe.swing.action.BasicAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Feb 10, 2005
 * Time: 12:30:11 PM
 * To change this template use File | Settings | File Templates.
 */


public class MountFileSystemAction extends BasicAction {


    // hello
    Logger log = Logger.getLogger(getClass().getName());
    private FolderChooser chooser = null;


    public MountFileSystemAction() {
        putValue(Action.NAME, "Mount FileSystem");
        putValue(Action.SHORT_DESCRIPTION, "Mount FileSystem for Image Access");
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("resources/icons/class_hi.gif")));
        putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_M));

        initialize();
    }

    private void initialize() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                MountFileSystemAction.this.chooser = new FolderChooser(DirectoryManager.getInstance().getCurrentLocalDirectory());
            }
        });

        thread.start();


    }


    public void execute(ActionEvent e) {

        if (chooser == null) {
            chooser = new FolderChooser(DirectoryManager.getInstance().getCurrentLocalDirectory());
        }


        final int res = chooser.showOpenDialog(Brainflow.getInstance().getApplicationFrame());

        if (res == FolderChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            for (int i = 0; i < files.length; i++) {
                try {
                    DirectoryManager.getInstance().mountFileSystem(VFS.getManager().resolveFile(files[i].getAbsolutePath()));

                } catch (FileSystemException ex) {
                    log.severe("FileSystemException caught, failed to resolve local file " + files[i]);
                    //todo need to catch this somewhere. Need mechanism to catch all runtime exceptionsof this type.
                    throw new RuntimeException(ex);
                }

                DirectoryManager.getInstance().setCurrentLocalDirectory(files[files.length - 1]);
            }
        }
    }

}

