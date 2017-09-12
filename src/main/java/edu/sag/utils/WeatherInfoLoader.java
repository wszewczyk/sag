package edu.sag.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.sag.agents.WeatherAgent;
import edu.sag.models.WeatherAgentInfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wmsze on 04.06.2017.
 */
public class WeatherInfoLoader {
    private static final String PATH_TO_JSON_FILE = "weather_agents_data.json";
    private static WeatherInfoLoader weatherInfoLoader;

    private static List<WeatherAgentInfo> weatherAgentInfoObjects = new ArrayList<>();

    private WeatherInfoLoader() {
        loadObjectFromFile();
    }

    public synchronized static WeatherInfoLoader getInstance() {
        if (weatherInfoLoader == null) {
            weatherInfoLoader = new WeatherInfoLoader();
        }
        return weatherInfoLoader;
    }

    public WeatherAgentInfo getByAgentId(String agentId) {
        return weatherAgentInfoObjects.stream()
                .filter(a -> a.getAgentId().equals(agentId))
                .findFirst().get();
    }

    public List<String> getAgentIds() {
        return weatherAgentInfoObjects.stream()
                .map(p -> p.getAgentId())
                .collect(Collectors.toList());
    }

    public void loadObjectFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_TO_JSON_FILE))) {
            Gson gson = new Gson();
            List<WeatherAgentInfo> weatherInfos = gson.fromJson(br, new TypeToken<List<WeatherAgentInfo>>() {
            }.getType());
            this.weatherAgentInfoObjects = weatherInfos;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
