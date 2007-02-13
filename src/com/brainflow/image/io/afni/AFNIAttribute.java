package com.brainflow.image.io.afni;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 12, 2007
 * Time: 7:55:59 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AFNIAttribute {

    enum AFNI_ATTRIBUTE_TYPE {
        string_attribute,
        float_attribute,
        integer_attribute,
        double_attribute;

    }


    public AFNIAttribute(String _name, int _count, String _content) {
        name = _name;
        count = _count;
        content = _content;
    }


    String name;

    int count;

    String content;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("type = " + getType());
        sb.append("\n");
        sb.append("name = " + name);
        sb.append("\n");
        sb.append("count = " + count);
        sb.append("\n");

        return sb.toString();
    }

    public abstract Object getData();

    public abstract AFNI_ATTRIBUTE_TYPE getType();

    public abstract void parseContent();

    public static AFNI_ATTRIBUTE_TYPE parseType(String typeStr) {
        StringTokenizer tokenizer = new StringTokenizer(typeStr, " ");
        String tok = null;

        while (tokenizer.hasMoreTokens()) {
            tok = tokenizer.nextToken();
        }

        return AFNI_ATTRIBUTE_TYPE.valueOf(tok);
    }

    public static int parseCount(String countStr) {
        StringTokenizer tokenizer = new StringTokenizer(countStr, " ");
        String tok = null;

        while (tokenizer.hasMoreTokens()) {
            tok = tokenizer.nextToken();
        }

        return Integer.parseInt(tok);

    }

    public static String parseName(String nameStr) {
        StringTokenizer tokenizer = new StringTokenizer(nameStr, " ");
        String tok = null;

        while (tokenizer.hasMoreTokens()) {
            tok = tokenizer.nextToken();
        }

        return tok;

    }

    public static AFNIAttribute createAttribute(AFNI_ATTRIBUTE_TYPE type, String name, int count, String content) {

        AFNIAttribute attr = null;

        switch (type) {
            case integer_attribute:
                attr = new IntegerAttribute(name, count, content);
                break;
            case float_attribute:
                attr = new FloatAttribute(name, count, content);
                break;
            case string_attribute:
                attr = new StringAttribute(name, count, content);
                break;
            default:
                throw new RuntimeException("unrecognized attribute : " + type);

        }

        return attr;

    }

    public static class IntegerAttribute extends AFNIAttribute {

        List<Integer> data = new ArrayList<Integer>();

        public IntegerAttribute(String name, int count, String content) {
            super(name, count, content);
            parseContent();
        }

        public final void parseContent() {
            StringTokenizer tokenizer = new StringTokenizer(content, " ");
            while (tokenizer.hasMoreTokens()) {
                data.add(Integer.parseInt(tokenizer.nextToken()));
            }
        }

        public AFNI_ATTRIBUTE_TYPE getType() {
            return AFNI_ATTRIBUTE_TYPE.integer_attribute;
        }

        public List<Integer> getData() {
            return data;
        }


        public String toString() {
            String ret = super.toString();
            StringBuffer sb = new StringBuffer();
            sb.append(ret);
           
            sb.append(Arrays.toString(data.toArray()));
            return sb.toString();
        }
    }

    public static class StringAttribute extends AFNIAttribute {

        List<String> data = new ArrayList<String>();

        public StringAttribute(String name, int count, String content) {
            super(name, count, content);
            parseContent();
        }

        public final void parseContent() {
            StringTokenizer tokenizer = new StringTokenizer(content.substring(1, content.length()), "~;");

            while (tokenizer.hasMoreTokens()) {
                data.add(tokenizer.nextToken());
            }
        }

        public AFNI_ATTRIBUTE_TYPE getType() {
            return AFNI_ATTRIBUTE_TYPE.string_attribute;
        }

        public List<String> getData() {
            return data;
        }

        public String toString() {
            String ret = super.toString();
            StringBuffer sb = new StringBuffer();
            sb.append(ret);

            sb.append(Arrays.toString(data.toArray()));
            return sb.toString();
        }
    }

    public static class FloatAttribute extends AFNIAttribute {

        List<Float> data = new ArrayList<Float>();

        public FloatAttribute(String name, int count, String content) {
            super(name, count, content);
            parseContent();
        }

        public final void parseContent() {
            StringTokenizer tokenizer = new StringTokenizer(content, " ");
            while (tokenizer.hasMoreTokens()) {
                data.add(Float.parseFloat(tokenizer.nextToken()));
            }
        }

        public AFNI_ATTRIBUTE_TYPE getType() {
            return AFNI_ATTRIBUTE_TYPE.float_attribute;
        }

        public List<Float> getData() {
            return data;
        }

        public String toString() {
            String ret = super.toString();
            StringBuffer sb = new StringBuffer();
            sb.append(ret);

            sb.append(Arrays.toString(data.toArray()));
            return sb.toString();
        }
    }


}

