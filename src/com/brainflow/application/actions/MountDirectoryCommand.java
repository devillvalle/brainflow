package com.brainflow.application.actions;

import com.brainflow.application.toplevel.DirectoryManager;
import com.pietschy.command.ActionCommand;
import com.pietschy.command.face.Face;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.FileSystemException;
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
public class MountDirectoryCommand extends ActionCommand {

    private String directoryPath;


    public MountDirectoryCommand(String directoryPath) {
        this.directoryPath = directoryPath;
        getFace(Face.MENU, true).setText(directoryPath);

    }

    protected void handleExecute() {
        try {
            DirectoryManager.getInstance().mountFileSystem(VFS.getManager().resolveFile(directoryPath));
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }
    }


}
