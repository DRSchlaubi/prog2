package javashooter.playground;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {

  public static synchronized void music(File track) {

    final File trackname = track;

    new Thread(new Runnable() {

      @Override
      public void run() {
        while (true) {

          try {

            Clip clip = AudioSystem.getClip();
            AudioInputStream inputstream = AudioSystem.getAudioInputStream(trackname);
            clip.open(inputstream);
            clip.loop(0);

            Thread.sleep(clip.getMicrosecondLength() / 1);

          } catch (Exception e) {
            e.printStackTrace();
          }

        }

      }

    }).start();

  }

}
