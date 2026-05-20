package testat5.playground;

import javashooter.controller.ObjectController;
import javashooter.gameobjects.GameObject;
import javashooter.rendering.CircleArtist;
import javashooter.rendering.PulsatingCircleArtist;
import spaceinvadersProject.playground.SpaceInvadersLevel;
import testat5.controller.ShieldController;
import testat5.controller.ZickZackController;

import java.awt.*;

public class Testat5Level extends SpaceInvadersLevel {
    private double powerUpUntil = 0.0;

    @Override
    protected void actionIfEgoCollidesWithCollect(GameObject collect, GameObject ego) {
        super.actionIfEgoCollidesWithCollect(collect, ego);
        powerUpUntil = getGameTime() + 5;

        var shieldObject = new GameObject("shield", this, 0, 0, 0, 0);
        shieldObject.setObjectController(new ShieldController(shieldObject, ego));
        shieldObject.addArtist(new CircleArtist(shieldObject, 50, new Color(255,0,0, 50)));

        addObject(shieldObject);
    }

    @Override
    protected void actionIfEgoObjectIsHit(GameObject eshot, GameObject ego) {
        super.actionIfEgoObjectIsHit(eshot, ego);
        powerUpUntil = 0.0;
    }

    @Override
    protected void actionIfEnemyIsHit(GameObject e, GameObject shot) {
        super.actionIfEnemyIsHit(e, shot);
    }

    @Override
    protected GameObject createEnemyShotObject(GameObject parentObject, String name, ObjectController limitedTimeController) {
        var object = super.createEnemyShotObject(parentObject, name, limitedTimeController);

        object.setObjectController(new ZickZackController(getGameTime(), 5, object));

        return object;
    }

    @Override
    public GameObject createEgoShot(String shotName, GameObject ego) {
        var shotObject = super.createEgoShot(shotName, ego);
        if (powerUpUntil >= getGameTime()) {
            shotObject.setOmega(5);
        }

        return shotObject;
    }

    @Override
    protected GameObject createEgoObject() {
        var egoObject = super.createEgoObject();
        egoObject.addArtist(new PulsatingCircleArtist(egoObject, 15, Color.GREEN, .30, .10));
        return egoObject;
    }

    @Override
    public void updateObjects() {
        super.updateObjects();
        if (powerUpUntil >= getGameTime()) {
            setFlagAsString("shoot_pressed", "true", false);
            setFlagAsString("power_shoot", "true", false);
            var ego = getObject("ego");
            shoot(ego);
        } else if (getFlagAsString("power_shoot", false, "false").equals("true")) {
            setFlagAsString("shoot_pressed", "false", false);
            setFlagAsString("power_shoot", "false", false);
            powerUpUntil = 0;
        }
    }
}
