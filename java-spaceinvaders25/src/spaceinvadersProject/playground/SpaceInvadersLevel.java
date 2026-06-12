package spaceinvadersProject.playground;

import javashooter.controller.*;

import javashooter.gameobjects.AnimatedGameobject;
import javashooter.gameobjects.GameObject;
import javashooter.gameobjects.RectObject;
import javashooter.gameobjects.TextObject;
import javashooter.gameobjects.FallingStar;
import javashooter.gameutils.*;
import javashooter.playground.*;

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

import spaceinvadersProject.controller.*;
import spaceinvadersProject.gameobjects.EgoObject;

import java.util.Scanner;

/**
 * Class that realizes all the game logic of a SpaceInvaders-type level. This is the class that you
 * inherit from if you want to create your own SpaceInvaders-tpe levels. Functions performed by this
 * class are:
 * <ul>
 * <li>initially set up the level, spawn all object etc., in method {@link #prepareLevel}
 * <li>define a game state machine, replay initial message and detect collisions in method
 * {@link #prepareLevel}
 * <li>create a lot of utility functions to redefine if you want to change a certain aspect of the
 * level
 * </ul>
 */
public abstract class SpaceInvadersLevel extends Playground {

  public static final int LEVEL2STARS = 80;
  public static final double BONUS_DURATION = 1.;
  public static final double ENEMYSPEEDX = 120;
  public static final double ENEMYSPEEDY = 80;
  public static final double ENEMYSCALE = 1.0;
  public static final double ENEMYSHOTSPEED = 150;
  public static final int NRSHARDS = 50;
  public static final double EXPL_DURATION = 1.;
  public static final int NR_ENEMIES = 30;
  public static final int NR_COLLECT = 5;
  public static final Color EXPL_COLOR = Color.RED;
  public static final double SHARDSPEED = 400;
  public static final double STARSPEED = 200;
  public static final double STARTTEXTSPEED = 150;
  public static final double STARTPERIOD = 5.;
  public static final double DYING_INTERVAL = 2.0;
  public static final int CANVASX = 700;
  public static final int CANVASY = 700;
  public static final double SHOTSPEED = 350;
  public static final double EGOSPEED = 400;
  public static final double EGORAD = 15;
  public static final double LEVEL_INIT_TIME = 1.0;

  /** saveable state variables */
  protected boolean lost = false;
  protected boolean doneLevel = false;
  
 
  /** Resources */
  public File smash = null;
  public static File laser = null;
  double lastShot = -1 ;

  public BufferedImage[] alienImage = null;
  public double[] alienshowTime = null;

  public BufferedImage[] heartImage = null;
  public double[] heartshowTime = null;

  protected Animation enemyAnim = null;
  protected Animation heartAnim = null;


  public SpaceInvadersLevel() {
    super();
    // Initialisierung der Flags mit den Standardwerten
    setFlagAsInt("/points", 0, true);
    setFlagAsInt("/egoLives", 5, true);
    setFlagAsString("/levelStatus", "start", false);
    setFlagAsString("/detailedStatus", "std", false);
    setFlagAsDouble("/dying", -1.0, false);
    setFlagAsInt("/highscore", -1, true);
    setFlagAsInt("/egoShotCounter", 0, false);
    setFlagAsInt("/enemyShotCounter", 0, false);
    setFlagAsDouble("/t0", 0.0, false);
    setFlagAsDouble("/lastEgoShot", 0.0, false);
   
  }


  /* ******************************************************** */
  /*  LEVEL configuration methods, overwrite!
   * ********************************************************* */
  
  public String getName() {
    return "SpaceInvadersLevel" ;
  }
  
  
  public int preferredSizeX() {
    return CANVASX;
  }

  public int preferredSizeY() {
    return CANVASY;
  }


  public boolean resetRequested() {
    return false;
  }

  protected double calcEnemyShotProb() {
    return 0.1 * this.getTimestep();
  }

  protected double calcEnemySpeedX() {
    return ENEMYSPEEDX;
  }


  protected double calcEnemySpeedY() {
    return ENEMYSPEEDY;
  }

  protected int calcNrEnemies() {
    return NR_ENEMIES;
  }

  protected int calcNrCollect() {
    return NR_COLLECT;
  }


  protected double calcEgoSpeed() {
    return SpaceInvadersLevel.EGOSPEED ;
  }

  protected String getStartupMessage() {
    return "SpaceInvadersLevel!!!";
  }




  /* ******************************************************************* */
  /* Creation methods, overwrite them!! */
  /* **********************************************************************/

  /** Creates one alien and returns it, no addition t oplayground.
   * @param name String For registration in Playground
   * @param x_enemy double x-position
   * @param y_enemy double y-position
   * @param vx_enemy double x-speed
   * @param vy_enemy double y-speed
   * @param enemyController ObjectController Well, the javashooter.controller. Duh!
   * @param gameTime double gameTime in s
   */
  protected GameObject createSingleEnemy(String name, double x_enemy, double y_enemy,
      double vx_enemy, double vy_enemy, ObjectController enemyController, double gameTime) {

    GameObject tmp =
        new AnimatedGameobject(name, this, x_enemy, y_enemy, vx_enemy, vy_enemy, ENEMYSCALE,
            this.enemyAnim, gameTime, "loop").setController(enemyController).generateColliders();
    return tmp;
  }

  ObjectController createEnemyController() {
    return new EnemyController();
  }  


  protected GameObject createEnemyShotObject(GameObject parentObject, String name,
      ObjectController limitedTimeController) {
    GameObject to =
        new TextObject(name, this, parentObject.getX(), parentObject.getY(), 0, ENEMYSHOTSPEED, "I",
            20, Color.YELLOW).generateColliders().setController(limitedTimeController);
    /*
     * // auch denkbar! GameObject to = new RectObject(name, this, parentObject.getX(),
     * parentObject.getY(), 0, ENEMYSHOTSPEED, 4, 20,
     * Color.YELLOW).addController(limitedTimeController) ; to.generateColliders();
     */
    return to;
  }

  /** Creates an enemy shot, adds it to javashooter.playground and returns it.
   * @param e GameObject The enemy object for which the shot may be created or not
   * @return A reference to the created object, or null if none is created
   */
  protected GameObject createEnemyShot(GameObject e) {
    double gameTime = this.getGameTime();

    double PROB = calcEnemyShotProb();
    double diceThrow = Math.random();
    int nrEnemyShots = getFlagAsInt("/enemyShotCounter", false, 0) + 1;

    if (diceThrow < PROB) {
      LimitedTimeController limitedTimeController = new LimitedTimeController(gameTime, 5.);

      GameObject textObject =
          createEnemyShotObject(e, "enmyShot" + nrEnemyShots, limitedTimeController);

      addObject(textObject);
      setFlagAsInt("/enemyShotCounter", nrEnemyShots, false);
      return textObject ;
    }
    return null ;
  }

  public GameObject createEgoShot(String shotName, GameObject ego) {
    SimpleShotController simpleshot = new SimpleShotController();
    GameObject ss = new RectObject(shotName, this, ego.getX(), ego.getY(), 0,
        -1. * SpaceInvadersLevel.SHOTSPEED, 4, 12, Color.CYAN).setController(simpleshot);
    ss.generateColliders();
    return ss ;
  }


  protected GameObject createSingleCollect(String name) {
    double gameTime = this.getGameTime();
    double cspeedy = 20.;
    double x_collect = Math.random() * this.preferredSizeX();
    double y_collect = Math.random() * this.preferredSizeY() / 3;
    double vx_collect = 2 * (Math.random() - 0.5) * 0;
    double vy_collect = Math.random() * cspeedy;

    GameObject tmp = new AnimatedGameobject(name, this, x_collect, y_collect, vx_collect,
        vy_collect, 0.3, this.heartAnim, gameTime, "loop").generateColliders()
        .setController(new EnemyController());
    return tmp;
  }


  protected void createEnemies() {
    // create enemies
    double gameTime = this.getGameTime();
    double speedx = this.calcEnemySpeedX();
    double speedy = this.calcEnemySpeedY();
    for (int i = 0; i < this.calcNrEnemies(); i++) {
      double x_enemy = Math.random() * this.preferredSizeX();
      double y_enemy = Math.random() * this.preferredSizeY() / 3;
      double vx_enemy = 2 * (Math.random() - 0.5) * speedx;
      double vy_enemy = Math.random() * speedy;

      ObjectController enemyController = createEnemyController();
      GameObject enemy = createSingleEnemy("enemy" + i, x_enemy, y_enemy, vx_enemy, vy_enemy,
          enemyController, gameTime);
      addObject(enemy);
    }
    System.out.println("Created enemies"+calcNrEnemies());
  }

  protected void createCollectables() {
    double gameTime = this.getGameTime();
    // create collectables
    for (int i = 0; i < this.calcNrCollect(); i++) {


      GameObject collect = createSingleCollect("collect" + i);

      addObject(collect);
    }
  }

  protected GameObject createEgoObject() {
    // add ego to javashooter.playground at lower bottom
    //EgoController egoController = new CollisionAwareEgoController(this.EGORAD);
    CollisionAware4WayController egoController = new CollisionAware4WayController(this.calcEgoSpeed());
    egoController.setSpeed(this.calcEgoSpeed()) ;


    GameObject ego = new EgoObject("ego", this, 50, preferredSizeY() - 60, 0, 0, EGORAD)
        .setController(egoController).generateColliders();

    return ego ;
  }


  protected void createStars() {
    // add stars to javashooter.playground
    for (int i = 1; i <= LEVEL2STARS; i++) {
      FallingStarController fsc = new FallingStarController();

      GameObject star = new FallingStar("star" + i, this, Math.random() * preferredSizeX(),
          Math.random() * 15, 0.0, Math.random() * STARSPEED, Color.WHITE, 1.).setController(fsc);

      addObject(star);
    }
  }


  /** Helper method, creates explosion when ego or alien explode.    
   * Spawns a cloud of short-lived shards. 
   * @param gameTime
   * @param e
   * @param basename
   * @param interval
   * @param color
   */
  protected void createExplosion(double gameTime, GameObject e, String basename, double interval,
      Color color) {
    // 
    for (int i = 0; i < NRSHARDS; i++) {
      double vx = 2 * (Math.random() - 0.5) * SHARDSPEED + e.getVX();
      double vy = 2 * (Math.random() - 0.5) * SHARDSPEED + e.getVY();
      addObject(
          new FallingStar("shard" + gameTime + "/" + i, this, e.getX(), e.getY(), vx, vy, color, 2)
          .setController(new LimitedTimeController(gameTime, interval)));
    }
  }

  /*********************************************************** */
  /* EVENT METHODS */
  /* These are called automatically by the level logic. 
   * Just redefine them! */
  /* *********************************************************** */
  /** Default action executed if we shoot an enemy. 
   * 
   * @param e
   * @param shot
   */
  protected void actionIfEnemyIsHit(GameObject e, GameObject shot) {

    double gameTime = this.getGameTime();
    createExplosion(gameTime, e, "shard", DYING_INTERVAL, Color.RED);

    //Music.music(smash);

    // delete enemy
    deleteObject(e.getId());

    // delete shot
    deleteObject(shot.getId());

    // add to points counter
    //Integer pts = (Integer) getGlobalFlag("points");
    //setGlobalFlag("points", pts + 200);
    int pts = getFlagAsInt("/points", true, 0);
    setFlagAsInt("/points", pts + 200, true);

    // reduce global enemies counter
    //this.nrEnemies.set(this.nrEnemies.get()-1) ;

  }

  /** Default actiion executed when ego collides with collect object. 
   * @param collect
   * @param ego
   */
  protected void actionIfEgoCollidesWithCollect(GameObject collect, GameObject ego) {
    double gameTime = this.getGameTime();

    if (this.getObject("bonustext") == null) {

      // spawn a bonus points object
      double vx = 2 * (Math.random() - 0.5) * SHARDSPEED + collect.getVX();
      double vy = 2 * (Math.random() - 0.5) * SHARDSPEED + collect.getVY();


      LimitedTimeController bonusTextController =
          new LimitedTimeController(gameTime, SpaceInvadersLevel.EXPL_DURATION);
      GameObject bonusText = new TextObject("bonustext", this, collect.getX(), collect.getY(), vx,
          vy, "Extra life!!", 20, Color.YELLOW).setController(bonusTextController);

      addObject(bonusText);

      // delete collect
      deleteObject(collect.getId());

      // add to points counter
      int lives = getFlagAsInt("/egoLives", true, 0);
      setFlagAsInt("/egoLives", lives + 1, true);
    }

  }

  /** Default action that is executed whenever ego colliudes with an enemy.
   * standard behavior: set temporary text over ego and deduct points. This text lives only for a short time. 
   * While it lives, further collisions are ignored. 
   * Otherwise a single collision would result in a lot of deducted
   * points...
   */
  protected void actionIfEgoCollidesWithEnemy(GameObject enemy, GameObject ego) {
    double gameTime = this.getGameTime();
    GameObject auaObj = this.getObject("AUA-EGO");

    if (auaObj == null) {
      addObject(new TextObject("AUA-EGO", this, ego.getX(), ego.getY() - 20, ego.getVX(),
          ego.getVY(), "AUAA!!", 10, Color.RED)
          .setController(new LimitedTimeController(gameTime, BONUS_DURATION)));
      // deduct points
      int pts = getFlagAsInt("/points", true, 0);
      setFlagAsInt("/points", pts - 500, true);
      //Integer pts = (Integer) getGlobalFlag("points");
      //setGlobalFlag("points", pts - 500);
    }

  }

  protected void actionIfEgoObjectIsHit(GameObject eshot, GameObject ego) {
    // System.out.println("collision of " + eshot.getId() + " and " + ego.getId());

    double gameTime = this.getGameTime();
    this.deleteObject(eshot.getId());

    int lives = getFlagAsInt("/egoLives", true, 0);
    setFlagAsInt("/egoLives", lives - 1, true);

    if (getFlagAsInt("/egoLives", true, 0) <= 0) {
      lost = true;
      // TODO !! fix
      //HighscoreManager hsm = new HighscoreManager();
      //hsm.writeHSToFile(this.highScore.get());

    }

    LinkedList<GameObject> eshots = collectObjects("enmyShot", true);
    for (GameObject _eshot : eshots) {
      deleteObject(_eshot.getId());
    }
    setFlagAsString("/detailedStatus", "dying", false);
    setFlagAsDouble("/t0", gameTime, false);
    ego.setActive(false);
    createExplosion(gameTime, ego, "egoexp", DYING_INTERVAL, Color.WHITE);
    LinkedList<GameObject> enemies = collectObjects("enemy", false);
    for (GameObject enemy : enemies) {
      enemy.setY(0);
      enemy.setActive(false);
    }

  }

  /* ***************************************************************** */
  /* Preparation and management methods */
  /* ***************************************************************** */

  /**
   * initially sets up the level or loads it from a savegame file.
   * Loads resources etc. After this method, levelstatus is set to "start" if it is a fresh init, 
   * otherwise the status and the flags are taken from the savegame. resources are loaded and assigned to objects 
   * regardless of whether is it a savegame.
   * TODO level.based resource management: if level is loaded, all object must 
   * reload external resources like
   * sound files and animations, or else these must be stored in flags as well.
   * 
   * @param id String identifies level.
   */
  @Override
  public void prepareLevel(String id) {
    System.out.println("PREPARE");
    reset();
    setFlagAsString("/levelStatus", "start", false); // Set level status to "start"
    setFlagAsDouble("/t0", this.getGameTime(), false); // Set initial time
    setFlagAsDouble("/lastEgoShot", getFlagAsDouble("/t0", false, 0.0), false); // Set last ego shot time
   

   

    // set up flags that some objects rely on
    // ???
    //setLevelFlag("delete", new LinkedList<String>());
    //setLevelFlag("replace", new LinkedList<String>());
    // ------------

    // setu p boundaries of the level
    GameObject borderObj1 = new RectObject("border1",this, 0,CANVASY/2.,0,0,10,CANVASY, Color.WHITE).generateColliders() ;
    addObject(borderObj1) ;

    GameObject borderObj2 = new RectObject("border2",this, CANVASX,CANVASY/2.,0,0,10,CANVASY, Color.WHITE).generateColliders() ;
    addObject(borderObj2) ;

    GameObject borderObj3 = new RectObject("border3",this, CANVASX/2.,CANVASY+10,0,0,CANVASX,10, Color.WHITE).generateColliders() ;
    addObject(borderObj3) ;

    // Musik laden
    if (this.smash == null) {
      this.smash = new File("./audio/smash.wav");
    }
    if (this.laser == null) {
      this.laser = new File("./audio/laser.wav");
    }

    // ----- Alien
    if (this.enemyAnim == null) {
      String dateiName = "./video/sweetAlien.txt";
      this.enemyAnim = new Animation(dateiName);
    }

    // -----Heart
    if (this.heartAnim == null) {
      String heartName = "./video/heart.txt";
      this.heartAnim = new Animation(heartName);
    }

    // JW: Highscore aus Datei laden und aktualisieren
    HighscoreManager dh = new HighscoreManager();
    int alltimeHighscore = dh.readHSFromFile();
    setFlagAsInt("/highscore", alltimeHighscore, true); // Set the high score
    System.out.println("HIGHSCORE" + alltimeHighscore);

    // create and connect LevelController
    LevelController ctrl = new LevelController();
    this.setLevelController(ctrl) ;
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

    // draw score in upper left part of javashooter.playground
    Font drawFont = new Font("SansSerif", Font.PLAIN, 20);
    AttributedString as = new AttributedString("Points: " + getFlagAsInt("/points", true, 0));
    as.addAttribute(TextAttribute.FONT, drawFont);
    as.addAttribute(TextAttribute.FOREGROUND, Color.yellow);
    g2.drawString(as.getIterator(), 10, 20);

    // draw lives counter in upper left part of javashooter.playground
    Integer lives =getFlagAsInt("/egoLives", true, 0);
    Font drawFont2 = new Font("SansSerif", Font.PLAIN, 20);
    AttributedString as2 = new AttributedString("Lives: " + lives);
    as2.addAttribute(TextAttribute.FONT, drawFont2);
    as2.addAttribute(TextAttribute.FOREGROUND, Color.yellow);
    g2.drawString(as2.getIterator(), preferredSizeX() - 100, 20);

    // ergaenzt durch JW, draw highscore in left part of javashooter.playground under score
    Integer highscore = getFlagAsInt("/highscore", true, 0);
    Font drawFont3 = new Font("SansSerif", Font.PLAIN, 20);
    AttributedString as3 = new AttributedString("Highscore: " + highscore);
    as3.addAttribute(TextAttribute.FONT, drawFont3);
    as3.addAttribute(TextAttribute.FOREGROUND, Color.yellow);
    g2.drawString(as3.getIterator(), 10, 40);

    if (isPaused()) {
      Font drawFont4 = new Font("SansSerif", Font.PLAIN, 50);
      AttributedString as4 = new AttributedString("Das Spiel wurde pausiert.");
      as4.addAttribute(TextAttribute.FONT, drawFont4);
      as4.addAttribute(TextAttribute.FOREGROUND, Color.red);
      g2.drawString(as4.getIterator(), 30, 400);
    }    

  }

  /** Adds ego object and stars and displays startup message. Is called from applyGameLogic */
  protected void setupInitialState() {
    double gameTime = this.getGameTime();
    setFlagAsString("/levelStatus", "starting", false); // Set level status to "starting"

    this.createStars();

    // set up ego object
    addObject(this.createEgoObject());

    // add text object to javashooter.playground
    ObjectController ctrl = new LimitedTimeController(gameTime, 3.);
    GameObject readyText = new TextObject("ready?", this, preferredSizeX() / 2, 0, 0, 100,
        this.getStartupMessage(), 50, Color.RED).setController(ctrl);
    addObject(readyText);

  }


  public void checkLevelCompletion(LinkedList<GameObject> enemies) {
    //System.out.println(this.getName()+"enemies="+enemies.size()) ;
    if (enemies.size() <= 0) {
      HighscoreManager hsm = new HighscoreManager();
      hsm.writeHSToFile(getFlagAsInt("/points", true, 0), getFlagAsInt("/highscore", true, 0));
      getFlagAsInt("/highscore", true, 0); // Retrieve high score
      this.doneLevel = true;
    }
  }


  /** produce an  ego-shot if space pressed */
  public void shoot(GameObject ego) {
    String flag = this.getFlagAsString("shoot_pressed", false, "false") ;
    if (flag.equals("true") == true) {
    	if (this.getGameTime() - getFlagAsDouble("/lastEgoShot", false, 0.0) > 0.1) {
    	    int egoShotCounter = getFlagAsInt("/egoShotCounter", false, 0) + 1;
    	    setFlagAsInt("/egoShotCounter", egoShotCounter, false); // Update ego shot counter
    	    String shotName = "simpleShot" + egoShotCounter;
    	    GameObject ss = this.createEgoShot(shotName, ego);
    	    addObject(ss);
    	    setFlagAsDouble("/lastEgoShot", this.getGameTime(), false); // Update last ego shot time
    	}
    }
  }  

  /**
   * applies the logic of the level: For now, this is just about deleting shots that are leaving the
   * screen
   */
  @Override
  public void applyGameLogic() {
    double gameTime = this.getGameTime();

    if (getFlagAsString("/levelStatus", false, "start").equals("start")) {
        setupInitialState();
    } else if (getFlagAsString("/levelStatus", false, "start").equals("starting")) {
    	if ((gameTime - (getFlagAsDouble("/t0", false, 0.0) != null ? getFlagAsDouble("/t0", false, 0.0).doubleValue() : 0.0)) > LEVEL_INIT_TIME) {
    	    setFlagAsString("/levelStatus", "init", false);
    	}
        
    } else if (getFlagAsString("/levelStatus", false, "start").equals("init")) {
        this.createEnemies();
        this.createCollectables();
        setFlagAsString("/levelStatus", "playing", false);
    } else if (getFlagAsString("/levelStatus", false, "start").equals("playing")) {
        GameObject s = this.getObject("ego");
        if (getFlagAsString("/detailedStatus", false, "std").equals("std")) {

        shoot(s) ;

        // check for collisions of enemy and shots, reuse shots list from before..
        LinkedList<GameObject> enemies = collectObjects("enemy", false);

        // check whether all enemies have been destroyed or escaped
        checkLevelCompletion(enemies) ; 

        // loop over enemies to check for collisions or suchlike ...
        LinkedList<GameObject> shots = collectObjects("simpleShot", true);
        for (GameObject e : enemies) {
          // if ego collides with enemy..
          if (s.collisionDetection(e)) {
            actionIfEgoCollidesWithEnemy(e, s);
          }

          // if shot collides with enemy
          for (GameObject shot : shots) {
            if (e.collisionDetection(shot)) {
              actionIfEnemyIsHit(e, shot);
            }
          }
        }

        // Wenn Herzen einsammeln
        LinkedList<GameObject> collects = collectObjects("collect", false);
        for (GameObject c : collects) {
          if (s.collisionDetection(c)) {
            actionIfEgoCollidesWithCollect(c, s);
          }
        }

        // loop over enemies and, with a certain probability, launch an enemy shot for each one
        for (GameObject e : enemies) {
          createEnemyShot(e);
        }

        // check for collisions between ego object and enemy shots
        LinkedList<GameObject> eshots = collectObjects("enmyShot", true);
        for (GameObject eshot : eshots) {

          if (s.collisionDetection(eshot)) {
            // System.out.println("COLLISION"+ eshot.scol.get(0)+"/"+ s.scol.get(0));
            actionIfEgoObjectIsHit(eshot, s);
          }
        }
      } // if substatus..
      else if (getFlagAsString("/detailedStatus", false, "").equals("dying")) {  
    	  
    	  Double t0Value = getFlagAsDouble("/t0", false, 0.0);

    	  if (gameTime - t0Value > DYING_INTERVAL) {
    	      LinkedList<GameObject> enemies = collectObjects("enemy", false);
    	      setFlagAsString("/detailedStatus", "std", false);
              for (GameObject enemy : enemies) {
                enemy.setY(0);
                enemy.setActive(true);
              }
    	      s.setActive(true);
    	  }
    	  
        }
      }

    } // if (gameStatus == "playing")

  public boolean gameOver() {
    return lost;
  }

  public boolean levelFinished() {
	  return (getFlagAsString("/levelStatus", false, "playing").equals("playing")) && doneLevel;
  }



}
