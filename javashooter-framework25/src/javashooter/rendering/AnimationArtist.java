package javashooter.rendering;

import javashooter.gameobjects.GameObject;
import javashooter.playground.Animation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Rendering an object by displaying a predefined {@link Animation}, i.e., a sequence of bitmaps.
 * laxback can be forward(once), backward(once) and looping.
 */
public class AnimationArtist extends Artist {

  protected LinkedList<BufferedImage> imageArray;
  protected LinkedList<Double> showtime;
  protected double t0;

  protected int loopFrame = 0;

  protected double w = 0, h = 0, scale = 0;
  protected String abspielmodus = "";

  public AnimationArtist(GameObject go, Animation anim, double t0, String abspielmodus,
      double scale) {
    super(go);

    this.imageArray = anim.getImageList();
    this.showtime = anim.getShowtimeList();
    this.t0 = t0;
    //System.out.println("AnimationArtist start = ");
    this.loopFrame = 0;

    for (int i = 0; i < imageArray.size(); i++) {
      //System.out.println("ANIMRAWWH=" + imageArray.get(i).getWidth());
      this.w = Math.max(this.w, imageArray.get(i).getWidth());
      this.h = Math.max(imageArray.get(i).getHeight(), this.h);
    }

    //System.out.println("AnimationArtist RAW WH = " + this.w + "/" + this.h);

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
    double i2 = this.getGameTime();

    double elapsedTime = i2 - t0;

    // Differenz von sec in Millisekunden umwandeln
    double mseconds = (double) elapsedTime;
    // System.out.println("showtime= "+showtime.get(loopFrame)+" elapsed= "+elapsedTime+" i2= "+i2+"
    // zeit= "+zeit)
    // ;

    if (abspielmodus.equals("loop")) {
      if (mseconds >= showtime.get(loopFrame)) {
        loopFrame++;
        if (loopFrame >= imageArray.size())
          loopFrame = 0;
        t0 = i2;
      }
    }

    if (abspielmodus.equals("vorwaerts")) {
      if (mseconds >= showtime.get(loopFrame) && loopFrame < imageArray.size() - 1) {
        loopFrame++;
        t0 = i2;
      }
    }

    if (abspielmodus.equals("rueckwaerts")) {
      if (mseconds >= showtime.get(loopFrame) && loopFrame > 1) {
        loopFrame--;
        t0 = i2;
      }
    }

    g.drawImage(imageArray.get(loopFrame), (int) Math.round(this.getX()*0 - w / 2.),
        (int) Math.round(this.getY()*0 - h / 2.), (int) w, (int) h, null);

  }

}
