package com.brainflow.actions;

import com.brainflow.application.managers.DirectoryManager;
import org.apache.commons.vfs.VFS;
import org.bushe.swing.action.BasicAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 30, 2006
 * Time: 12:54:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class MountDirectoryAction extends BasicAction {

    private String directoryPath;


    public MountDirectoryAction(String directoryPath) {
        this.directoryPath = directoryPath;
        this.putValue(Action.NAME, directoryPath);
    }


    protected void execute(ActionEvent actionEvent) throws Exception {
        DirectoryManager.getInstance().mountFileSystem(VFS.getManager().resolveFile(directoryPath));
    }
}
