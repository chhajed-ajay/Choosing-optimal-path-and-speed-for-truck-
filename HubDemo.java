package demo19006;

import java.util.*;
import java.util.LinkedList;
import java.util.ArrayList; // import the ArrayList class
import java.util.Queue;
import base.Highway;
import base.Hub;
import base.Location;
import base.Truck;
import base.Network;

class HubDemo extends Hub {

	public HubDemo(Location loc) {
		super(loc);
	}

	@Override
	public synchronized boolean add(Truck truck) {
		if (trucksQueue.size() >= this.getCapacity())
			return false;
		else {
			if (truck != null) {
				trucksQueue.add(truck);
			}
			return true;
		}
	}

	@Override
	public synchronized void remove(Truck truck) {
		if (truck != null) {
			trucksQueue.remove(truck);
			this.lastHub = truck.getLastHub();
		}

	}

	@Override
	public Highway getNextHighway(Hub from, Hub dest) {

		ArrayList<Highway> highwaysList = from.getHighways();

		for (Highway h : highwaysList) {
			if (h.getEnd().equals(this.lastHub)) {
				highwaysList.remove(h);
				if (highwaysList.size() == 0) { // if no available highways then add it again
					highwaysList.add(h);
				}
				break;
			}

		}

		ArrayList<Highway> sortedHighways = new ArrayList<>(highwaysList);
		Collections.sort(sortedHighways, new SortingHighways(dest));

		for (Highway h : sortedHighways) {
			// returning the closest hub which also has capacity 
			if (h.hasCapacity()) { 
				return h;
			}
		}
		return null;
	}


	// processQ
	@Override
	protected synchronized void processQ(int deltaT) {
		ArrayList<Truck> temp = new ArrayList<>();
		for (Truck t : trucksQueue) {
			Highway _highway = getNextHighway(this, Network.getNearestHub(t.getDest()));
			if (t != null && _highway != null) {
				t.enter(_highway);
				_highway.add(t);
				temp.add(t);
				// remove(t);
			}
		}

		for (int i = 0; i < temp.size(); i++) {
			remove(temp.get(i));
		}

	}
	 
	// data members
	private LinkedList<Truck> trucksQueue = new LinkedList<Truck>(); // trucks queue
	private Hub lastHub;

}

// class to sort highways
class SortingHighways implements Comparator<Highway> {
	private Hub destination;

	SortingHighways(Hub dest) {
		this.destination = dest;
	}

	public int compare(Highway h1, Highway h2) {
		return destination.getLoc().distSqrd(h1.getEnd().getLoc())
				- destination.getLoc().distSqrd(h2.getEnd().getLoc());
	}
}
