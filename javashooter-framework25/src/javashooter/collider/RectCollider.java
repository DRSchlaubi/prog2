package javashooter.collider;

import java.awt.Color;

import javashooter.controller.ObjectController;
import javashooter.gameobjects.*;
import javashooter.playground.Playground;

import java.awt.Graphics2D;
import java.util.LinkedList;

/**
 * Collider for a rectangular object.
 * 
 * @author geppe
 *
 */

public class RectCollider extends Collider {

  // double x;
  // double y;
  // double vx;
  // double vy;
  double w, h;
  double [] edgesIntrinsic = null ;
  double [] edges = null ; 
  

  private Color color = Color.WHITE;

  public RectCollider(String id, GameObject o, double w, double h) {
    super(id, o);
    this.w = w;
    this.h = h;
    /** Edge coords in intrinsic CS of rect collider, relative to rect center */
    this.edgesIntrinsic = new double[] {-w/2., -h/2., w/2., -h/2., w/2., h/2., -w/2., h/2. } ;
    /** Edge coordinates in global CS, but relative to rect center */
    this.edges = new double[] {-w/2., -h/2., w/2., -h/2., w/2., h/2., -w/2., h/2. } ;
    this.setPhi(0) ;
  }
  
  
  /** Returns a an array of 8 values in global CS:
   * 
   * @return 8-elem Array in format{ ulx, uly, urx, ury, lrx, lry, llx, lly }
   */
  public double[] getEdges() {
    return this.edges ;
  }
  
  
  @Override
  public void setPhi(double phi) {
    
    // mis-use this.trafo for transforming intrisic edge coords to global CS
    // every time phi as changed
    this.trafo.setToIdentity() ;
    this.trafo.setToRotation(-phi) ;
    this.trafo.transform(this.edgesIntrinsic, 0, this.edges, 0, 4) ;
    /*for(int i=0; i< 8; i+= 2) {
      this.edges[i] += getX() ;
      this.edges[i+1] += getY() ;
    }*/
    
    super.setPhi(phi);
  }

  public String toString() {
    return " " + w + " " + h + " ";
  }
  
    
  /** Test whether a point in global CS is inside the rect.
   * Done by trafo to intrinsic CS where the test is simple. 
   * @param x pointx
   * @param y pointy
   * @return true or false
   */
  public boolean pointIsInside(double x, double y) {
    //System.out.println("Querying: ("+x+","+y+") inside "+this.getX()+"/"+this.getY()+"/"+this.w+"/"+this.h); 
    double[] dst = new double[] {2.,2.} ;
    this.trafo.transform(new double[] {x-getX(), y-getY()}, 0, dst, 0, 1) ;
    double ix = dst[0] ;
    double iy = dst[1] ;
    
    if (ix < -this.w/2.) return false ;
    if (ix > this.w/2.) return false ;
    if (iy < -this.h/2.) return false ;
    if (iy > this.h/2.) return false ;        
    return true ;
    
  }

  public boolean checkCollisionRectRect(Collider other, double dx, double dy) {
    RectCollider r1 = this;
    RectCollider r2 = (RectCollider) other;
    
    //System.out.println("COLLRR:"+this.getX()+"/"+getY()+"---"+other.getX()+"/"+other.getY()) ;
    
    // simple test: if any edge of r1 is in r2, 
    // or any edge of r2 in r1 --> collision!!
    for(int pt = 0; pt < 8; pt+=2) {
      double[]otherEdges = r2.getEdges();
      if (r2.pointIsInside(this.edges[pt]+this.getX(), this.edges[pt+1]+this.getY())) return true ;
      if (r1.pointIsInside(otherEdges[pt]+other.getX(), otherEdges[pt+1]+other.getY())) return true ;
    }
    
    return false ; 
  }

  public boolean checkCollisionRectCirc(Collider other, double dx, double dy) {
    
    // transform difference vector between middle points to the intrinsic coordinate frame of "this".
    double[] dst = new double[] {2.,2.} ;
    this.trafo.transform(new double[] {dx, dy}, 0, dst, 0, 1) ;
    
    double _dx = dst[0] ;
    double _dy = dst[1] ;
    
    RectCollider r = this;
    CircleCollider c = (CircleCollider) (other);
    
    double z = 0.5 ;
    //if(this.gameobject.getPhi() != 0.0)
    // System.out.println("diff;: "+dx+"/"+dy) ;    
        
    double circleDistX = Math.abs(_dx);
    double circleDistY = Math.abs(_dy);

    // System.out.println("c.x:"+c.x+" "+"c.y:"+c.y+" "+"c.r:"+c.r+" "+"r.x:"+r.x+" "+"r.y:"+r.y+"
    // "+"r.w:"+r.w+" "+"r.h:"+r.h+" "+"circleDistX:"+circleDistX+" "+"circleDistY:"+circleDistY);

    if (circleDistX > (r.w / 2 + c.r*z))
      return false;
    if (circleDistY > (r.h / 2 + c.r*z))
      return false;

    if (circleDistX <= (r.w / 2)) {
      // System.out.println("Kollision Rechteck mit Kreis");
      return true;
    }
    if (circleDistY <= (r.h / 2)) {
      // System.out.println("Kollision Rechteck mit Kreis2");
      return true;
    }

    double cornerDistSqr = Math.pow(circleDistX - r.w / 2, 2) + Math.pow(circleDistY - r.h / 2, 2); // Satz
                                                                                                    // des                                                                                                    // Pythagoras
    return (cornerDistSqr <= c.r * c.r *z*z); // falls true zurueckgegeben: Kollision
    
  }


  /**
   * example of how to implement own colliders. This one check for collisions with instances of
   * itself, and of {@link CircleCollider}. For this, the code tries to cast the parameter other to
   * both classes. If it is possible, collision is evaluated. If not, an exception is raised to
   * signal that the javashooter.collider type cannot be handled.
   * 
   * @param other subclass of {@link Collider}
   * @return false or true
   * @throws RuntimeException if other cannot be handled
   * 
   */
  @Override
  public boolean collidesWith(Collider other) {
    double dx = other.getX() - (this.getX()) ;
    double dy = other.getY() - (this.getY()) ;
    

    // rect circ
    try {
      return checkCollisionRectCirc(other, dx, dy);
    } catch (Exception e) {
      // do nothing
    }

    // rect rect
    try {
      return checkCollisionRectRect(other, dx, dy);
    } catch (Exception e) {
      // do nothing
    }

    try {
      return other.collidesWith(this);
    } catch (Exception e) {
      // do nothing
    }

    throw new RuntimeException("Collider type not implemented!");
  }
  
    
  /** Computes intersection point in intrinsic coordinates. Approximate other object
   * by itsd approach vecotr, ie the difference vector between two object centers.
   * We compute where, in intrinsic CS, the line defined by that vector intersects the object
   * first.
   * @param objCenter
   * @param dir
   * @param cp
   */
  public void computeContactPoint(double[] vector2CenterIntrinsic, double[] cp) {
    
    // now check all four sides of the rectangle for intersections 
    double tanAlpha = vector2CenterIntrinsic[0] / vector2CenterIntrinsic[1] ;   
    double xTop = this.h/2 * tanAlpha ; 
    
    /** If intersection is on top or bottom line */
    if (Math.abs(xTop) <= this.w / 2) {
      cp[0] = (vector2CenterIntrinsic[1] >= 0.)? xTop : -xTop ;
      cp[1] = this.h / 2 ;      
    } else { /** tanalpha cannot be 0 since otherwise object would be directly in front */
      double yRight = this.w/2 / tanAlpha ;
      cp[0] = this.w/2 ;
      cp[1] = (vector2CenterIntrinsic[0] >= 0.)? yRight : -yRight ;
    }        
  }
  
  

}
