package javashooter.playground;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javashooter.gameobjects.GameObject;

public class SaveGame {

  private static String datnam = "aktuellerSpielzustand.ser";

  @SuppressWarnings("unchecked")
  public static void save() {

    // javashooter.gameobjects.AnimatedGameobject
    ArrayList<String> objArrList = null;
    ObjectInputStream in = null;
    try {
      in = new ObjectInputStream(new FileInputStream(datnam));
      objArrList = (ArrayList<String>) in.readObject();
    } catch (FileNotFoundException ex) {
      System.out.println("Speicherdatei (noch) nicht vorhanden!");
    } catch (Exception ex) {
      System.out.println(ex);
    } finally {
      try {
        if (in != null)
          in.close();
      } catch (IOException e) {
      }
    }
    if (objArrList == null)
      objArrList = new ArrayList<String>();

    objArrList.add(new String("ArrayListgroesse: " + objArrList.size()));
    System.out.println(objArrList);

    ObjectOutputStream aus = null;
    try {
      aus = new ObjectOutputStream(new FileOutputStream(datnam));
      aus.writeObject(objArrList);
    } catch (IOException ex) {
      System.out.println(ex);
    } finally {
      try {
        if (aus != null) {
          aus.flush();
          aus.close();
        }
      } catch (IOException e) {
      }
    }
  }

}
