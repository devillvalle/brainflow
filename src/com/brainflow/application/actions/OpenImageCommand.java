package com.brainflow.application.actions;

import com.pietschy.command.file.AbstractFileOpenCommand;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 20, 2007
 * Time: 9:14:06 PM
 */
public class OpenImageCommand extends AbstractFileOpenCommand {

    public OpenImageCommand(String s) {
        super("open-image");
    }

    public OpenImageCommand(FileFilter... fileFilters) {
        super("open-image", fileFilters);
    }

    protected void performOpen(File[] files) {
        // stub
    }
}
