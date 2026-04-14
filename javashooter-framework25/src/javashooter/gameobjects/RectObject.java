package javashooter.gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.LinkedList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javashooter.collider.Collider;
import javashooter.collider.RectCollider;
import javashooter.controller.ObjectController;
import javashooter.playground.Music;
import javashooter.playground.Playground;
import javashooter.rendering.*;

/**
 * Convenience Class subclassing {@link GameObject}, pre-creationg a subclass of {@link Artist} that
 * draws a rectangular object. The javashooter.controller is left undefined, the javashooter.collider as well. However, a
 * single call to the overwritten method {@link #generateColliders} will in fact generate a
 * rectangular javashooter.collider of just the right size.
 *
 */
public class RectObject extends GameObject {

  protected double width, height;

  /**
   * Constructor.
   * 
   * @param id object name
   * @param pg containing {@link Playground} instance
   * @param x positionx
   * @param y positiony
   * @param vx speedx
   * @param vy speedy
   * @param width rectangle width
   * @param height rectangle height
   * @param color rectangle color, check class jawa.awt.Color
   */
  public RectObject(String id, Playground pg, double x, double y, double vx, double vy,
      double width, double height, Color color) {
    super(id, pg, x, y, vx, vy);

    this.width = width;
    this.height = height;

    this.artist = new RectArtist(this, width, height, color);
  }


  public RectObject generateColliders() {
    this.addCollider(new RectCollider("rectcollider_" + id, this, this.width, this.height));
    return this;
  }
  
  public double getW() {
    return this.width ;
  }
  
  public double getH() {
    return this.height ;
  }
  
}
