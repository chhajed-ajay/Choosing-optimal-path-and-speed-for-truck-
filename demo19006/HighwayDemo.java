package demo19006;

import java.util.LinkedList;
import java.util.Queue;

import base.Highway;
import base.Truck;

class HighwayDemo extends Highway {

	@Override
	public synchronized boolean hasCapacity() {
		if (TrucksHighwayQueue.size() < this.getCapacity())
			return true;
		else
			return false;
	}

	@Override
	public synchronized boolean add(Truck truck) {
		if (this.hasCapacity()) {
			if (truck != null) {
				TrucksHighwayQueue.add(truck);
				return true;
			} else
				return false;
		} else
			return false;
	}

	@Override
	public synchronized void remove(Truck truck) {
		if (TrucksHighwayQueue.size() <= 0 || truck == null)
			return;
		TrucksHighwayQueue.remove(truck);
	}


	// new data member
	private LinkedList<Truck> TrucksHighwayQueue = new LinkedList<>();

}
