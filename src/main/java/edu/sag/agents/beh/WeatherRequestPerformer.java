package edu.sag.agents.beh;

import edu.sag.agents.LocationAgent;
import edu.sag.agents.MobileAgent;
import edu.sag.agents.WeatherAgent;
import edu.sag.models.Location;
import edu.sag.models.WeatherAgentInfo;
import edu.sag.utils.MessageTypes;
import edu.sag.utils.Parameters;
import edu.sag.services.WeatherAgentService;
import edu.sag.utils.enums.Weather;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import javax.management.StringValueExp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wmsze on 04.06.2017.
 */
public class WeatherRequestPerformer extends Behaviour {

    WeatherAgentService weatherAgentService;

    private int step = 0;
    private int recivedMessages = 0;
    private long actionStart;
    private List<WeatherAgentInfo> weatherAgentInfoList;
    private List<AID> weatherAgentsAids;

    public WeatherRequestPerformer() {
        this.weatherAgentService = WeatherAgentService.getInstance();
        this.weatherAgentInfoList = new ArrayList<WeatherAgentInfo>();
    }

    @Override
    public void action() {

        if (this.step == 0) {
            actionStart = System.currentTimeMillis();
        }

        switch (this.step) {
            case 0:
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                weatherAgentsAids = this.weatherAgentService.getWeatherAgentsAids();
                cfp.addUserDefinedParameter(Parameters.MESSAGE_TYPE, MessageTypes.WEATHER_REQUEST);
                cfp.addUserDefinedParameter(Parameters.LAT, String.valueOf(((LocationAgent) myAgent).getLocation().getLat()));
                cfp.addUserDefinedParameter(Parameters.LON, String.valueOf(((LocationAgent) myAgent).getLocation().getLon()));
                for (int i = 0; i < weatherAgentsAids.size(); i++) {
                    cfp.addReceiver(weatherAgentsAids.get(i));
                }
                myAgent.send(cfp);
                step++;
                break;
            case 1:
                ACLMessage replay = myAgent.receive();
                if (replay != null && replay.getUserDefinedParameter(Parameters.MESSAGE_TYPE) == (MessageTypes.WEATHER_RESPONSE)) {
                    try
                    {
                        String replayContent = replay.getContent();

                        String weatherAgentId = replay.getUserDefinedParameter(Parameters.AGENT_ID);
                        String weatherAgentName = replay.getUserDefinedParameter(Parameters.AGENT_NAME);
                        Location location = new Location(Double.parseDouble(replay.getUserDefinedParameter(Parameters.LAT)),
                                Double.parseDouble(replay.getUserDefinedParameter(Parameters.LON)));
                        double temperature = Double.parseDouble(replay.getUserDefinedParameter(Parameters.TEMPERATURE));
                        Weather weather = Weather.valueOf(replay.getUserDefinedParameter(Parameters.WEATHER));

                        double distance = ((LocationAgent) this.myAgent).getDistanceToLocation(location);

                        System.out.println("Recived weatherAgentId: " + weatherAgentId);
                    System.out.println("Recived weather: " + weather);
//                    System.out.println("Recived weatherAgentName: " + weatherAgentName);
//                    System.out.println("Recived distance: " + String.valueOf(distance));
//                    System.out.println("Recived temperature: " + temperature);

                        this.weatherAgentInfoList.add(new WeatherAgentInfo(weatherAgentId, weatherAgentName, location, temperature, weather));
                        recivedMessages++;
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error: bad reciving");
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean done() {
        boolean done = recivedMessages == weatherAgentsAids.size() || System.currentTimeMillis() - (this.actionStart + 10000) > 0;

        if(done)
        {
            ((MobileAgent)this.myAgent).decideAboutJogging(this.weatherAgentInfoList);
        }

        return done;
    }
}
