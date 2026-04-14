package javashooter.collider;

import java.awt.Graphics2D;
import java.util.LinkedList;

import javashooter.controller.ObjectController;
import javashooter.gameobjects.GameObject;
import javashooter.playground.Playground;

/**
 * Abstract base class for all javashooter.collider types. Colliders detect when one GameObject
 * intersects/collides with another. For this purpose, a javashooter.collider must be fitte to the shape of an
 * object. Each GameObject can have several colliders, since complex ahapes need to be approximated
 * by basic shapes like circle, and rectangle. These basic shapes are represented by
 * {@link CircleCollider} and {@link RectCollider}. <br>
 * New colliders can be added representing more complex ways that objects can collide. For this, you
 * subclass {@link Collider} and overwrite the method {@link #collidesWith}. To ensire consistency,
 * each new javashooter.collider must be able to handle all old javashooter.collider classes, but the old ones do not need
 * to be changed. To handle an old javashooter.collider class X, in {@link #collidesWith} you cast the argument
 * to class X. If this is not possible, you raise an exception. If it is possible, return true or
 * false depending on whether there is a collision. See {@link CircleCollider} and
 * {@link RectCollider} for examples.
 * 
 * A Collider observes the rotation of its parent object and maintains an affine trafo
 * that transforms external coordinates into the coordinate system of the parent object, 
 * in which the rotation angle is zero. This trafo is therefore a pure rotation and assumes
 * that the center position of the object has been subtracted already.
 * 
 * @author geppe
 *
 */
public abstract class Collider {


  public String id = null;

  protected GameObject gameobject = null;

  protected double dx = 0.;
  double dy = 0.;
  
  /** This transformation transforms TO the coordinate system of the collider.
   */
  java.awt.geom.AffineTransform trafo = new java.awt.geom.AffineTransform() ;

  /**
   * Constructor.
   * 
   * @param id unique name of javashooter.collider instance
   * @param o parent object, {@link GameObject} or subclass
   */
  public Collider(String id, GameObject o) {
    this.gameobject = o;

    this.id = id;
    this.trafo.setToIdentity();
  }


  public Collider setOffsets(double dx, double dy) {
    this.dx = dx;
    this.dy = dy;
    return this;
  }

  public String toString() {
    return "baseColl";
  }

  public double getX() {
    return this.gameobject.getX() + this.dx;
  }


  public double getY() {
    return this.gameobject.getY() + this.dy;
  }


  public String getId() {
    return id;
  }


  public void setObject(GameObject gameObject) {
    this.gameobject = gameObject;
  }
  
  
  /** Sets a new rotation angle. At the same time, 
   * recomputes the trao that transforms from the global CS 
   * to the intrinsic cs. 
   * @param phi Angle. Duh!
   */
  public void setPhi(double phi) {
    //System.out.println("!!!!!!!!ROT!!!"+phi) ;
    this.trafo.setToRotation(phi) ;    
  }
  
  /** convert a velocity/offset from global to intrnsic cs. No translation! */
  public void convertDirection(double []src, double[] dest) {
    this.trafo.transform(src, 0, dest, 0, 1) ;
  }
  
  
  /** Convert a velocity vector from intrinsic to global cs. No translation! */
  public void convertDirectionBack(double []src, double[] dest) {
  try {
    this.trafo.inverseTransform(src, 0, dest, 0, 1);
  } catch(Exception e) {
    System.out.println("This exception can never hapen bec rotations are always invertible!") ;      
  }
}
  
  
   
  public void computeAproachVector(double[] dir, double[] av) {
    /** This is the vector from object to other object center, in intrinsic coords */
    this.convertDirection(dir, av) ;
  }
  

  public abstract void computeContactPoint(double [] vector2CenterIntrinsic, double[] cp) ;
  
  
//  protected double convertX()

  /**
   * Collision detection with other Collider.
   * 
   * @param other Subclass of {@link Collider}
   * @return true or false, depending on whether there is an exception
   */
  abstract public boolean collidesWith(Collider other);


}
