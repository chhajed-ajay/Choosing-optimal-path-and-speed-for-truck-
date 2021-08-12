package demo19006;

import java.util.*;
import base.*;

class TruckDemo extends Truck {

	// constructor
	TruckDemo() {
		// at time of instantiation
		onHwy = false;
		lastHub = null;
		hubs = new ArrayList<>();
		hubWait = false;
	}

	// update method of TruckDemo
	@Override
	protected synchronized void update(int deltaT) {

		presentTime = presentTime + deltaT;

		if (this.getStartTime() > presentTime) {
			return;
		} else {

			int y_current = this.getLoc().getY();
			int x_current = this.getLoc().getX();

			if (y_current == this.getSource().getY() && x_current == this.getSource().getX()) {
				Hub closeHub = Network.getNearestHub(this.getSource());
				if (closeHub.add(this)) {
					this.hubWait = true;
					this.onHwy = false;
					this.setLoc(closeHub.getLoc());
				}
			} else {

				Hub destination_Hub = Network.getNearestHub(this.getDest());
				if (x_current == destination_Hub.getLoc().getX() && y_current == destination_Hub.getLoc().getY()) {
					this.setLoc(this.getDest());
					this.hubWait = false;
					this.onHwy = false;
					this.lastHub = destination_Hub;

				} else if (hwyWait) {
					Hub endingHub = this.highway.getEnd();
					if (endingHub.add(this)) {
						this.hubWait = true;
						this.onHwy = false;
						this.hwyWait = false;
						this.highway.remove(this);
						hubs.add(endingHub);
					} else {
						this.onHwy = false;
						this.hwyWait = true;
					}

					this.setLoc(endingHub.getLoc());

				} else if (onHwy) {


					int Y1 = highway.getStart().getLoc().getY();
					int X1 = highway.getStart().getLoc().getX();
					int Y2 = highway.getEnd().getLoc().getY();
					int X2 = highway.getEnd().getLoc().getX();

					double length = Math.sqrt((Y2 - Y1) * (Y2 - Y1) + (X2 - X1) * (X2 - X1)); // highway length

					int X_dest, Y_dest;

					int distToTravel = this.speed * deltaT / 100;

					X_dest = x_current + (int) (distToTravel * (X2 - X1) / length);
					Y_dest = y_current + (int) (distToTravel * (Y2 - Y1) / length);

					double highwayLength = Math.sqrt(Math.pow(Y1 - Y2, 2) + Math.pow(X1 - X2, 2));
					this.distance = this.distance + distToTravel;

					if (distance >= highwayLength) {

						Hub endingHub = this.highway.getEnd();

						if (endingHub.add(this)) {
							this.onHwy = false;
							this.hubWait = true;
							hubs.add(endingHub);
							this.highway.remove(this);
							this.hwyWait = false;
						} else {
							// if we can't add to that hub, then truck will wait on highway
							this.onHwy = false; // on highway shows truck is moving or not
							this.hwyWait = true; // this shows truck is waiting or not
						}

						//setting location
						this.setLoc(endingHub.getLoc()); 

					} else {
						Location destinationLoc = new Location(X_dest, Y_dest); // destination
						this.setLoc(destinationLoc); // setting location
						this.hwyWait = false;
						this.hubWait = false;
						this.onHwy = true;
					}

				}

			}
		}

	}

	// getting last hub
	public Hub getLastHub() {
		return lastHub;
	}

	// truck name
	public String getTruckName() {
		return "Truck19006";
	}

	// enter function
	public void enter(Highway hwy) {
		this.distance = 0;  // distance covered by truck initially is zero
		this.onHwy = true;
		this.highway = hwy;  
		this.speed = hwy.getMaxSpeed(); // running at maximum speed
		this.lastHub = hwy.getStart(); //last hub is hub from where truck came from
		this.hubWait = false;  // initially false
		this.hwyWait = false;  // initially false

	}

	// new members
	private Hub lastHub; // last hub visited
	private ArrayList<Hub> hubs;
	private static int presentTime = 0;
	private Highway highway;
	private boolean onHwy; // on highway is true when truck is moving on highway.
	private boolean hubWait; // hubWait is true when hub is waiting.
	private boolean hwyWait; // hwyWait is true when highway hwy is waiting.
	private int speed; // speed
	private int distance; // distance

}
