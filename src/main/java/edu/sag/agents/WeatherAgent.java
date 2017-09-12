package edu.sag.agents;


import edu.sag.agents.beh.WeatherRequestServer;
import edu.sag.models.Location;
import edu.sag.models.WeatherAgentInfo;
import edu.sag.utils.WeatherInfoLoader;

/**
 * Created by wmsze on 28.05.2017.
 */
public class WeatherAgent extends LocationAgent {
    private WeatherAgentInfo weatherAgentInfo;
    private WeatherInfoLoader weatherInfoLoader;
    private String id;

    protected void setup() {
        this.weatherInfoLoader = WeatherInfoLoader.getInstance();
        Object[] args = getArguments();
        if (args != null && args.length != 0) {
            try {
                this.id = (String) args[0];
                System.out.println("Agent id: " + this.id);
                this.weatherAgentInfo = this.weatherInfoLoader.getByAgentId(this.id);
                System.out.println("Temperature: " + this.weatherAgentInfo.getTemperature());
                System.out.println("Weather: " + this.weatherAgentInfo.getWeather().toString());

                this.location = weatherAgentInfo.getLocation();
            } catch (Exception e) {
                System.out.println("Incorrect weather agent data");
                this.doDelete();
            }
        } else {
            System.out.println("No argument passed");
            this.doDelete();
        }

        addBehaviour(new WeatherRequestServer(this));
    }

    public String getWeather() {
        return this.weatherAgentInfo.toString();
    }

    public WeatherAgentInfo getWeatherInfo() {
        return this.weatherAgentInfo;
    }

    public String getId()
    {
        return this.id;
    }
}
