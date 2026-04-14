package javashooter.gameobjects;

import java.awt.Graphics2D;
import java.lang.Math;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.io.File;

import javashooter.collider.CircleCollider;
import javashooter.collider.Collider;
import javashooter.collider.RectCollider;
import javashooter.controller.ObjectController;
import javashooter.playground.Animation;
import javashooter.playground.Playground;
import javashooter.rendering.*;
import javax.imageio.ImageIO;


/**
 * Convenience Class subclassing {@link GameObject}, pre-creationg a subclass of {@link Artist} that
 * draws a bitmap animation described by the {@link Animation} class. The javashooter.controller is left
 * undefined, the javashooter.collider as well. A cll to gneerateCalliders will put in place a correct
 * rectangular javashooter.collider.
 *
 */

public class BitmapObject extends GameObject {

  protected BitmapArtist imgArtist;

  /**
   * auto-generates javashooter.collider according to box width/height
   * 
   * @return this
   */
  public GameObject generateColliders() {
    LinkedList<Collider> cols = new LinkedList<Collider>();
    // System.out.println("Created enelmy "+ this.animArtist.getW()+" "+this.animArtist.getH()) ;
    double w = this.imgArtist.getW();
    double h = this.imgArtist.getH();
    this.addCollider(new RectCollider("RectColl_" + this.id, this, w, h));

    return this;
  }


  /**
   * Constructor.
   * 
   * @param id object name
   * @param pg Playground instance decribing level
   * @param x posx
   * @param y posy
   * @param vx speedx
   * @param vy speedy
   * @param scale scale the animation by this factor
   * @param anim Animation instance into which avalid animation has been loaded.
   * @param t0 game time at construction of the object. Needed to control replay.
   * @param abspielmodus forward|backwards|loop are options here
   */
  public BitmapObject(String id, Playground pg, double x, double y, double vx, double vy,
      double scale, String uri) {
    super(id, pg, x, y, vx, vy); // Konstruktor-Aufruf GameObject
    
    BufferedImage img = null ;
    try {
      img = ImageIO.read(new File(uri));
    } catch (Exception e)
    {
    }
    this.artist = new BitmapArtist(this, img, scale);
    this.imgArtist = (BitmapArtist) (this.artist);

  }


}
