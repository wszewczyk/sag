package edu.sag.agents.beh;

import edu.sag.agents.WeatherAgent;
import edu.sag.models.Location;
import edu.sag.models.WeatherAgentInfo;
import edu.sag.utils.MessageTypes;
import edu.sag.utils.Parameters;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Created by wmsze on 07.06.2017.
 */
public class WeatherRequestServer extends CyclicBehaviour {

    WeatherAgent myAgent;

    public WeatherRequestServer(WeatherAgent agent)
    {
        this.myAgent = agent;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null && msg.getUserDefinedParameter(Parameters.MESSAGE_TYPE) == MessageTypes.WEATHER_REQUEST)
        {
            double lat = Double.parseDouble(msg.getUserDefinedParameter(Parameters.LAT));
            double lon = Double.parseDouble(msg.getUserDefinedParameter(Parameters.LON));

            double distance = this.myAgent.getDistanceToLocation(new Location(lat, lon));
            WeatherAgentInfo weatherAgentInfo = this.myAgent.getWeatherInfo();

            ACLMessage replay = msg.createReply();
            String weather = myAgent.getWeather();
            replay.setPerformative(ACLMessage.INFORM);
            replay.addUserDefinedParameter(Parameters.MESSAGE_TYPE, MessageTypes.WEATHER_RESPONSE);
            replay.addUserDefinedParameter(Parameters.DISTANCE, String.valueOf(distance));
            replay.addUserDefinedParameter(Parameters.TEMPERATURE, String.valueOf(weatherAgentInfo.getTemperature()));
            replay.addUserDefinedParameter(Parameters.WEATHER, String.valueOf(weatherAgentInfo.getWeather()));
            replay.addUserDefinedParameter(Parameters.LAT, String.valueOf(myAgent.getLocation().getLat()));
            replay.addUserDefinedParameter(Parameters.LON, String.valueOf(myAgent.getLocation().getLon()));
            replay.addUserDefinedParameter(Parameters.AGENT_NAME, myAgent.getName());
            replay.addUserDefinedParameter(Parameters.AGENT_ID, myAgent.getId());

            replay.setContent(weather);
            myAgent.send(replay);
        }
    }
}
