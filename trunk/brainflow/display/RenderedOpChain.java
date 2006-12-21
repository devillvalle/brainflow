package com.brainflow.display;
import java.awt.image.*;
import javax.media.jai.*;
import java.util.*;
import java.awt.image.renderable.*;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.beans.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class RenderedOpChain implements PropertyChangeListener{

    RenderedImage source;
    Stack chain = new Stack();
    LinkedHashMap operatorMap = new LinkedHashMap();


    private transient Vector propertyChangeListeners;

    public RenderedOpChain(RenderedImage _source) {
        source = _source;
        chain.push(source);

    }


    public boolean containsNode(String label) {
        if (operatorMap.containsKey(label))
            return true;
        return false;
    }

    public ParameterBlock getParameterBlock(String label) {
        RenderedOp rop = (RenderedOp)operatorMap.get(label);
        return rop.getParameterBlock();
    }

    public RenderedOp getRenderedOp(String label) {
        return (RenderedOp)operatorMap.get(label);
    }


    public void addNode(String op, ParameterBlock pb, String label) {
        pb.setSource((RenderedImage)chain.peek(),0);
        RenderedOp rop = JAI.create(op, pb);

        operatorMap.put(label, rop);
        chain.push(rop);
        rop.addPropertyChangeListener(this);



    }

    public void removeNode(String label) {
        if (!operatorMap.containsKey(label)) {
            throw new IllegalArgumentException("Cannot remove node that does not exist!");
        }

        RenderedOp rop = (RenderedOp)operatorMap.get(label);

        int index = chain.indexOf(rop);
        if (index == (chain.size()-1)) {
            chain.remove(index);
            operatorMap.remove(label);
        }

        else {
            RenderedOp postOp = (RenderedOp)chain.get(index+1);
            postOp.setSource(rop.getSourceObject(0), 0);
            chain.remove(index);
            operatorMap.remove(rop);
        }
    }



    public void replaceNode(String op, ParameterBlock pb, String label) {
        int index = chain.indexOf(operatorMap.get(label));
        if (index == -1) {
            System.out.println("adding new node " + label);
            this.addNode(op, pb, label);
            return;
        }
        try {
            pb.setSource(chain.get(index-1),0);
            RenderedOp newOp = JAI.create(op, pb);
            newOp.addPropertyChangeListener(this);
            operatorMap.put(label, newOp);
            chain.set(index, newOp);
            firePropertyChange(new PropertyChangeEventJAI(this, "ReplaceNode", "old", "new"));
        } catch (IllegalArgumentException e) {
            RenderedImage src = (RenderedImage)chain.get(index-1);
            System.out.println("X: " + src.getMinX());
            System.out.println("Y: " + src.getMinY());
            System.out.println("W: " + src.getWidth());
            System.out.println("H: " + src.getHeight());
            throw e;
        }

    }



    public void addNode(String op, ParameterBlock pb, RenderingHints hints, String label) {
        pb.setSource((RenderedImage)chain.peek(), 0);
        RenderedOp rop = JAI.create(op, pb, hints);
        operatorMap.put(label, rop);
        chain.push(rop);
        rop.addPropertyChangeListener(this);
    }



    public void updateParameters(String label, ParameterBlock nparams) {
        RenderedOp rop = (RenderedOp)operatorMap.get(label);

        if (rop != null) {
            rop.setParameterBlock(nparams);
        }
        else
            throw new RuntimeException("attempting to update non-existent operation node " + label);

    }

    public void updateParameters(String label, ParameterBlock nparams, RenderingHints hints) {
        RenderedOp rop = (RenderedOp)operatorMap.get(label);

        if (rop != null) {
            rop.setParameterBlock(nparams);
            rop.setRenderingHints(hints);
        }
        else
            throw new RuntimeException("attempting to update non-existent operation node " + label);

    }

    public RenderedImage getSink() {
        return (RenderedImage)chain.peek();
    }



    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        if (propertyChangeListeners != null && propertyChangeListeners.contains(l)) {
            Vector v = (Vector) propertyChangeListeners.clone();
            v.removeElement(l);
            propertyChangeListeners = v;
        }
    }

    public void printInfo() {
        for (int i=0; i<chain.size(); i++) {
            RenderedImage rop = (RenderedImage)chain.get(i);
            if (rop instanceof RenderedOp) {
                RenderedOp rope = (RenderedOp)rop;
                String opname = rope.getOperationName();
                System.out.println("operator is: " + opname);
                int params = rope.getNumParameters();
                ParameterBlock pb = rope.getParameterBlock();
                for (int j=0; j<params; j++) {
                    System.out.println(pb.getObjectParameter(j));
                }


            }
        }
    }


    public void forceRender() {
        for (int i=0; i<chain.size(); i++) {
            RenderedImage rop = (RenderedImage)chain.get(i);
            if (rop instanceof RenderedOp) {
                String opname = ((RenderedOp)rop).getOperationName();
                System.out.println("operator is: " + opname);
            }
            System.out.println("" + i + " " + rop.getWidth() + " " + rop.getHeight());
        }
    }
    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        Vector v = propertyChangeListeners == null ? new Vector(2) : (Vector) propertyChangeListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            propertyChangeListeners = v;
        }
    }
    protected void firePropertyChange(PropertyChangeEvent e) {
        if (propertyChangeListeners != null) {
            Vector listeners = propertyChangeListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((PropertyChangeListener) listeners.elementAt(i)).propertyChange(e);
            }
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (propertyChangeListeners != null) {
            firePropertyChange(e);
        }
    }


}
