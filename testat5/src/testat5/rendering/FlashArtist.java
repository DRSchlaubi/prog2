package testat5.rendering;

import javashooter.gameobjects.GameObject;
import javashooter.rendering.PulsatingCircleArtist;
import javashooter.rendering.RectArtist;

import java.awt.*;

public class FlashArtist extends PulsatingCircleArtist {

    public FlashArtist(GameObject go, double egoRad, Color color, double reduction, double freq) {
        super(go, egoRad, color, reduction, freq);
    }

    @Override
    public void draw(Graphics2D g) {
        var time = gameObject.getGameTime();
        var remainder = time - (int) time;
        this.color = (remainder % .2 > .1) ? new Color(0x00AA00) : new Color(0x99FF99);
        super.draw(g);
    }
}
