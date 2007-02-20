package com.brainflow.display;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.BufferedValueModel;
import com.jgoodies.binding.value.ValueModel;

import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 3, 2004
 * Time: 10:28:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class Property<T> {

    //private ExtendedPropertyChangeSupport support = new ExtendedPropertyChangeSupport(this);
    private PresentationModel model;
    private T property;

    public static final String PROPERTYNAME_AFTER_BEAN = PresentationModel.PROPERTYNAME_AFTER_BEAN;
    public static final String PROPERTYNAME_BEAN = PresentationModel.PROPERTYNAME_BEAN;
    public static final String PROPERTYNAME_BEFORE_BEAN = PresentationModel.PROPERTYNAME_BEFORE_BEAN;
    public static final String PROPERTYNAME_BUFFERING = PresentationModel.PROPERTYNAME_BUFFERING;
    public static final String PROPERTYNAME_CHANGED = PresentationModel.PROPERTYNAME_CHANGED;
    public static final String PROPERTYNAME_TRIGGERCHANNEL = PresentationModel.PROPERTYNAME_TRIGGERCHANNEL;
    public static final String PROPERTYNAME_PARAMETER_CHANGED = "property";

   
    public Property(T param) {
        property = param;
        model = new PresentationModel(param);
    }

    public void setProperty(T prop) {
        property = prop;
        model.setBean(property);

    }

    public T getProperty() {
        return property;
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





}
