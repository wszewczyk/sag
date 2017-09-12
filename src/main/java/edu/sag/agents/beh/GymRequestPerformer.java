package edu.sag.agents.beh;

import edu.sag.agents.LocationAgent;
import edu.sag.agents.MobileAgent;
import edu.sag.models.GymAgentInfo;
import edu.sag.models.Location;
import edu.sag.models.WeatherAgentInfo;
import edu.sag.services.GymAgentService;
import edu.sag.utils.MessageTypes;
import edu.sag.utils.Parameters;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wmsze on 02.09.2017.
 */
public class GymRequestPerformer extends Behaviour{
    GymAgentService gymAgentService;

    private int step = 0;
    private int recivedMessages = 0;
    private long actionStart;
    private List<GymAgentInfo> gymAgentInfoList;
    private List<AID> gymAgentsAids;

    public GymRequestPerformer(){
        this.gymAgentService = GymAgentService.getInstance();
        this.gymAgentInfoList = new ArrayList<GymAgentInfo>();
    }

    @Override
    public void action() {
        if (this.step == 0) {
            actionStart = System.currentTimeMillis();
        }
        switch(step)
        {
            case 0:
                ACLMessage cfp = new ACLMessage(ACLMessage.REQUEST);
                gymAgentsAids = this.gymAgentService.getWeatherAgentsAids();
                cfp.addUserDefinedParameter(Parameters.MESSAGE_TYPE, MessageTypes.GYM_REQUEST);
                cfp.addUserDefinedParameter(Parameters.LAT, String.valueOf(((LocationAgent) myAgent).getLocation().getLat()));
                cfp.addUserDefinedParameter(Parameters.LON, String.valueOf(((LocationAgent) myAgent).getLocation().getLon()));
                for (int i = 0; i < gymAgentsAids.size(); i++) {
                    cfp.addReceiver(gymAgentsAids.get(i));
                }
                myAgent.send(cfp);
                step++;
                break;
            case 1:
                ACLMessage replay = myAgent.receive();
                if(replay != null && replay.getUserDefinedParameter(Parameters.MESSAGE_TYPE) == (MessageTypes.GYM_RESPONSE))
                {
                    try
                    {
                        String replayContent = replay.getContent();

                        String gymAgentId = replay.getUserDefinedParameter(Parameters.AGENT_ID);
                        String gymAgentName = replay.getUserDefinedParameter(Parameters.AGENT_NAME);
                        Location location = new Location(Double.parseDouble(replay.getUserDefinedParameter(Parameters.LAT)),
                                Double.parseDouble(replay.getUserDefinedParameter(Parameters.LON)));
                        double load = Double.parseDouble(replay.getUserDefinedParameter(Parameters.LOAD));
                        this.gymAgentInfoList.add(new GymAgentInfo(gymAgentId,gymAgentName,location,load));
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
        boolean done = recivedMessages == gymAgentsAids.size() || System.currentTimeMillis() - (this.actionStart + 10000) > 0;
        if(done)
        {
            ((MobileAgent)this.myAgent).chooseGym(gymAgentInfoList);
        }
        return done;
    }
}
