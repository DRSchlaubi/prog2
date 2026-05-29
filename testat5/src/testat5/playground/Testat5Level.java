package testat5.playground;

import javashooter.controller.ObjectController;
import javashooter.gameobjects.GameObject;
import javashooter.rendering.PulsatingCircleArtist;
import spaceinvadersProject.playground.SpaceInvadersLevel;
import testat5.controller.ZickZackController;
import testat5.rendering.FlashArtist;

import java.awt.*;

public class Testat5Level extends SpaceInvadersLevel {
    private double powerUpUntil = 0;
    private boolean powerUpEnabled = false;

    @Override
    protected void actionIfEgoCollidesWithCollect(GameObject collect, GameObject ego) {
        super.actionIfEgoCollidesWithCollect(collect, ego);
        powerUpUntil = getGameTime() + 5;
    }

    @Override
    public GameObject createEgoShot(String shotName, GameObject ego) {
        var egoShot = super.createEgoShot(shotName, ego);
        egoShot.setOmega((getGameTime() <= powerUpUntil) ? Math.PI : 0);
        return egoShot;
    }

    @Override
    public void updateObjects() {
        super.updateObjects();
        if (getGameTime() <= powerUpUntil) {
            powerUpEnabled = true;
            setFlagAsString("shoot_pressed", "true", false);
        }
        if (getGameTime() > powerUpUntil && powerUpEnabled) setFlagAsString("shoot_pressed", "false", false);
    }

    @Override
    protected GameObject createEnemyShotObject(GameObject parentObject, String name, ObjectController limitedTimeController) {
        var shot = super.createEnemyShotObject(parentObject, name, limitedTimeController);
        shot.setController(new ZickZackController(getGameTime(), 2));
        return shot;
    }

    @Override
    protected GameObject createEgoObject() {
        var ego = super.createEgoObject();
        ego.addArtist(new FlashArtist(ego, 15, Color.GREEN, 0.7, 0.06666666666667));
        return ego;
    }
}
