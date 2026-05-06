package testat4.w8.rendering;

import javashooter.gameobjects.GameObject;
import javashooter.rendering.RectArtist;

import java.awt.*;

public class FlashArtist extends RectArtist {
    public FlashArtist(GameObject go, double width, double height) {
        super(go, width, height, Color.BLUE);
    }

    @Override
    public void draw(Graphics2D g) {
        var time = gameObject.getGameTime();
        var remainder = time - (int) time;
        this.color = (remainder > .5) ? Color.RED : Color.BLUE;
        super.draw(g);
    }
}
