package javashooter.playground;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javashooter.gameutils.GameObjectPred;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.FileReader;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import javashooter.collider.Collider;
import javashooter.controller.ObjectController;
import javashooter.gameobjects.GameObject;

/**
 * Playground represents a level of the game, focussing on the game LOGIC and the graphical
 * representation of the level itself (not its {@link GameObject} instances). In particular, an
 * instance of Playground
 * <ul>
 * <li>manages the different moving or static objects in a level (e.g., collisions, adding objects,
 * removing objects). This is mainly done by the methods {@link #addObject}, {@link #deleteObject}
 * and {@link #getObject}.
 * <li>processes keyboard inputs provided by GameMain
 * <li>represents the state of a level represented by <b>flags</b>. Each flag has a name (a String)
 * and a value of String, Integer or Double. Methods: {@link #setFlagAsInt}, {@link #getFlagAsInt}.
 * As an example, the current score is a flag usually named "points", with an Integer as a value.
 * This value can be retrieved and manipulated using the above mentioned methods. The reason we use
 * flags and not direct attributes of the playground (or derived) class for communication is that,
 * in this way, the controllers/artist/etc instances do not need to assume that the playground class
 * is of a particular type.
 * </ul>
 */

public abstract class Playground {

  public static final int FLAGS_GLOBAL = 1;
  public static final int FLAGS_LEVEL = 2;
  public static final int FLAGS_ALL = 3;

  /** only one set of objects exists concurrently so this can be static */
  protected static HashMap<String, GameObject> gameObjects = new HashMap<String, GameObject>();

  /** only one set of objects exists concurrently so this can be static */
  protected static HashMap<String, Object> flags = new HashMap<String, Object>();

  protected String level = "";
  protected double timeStep = 0;
  protected double gameTime = 0;

  LinkedList<GameObject> addables = new LinkedList<GameObject>();
  LinkedList<String> removables = new LinkedList<String>();

  Stack<KeyEvent> keyEvents;
  Stack<MouseEvent> mouseEvents;

  ObjectController levelController = null;

  /**
   * Stores pending broadcast messages to be processed.
   */
  private LinkedList<String> broadcastMessages = new LinkedList<>();

  // ???
  protected double[] defaultShift = new double[] {0.0, 0.0};

  protected boolean pausedFlag = false;


  public Playground() {}

  /**
   * Returns the levels static name, to be redefined.
   */
  public abstract String getName();

  /**
   * Adds a graphics object to a level. Not immediately but in the next time step to avoid
   * inconsistencies.
   * 
   * @param o GameObject The object to be added
   */
  public void addObject(GameObject o) {
    addables.addLast(o);
  }

  /**
   * Adds a graphics object to a level. NOT recommended!
   * 
   * @param o GameObject The object to be added
   */
  public void addObjectNow(GameObject o) {
    gameObjects.put(o.getId(), o);
  }



  /**
   * Here, the level communicates its size preference to the GameUI, called automatically.
   */
  public abstract int preferredSizeX();

  public abstract int preferredSizeY();


  /**
   * Sends a broadcast message to all registered objects, not immediately but in next timestep to
   * avoid inconsistencies.
   * 
   * @param message The String message to be broadcasted.
   */
  public void broadcast(String message) {
    broadcastMessages.add(message); // Nachricht zwischenspeichern in der Liste
  }


  /**
   * Processes all pending broadcast messages and notifies the relevant objects.
   */
  public void processBroadcastMessages() {

    // System.out.println("ProssBroadcast aufgerufen");

    for (String message : broadcastMessages) { // Alle Nachrichten durchlaufen
      for (GameObject obj : gameObjects.values()) { // Alle Objekte durchlaufen
        ObjectController controller = obj.getObjectController();

        if (controller == null) {
          // System.out.println("Fehler: Objekt '" + obj.getName() + "' hat keinen Controller!");

        } else {
          try {
            controller.receive(message);

          } catch (Exception e) {
            System.out.println("Fehler beim Empfang der Nachricht für Objekt '" + obj.getName()
                + "': " + e.getMessage());
          }
        }
      }
    }

    broadcastMessages.clear(); // Nachrichten nach der Verarbeitung leeren
  }

  /**
   * Reinitializes the level.
   */
  public void reset() {
    gameObjects.clear();
    // TODO: clear level flags!!
  }


  /**
   * Performs final cleanup operations @ the end of a timestep
   */
  public void cleanupBroadcasts() {

    // System.out.println("Nachrichten wurden gelöscht!");

    for (GameObject obj : gameObjects.values()) {
      ObjectController controller = obj.getObjectController();
      if (controller != null) {
        controller.cleanupBroadcasts(); // Nachrichten löschen

      }
    }
  }


  /**
   * Search all game objects for objects with a certain substring in their name. USEFUL!
   * 
   * @param substr String. The string that must be part of the object name if object is to be
   *        considered found.
   * @param filterInactive deprecated, does nothing regardless of value
   * @return a reference to a LinkedList filled with all objects that have <b>substr</b> in thier
   *         name
   */
  public LinkedList<GameObject> collectObjects(String substr, boolean filterInactive) {
    LinkedList<GameObject> l = new LinkedList<GameObject>();
    for (Map.Entry<String, GameObject> entry : this.gameObjects.entrySet()) {
      GameObject obj = entry.getValue();
      if (obj.getId().contains(substr)) {
        if (filterInactive == true) {
          if (obj.isActive()) {
            l.add(obj);
          }
        } else {
          l.add(obj);
        }
      }
    }
    return l;
  };


  public LinkedList<GameObject> collectObjects(GameObjectPred p) {
    LinkedList<GameObject> l = new LinkedList<GameObject>();
    for (Map.Entry<String, GameObject> entry : this.gameObjects.entrySet()) {
      GameObject obj = entry.getValue();
      if (p.check(obj)) {
        l.add(obj);
      }
    }
    return l;
  };

  /**
   * Removes a graphics object from a level, not immediately but in the next timestep.
   * 
   * @param id String The unique identifier of the object
   */
  public void deleteObject(String id) {
    removables.addLast(id);
  }

  /**
   * Removes a graphics object from a level immediately, NOT recommended.
   * 
   * @param id String The unique identifier of the object
   */
  public void  deleteObjectNow(String id) {
    gameObjects.remove(id);
  }

  /**
   * Retrieves a graphics object by name.
   * 
   * @param id String Unique id of the object
   * @return reference to the requested game object, or null if not found
   */
  public GameObject getObject(String id) {
    return gameObjects.get(id);
  }


  /**
   * Internal, NEVER call directly.
   * 
   * @param keyEvents Set of all keyEvents received by the UI in this timestep.
   */
  public void processKeyEvents(Stack<KeyEvent> keyEvents) {
    // System.out.println("KEYS="+keyEvents.size()) ;
    this.keyEvents = keyEvents;
    // this.setGlobalFlag("keyboard_events", keyEvents);
  }

  /**
   * Internal, NEVER call directly.
   * 
   * @param mouseEvents Set of all MouseEvents received by the UI in this timestep.
   */
  public void processMouseEvents(Stack<MouseEvent> mouseEvents) {
    this.mouseEvents = mouseEvents;
  }

  /**
   * Can be called by, e.g., {@link javashooter.controller.ObjectController} instances to react to
   * user inputs.
   * 
   * @return list of current unhandled key events
   */
  public Stack<KeyEvent> getKeyEvents() {
    return this.keyEvents;
  }

  /**
   * Can be called by, e.g., {@link javashooter.controller.ObjectController} instances to react to
   * user inputs.
   * 
   * @return list of current unhandled mouse events
   */
  public Stack<MouseEvent> getMouseEvents() {
    return this.mouseEvents;
  }


  public boolean isPaused() {
    return this.pausedFlag;
  }

  public void setPaused(boolean p) {
    this.pausedFlag = p;
  }

  public void togglePause() {
    pausedFlag = !pausedFlag;

  }


  /**
   * Method meant to be filled with own code, handles the entire game logic (collision checks, timed
   * events, ...).
   */
  public abstract void applyGameLogic();

  public abstract boolean gameOver();

  public abstract boolean levelFinished();

  public abstract boolean resetRequested();

  public double[] getShift() {
    return this.defaultShift;
  }

  /**
   * Calls all object update methods in level. Internal, never call directly.
   * 
   */
  public void updateObjects() {


    for (GameObject gameObject : gameObjects.values()) {
      if (gameObject.isActive() == true) {
        gameObject.updateObject();
      }
    }

    // delayed addition of objects
    for (GameObject o : addables) {
      addObjectNow(o);
    }

    // delayed removal of objects
    for (String s : removables) {
      deleteObjectNow(s);
    }

    // delayed sending of broadcasts
    processBroadcastMessages();

    // cleanup "delayed" lists
    removables.clear();
    addables.clear();
    cleanupBroadcasts();
  }

  public void setTimestep(double s) {
    timeStep = s;
  }

  public double getTimestep() {
    return timeStep;
  }

  public void setGameTime(double s) {
    this.gameTime = s;
  }

  public double getGameTime() {
    return this.gameTime;
  }

  /**
   * Installs a level javashooter.controller, i.e., a normal ObjectController instance attached to a
   * dummy object that is invisible and has not javashooter.collider. Since every
   * javashooter.controller has access to the Playground instance of its associated object, it can
   * control the Playground instance. For example, applyGameLogic() could be replaced by a dedicated
   * LevelController.
   */
  public void setLevelController(ObjectController ctrl) {
    this.levelController = ctrl;
    GameObject dummy = new GameObject("dummyObject", this, ctrl, 0, 0, 0, 0);
    ctrl.setObject(dummy);
  }


  public void updateLevel() {
    if (this.levelController != null) {
      this.levelController.updateObject();
    }
  }

  /**
   * To be redefined!! Draws level background and global information like points etc., NOT moving
   * objects.
   * 
   * @param g2 Graphics2D abstract drawing object of java Swing, used to carry out all drawing
   *        operations.
   */
  public abstract void redrawLevel(Graphics2D g2);

  /**
   * To be redefined!! Draws mainly h level background and global information like points etc., NOT
   * moving objects but AFTER having drawn objects.
   * 
   * @param g2 Graphics2D abstract drawing object of java Swing, used to carry out all drawing
   *        operations.
   */
  public void redrawLevelAfter(Graphics2D g2) {}

  /**
   * Internal, do not call directly.
   * 
   * @param g2 Graphics2D abstract drawing object of java Swing, used to carry out all drawing
   *        operations.
   */
  public void redraw(Graphics2D g2) {
    AffineTransform at = new AffineTransform();
    double[] shift = this.getShift();
    at.setToTranslation(shift[0], shift[1]);
    g2.setTransform(at);
    redrawLevel(g2);
    for (GameObject gameObject : gameObjects.values()) {
      if (gameObject.isActive()) {
        // System.out.println("draw: "+gameObject.getName()) ;
        gameObject.draw(g2);
      }
    }
    redrawLevelAfter(g2);
  }

  /**
   * Sets up a single level. Prepares all objects etc. Is called ONCE at the start of each level by
   * the game engine.
   * 
   * @param level String a string identifying the level number etc
   */
  public abstract void prepareLevel(String level);

  protected void centeredText(Graphics2D g2, String text, Color fgColor, Color bgColor) {
    Font font = new Font("SansSerif", Font.PLAIN, 50);
    FontRenderContext frc = g2.getFontRenderContext();
    Rectangle2D textBounds = font.getStringBounds(text, frc);
    AttributedString as4 = new AttributedString(text);
    as4.addAttribute(TextAttribute.FONT, font);
    as4.addAttribute(TextAttribute.FOREGROUND, fgColor);
    as4.addAttribute(TextAttribute.BACKGROUND, bgColor);
    g2.drawString(as4.getIterator(), (int) (this.preferredSizeX() / 2 - textBounds.getWidth() / 2.),
        this.preferredSizeY() / 2);
  }

  protected void drawText(Graphics2D g2, int size, String text, int x, int y, Color c) {
    Font drawFont = new Font("SansSerif", Font.PLAIN, size);
    AttributedString as = new AttributedString(text);
    as.addAttribute(TextAttribute.FONT, drawFont);
    as.addAttribute(TextAttribute.FOREGROUND, c);
    g2.drawString(as.getIterator(), x, y);
  }

  // ================== Flag Functionality ================== //

  /**
   * Sets a global flag with the specified key and value.
   *
   * @param key The key of the flag.
   * @param value The value of the flag (can be Integer, String, Float, etc.).
   */
  private void setGlobalFlag(String key, Object value) {
    flags.put("/global/" + key, value);
  }

  /**
   * Sets a level-specific flag with the specified key and value.
   *
   * @param key The key of the flag.
   * @param value The value of the flag (can be Integer, String, Float, etc.).
   */
  private void setFlag(String key, Object value, boolean global) {
    String name = ((global == true) ? "/global/" : "/" + this.getName() + "/") + key;
    flags.put(name, value);
  }

  /**
   * Retrieves the value of a level-specific or global flag. Returns default if this does not exist
   * and creates it with default value.
   *
   * @param key The key of the flag.
   * @return The value of the flag, or null if the flag does not exist.
   */
  private Object getFlag(String key, boolean global, Object def) {
    String name = ((global == true) ? "/global/" : "/" + this.getName() + "/") + key;
    Object val = this.flags.get(name);
    if (val == null) {
      this.flags.put(name, def);
      return def;
    } else {
      return val;
    }
  }


  /**
   * Sets a flag with an Integer value.
   *
   * @param key The key of the flag.
   * @param value The Integer value to set.
   * @throws IllegalArgumentException if the value is not an Integer.
   */
  public void setFlagAsInt(String key, Integer value, boolean global) {
    setFlag(key, value, global);
  }

  /**
   * Sets a flag with a String value.
   *
   * @param key The key of the flag.
   * @param value The String value to set.
   * @throws IllegalArgumentException if the value is not a String.
   */
  public void setFlagAsString(String key, String value, boolean global) {
    setFlag(key, value, global);
  }


  /**
   * Sets a flag with a Double value.
   *
   * @param key The key of the flag.
   * @param value The Double value to set.
   * @throws IllegalArgumentException if the value is not a Double.
   */
  public void setFlagAsDouble(String key, Double value, boolean global) {
    setFlag(key, value, global);
  }

  /**
   * Retrieves the value of a global flag as an Integer.
   *
   * @param key The key of the flag.
   * @return The value of the flag as an Integer, or null if the flag does not exist or is not an
   *         Integer.
   */
  public Integer getFlagAsInt(String key, boolean global, Integer def) {
    Object value = getFlag(key, global, def);
    return (value instanceof Integer) ? (Integer) value : null;
  }


  /**
   * Retrieves the value of a global/level-flag as a String.
   *
   * @param key The key of the flag.
   * @return The value of the flag as a String, or null if the flag does not exist or is not a
   *         String.
   */
  public String getFlagAsString(String key, boolean global, String def) {
    Object value = getFlag(key, global, def);
    return (value instanceof String) ? (String) value : null;
  }

  /**
   * Retrieves the value of a global flag as a Double.
   *
   * @param key The key of the flag.
   * @return The value of the flag as a Float, or null if the flag does not exist or is not a Float.
   */
  public Double getFlagAsDouble(String key, boolean global, Double def) {
    Object value = getFlag(key, global, def);
    return (value instanceof Double) ? (Double) value : null;
  }

  /**
   * Erhöht den Wert eines numerischen Flags (Integer, Double, etc.) um einen bestimmten Betrag.
   *
   * @param key Der Schlüssel des Flags.
   * @param amount Der Betrag, um den der Wert erhöht werden soll (kann positiv oder negativ sein).
   * @throws IllegalArgumentException Wenn der Flag nicht existiert oder kein numerischer Typ ist.
   */
  public void incrementFlag(String key, boolean global, Number def, Number amount) {
    // Hole den aktuellen Wert des Flags
    Object value = getFlag(key, global, def);

    if (!(value instanceof Number)) {
      throw new IllegalArgumentException("Flag '" + key + "' ist kein numerischer Typ.");
    }

    // Erhöhe den Wert um den angegebenen Betrag
    Number currentValue = (Number) value;
    Number newValue;

    if (currentValue instanceof Integer) {
      newValue = currentValue.intValue() + amount.intValue();
    } else if (currentValue instanceof Double) {
      newValue = currentValue.doubleValue() + amount.doubleValue();
    } else {
      throw new IllegalArgumentException(
          "Flag '" + key + "' hat einen nicht unterstützten numerischen Typ.");
    }

    // Speichere den aktualisierten Wert
    setFlag(key, newValue, global);
  }
}
