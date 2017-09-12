package edu.sag.agents;

import edu.sag.agents.beh.*;
import edu.sag.models.GymAgentInfo;
import edu.sag.models.Location;
import edu.sag.models.WeatherAgentInfo;
import edu.sag.utils.Agents;
import edu.sag.utils.MessageTypes;
import edu.sag.utils.Parameters;
import edu.sag.utils.enums.ActivityType;
import edu.sag.utils.enums.MobileAgentManagerAction;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.List;

/**
 * Created by wmsze on 28.05.2017.
 */
public class MobileAgent extends LocationAgent{

    private ActivityType activityType;

    protected void setup()
    {
        Object[] args = getArguments();
        try
        {
            this.location = new Location();
            for (int i = 0; i<args.length; i++) {
                if(i == 0)
                {
                    this.location.setLat(Double.parseDouble((String)args[i]));
                }
                else if(i == 1)
                {
                    this.location.setLon(Double.parseDouble((String)args[i]));
                }
                else if(i == 2)
                {
                    this.activityType = ActivityType.valueOf((String)args[i]);
                }
            }

        }
        catch(Exception e)
        {
            this.deleteAgent();
        }

        addBehaviour(new ReportMobileAgent(this));

        switch (this.activityType)
        {
            case JOGGING:
                addBehaviour(new WeatherRequestPerformer());
                break;
            case GYM:
                addBehaviour(new GymRequestPerformer());
                break;
            case FOOTBALL:
                addBehaviour(new FootballMatchOrganizer(this));
                addBehaviour(new FootballPlayer(this));
                break;
            default:
                break;
        }
    }

    public void decideAboutJogging(List<WeatherAgentInfo> weatherAgentInfoList)
    {
        double res = 0;
        for(WeatherAgentInfo weatherAgentInfo : weatherAgentInfoList)
        {
            switch (weatherAgentInfo.getWeather())
            {
                case RAINY:
                    res += -1 * this.getDistanceToLocation(weatherAgentInfo.getLocation());
                    break;
                case SUNNY:
                    res += getDistanceToLocation(weatherAgentInfo.getLocation());
                    break;
                default:
                    break;
            }
        }
        if(res > 0)
        {
            System.out.println("You can go jogging");
        }
        else
        {
            System.out.println("You cannot go jogging");
        }
    }

    public void chooseGym(List<GymAgentInfo> gymAgentInfos)
    {
        if(gymAgentInfos.size() == 0)
        {
            System.out.println("There is no gym nearby");
            return;
        }

        String bestGym = gymAgentInfos.get(0).getAgentName();
        double bestRate = rateGym(gymAgentInfos.get(0));
        for(int i = 1; i < gymAgentInfos.size(); i++)
        {
            double rate = rateGym(gymAgentInfos.get(i));
            if(rate > bestRate){
                bestGym = gymAgentInfos.get(i).getAgentName();
                bestRate = rate;
            }
        }
        System.out.println("Best gym for you is: " + bestGym);
    }

    private double rateGym(GymAgentInfo gymAgentInfo)
    {
        double load = gymAgentInfo.load;
        double distance = this.getDistanceToLocation(gymAgentInfo.getLocation());
        return - load - distance;
    }

    private void deleteAgent()
    {
        System.out.println("Failure during mobile agent initialization");
        doDelete();
    }

    @Override
    public void doDelete() {
        ACLMessage informMessage = new ACLMessage(ACLMessage.INFORM);
        AID brokerAgentAid = new AID(Agents.BROKER_AGENT, AID.ISLOCALNAME);
        informMessage.addUserDefinedParameter(Parameters.AGENT_NAME, this.getLocalName());
        informMessage.addUserDefinedParameter(Parameters.ACTION, String.valueOf(MobileAgentManagerAction.REMOVE));
        informMessage.addUserDefinedParameter(Parameters.MESSAGE_TYPE, MessageTypes.UNREGISTER_WITH_BROKER);
        informMessage.addReceiver(brokerAgentAid);
        this.send(informMessage);
        super.doDelete();
    }

    public ActivityType getActivityType()
    {
        return this.activityType;
    }
}
