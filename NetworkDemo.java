package demo19006;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import base.Location;
import base.Network;
import base.Highway;
import base.Truck;
import base.Hub;
import base.Display;

class NetworkDemo extends Network {

    private ArrayList<Highway> highways = new ArrayList<>();
    private ArrayList<Truck> trucks = new ArrayList<>();
    private ArrayList<Hub> hubs = new ArrayList<>();

    public void add(Hub hub) {
        if (hub != null) // adding only if hub is not null
            hubs.add(hub);
    }

    public void add(Highway hwy) {
        if (hwy != null) // adding only if highway is not null
            highways.add(hwy);
    }

    public void add(Truck truck) {
        if (truck != null) // adding only if truck is not null
            trucks.add(truck);
    }

    public void start() {

        // starting all hubs
        for (Hub h : hubs) {
            h.start();
        }

        // starting all trucks
        for (Truck t : trucks) {
            t.start();
        }

    }

    // it is calling draw of every object of truck, hub, highway.
    public void redisplay(Display disp) {

        for (Truck t : trucks) {
            t.draw(disp);
        }

        for (Hub h : hubs) {
            h.draw(disp);
        }

        for (Highway hwy : highways) {
            hwy.draw(disp);
        }

    }

    // method to find nearest hub from a particular location
    protected Hub findNearestHubForLoc(Location loc) {
        Hub closest = hubs.get(0);
        double dist = 100000;
        for (Hub h : hubs) {

            // finding the closest hub by comparing distances
            if (Math.sqrt(h.getLoc().distSqrd(loc)) < dist) {
                dist = Math.sqrt(h.getLoc().distSqrd(loc));
                closest = h;
            }
        }

        return closest; // returning the nearest hub (closest distance one)
    }
}
