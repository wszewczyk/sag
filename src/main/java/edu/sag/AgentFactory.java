package edu.sag;

import edu.sag.agents.BrokerAgent;
import edu.sag.agents.GymAgent;
import edu.sag.agents.WeatherAgent;
import edu.sag.models.GymAgentInfo;
import edu.sag.models.WeatherAgentInfo;
import edu.sag.services.GymAgentService;
import edu.sag.services.WeatherAgentService;
import edu.sag.utils.Agents;
import jade.tools.sniffer.AgentList;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.util.List;

public class AgentFactory {
    private static final boolean SIMPLE_CREATION = false;
    private static WeatherAgentService weatherAgentService = WeatherAgentService.getInstance();
    private static GymAgentService gymAgentService = GymAgentService.getInstance();

    public static void createAgents(AgentContainer agentContainer) throws ControllerException {
        AgentList agentList = new AgentList();
        if (SIMPLE_CREATION) {
            simpleCreation(agentContainer);
        } else {
            complexCreation(agentContainer);
        }
    }

    private static void simpleCreation(AgentContainer agentContainer) throws StaleProxyException {

    }

    private static void complexCreation(AgentContainer agentContainer) throws ControllerException {
        List<WeatherAgentInfo> weatherAgentInfos = weatherAgentService.getWeatherAgentInfos();
        List<GymAgentInfo> gymAgentInfos = gymAgentService.getGymAgentInfos();
        for(int i = 0; i < weatherAgentInfos.size(); i++)
        {
            agentContainer.createNewAgent(weatherAgentInfos.get(i).getAgentName(), WeatherAgent.class.getName(), new Object[]{String.valueOf(i + 1)}).start();
        }
        for(int i = 0; i < gymAgentInfos.size(); i++)
        {
            agentContainer.createNewAgent(gymAgentInfos.get(i).getAgentName(), GymAgent.class.getName(), new Object[]{String.valueOf(i + 1)}).start();
            System.out.println("Load: " + String.valueOf(gymAgentInfos.get(i).getLoad()));
        }
        agentContainer.createNewAgent(Agents.BROKER_AGENT, BrokerAgent.class.getName(), null).start();

    }
}
