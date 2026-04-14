package javashooter.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.WindowConstants;

import javashooter.playground.Playground;

import java.awt.event.*;
import java.awt.event.KeyListener;
import java.awt.event.*;

/**
 * The main window of the game. Defines the menu, buttons, drawing area, ... 
 * Do not modify this!!
 */
public class GameUI implements ActionListener {

  private static volatile int newAction = -1;

  private JFrame frame = null;
  private JPanel panel = null;
  private JPanel buttonPanel = null;
  private GamePanel canvas = null;

  JMenuItem playItem;
  JMenuItem loadItem;
  JMenuItem saveItem;
  JMenuItem quitItem;
  JMenuItem aboutItem;
  JMenu gameMenu;
  JMenu helpMenu;
  JButton button;
  JButton button2;

  public static final int ACTION_NEW = 1;
  public static final int ACTION_LOAD = 2;
  public static final int ACTION_SAVE = 3;
  public static final int ACTION_RESET = 4;
  public static final int ACTION_QUIT = 5;
  public static final int ACTION_BUTTON = 6;
  public static final int ACTION_PAUSE = 6;
  public static final int ACTION_ABOUT = 7;

  Stack<KeyEvent> keyEvents;
  Stack<MouseEvent> mouseEvents;

  public GameUI(int sizeX, int sizeY) {

    // graphische Zeichenfläche des spiels erzeugen
    canvas = new GamePanel(sizeX, sizeY);

    canvas.setVisible(true);


    // contentPane erzeugen
    panel = new JPanel();
    panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));


    // panel.setLayout(new FlowLayout());
    // panel.setLayout(new GridBagLayout());
    // panel.setLayout(new SpringLayout());
    panel.add(canvas);

    // Hauptfenster erzeugen+konfigurieren!
    frame = new JFrame("Prog2 Ballerspiel!");
    frame.setContentPane(panel);

    // Menuleiste erzeugen und hinzufügen!
    this.playItem = new JMenuItem("New Game");
    this.loadItem = new JMenuItem("Restore game");
    this.saveItem = new JMenuItem("Save game");
    this.quitItem = new JMenuItem("Exit game");

    this.playItem.addActionListener(this);
    this.loadItem.addActionListener(this);
    this.saveItem.addActionListener(this);
    this.quitItem.addActionListener(this);
    this.gameMenu = new JMenu("Game");
    this.gameMenu.add(playItem);
    this.gameMenu.add(loadItem);
    this.gameMenu.add(saveItem);
    this.gameMenu.addSeparator();
    this.gameMenu.add(quitItem);

    this.helpMenu = new JMenu("Help");
    this.aboutItem = new JMenuItem("About");
    this.helpMenu.add(this.aboutItem);
    this.aboutItem.addActionListener(this);

    JMenuBar mb = new JMenuBar();
    mb.add(this.gameMenu);
    mb.add(this.helpMenu);

    frame.setJMenuBar(mb);

    this.button = new JButton("(Re)Start");
    this.button.addActionListener(this);

    this.button2 = new JButton("Pause");
    this.button2.addActionListener(this);

    this.buttonPanel = new JPanel();
    this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, BoxLayout.X_AXIS));
    this.buttonPanel.add(this.button);
    this.buttonPanel.add(this.button2);

    this.panel.add(this.buttonPanel);


    // showtime!
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(sizeX, sizeY + 20);
    frame.setVisible(true);
    System.out.println("!!") ;
    canvas.createBufferStrategy(2) ;

  }

  public HashMap<Integer, Integer> getCurrentKey() {
    return canvas.getCurrentKey();
  }

  public Stack<KeyEvent> getKeyEvents() {
    return this.canvas.getKeyEvents();
  }

  public Stack<MouseEvent> getMouseEvents() {
    return this.canvas.getMouseEvents();
  }

  public synchronized void repaint() {
    canvas.paint(null);
  }

  public void setPlayground(Playground pg) {
    canvas.setPlayground(pg);
  }


  public static int getNewAction() {
    return newAction;
  }


  public static void resetAction() {
    newAction = -1;
  }


  public void grabFocus() {
    this.canvas.requestFocus();
  }

  /** Sets size of both main game JFrame, as well as the game canvas, and adds 50 pixs
   *  to the height to make room for the main game buttons on the bottom.
   */
  public void setSize(int sizeX, int sizeY) {
    this.canvas.setSize(sizeX, sizeY);
    this.frame.pack();
    this.frame.setSize(new Dimension(sizeX, sizeY+50));
  }

  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == this.quitItem) {
      System.exit(0);
    } else if (ae.getSource() == this.playItem) {
      System.out.println("new game");
      newAction = ACTION_NEW;
    } else if (ae.getSource() == this.aboutItem) {
      System.out.println("about");
      newAction = ACTION_ABOUT;
    } else if (ae.getSource() == this.button) {
      System.out.println("click");
      newAction = ACTION_NEW;
    } else if (ae.getSource() == this.button2) {
      System.out.println("click2");
      newAction = ACTION_PAUSE;
    } else if (ae.getSource() == this.saveItem) {
      System.out.println("save");
      newAction = ACTION_SAVE;
    } else if (ae.getSource() == this.loadItem) {
      System.out.println("load");
      newAction = ACTION_LOAD;
    }
  }


}
