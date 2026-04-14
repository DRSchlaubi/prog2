package javashooter.rendering;

import java.awt.Color;
import java.awt.Graphics2D;

import javashooter.gameobjects.GameObject;

/**
 * Rendering an object as a filled circle of a specified color and size.
 */
public class CircleArtist extends Artist {

  protected double egoRad;
  protected Color color;

  public CircleArtist(GameObject go) {
    super(go);
    this.egoRad = 15.0;
    // TODO Auto-generated constructor stub
  }

  public CircleArtist(GameObject go, double egoRad, Color color) {
    super(go);
    this.egoRad = egoRad;
    this.color = color;
  }

  /**
   * Draw the circle.
   * 
   * @param g The Swing graphics context.
   */
  @Override
  public void draw(Graphics2D g) {
    g.setColor(this.color);
    int posX = (int) (- egoRad);
    int posY = (int) (- egoRad);
    int rad = (int) (2 * egoRad);
    g.fillOval(posX, posY, rad, rad);
  }

}
