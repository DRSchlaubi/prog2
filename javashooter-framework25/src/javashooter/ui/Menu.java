package javashooter.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javashooter.playground.Playground;
import javashooter.ui.GameUI;

public class Menu {

  private JFrame frame;
  private JPanel buttonPanel;
  private JButton playButton;
  private JButton exitButton;
  private JButton restoreButton;
  private JButton saveButton;

  public Menu() {
    frame = new JFrame("Prog2 Spiel Hauptmenü");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);

    buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50));

    playButton = new JButton("Play");
    playButton.setBackground(Color.YELLOW);
    playButton.setOpaque(true);
    playButton.addMouseListener(new MyMouseListener());
    buttonPanel.add(playButton);

    restoreButton = new JButton("Restore");
    restoreButton.setBackground(Color.YELLOW);
    restoreButton.setOpaque(true);
    restoreButton.addMouseListener(new MyMouseListener());
    buttonPanel.add(restoreButton);

    saveButton = new JButton("Save");
    saveButton.setBackground(Color.YELLOW);
    saveButton.setOpaque(true);
    saveButton.addMouseListener(new MyMouseListener());
    buttonPanel.add(saveButton);

    exitButton = new JButton("Exit");
    exitButton.setBackground(Color.YELLOW);
    exitButton.setOpaque(true);
    exitButton.addMouseListener(new MyMouseListener());
    buttonPanel.add(exitButton);


    frame.add(buttonPanel);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }


  private class MyMouseListener extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent event) {
      JButton button = (JButton) event.getSource();

      if (button.equals(playButton)) {
        GameUI gameUI = new GameUI(700, 700);

        Playground playground = null;
        gameUI.setPlayground(playground);

        System.out.println("Prog2Spiel beginnt ...");
      } else if (button.equals(exitButton)) {
        System.exit(0);
      }
    }

    @Override
    public void mouseEntered(MouseEvent event) {
      JButton button = (JButton) event.getSource();
      button.setBackground(Color.GRAY);
    }

    @Override
    public void mouseExited(MouseEvent event) {
      JButton button = (JButton) event.getSource();
      button.setBackground(Color.YELLOW);
    }
  }
}
