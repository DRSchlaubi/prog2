package javashooter.rendering;

import java.awt.Color;

import javashooter.collider.*;
import javashooter.gameobjects.GameObject;

import java.awt.Graphics2D;

/**
 * Base class for all Artists that draw {@link GameObject} instances. Contains many convenience
 * methods that are handled by the containing instance of {@link GameObject}, and the method draw
 * which you must overwrite.
 */
public abstract class Artist {
  protected GameObject gameObject;

  public Artist(GameObject go) {
    this.gameObject = go;
  }

  public double getX() {
    return this.gameObject.getX();
  }


  public double getY() {
    return this.gameObject.getY();
  }


  public double getVX() {
    return this.gameObject.getX();
  }

  public double getVY() {
    return this.gameObject.getX();
  }


  public double getGameTime() {
    return this.gameObject.getGameTime();
  }


  /**
   * Draw an instances of {@link GameObject}, overwrite this to get new visual appearance.
   * 
   * @param g The current Graphics2D context obtained from Swing.
   */
  public abstract void draw(Graphics2D g);

}
