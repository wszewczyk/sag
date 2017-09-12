package edu.sag.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.sag.models.WeatherAgentInfo;
import jade.core.AID;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wmsze on 05.06.2017.
 */
public class WeatherAgentService {
    private static WeatherAgentService agentInfoLoader;
    private String PATH_TO_JSON_FILE = "weather_agents_data.json";
    private List<WeatherAgentInfo> agentInfos;
    private List<AID> agents;

    private WeatherAgentService()
    {
        loadObjectFromFile();
    }

    public static WeatherAgentService getInstance()
    {
        if(agentInfoLoader == null)
        {
            agentInfoLoader = new WeatherAgentService();
        }
        return agentInfoLoader;
    }

    private void loadObjectFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_TO_JSON_FILE))) {
            Gson gson = new Gson();
            List<WeatherAgentInfo> agentInfos = gson.fromJson(br, new TypeToken<List<WeatherAgentInfo>>() {
            }.getType());
            this.agentInfos = agentInfos;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<WeatherAgentInfo> getWeatherAgentInfos()
    {
        return agentInfos;
    }

    public List<AID> getWeatherAgentsAids()
    {
        if(this.agents != null)
        {
            return this.agents;
        }
        else
        {
            this.agents = new ArrayList<>();
            for(WeatherAgentInfo info : agentInfos)
            {
                this.agents.add(new AID(info.getAgentName(),AID.ISLOCALNAME));
            }
            return this.agents;
        }
    }
}
