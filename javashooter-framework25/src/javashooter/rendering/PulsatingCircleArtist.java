package javashooter.rendering;

import java.awt.Color ;
import java.awt.Graphics2D;

import javashooter.gameobjects.GameObject;

public class PulsatingCircleArtist extends CircleArtist {
	protected double r0 = -1. ;
	protected boolean blink = false;
	protected double red = 0.0 ;
	protected double f = 1.0 ;
	
	
	public PulsatingCircleArtist (GameObject go, double egoRad, Color color, 
			double reduction, double freq) {
		super(go, egoRad, color) ;
		this.red = reduction ;
		this.r0 = egoRad ;
		this.f = freq ;
	}

	@Override
	  public void draw(Graphics2D g) {
	  
  	    if ((this.gameObject.getGameTime() % (2*this.f)) > this.f) {
  	    	this.egoRad = this.r0 * red ;
  	    } else {
       	  this.egoRad = this.r0 ;  	    	
  	    }
  	    super.draw(g) ;
	}
}
