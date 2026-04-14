package javashooter.controller;
import javashooter.gameobjects.*;

public class FollowController extends LimitedTimeController {
    GameObject follow ;
	public FollowController(GameObject follow, double g0, double duration) {
		super(g0, duration) ;
		this.follow = follow ;
	}
	
	
	@Override
	public boolean checkBorder() {
	  return false ;
	}

	@Override
	public void updateObject() {
		this.setVX(follow.getVX()) ;
		this.setVY(follow.getVY()) ;
		super.updateObject() ;		
	}

}
