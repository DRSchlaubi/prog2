package testat5.controller;

import javashooter.controller.LimitedTimeController;

public class ZickZackController extends LimitedTimeController {
    /**
     * Constructor.
     *
     * @param g0       int initial game time at creation
     * @param duration int duration in seconds
     */
    public ZickZackController(double g0, double duration) {
        super(g0, duration);
    }

    @Override
    public void updateObject() {
        setVX((getGameTime() - (int) getGameTime() > .5) ? 30 : -30);
        super.updateObject();
    }
}
