package javashooter.rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import javashooter.gameobjects.GameObject;

/**
 * Rendering an object as a text of a specified color, size and font.
 */
public class TextArtist extends Artist {

  private String text = null;
  private int size = 1;
  private Color textColor = null;
  protected double textWidth, textHeight;
  Font serifFont = null;

  public TextArtist(GameObject go, String text, int size, Color textColor) {
    super(go);
    this.size = size;
    this.text = text;

    this.serifFont = new Font("Serif", Font.PLAIN, size);
    FontRenderContext frc = new FontRenderContext(null, false, false);
    this.textWidth = (int) (serifFont.getStringBounds(text, frc).getWidth());
    this.textHeight = (int) (serifFont.getStringBounds(text, frc).getHeight());

    this.textColor = textColor;
  }


  public String getText() {
    return this.text;
  }
  
  public void setText(String s) {
    this.text = s;
  }
  


  public double getTextWidth() {
    return textWidth;
  }


  public void setTextWidth(double textWidth) {
    this.textWidth = textWidth;
  }



  public double getTextHeight() {
    return textHeight;
  }
  
  
  public Color getTextColor() {
	  return this.textColor ;
  }
  
  
  public void setTextColor(Color c) {
	  this.textColor = c;
  }


  public void setTextHeight(double textHeight) {
    this.textHeight = textHeight;
  }

  /**
   * Draw the text.
   * 
   * @param g The Swing graphics context.
   */
  @Override
  public void draw(Graphics2D g) {
    AttributedString as = new AttributedString(this.text);
    as.addAttribute(TextAttribute.FONT, this.serifFont);
    as.addAttribute(TextAttribute.FOREGROUND, this.textColor);

    g.drawString(as.getIterator(), (int) Math.round( - this.textWidth / 2.),
        (int) Math.round(+ this.textHeight / 2.));
  }

}
