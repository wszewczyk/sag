package edu.sag.models;

import edu.sag.utils.enums.Weather;

/**
 * Created by wmsze on 05.06.2017.
 */
public class GymAgentInfo {

    private String agentId;

    private String agentName;

    private Location location;

    public double load;

    public GymAgentInfo(String agentId, String agentName, Location location, double  load) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.location = location;
        this.load = load;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getLoad() {
        return load;
    }

    public void setLoad(double load) {
        this.load = load;
    }
}
