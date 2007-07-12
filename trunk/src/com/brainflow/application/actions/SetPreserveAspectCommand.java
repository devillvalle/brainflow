package com.brainflow.application.actions;

import com.pietschy.command.ActionCommand;
import com.pietschy.command.group.CommandGroup;
import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleVetoException;
import com.brainflow.application.toplevel.Brainflow;
import com.brainflow.core.ImageView;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 30, 2007
 * Time: 6:58:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SetPreserveAspectCommand extends ToggleCommand {

    private ImageView view;

    public SetPreserveAspectCommand(ImageView view) {
        super("preserve-aspect");
        this.view = view;
        setSelected(view.isPreserveAspect());
    }

    protected void handleSelection(boolean b) throws ToggleVetoException {
        if (view.isPreserveAspect() != b)
            view.setPreserveAspect(b);

    }


}
