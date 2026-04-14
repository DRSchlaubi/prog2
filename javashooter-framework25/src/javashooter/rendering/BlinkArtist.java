package javashooter.rendering;

import java.awt.Color ;
import java.awt.Graphics2D;

import javashooter.gameobjects.GameObject;

public class BlinkArtist extends RectArtist {
	protected double t0 = -1. ;
	protected boolean blink = false;
	
	public BlinkArtist(GameObject go, double width, double height, Color color) {
		super(go, width, height, color) ;
	}

	@Override
	  public void draw(Graphics2D g) {
	  
  	    if ((this.gameObject.getGameTime()-this.t0 >= 0.2)) {
  	      if (this.blink == false) {
  	  	    this.t0 = this.gameObject.getGameTime() ;
	  	    this.color = Color.BLUE ;
		    this.blink = true ;
  	      } else { 
    	      this.t0 = this.gameObject.getGameTime() ;
    		  this.color = Color.RED ;
  	    	  this.blink = false ;
  	      }
  	    	
  	    }
  	    super.draw(g) ;
	}
}
