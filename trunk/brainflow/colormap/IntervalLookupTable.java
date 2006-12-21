package com.brainflow.colormap;


import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 23, 2005
 * Time: 7:43:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class IntervalLookupTable<T extends Interval> {


    private List<T> intervalList = new LinkedList<T>();
    private final Comparator wi_comparator = new AbstractInterval.WithinIntervalComparator();
    private final Comparator i_comparator = new AbstractInterval.IntervalComparator();

    public IntervalLookupTable() {
        intervalList = new ArrayList<T>();
    }


    public void addInterval(T ival) {
        assert!intervalList.contains(ival) : "entry already exists for interval: " + ival;
        // Search for the non-existent item
        // System.out.println("searching for: " + ival);
        int index = Collections.binarySearch(intervalList, ival, i_comparator);

        // Add the non-existent item to the list
        if (index < 0) {
            //System.out.println("not found, adding to end: " + intervalList.size());
            intervalList.add(-index - 1, ival);
        } else {
            //System.out.println("found, somewhere in middle: " + index);
            intervalList.add(index, ival);
        }
    }

    public void addIntervalToEnd(T ival) {
        if (intervalList.size() == 0) {
            intervalList.add(ival);
            return;
        }

        assert ival.getMinimum() >= getLastInterval().getMaximum();
        intervalList.add(ival);

    }

    public void addIntervals(T[] intervals) {
        for (T i : intervals) {
            addInterval(i);
        }
    }

    public T getFirstInterval() {
        if (intervalList.size() > 0) {
            return intervalList.get(0);
        }

        return null;
    }

    public T getLastInterval() {
        if (intervalList.size() > 0) {
            return intervalList.get(intervalList.size() - 1);
        }

        return null;
    }

    public void removeFirstInterval() {
        if (!intervalList.isEmpty()) {
            intervalList.remove(0);
        }
    }

    public void removeLastInterval() {
        if (!intervalList.isEmpty()) {
            intervalList.remove(intervalList.size() - 1);
        }
    }

    public int getNumIntervals() {
        return intervalList.size();
    }

    public void insertInterval(int index, T interval) {
        intervalList.add(index, interval);

    }

    public void setInterval(int index, T interval) {
        intervalList.set(index, interval);
    }

    public int squeezeInterval(T interval) {
        return placeInterval(interval);
    }

    public void suture(int index) {
        System.out.println("suturing ...");
        intervalList = suture(index, intervalList);
    }

    private List<T> suture(int index, List<T> ilist) {
        if (index < 0 || index > (ilist.size() - 1)) {
            return ilist;
        }

        if (ilist.size() == 1) {
            // no suturing required or possible
            return ilist;
        } else if (index == 0) {
            T next = ilist.get(index + 1);
            T replace = ilist.get(index);
            if (Double.compare(replace.getMaximum(), next.getMinimum()) != 0) {

                next.setRange(replace.getMaximum(), next.getMinimum());
            }

        } else if (index == (ilist.size() - 1)) {

            T prev = ilist.get(index - 1);
            T replace = ilist.get(index);

            if (Double.compare(prev.getMaximum(), replace.getMinimum()) != 0) {
                prev.setRange(prev.getMinimum(), replace.getMinimum());
            }


        } else {
            T prev = ilist.get(index - 1);
            T replace = ilist.get(index);
            T next = ilist.get(index + 1);

            if (Double.compare(prev.getMaximum(), replace.getMinimum()) != 0) {
                prev.setRange(prev.getMinimum(), replace.getMinimum());
            }

            if (Double.compare(replace.getMaximum(), next.getMinimum()) != 0) {
                next.setRange(replace.getMaximum(), next.getMinimum());
            }

        }

        return suture(index + 1, ilist);

    }


    private int placeInterval(T interval) {
        List<T> newList = new ArrayList<T>();

        Iterator<T> iter = iterator();
        boolean placed = false;
        int replaceIndex = -1;

        while (iter.hasNext()) {
            T ival = iter.next();
            if (!interval.overlapsWith(ival)) {
                newList.add(ival);
            } else if (ival.equals(interval)) {
                replaceIndex = newList.size();
                newList.add(interval);
                placed = true;
            } else if (interval.containsInterval(ival) && !placed) {
                replaceIndex = newList.size();
                newList.add(interval);
                placed = true;
            } else if (interval.overlapsWith(ival) && !placed) {
                if (interval.getMinimum() == ival.getMinimum()) {
                    // intervals share a lower bound, just add the interval
                    replaceIndex = newList.size();
                    newList.add(interval);

                } else {
                    T truncated = (T) ival.truncate(interval.getMinimum());
                    newList.add(truncated);
                    replaceIndex = newList.size();
                    newList.add(interval);
                }
                
                placed = true;
            } else {

            }

        }

        newList = suture(replaceIndex, newList);
        intervalList = newList;

        assert replaceIndex != -1;
        return replaceIndex;


    }

    public T getInterval(int index) {
        return intervalList.get(index);
    }

    public Iterator<T> iterator() {
        return intervalList.iterator();
    }

    public T lookup(double val) {
        int index = 0;
        try {
            index = Collections.binarySearch(intervalList, val, wi_comparator);
            return intervalList.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {

            System.out.println("index = " + index);
            System.out.println("value = " + val);
            throw e;

        }
    }

    public double getMaximumValue() {
        if (intervalList.isEmpty()) return 0;
        Interval ival = intervalList.get(intervalList.size() - 1);
        if (ival != null) {
            return ival.getMaximum();
        }

        return 0;

    }

    public double getMinimumValue() {
        if (intervalList.isEmpty()) return 0;
        T ival = intervalList.get(0);
        if (ival != null) {
            return ival.getMinimum();
        }

        return 0;

    }

    public String toString() {
        Iterator<T> iter = intervalList.iterator();
        StringBuffer sb = new StringBuffer();

        while (iter.hasNext()) {
            T ival = iter.next();
            sb.append(ival);
            sb.append("  ");
        }

        return sb.toString();
    }


    public static void main(String[] args) {
        IntervalLookupTable itl = new IntervalLookupTable();


        int NINTERVALS = 20000;
        for (int i = NINTERVALS; i > 0; i--) {
            itl.addInterval(new ColorInterval(i - 1, i));
        }


        for (int i = 0; i < 64; i++) {
            double x = Math.random();
            double y = x * NINTERVALS;

        }

        for (int j = 0; j < 100; j++) {
            double begin = System.currentTimeMillis();

            for (int i = 0; i < 256 * 256; i++) {
                double x = Math.random();
                double y = x * NINTERVALS;
                Interval it = itl.lookup(y);
            }

            double end = System.currentTimeMillis();


            System.out.println("time was: " + (end - begin));
        }
    }


}
