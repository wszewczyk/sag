package edu.sag.agents;

/**
 * Created by wmsze on 15.08.2017.
 */

import edu.sag.models.Location;
import edu.sag.services.LocationService;
import jade.core.Agent;

public class LocationAgent extends Agent {
    protected Location location;

    public LocationAgent(){
    };

    public LocationAgent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getDistanceToLocation(Location loc) {
        return LocationService.getDistance(this.location, loc);
    }
}
