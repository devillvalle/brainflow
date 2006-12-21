package com.brainflow.display;

import com.brainflow.colormap.LinearColorMap;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.BufferedValueModel;
import com.jgoodies.binding.value.ValueModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 3, 2004
 * Time: 10:28:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class DisplayParameter<T> {

    //private ExtendedPropertyChangeSupport support = new ExtendedPropertyChangeSupport(this);
    private PresentationModel model;
    private T parameter;

    public static final String PROPERTYNAME_AFTER_BEAN = PresentationModel.PROPERTYNAME_AFTER_BEAN;
    public static final String PROPERTYNAME_BEAN = PresentationModel.PROPERTYNAME_BEAN;
    public static final String PROPERTYNAME_BEFORE_BEAN = PresentationModel.PROPERTYNAME_BEFORE_BEAN;
    public static final String PROPERTYNAME_BUFFERING = PresentationModel.PROPERTYNAME_BUFFERING;
    public static final String PROPERTYNAME_CHANGED = PresentationModel.PROPERTYNAME_CHANGED;
    public static final String PROPERTYNAME_TRIGGERCHANNEL = PresentationModel.PROPERTYNAME_TRIGGERCHANNEL;
    public static final String PROPERTYNAME_PARAMETER_CHANGED = "parameter";

    public DisplayParameter(T param) {
        parameter = param;
        model = new PresentationModel(param);
    }

    public void setParameter(T param) {
        parameter = param;
        model.setBean(parameter);

    }

    public T getParameter() {
        return parameter;
    }

    public boolean isDirty() {
        return model.isChanged();
    }

    public void clean() {
        model.resetChanged();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        model.addBeanPropertyChangeListener(listener);
        model.addPropertyChangeListener(PresentationModel.PROPERTYNAME_BEAN, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        model.removeBeanPropertyChangeListener(listener);
        model.removePropertyChangeListener(listener);
    }

    public void addSubstitutionListener(PropertyChangeListener listener) {
        model.addPropertyChangeListener(PresentationModel.PROPERTYNAME_BEAN, listener);
    }

    public void removeSubstitutionListener(PropertyChangeListener listener) {
        model.removePropertyChangeListener(PresentationModel.PROPERTYNAME_BEAN, listener);
    }

    public ValueModel getModel(String propertyName) {
        return model.getModel(propertyName);
    }

    public BufferedValueModel getBufferedModel(String propertyName) {
        return model.getBufferedModel(propertyName);
    }


    public static void main(String[] args) {
        LinearColorMap map = new LinearColorMap();
        LinearColorMap map2 = new LinearColorMap();

        PresentationModel model = new PresentationModel(map);
        model.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                System.out.println("event " + e.getPropertyName());
            }
        });

        model.addBeanPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                System.out.println("bean event " + e.getPropertyName());
            }
        });

        map.setUpperAlphaThreshold(77);
        model.setBean(map2);
        map2.setUpperAlphaThreshold(78);
        map2.setLowClip(22);
        map.setLowClip(33);
        map2.setHighClip(66);

    }


}
