package edu.sag.models;

import edu.sag.utils.enums.Weather;

/**
 * Created by wmsze on 05.06.2017.
 */
public class WeatherAgentInfo {

    private String agentId;

    private String agentName;

    private Location location;

    private double temperature;

    private Weather weather;

    public WeatherAgentInfo(String agentId, String agentName, Location location, double temperature, Weather weather) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.location = location;
        this.temperature = temperature;
        this.weather = weather;
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

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}
