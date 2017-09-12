package edu.sag.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.sag.models.GymAgentInfo;
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
public class GymAgentService {
    private static GymAgentService agentInfoLoader;
    private String PATH_TO_JSON_FILE = "gym_agents_data.json";
    private List<GymAgentInfo> agentInfos;
    private List<AID> agents;

    private GymAgentService()
    {
        loadObjectFromFile();
    }

    public static GymAgentService getInstance()
    {
        if(agentInfoLoader == null)
        {
            agentInfoLoader = new GymAgentService();
        }
        return agentInfoLoader;
    }

    private void loadObjectFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_TO_JSON_FILE))) {
            Gson gson = new Gson();
            List<GymAgentInfo> agentInfos = gson.fromJson(br, new TypeToken<List<GymAgentInfo>>() {
            }.getType());
            this.agentInfos = agentInfos;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<GymAgentInfo> getGymAgentInfos()
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
            for(GymAgentInfo info : agentInfos)
            {
                this.agents.add(new AID(info.getAgentName(),AID.ISLOCALNAME));
            }
            return this.agents;
        }
    }
}
