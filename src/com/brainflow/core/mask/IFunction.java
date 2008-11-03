package com.brainflow.core.mask;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 30, 2008
 * Time: 6:24:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IFunction<T> {


    public int getNumArguments();

    public T evaluate();

    public String getName();

    




}
