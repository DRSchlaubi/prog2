package javashooter.playground;

import java.io.*;
import java.util.Scanner;



// Klasse um Highscore umzusetzen
public class HighscoreManager {
  Scanner s;

  public HighscoreManager() {
    try {
      s = new Scanner(new File("./highscore.txt"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public int readHSFromFile() {
    if (s.hasNext()) {
      int highscore = s.nextInt();
      return highscore;
    }
    return 0;
  }

  public static void writeHSToFile(Integer pts, Integer highscore) {
    String highscore2 = String.valueOf(pts);
    try {
      if (pts > highscore) {
        FileWriter fw = new FileWriter("./highscore.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(highscore2);
        bw.close();
      }
    } catch (IOException e) {
      System.out.println("AAAAAAARGHHH!");
    }
    System.out.println("Datei wurde geöffnet und geschrieben mit dem Wert " + highscore2);

  }

  public void closeFile() {
    s.close();
  }
}
