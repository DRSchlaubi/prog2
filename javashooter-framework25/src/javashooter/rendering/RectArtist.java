package javashooter.rendering;

import java.awt.Color;
import java.awt.Graphics2D;

import javashooter.gameobjects.GameObject;

/**
 * Rendering an object as a filled rectangle of a specified color and size.
 */
public class RectArtist extends Artist {

  protected Color color;
  protected double width, height;

  public RectArtist(GameObject go, double width, double height, Color color) {
    super(go);
    this.color = color;
    this.width = width;
    this.height = height;
  }

  public void setColor(Color c) {
    this.color = c; 
  }

  public void setWidth(double w) {
    this.width = w ;
  }


  public double getW() {
    return this.width ;
  }


  public double getH() {
    return this.height ;
  }

  /**
   * Draw the rect.
   * 
   * @param g The Swing graphics context.
   */
  @Override
  public void draw(Graphics2D g) {
    g.setColor(this.color);

    g.fillRect((int) (- this.width / 2.), (int) ( - this.height / 2.),
        (int) this.width, (int) this.height);
    //g.dispose();

  }

}
