package com.brainflow.graphics;
import java.awt.*;


public interface GraphicsVisitor {

  public void drawObject(Graphics g);
  public void flush();

} 