package spaceinvadersProject.gameobjects;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

import javashooter.collider.*;
import javashooter.controller.ObjectController;
import javashooter.gameobjects.GameObject;
import javashooter.playground.Playground;
import javashooter.rendering.*;

/**
 * Convenience Class subclassing {@link GameObject}, pre-creationg a subclass of {@link Artist} that
 * draws a circular white object. The javashooter.controller is left undefined, the javashooter.collider as well. However, a
 * single call to the overwritten method {@link #generateColliders} will in fact generate a circle
 * javashooter.collider of just the right size.
 *
 */
public class EgoObject extends GameObject {

  double egoRad = 0;

  /**
   * Constructor.
   * 
   * @param id object name
   * @param pg containing {@link Playground} instance
   * @param x positionx
   * @param y positiony
   * @param vx speedx
   * @param vy speedy
   * @param egoRad circle radius
   */

  public EgoObject(String id, Playground pg, double x, double y, double vx, double vy,
      double egoRad) {
    super(id, pg, x, y, vx, vy);
    this.egoRad = egoRad;
    this.artist = new CircleArtist(this, egoRad, Color.WHITE);
  }

  public GameObject generateColliders() {
    CircleCollider coll = new CircleCollider("coll", this, this.egoRad);
    this.addCollider(coll);
    return this;
  }

}
