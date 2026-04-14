package javashooter.controller;





import java.awt.Color;
import java.awt.event.KeyEvent;

import javashooter.controller.*;
import javashooter.gameobjects.*;
import javashooter.playground.*;

import java.util.*;
import java.awt.event.*;


/** This class controls the pacman based on used inputs. 
 * It extends the normal javashooter.controller in a) that is detects obstacles and reacts to illegal movements. 
 * b) paints the opening and cloisng pacman mouth looking into the right direction
 * Convention: all GameObjects that contain the string "border" are consider obstacles
 */
public class CollisionAware4WayController extends Simple4WayController  {
  int direction = 0 ;



  public CollisionAware4WayController(double egoSpeed) {
    super();
    this.setSpeed(egoSpeed) ;
  }



  /** Idea: execute a fake movement in the current direction and check for collisions. If there are any -->y illegal movement! 
   */
  @Override
  public boolean movementPossible(int reqState) {
	  
	// store old state
	double x = getX(), y = getY() ;
	double vx = getVX(), vy = getVY() ;
	
	// set speed corresponding to requested heading
	setVX(getDXs()[reqState]*getMovementSpeed()) ;
	setVY(getDYs()[reqState]*getMovementSpeed()) ;
	applySpeedVector() ;
	
    // check for collisions. we could do this here by simply looking up the pacman map
    boolean allowed = true ;
	LinkedList<GameObject> coll = pg().collectObjects("border", false) ;
	for (GameObject go : coll) {
	  if (this.gameObject.collisionDetection(go)) {
	    allowed = false; 
	    break ;
	  }
    }
    
    // restore old pos and speeds
    this.setX(x); this.setY(y) ;
    this.setVX(vx); this.setVY(vy) ;
    
    return allowed ;
  }


  public double getMovementSpeed() {
    return this.speed ;
  }
  
  
  
}
