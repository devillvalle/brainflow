package com.brainflow.image.data;

import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 15, 2007
 * Time: 12:21:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class RGBAImage {


    private UByteImageData2D red;

    private UByteImageData2D green;

    private UByteImageData2D blue;

    private UByteImageData2D alpha;

    private IImageData2D source;

    public RGBAImage(IImageData2D source, UByteImageData2D red, UByteImageData2D green, UByteImageData2D blue, UByteImageData2D alpha) {
        if (!checkSpace(red.getImageSpace(),
                        green.getImageSpace(),
                        blue.getImageSpace(),
                        alpha.getImageSpace())) {
            throw new IllegalArgumentException("All image bands mut have equivalent image spaces");
        }

        this.source = source;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }


    public IImageData2D getSource() {
        return source;
    }

    private boolean checkSpace(IImageSpace... spaces) {
        for (int i=1; i<spaces.length; i++) {
            if (!spaces[i].equals(spaces[i-1])) {
                return false;
            }
        }

        return true;

    }

    public int getWidth() {
        return red.getImageSpace().getDimension(Axis.X_AXIS);
    }

    public int getHeight() {
        return red.getImageSpace().getDimension(Axis.Y_AXIS);
    }

    public RGBAImage(ImageSpace2D space) {
        this.red = new UByteImageData2D(space);
        this.green = new UByteImageData2D(space);
        this.blue = new UByteImageData2D(space);
        this.alpha = new UByteImageData2D(space);
    }

    public final byte getRed(int x, int y) {
        return red.get(x,y);
    }

    public final byte getGreen(int x, int y) {
        return green.get(x,y);
    }

    public final byte getBlue(int x, int y) {
        return blue.get(x,y);
    }

    public final byte getAlpha(int x, int y) {
        return alpha.get(x,y);
    }

    public UByteImageData2D getRed() {
        return red;
    }

    public UByteImageData2D getGreen() {
        return green;
    }

    public UByteImageData2D getBlue() {
        return blue;
    }

    public UByteImageData2D getAlpha() {
        return alpha;
    }

    public void setRed(UByteImageData2D _red) {
        if (!_red.getImageSpace().equals(red.getImageSpace())) {
            throw new IllegalArgumentException("incomaptible image space");

        }

        red = _red;
    }

     public void setGreen(UByteImageData2D _green) {
        if (!_green.getImageSpace().equals(green.getImageSpace())) {
            throw new IllegalArgumentException("incomaptible image space");
        }
    }

     public void setBlue(UByteImageData2D _blue) {
        if (!_blue.getImageSpace().equals(blue.getImageSpace())) {
            throw new IllegalArgumentException("incomaptible image space");
        }

         blue = _blue;
    }

    public void setAlpha(UByteImageData2D _alpha) {
        if (!_alpha.getImageSpace().equals(alpha.getImageSpace())) {
            throw new IllegalArgumentException("incomaptible image space");
        }

        alpha = _alpha;
    }



    
}