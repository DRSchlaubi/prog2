package testat5.controller;

import javashooter.controller.LimitedTimeController;
import javashooter.gameobjects.GameObject;

public class ShieldController extends LimitedTimeController {
    private final GameObject egoObject;

    public ShieldController(GameObject gameObject, GameObject egoObject) {
        super(egoObject.getGameTime(), 5);
        this.gameObject = gameObject;
        this.egoObject = egoObject;
    }

    @Override
    public void updateObject() {
        super.updateObject();
        setX(egoObject.getX());
        setY(egoObject.getY());
    }
}
