package testat5.controller;

import javashooter.controller.LimitedTimeController;
import javashooter.gameobjects.GameObject;

public class ZickZackController extends LimitedTimeController {
    /**
     * Constructor.
     *
     * @param g0       int initial game time at creation
     * @param duration int duration in seconds
     */
    public ZickZackController(double g0, double duration, GameObject gameObject) {
        super(g0, duration);
        this.gameObject = gameObject;
    }

    @Override
    public void updateObject() {
        var time = getGameTime();
        var remainder = time - (int) time;
        setVX(remainder > .5 ? 30 : -30);
        super.updateObject();
    }
}
