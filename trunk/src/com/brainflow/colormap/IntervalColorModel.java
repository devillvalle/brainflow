package com.brainflow.colormap;

import cern.colt.list.DoubleArrayList;
import com.brainflow.core.BrainflowException;
import com.brainflow.utils.ArrayUtils;
import com.brainflow.utils.BSpline;

import com.brainflow.utils.ToStringGenerator;
import com.brainflow.utils.Range;

import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import java.awt.*;
import java.awt.image.IndexColorModel;
import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public final class IntervalColorModel implements Serializable {

    private final static Logger log = Logger.getLogger(IntervalColorModel.class.getName());

    private Range displayRange;

    private transient IndexColorModel wrappedModel;

    private String name;

    public IntervalColorModel(Range _displayRange, IndexColorModel model, String _name) {
        wrappedModel = model;
        displayRange = _displayRange;
        name = _name;

    }

    public static IntervalColorModel getGrayScale(Range displayRange) {
        return new IntervalColorModel(displayRange, ColorTable.GRAYSCALE, "grayscale");
              
    }


    public void setName(String newName) {
        name = newName;
    }


    public IntervalColorModel resampleMap(int mapSize) {

        double[] reds = getReds();
        double[] blues = getBlues();
        double[] greens = getGreens();
        double[] alphas = getAlphas();
        double[] xpoints = new double[wrappedModel.getMapSize()];
        for (int i=0; i<xpoints.length; i++) {
            xpoints[i] = i;
        }


        BSpline spline1 = new BSpline(new DoubleArrayList(xpoints), new DoubleArrayList(reds));
        BSpline spline2 = new BSpline(new DoubleArrayList(xpoints), new DoubleArrayList(blues));
        BSpline spline3 = new BSpline(new DoubleArrayList(xpoints), new DoubleArrayList(greens));
        BSpline spline4 = new BSpline(new DoubleArrayList(xpoints), new DoubleArrayList(alphas));



        DoubleArrayList[] nreds = spline1.evaluateSpline(mapSize);
        DoubleArrayList[] nblues = spline2.evaluateSpline(mapSize);
        DoubleArrayList[] ngreens = spline3.evaluateSpline(mapSize);
        DoubleArrayList[] nalphas = spline4.evaluateSpline(mapSize);

        nreds[1].trimToSize();
        ngreens[1].trimToSize();
        nblues[1].trimToSize();
        nalphas[1].trimToSize();

        reds = nreds[1].elements();
        greens = ngreens[1].elements();
        blues = nblues[1].elements();
        alphas = nalphas[1].elements();

        IndexColorModel nmodel = new IndexColorModel(8, mapSize, ArrayUtils.castToBytes(reds), ArrayUtils.castToBytes(greens),
        ArrayUtils.castToBytes(blues), ArrayUtils.castToBytes(alphas));

        return new IntervalColorModel(new Range(displayRange.getMin(), displayRange.getMax()), nmodel, getName() +
                "resampled(" + mapSize + ")");


    }

    public String getName() {
        return name;
    }


    public Range getDisplayRange() {
        return new Range(displayRange.getMin(), displayRange.getMax());
    }


    public void setDisplayRange(Range _displayRange) {
        displayRange = _displayRange;
    }


    public IndexColorModel getWrappedModel() {
        return wrappedModel;
    }



    public IntervalColorModel safeCopy() {
        IntervalColorModel nmodel = new IntervalColorModel(new Range(displayRange.getMin(), displayRange.getMax()), wrappedModel, getName());

        return nmodel;
    }


    public IntervalColorModel makeAndCopy(Color constantColor, String name) {
        IndexColorModel icm = ColorTable.createConstantMap(constantColor);
        IntervalColorModel nmodel = new IntervalColorModel(new Range(displayRange.getMin(), displayRange.getMax()), icm, getName());
        return nmodel;

    }

    public IntervalColorModel makeAndCopy(IndexColorModel _wrappedModel, String name) {
        IntervalColorModel nmodel = new IntervalColorModel(new Range(displayRange.getMin(), displayRange.getMax()), _wrappedModel, getName());
        return nmodel;
    }

    public final int indexOf(double value) {
        double intervalSize = displayRange.getMax() - displayRange.getMin();
        int mapSize = wrappedModel.getMapSize();
        int nbin = (int)(((value-displayRange.getMin())/intervalSize)*mapSize);
        if (nbin >= mapSize)
            nbin = mapSize-1;
        else if (nbin < 0)
            nbin=0;

        return nbin;
    }

    public String classInfo() {
        return new ToStringGenerator().generateToString(this);
    }

    public Color getIndexedColor(int idx) {
        return new Color(wrappedModel.getRGB(idx));
    }

    public double[] getReds() {
        byte[] b = new byte[wrappedModel.getMapSize()];
        wrappedModel.getReds(b);
        return ArrayUtils.castToDoubles(b);
    }

    public double[] getBlues() {
        byte[] b = new byte[wrappedModel.getMapSize()];
        wrappedModel.getBlues(b);
        return ArrayUtils.castToDoubles(b);
    }

    public double[] getGreens() {
        byte[] b = new byte[wrappedModel.getMapSize()];
        wrappedModel.getGreens(b);
        return ArrayUtils.castToDoubles(b);
    }

    public double[] getAlphas() {
        byte[] b = new byte[wrappedModel.getMapSize()];
        wrappedModel.getAlphas(b);
        return ArrayUtils.castToDoubles(b);
    }

    public Color getColor(double value) {
        int idx = indexOf(value);
        int rgb = wrappedModel.getRGB(idx);
        return new Color(rgb);
    }

    public int getMapSize() {
        return wrappedModel.getMapSize();
    }

    public String toString() {
        return getName();
    }

    public static IntervalColorModel createFromXMLInputStream(InputStream istream) throws BrainflowException {
        assert istream != null : "IntervalColorModel.createFromXMLInputStream passed a null InputStream";

        try {
            StringBuffer sb = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }

            return IntervalColorModel.createFromXml(sb.toString());

        } catch(IOException e) {
            throw new BrainflowException("Error loading color model from XML stream", e);
        }

    }

    public static IntervalColorModel createFromXml(String rgbaXml) throws BrainflowException {
        IntervalColorModel ret = null;

        try {
            SAXBuilder parser = new SAXBuilder();
            Document doc = parser.build(new java.io.StringReader(rgbaXml));
            Element root = doc.getRootElement();
            Element info = root.getChild("MapInfo");
            int size = info.getAttribute("mapSize").getIntValue();
            String name = info.getAttribute("mapName").getValue();

            byte[] reds = new byte[size];
            byte[] greens = new byte[size];
            byte[] blues = new byte[size];
            byte[] alphas = new byte[size];

            Element table = root.getChild("Table");
            List entries = table.getChildren("color");
            for (Iterator iter = entries.iterator(); iter.hasNext(); ) {
                Element e = (Element)iter.next();
                int index = e.getAttribute("index").getIntValue();
                int red = e.getAttribute("r").getIntValue();
                int green = e.getAttribute("g").getIntValue();
                int blue = e.getAttribute("b").getIntValue();
                int alpha = e.getAttribute("a").getIntValue();
                reds[index] = (byte)red;
                greens[index] = (byte)green;
                blues[index] = (byte)blue;
                alphas[index] = (byte)alpha;
            }

            IndexColorModel cm = new IndexColorModel(8, size, reds, greens, blues, alphas);

            if (cm.hasAlpha() == false) {
                cm = new IndexColorModel(8, size, reds, greens, blues, Transparency.TRANSLUCENT);
            }

            ret = new IntervalColorModel(new Range(0,256), cm, name);
            //ret.setName(name);

        }
        catch(JDOMException e) {
            throw new BrainflowException(e);
        }
        catch(IOException e) {
            throw new BrainflowException(e);
        }



        return ret;


    }

    public String encodeAsXml() {
        Document document = new Document();
        Element root = new Element("root");
        document.setRootElement(root);
        root.addContent("XML Serialized RGBA Color Map");
        Element sourceClass = new Element("Class");
        sourceClass.addContent(getClass().getName());
        Element mapInfo = new Element("MapInfo");
        mapInfo.setAttribute("mapSize", String.valueOf(wrappedModel.getMapSize()));
        mapInfo.setAttribute("mapName", getName());
        root.addContent(mapInfo);
        Element rgbaTable = new Element("Table");
        root.addContent(rgbaTable);
        for (int i=0; i<wrappedModel.getMapSize(); i++) {
            Color c = getIndexedColor(i);
            Element entry = new Element("color");
            entry.setAttribute("index", "" + i);
            entry.setAttribute("r", "" + c.getRed());
            entry.setAttribute("g", "" + c.getGreen());
            entry.setAttribute("b", "" + c.getBlue());
            entry.setAttribute("a", "" + c.getAlpha());
            rgbaTable.addContent(entry);
        }

        XMLOutputter serializer = new XMLOutputter();
        //serializer.setNewlines(true);
        return serializer.outputString(document);
    }


    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        byte[] reds = new byte[256];
        byte[] greens = new byte[256];
        byte[] blues = new byte[256];
        byte[] alphas = new byte[256];
        boolean hasAlpha = wrappedModel.hasAlpha();
        wrappedModel.getReds(reds);
        wrappedModel.getBlues(blues);
        wrappedModel.getGreens(greens);
        if (hasAlpha) {
            wrappedModel.getAlphas(alphas);
        }
        s.writeBoolean(hasAlpha);
        s.write(reds);
        s.write(greens);
        s.write(blues);
        if (hasAlpha) {
            s.write(alphas);
        }
    }


    private void readObject(ObjectInputStream s) throws IOException  {
        try {
            s.defaultReadObject();
            byte[] reds = new byte[256];
            byte[] greens = new byte[256];
            byte[] blues = new byte[256];
            byte[] alphas = null;
            boolean hasAlpha = s.readBoolean();
            s.read(reds);
            s.read(greens);
            s.read(blues);
            if (hasAlpha) {
                alphas = new byte[256];
                s.read(alphas);
                wrappedModel = new IndexColorModel(8, 256, reds,greens, blues, alphas);
            }
            else {
                wrappedModel = new IndexColorModel(8, 256, reds,greens, blues);
            }

        } catch (ClassNotFoundException e) {
            log.severe("Serialization error in IndexColorModel.readObject ");
            
        }

    }



    public static void main(String[] args) {
        IndexColorModel tmp = ColorTable.SPECTRUM;
        IntervalColorModel icm = new IntervalColorModel(new Range(0,256),  tmp, "spectrum");

        String s = icm.encodeAsXml();
        try{
            icm = IntervalColorModel.createFromXml(s);
        } catch (Exception e) { e.printStackTrace(); }

        try {
            FileWriter writer = new FileWriter(new File("c:\\spectrum.rgba"));
            writer.write(s);
            writer.close();
        } catch (Exception e) {}
    }

}
