package javashooter.ui;

import java.awt.Toolkit ;
import java.awt.Canvas ;
import java.awt.Point ;
import java.awt.image.BufferedImage ;
import java.awt.Cursor ;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Stack;
import javax.swing.JPanel;
import java.awt.image.BufferStrategy ;

import javashooter.playground.Playground;

import java.awt.event.*;


/**
 * Models the area where the game is actually drawn, as a part the main window of the application.
 * Contains all of the Swing logic to draw the game, and to listen and register keyboard and mouse
 * inputs which are passed to the game logic.
 */
class GamePanel extends Canvas implements KeyListener, MouseListener, MouseMotionListener {
  private static final long serialVersionUID = 1L;
  private Playground playground = null;
  private HashMap<Integer, Integer> keys = new HashMap<Integer, Integer>();
  private int sizeX, sizeY;
  Stack<KeyEvent> keyEvents = new Stack<KeyEvent>();
  Stack<MouseEvent> mouseEvents = new Stack<MouseEvent>();
  private BufferStrategy bufferStrategy ;
  
  GamePanel(int sizeX, int sizeY) {
    super();
    //setPreferredSize(new Dimension(sizeX, sizeY));
    addKeyListener(this);
    addMouseListener(this);
    addMouseMotionListener(this);
    // https://stackoverflow.com/questions/1984071/how-to-hide-cursor-in-a-swing-application
    // Transparent 16 x 16 pixel cursor image.
    BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

    // Create a new blank cursor.
    Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
        cursorImg, new Point(0, 0), "blank cursor");

    // Set the blank cursor to the JFrame.
    this.setCursor(blankCursor);

    
  }
  
  
  public void setSize(int sizeX, int sizeY) {
    setPreferredSize(new Dimension(sizeX, sizeY));
    this.sizeX = sizeX;
    this.sizeY = sizeY;
  } 

  
  @Override
  public void createBufferStrategy(int num) {
    this.addNotify();    
    super.createBufferStrategy(num) ;
    this.bufferStrategy = this.getBufferStrategy();
    
  }


  Stack<KeyEvent> getKeyEvents() {
    return keyEvents;
  }


  Stack<MouseEvent> getMouseEvents() {
    return mouseEvents;
  }


  public void setPlayground(Playground pg) {
    this.playground = pg;
  }

  @Override
  public synchronized void paint(Graphics g) {    
    // This too would lead to flickering!!
    //super.paint(g);
    do {
      do {
        Graphics g2 = this.bufferStrategy.getDrawGraphics() ;
        if (playground != null) {
          playground.redraw((Graphics2D) g2);
        }
        g2.dispose() ;
      } while (this.bufferStrategy.contentsRestored());
      this.bufferStrategy.show();
    } while (this.bufferStrategy.contentsLost());
  } 
  
  /* Apparently update clears the background first, which leads to flickering!
   * So override it and do not clear the background!
   */
  @Override
  public void update(Graphics g) {  
    this.paint(g); 
  }
  


  public HashMap<Integer, Integer> getCurrentKey() {
    return keys;
  }


  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    /*
     * this.keys.put(e.getKeyCode(), 2); currentKey = e.getKeyCode();
     * //System.out.println("keyCode " + e.getKeyCode());
     */
    //System.out.println(e.paramString()) ;
    this.keyEvents.push(e);
  }

  @Override
  public void keyReleased(KeyEvent e) { /*
                                         * this.keys.put(e.getKeyCode(), 1); currentKey =
                                         * e.getKeyCode(); releasedFlag = true;
                                         */
    this.keyEvents.push(e);
    // System.out.println(e.paramString()) ;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    this.mouseEvents.push(e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // this.mouseEvents.push(e) ;
  }


  @Override
  public void mousePressed(MouseEvent e) {
    this.mouseEvents.push(e) ;
  }


  @Override
  public void mouseMoved(MouseEvent e) {
    this.mouseEvents.push(e) ;
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    this.mouseEvents.push(e) ;
  }



  @Override
  public void mouseEntered(MouseEvent e) {
    // this.mouseEvents.push(e) ;
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // this.mouseEvents.push(e) ;
  }
}
