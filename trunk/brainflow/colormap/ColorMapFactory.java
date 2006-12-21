/*
 * ColorMapFactory.java
 *
 * Created on February 3, 2003, 3:35 PM
 */


package com.brainflow.colormap;

import com.brainflow.utils.Range;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import java.awt.*;
import java.awt.image.IndexColorModel;
import java.io.IOException;

/**
 *
 * @author  Bradley
 */
public class  ColorMapFactory {
    
    /** Creates a new instance of ColorMapFactory */
    public ColorMapFactory() {
    }
    
    //public IntervalColorModel readFromXml() {
    
    public static Document encodeAsXml(IntervalColorModel icm) {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        root.addContent("XML Serialized RGBA Color Map");
        Element mapInfo = new Element("MapInfo");
        mapInfo.setAttribute("mapSize", String.valueOf(icm.getMapSize()));
        mapInfo.setAttribute("mapName", icm.getName());
        root.addContent(mapInfo);
        Element rgbaTable = new Element("Table");
        root.addContent(rgbaTable);
        for (int i=0; i<icm.getMapSize(); i++) {
            Color c = icm.getIndexedColor(i);
            Element entry = new Element("color");
            entry.setAttribute("index", "" + i);
            entry.setAttribute("r", "" + c.getRed());
            entry.setAttribute("g", "" + c.getGreen());
            entry.setAttribute("b", "" + c.getBlue());
            entry.setAttribute("a", "" + c.getAlpha());
            rgbaTable.addContent(entry);
        }
        
        return document;
        
    }
    
    
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
