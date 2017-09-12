package edu.sag.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.sag.models.GymAgentInfo;
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
public class GymInfoLoader {
    private static final String PATH_TO_JSON_FILE = "gym_agents_data.json";
    private static GymInfoLoader gymInfoLoader;

    private static List<GymAgentInfo> gymAgentInfoObjects = new ArrayList<>();

    private GymInfoLoader() {
        loadObjectFromFile();
    }

    public synchronized static GymInfoLoader getInstance() {
        if (gymInfoLoader == null) {
            gymInfoLoader = new GymInfoLoader();
        }
        return gymInfoLoader;
    }

    public GymAgentInfo getByAgentId(String agentId) {
        return gymAgentInfoObjects.stream()
                .filter(a -> a.getAgentId().equals(agentId))
                .findFirst().get();
    }

    public List<String> getAgentIds() {
        return gymAgentInfoObjects.stream()
                .map(p -> p.getAgentId())
                .collect(Collectors.toList());
    }

    public void loadObjectFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_TO_JSON_FILE))) {
            Gson gson = new Gson();
            List<GymAgentInfo> gymInfos = gson.fromJson(br, new TypeToken<List<GymAgentInfo>>() {
            }.getType());
            this.gymAgentInfoObjects = gymInfos;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
