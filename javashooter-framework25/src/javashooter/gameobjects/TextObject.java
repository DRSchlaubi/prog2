package javashooter.gameobjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.LinkedList;

import javashooter.collider.*;
import javashooter.controller.ObjectController;
import javashooter.playground.Playground;
import javashooter.rendering.*;

/**
 * Convenience Class subclassing {@link GameObject}, pre-creationg a subclass of {@link Artist} that
 * draws a text. The javashooter.controller is left undefined, the javashooter.collider as well. However, a single call to
 * the overwritten method {@link #generateColliders} will in fact generate a rect javashooter.collider of just
 * the right size for the text.
 *
 */
public class TextObject extends GameObject {

  private String text = null;
  private int size = 1;
  private Color textColor = null;
  protected double rx, ry;

  public String getText() {
    return this.text;
  }  
  
  public Color getTextColor() {
	  return this.textColor ;
  }
  
  
  public void setTextColor(Color c) {
	  this.textColor = c ;
	  ((TextArtist)(this.artist)).setTextColor(c) ;
  }

  /**
   * Constructor.
   * 
   * @param id object name
   * @param javashooter.playground containing {@link Playground} instance
   * @param x positionx
   * @param y positiony
   * @param vx speedx
   * @param vy speedy
   * @param size font size in Pxiels
   * @param text String to be displayed
   * @param textColor text color, see java.awt.Color
   */
  public TextObject(String id, Playground playground, double x, double y, double vx, double vy,
      String text, int size, Color textColor) {
    super(id, playground, x, y, vx, vy);

    this.artist = new TextArtist(this, text, size, textColor);
    this.textColor = textColor ;

    //this.setColliders(new LinkedList<Collider>());

  }
  
  
  public void setText(String s) {
    this.text = s ;
    ((TextArtist)this.artist).setText(s) ;
  }

  public TextObject generateColliders() {
    // damit man auf getTextWidth/Height zugreifen kann! this.artist ist vom Basistyp artist und hat
    // das nicht..
    TextArtist kruecke = (TextArtist) (this.artist);

    
    addCollider(new RectCollider("rect", this, kruecke.getTextWidth(), kruecke.getTextHeight()));
    return this ;
  }


}
