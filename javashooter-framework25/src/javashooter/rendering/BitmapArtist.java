package javashooter.rendering;

import javashooter.gameobjects.GameObject;
import javashooter.playground.Animation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

    /**
     */
public class BitmapArtist extends Artist {

  protected BufferedImage img;
  protected double w = 0, h = 0, scale = 0;
  
  public BitmapArtist(GameObject go, BufferedImage img, double scale) {
    super(go);

    this.img = img;

    this.w = img.getWidth();
    this.h = img.getHeight();


    this.w *= scale;
    this.h *= scale;
  }

  public double getW() {
    return w;
  }

  public double getH() {
    return h;
  }

  /**
 * Draw the anmation, based on the stored {@link Animation} object and the replay mode.
 * 
 * @param g The Graphics context from Swing.
 */
  @Override
  public void draw(Graphics2D g) {

    g.drawImage(img, (int)(-w / 2.), (int)(-h / 2.), (int) w, (int) h, null);

  }

}

