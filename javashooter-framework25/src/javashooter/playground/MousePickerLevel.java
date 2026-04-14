package javashooter.playground;

// import utilities.* ;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import javax.imageio.ImageIO;
import javashooter.gameutils.* ;

import javashooter.collider.CircleCollider;
import javashooter.collider.Collider;
import javashooter.collider.RectCollider;
import javashooter.controller.LevelController;
import javashooter.controller.LimitedTimeController;
import javashooter.controller.ObjectController;
import javashooter.gameobjects.AnimatedGameobject;
import javashooter.gameobjects.GameObject;
import javashooter.gameobjects.RectObject;
import javashooter.gameobjects.TextObject;

import java.util.Scanner;

import java.awt.event.*;


/**
 * Class that realizes all the game logic of a SpaceInvaders-type level. This is the class that you
 * inerit from if you want to create your own SpaceInvaders-tpe levels. Functions performed by this
 * class are:
 * <ul>
 * <li>initially set up the level, spawn all object etc., in method {@link #prepareLevel}
 * <li>define a game state machine, replay initial message and detect collisions in method
 * {@link #prepareLevel}
 * <li>create a lot of utility functions to redefine if you want to change a certain aspect of the
 * level
 * </ul>
 */
public abstract class MousePickerLevel extends Playground {


  protected boolean lost = false;
  protected boolean doneLevel = false;
  protected GameObject clickObject = null ;
  protected Collider clickCollider = null ;
  
  // set up flags that some objects rely on
  /*LevelFlag<String> userChoice = new LevelFlag<String>("user_choice", "") ;
  LevelFlag<String> gameStatus = new LevelFlag<String>("gameStatus", "initialized") ;
  LevelFlag<String> detailedStatus = new LevelFlag<String>("detailedStatus", "std") ;
  LevelFlag<Integer> mouseX = new LevelFlag<Integer>("mouseX", -1) ;
  LevelFlag<Integer> mouseY = new LevelFlag<Integer>("mouseY", -1) ;
  LevelFlag<Integer> mouseB = new LevelFlag<Integer>("mousebutton", -1) ;*/
  
  
  public MousePickerLevel() {
    super();

    // click object dient dazu festzustellen mit welchem Objekt des Playground des Mausklick kollidiert
    this.clickObject = new GameObject("__clickObject", null,  -1,-1,0,0) ;
    this.clickCollider = new RectCollider("__clickCollider", this.clickObject, 5.,5.) ;
    this.clickObject.addCollider(this.clickCollider) ;

  }


  /**
   * initially sets up the level. Not called by user, but called every time a layer is restarted
   * from scratch. So make sure that this is possible. Here, resources are loaded only once even if
   * method is called several times.
   * 
   * @param id String identifies level.
   */
  @Override
  public void prepareLevel(String id) {
    System.out.println("PREPARE");
    reset();
    //resetFlags(FLAGS_LEVEL);

  }



  /**
   * (re)draws the level but NOT the objects, they draw themselves. Called by the main game loop.
   * This method mainly draws the background and the scoreboard.
   * 
   * @param g2 Graphics2D object that can, and should be, draw on.
   */
  @Override
  public void redrawLevel(Graphics2D g2) {
    // Set anti-alias!
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    // Set anti-alias for text
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

    // fill background with black
    int[] x = {0, preferredSizeX(), preferredSizeX(), 0};
    int[] y = {0, 0, preferredSizeY(), preferredSizeY()};
    Polygon bg = new Polygon(x, y, 4);
    g2.setColor(Color.BLACK);
    g2.fill(bg);

  }

  /** Adds ego object and stars and displays startup message. Is called from applyGameLogic */
  protected void setupInitialState() {
    addObject(this.clickObject) ;

    // add level javashooter.controller for processing mouse clicks
    LevelController ctrl = new LevelController();
      GameObject dummyObject = new TextObject("dummyControlObj", this, 0,0, 0, 0,
        ".", 50, Color.RED).setController(ctrl);
      addObject(dummyObject) ;

  }


  protected abstract void createChoices() ;


  void checkCollisions() { /*
      
      Integer x = this.mouseX.get() ;
      Integer y = this.mouseY.get() ;
      Integer b = this.mouseB.get() ;

      if (b == null) return ;
      if (b == -1) return ;

      //System.out.println("Mouse="+x+" "+y) ;
     
      // bei Links-Klicks wird berstimmt auf welches GameObject geklickt wurde.
      if (b == MouseEvent.BUTTON1) {
        LinkedList<GameObject> options = this.collectObjects("option_", true);
        this.clickObject.setX(x) ;
        this.clickObject.setY(y) ;
        for (GameObject option : options ) {
          if (this.clickObject.collisionDetection(option)) {
            this.userChoice.set(option.getId()) ;
            //System.out.println("Selected "+option.getId()) ;
          }
        }
      }*/
      
  }


  /**
   * applies the logic of the level: For now, this is just about deleting shots that are leaving the
   * screen
   */
  @Override
  public void applyGameLogic() { /*
    double gameTime = this.getGameTime();

    if (gameStatus.get().equals("initialized") == true) {
      setupInitialState();
      createChoices() ;
      this.gameStatus.set("playing");

    } else if (gameStatus.get().equals("playing") == true) {
      this.checkCollisions() ;
      String uc = this.userChoice.get() ; 
      if (!uc.equals("")) {
        this.doneLevel = true ;
      }
    } 

*/
  }

  public boolean gameOver() {
    return lost;
  }

  public boolean levelFinished() {
    return doneLevel;
  }



}
