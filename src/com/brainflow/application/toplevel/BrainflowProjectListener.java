package com.brainflow.application.toplevel;

import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 25, 2007
 * Time: 5:25:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BrainflowProjectListener  {

    public void modelAdded(BrainflowProjectEvent event);

    public void modelRemoved(BrainflowProjectEvent event);

    public void intervalAdded(BrainflowProjectEvent event);

    public void contentsChanged(BrainflowProjectEvent event);

    public void intervalRemoved(BrainflowProjectEvent event);
}
