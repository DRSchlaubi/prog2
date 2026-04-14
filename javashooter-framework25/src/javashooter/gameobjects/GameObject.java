package javashooter.gameobjects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.lang.reflect.*;

import javashooter.collider.Collider;
import javashooter.controller.ObjectController;
import javashooter.playground.Playground;
import javashooter.rendering.*;

/**
 * The class {@link GameObject} represents an object appearing in a level of the game described by
 * an instance of the class {@link javashooter.playground.Playground}. An instance of
 * {@link GameObject} has the following properties:
 * <ul>
 * <li>a name that is unique within a certain {@link Playground}
 * <li>2D screen position x and y in pixels
 * <li>2D speed x and y in pixels/s
 * <li>a reference to the {@link javashooter.playground.Playground} object it belongs to
 * </ul>
 * <br>
 * In order to allow a flexible composition of object properties, an instance of {@link GameObject}
 * is an aggregation of up to three instances of other classes. To put it another way, each instance
 * has attributes that are references to:
 * <ul>
 * <li>a subclass of {@link javashooter.controller.ObjectController} that handles the movement/input
 * logic of the object<br>
 * <li>a subclass of {@link javashooter.collider.Collider} that handles the collision detection of
 * the object<br>
 * <li>a subclass of {@link javashooter.rendering.Artist} that draws the object<br>
 * </ul>
 * When a GameObject is constructed using <strong>new</strong>, all of these references are set to
 * <strong>null</strong>. In this state, the object does nothing, collides with nothing and is not
 * visible because it is not drawn. You can use calls to the methods {@link #setColliders},
 * {@link #addArtist}, {@link #addCollider} and {@link #setController} to set these references
 * later, thus giving to an object a behavior, the ability to collide with others, and a visual
 * appearance. <br>
 * Normally, it is not necessary to create subclasses of GameObject since the composition of
 * different behavior, collision and appearance properties is governed by subclassing the classes
 * {@link javashooter.controller.ObjectController}, {@link javashooter.collider.Collider} and
 * {@link javashooter.rendering.Artist} and then adding them to the GameObject instance. <br>
 * However, some subclasses of GameObject define convenience constructors where you can directly
 * provide these references to have more readable code. Examples are: {@link AnimatedGameobject},
 * {@link TextObject}, {@link RectObject} and {@link CircleObject}.
 * 
 * For now, only colliders that are centered on the object itself are supported.
 */
public class GameObject {

  public static final int RADIUS = 0;
  public static final int RECTANGLE = 1;
  public static final int MASK = 2;

  // delegation instances
  protected Artist artist = null;
  private ObjectController controller = null;
  public LinkedList<Collider> scol;

  public String id = null;
  protected double x = 0;
  protected double vx = 0;
  protected double y = 0;
  protected double vy = 0;
  protected double ax = 0;
  protected double ay = 0;
  protected double phi = 0;
  protected double omega = 0;
  /** Contact point from last collisison */
  protected double[] cp = new double[2] ;
  /** Approach vector for last object that gave a collision */
  protected double[] av = new double[2] ;
  /** Collider that had last collision */
  protected Collider collCollider ;
  
  // protected BufferedImage mask = null; // TODO!!
  protected boolean active = true;
  // public int collisionMode = GameObject.RADIUS;
  protected Playground playground = null;

  protected int z = 0;

  public int get() {
    return z;
  }


  public GameObject setZ(int z) {
    this.z = z;
    return this;
  }
  
  public GameObject setOmega(double omega) {
    this.omega = omega;
    return this;
  }
  
  
  public double getOmega() {
    return this.omega ;
  }
  

  public String getName() {
    return this.id;
  }

  public double getPhi() {
    return this.phi;
  }
  
  
  public GameObject setPhi(double phi) {
    this.phi = phi ;
    for(Collider c : scol) {
      c.setPhi(phi);
    }
    return this ;
  }


  /**
   * convenience constructor.
   * 
   * @param id Object name, must be unique
   * @param javashooter.playground Level this object should be part of
   * @param javashooter.controller Can be null if no javashooter.controller is needed
   * @param x X position
   * @param y Y poition
   * @param vx X speed
   * @param vy Y speed
   */
  public GameObject(String id, Playground playground, ObjectController controller, double x,
      double y, double vx, double vy) {
    setX(x);
    setY(y);
    setVX(vx);
    setVY(vy);
    this.id = id;
    this.controller = controller;
    this.scol = new LinkedList<Collider>();
    if (controller != null) {
      this.controller.setObject(this);
      this.controller.setPlayground(playground);
    }
    this.setPlayground(playground);
  }


  /**
   * primary constructor. Intializes al aggregated instances to null, need to be set afterwards.
   * 
   * @param id Object name, must be unique
   * @param javashooter.playground Level this object should be part of
   * @param x X position
   * @param y Y poition
   * @param vx X speed
   * @param vy Y speed
   */
  public GameObject(String id, Playground playground, double x, double y, double vx, double vy) {
    setX(x);
    setY(y);
    setVX(vx);
    setVY(vy);
    this.id = id;
    this.controller = null;
    this.scol = new LinkedList<Collider>();
    this.setPlayground(playground);
  }


  /**
   * Can be overwritten in subclasses to generate specific colliders automatically. For example, a
   * {@link TextObject} knows its own shape so it can generate the necessary rectangular
   * javashooter.collider automatically.
   * 
   * @return The instance of GameObject that is modified (i.e., this)
   */
  public GameObject generateColliders() {
    return this;
  }

  public GameObject setController(ObjectController c) {
    this.controller = c;
    this.controller.setObject(this);
    this.controller.setPlayground(playground);
    return this;
  }

  /**
   * Sets a new object javashooter.controller. Can be done anytime at all.
   * 
   * @param javashooter.controller An instance of {@link ObjectController} or one of its subclasses.
   */
  public void setObjectController(ObjectController controller) {
    this.controller = controller;
  }

  /**
   * Returns ref 2 object javashooter.controller.
   * 
   * @return Reference to ObjectController instance.
   */
  public ObjectController getObjectController() {
    return this.controller;
  }


  /**
   * set the Artist reference of this GmeObject.
   * 
   * @param a This subclass of {@link Artist}
   * @return this
   */
  public GameObject addArtist(Artist a) {
    this.artist = a;
    return this;
  }

  public Artist getArtist() {
    return this.artist ;
  }

  /**
   * Add a javashooter.collider to the existing list of colliders. If it is null, create it and add
   * the javashooter.collider anyway.
   * 
   * @param c A subclass of {@link Collider}
   * @return this
   */
  public GameObject addCollider(Collider c) {
    c.setPhi(this.phi); 
    this.scol.add(c);
    return this;
  }

  // -------------

  public Playground getPlayground() {
    return playground;
  }

  public void setPlayground(Playground playground) {
    this.playground = playground;
  }

  /**
   * convenience method, delegated to the containing {@link Playground} instance. Flag is prefixed
   * with object name so it does not interfere with same flags in other objects.
   * 
   * @param flag Flag name
   * @param value Flag value
   */ /*
  public void setObjectFlag(String flag, Object value) {
    this.playground.setLevelFlag(this.id + "/" + flag, value);
  } */

  /**
   * convenience method, delegated to the containing {@link Playground} instance. Flag is prefixed
   * with object name.
   * 
   * @param flag Flag name
   * @return flag value, null if flag does not exist
   */
  /*
  public Object getObjectFlag(String flag) {
    return this.playground.getLevelFlag(this.id + "/" + flag);
  } */

  /**
   * convenience method, delegated to the containing {@link Playground} instance. Flag is prefixed
   * with object name.
   * 
   * @param flag Flag name
   * @param createValue If flag does not exist, create it with this value
   * @return The flag's value, is of type Object so it must be cast
   */
  /*
  public Object getOrCreateObjectFlag(String flag, Object createValue) {
    return this.playground.getOrCreateLevelFlag(this.id + "/" + flag, createValue);
  }*/


  /**
   * Called by the game engine, DO NOT CALL YOURSELF. Is used to detect collision of this with
   * another GameObject instance other. The task is delegated to the list of aggregated colliders.
   * If there is no aggregated javashooter.collider, no collisions are reported.
   * 
   * @param other, a GameObject procided by the game engine
   * @return true or false, depending on whether this an other collide, as determined by both their
   *         colliders
   */
  public boolean collisionDetection(GameObject other) {
    for (Collider c : this.scol) {
      // System.out.println(other.id);
      for (Collider o : other.scol) {
        if (c.collidesWith(o)) {
          this.computeApproachVector(c, o, this.av) ;
          c.computeContactPoint(this.av, this.cp);
          this.collCollider = c ;
          return true;
        }
      }
    }
    return false;
  }

  /** Computes tha approach vector */
  void computeApproachVector(Collider own, Collider other, double[] av) {
    double[] dir = new double[] {other.getX()-this.getX(), other.getY()-this.getY()} ;
    own.computeAproachVector(dir, this.av);
  }
  /** WHAT TO DO? Transform SPEED VECTOR to INTRINSIC CS. COMPUTE REFLECTED 
   *  Vector, potentially based on contact point. transform back.
   */
  
  
  public double[] getContactPoint() { 
    return this.cp ;
  }
  
  
  /** Convert a speed vector from the intrinsic CS of the collider that had the last coll
   * for this object, back to the global CS
   * @param speedVec 2-Vector of speed in global CS
   * @param dest sppedVec in intrinsic CS
   */
  public void convertSpeedVectorBack(double[] speedVec, double[] dest) {
    this.collCollider.convertDirectionBack(speedVec, dest);
  }
  
  /** Convert a speed vector to the CS of the collider that had the last coll
   * for this object.
   * @param speedVec 2-Vector of speed in global CS
   * @param dest sppedVec in intrinsic CS
   */  
  public void convertSpeedVector(double[] speedVec, double[] dest) {
    this.collCollider.convertDirection(speedVec, dest);
  }
  


  /**
   * Is called by the game engine once per time step, DO NOT CALL YOURSELF. Gives an object the
   * chance to react to the current game state, change its position and speed a.s.o, react to user
   * inputs, ... All that is done here is to delegate that task to the aggregated subclass of
   * {@link ObjectController}. If that ref is null, nothing happens.
   */
  public void updateObject() {
    if (this.controller != null) {
      controller.updateObject();
    }
  }

  public boolean isActive() {
    return active;
  }

  public GameObject setActive(boolean flag) {
    this.active = flag;
    return this;
  }

  /**
   * gets the screen X position.
   * 
   * @return double screen x position
   */
  public double getX() {
    return x;
  }

  /**
   * gets the screen Y position.
   * 
   * @return double screen Y position
   */
  public double getY() {
    return y;
  }

  /**
   * gets the screen X speed in pixels per frame.
   * 
   * @return double screen x speed
   */
  public double getVX() {
    return vx;
  }

  /**
   * gets the screen Y speed in pixels per frame.
   * 
   * @return double screen y speed
   */
  public double getVY() {
    return vy;
  }

  /**
   * set screen x position.
   * 
   * @param x double new position
   */
  public void setX(double x) {
    if (this.active == true) {
      this.x = x;
    }
  }

  /**
   * set screen y position.
   * 
   * @param y double new position
   */
  public void setY(double y) {
    if (this.active == true) {
      this.y = y;
    }
  }

  /**
   * set screen x speed.
   * 
   * @param vx double new x position
   */
  public void setVX(double vx) {
    if (this.active == true) {
      this.vx = vx;
    }
  }

  /**
   * set screen y speed in pix per frame.
   * 
   * @param vy double new y speed.
   */
  public void setVY(double vy) {
    if (this.active == true) {
      this.vy = vy;
    }
  }

  /**
   * return the unique object ID.
   * 
   * @return String unique object ID
   */
  public String getId() {
    return id;
  }


  public void setId(String s) {
    this.id = s;
  }


  /**
   * Delegate gameTime computation to the Playground that a GameObject is part of.
   * 
   * @return The game time in seconds
   */
  public double getGameTime() {
    return this.playground.getGameTime();
  }

  /**
   * Draws the object in its current state. Is called by the game engine, should NOT be called by
   * the user. All it does it to delegate the task to the aggregated subclass of {@link Artist}. If
   * this referene is null, nothing happens.
   * 
   * @param g Graphics2D object that has all the necessary drawing functionalities
   */
  public void draw(Graphics2D g) {
    if (this.artist != null) {
      Graphics2D gg = (Graphics2D) g.create();
      // translate to CS with 0/0 at object center, and rotated so object is upright
      gg.translate(this.getX(), this.getY());
      gg.rotate(-this.getPhi());
      
      this.artist.draw(gg);
    }
  }


  /**
   * Experimental, use at your own risk. Uses reflection to call a particular setter method in one
   * of the aggregated instances. Assuming that is exists of course. Duh.
   * 
   * @param comp The aggregated instance to use. For now, only "javashooter.controller" is
   *        supported.
   * @param property The property to be set. Assumes that an appropriate setter exists.
   * @param value Value to be set.
   */
  public void setComponentProperty(String comp, String property, Object value) {
    if (comp.equals("javashooter.controller")) {
      Class<? extends Object> clO = this.controller.getClass();
      for (Method m : clO.getMethods()) {
        if (m.getName().indexOf(property) != -1) {
          System.out.println("Method " + property + " found!!");
          try {
            m.invoke(this.getObjectController(), value);
          } catch (Exception e) {
          }
        }
      }

    }
  }
}
