package testat2;

import javashooter.controller.KinematicsController;
import javashooter.gameobjects.RectObject;
import javashooter.playground.Playground;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class ExperimentierLevel extends Playground {
    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public int preferredSizeX() {
        return 500;
    }

    @Override
    public int preferredSizeY() {
        return 500;
    }

    @Override
    public void applyGameLogic() {

    }

    @Override
    public boolean gameOver() {
        return false;
    }

    @Override
    public boolean levelFinished() {
        return getGameTime() >= 3;
    }

    @Override
    public boolean resetRequested() {
        return false;
    }

    @Override
    public void redrawLevel(Graphics2D g2) {
        // fill background with black
        int[] x = {0, preferredSizeX(), preferredSizeX(), 0};
        int[] y = {0, 0, preferredSizeY(), preferredSizeY()};
        Polygon bg = new Polygon(x, y, 4);
        g2.setColor(Color.BLACK);
        g2.fill(bg);
    }

    @Override
    public void prepareLevel(String level) {
        System.out.println("Hello world!");

        var rect = new RectObject("johos", this,100,100,10, 10, 80, 10, Color.RED);
        rect.setOmega(100);
        rect.setController(new KinematicsController());
        addObject(rect);

        for (var i = 0; i < 100; i++) {
            var speed = ThreadLocalRandom.current().nextInt(-30, 30);
            var pixel = new RectObject(String.valueOf(i), this, 250, 250, speed, speed, 2, 2, Color.RED);
            pixel.setController(new KinematicsController());
            addObject(pixel);
        }
    }
}
