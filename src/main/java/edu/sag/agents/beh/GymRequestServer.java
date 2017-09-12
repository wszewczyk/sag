package edu.sag.agents.beh;

import edu.sag.agents.GymAgent;
import edu.sag.agents.WeatherAgent;
import edu.sag.models.GymAgentInfo;
import edu.sag.models.Location;
import edu.sag.models.WeatherAgentInfo;
import edu.sag.utils.MessageTypes;
import edu.sag.utils.Parameters;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.lang.reflect.Method;

/**
 * Created by wmsze on 07.06.2017.
 */
public class GymRequestServer extends CyclicBehaviour {

    GymAgent myAgent;

    public GymRequestServer(GymAgent agent)
    {
        this.myAgent = agent;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null && msg.getUserDefinedParameter(Parameters.MESSAGE_TYPE) == (MessageTypes.GYM_REQUEST))
        {
            double lat = Double.parseDouble(msg.getUserDefinedParameter(Parameters.LAT));
            double lon = Double.parseDouble(msg.getUserDefinedParameter(Parameters.LON));

            double distance = this.myAgent.getDistanceToLocation(new Location(lat, lon));
            GymAgentInfo gymAgentInfo = this.myAgent.getGymAgentInfo();

            ACLMessage replay = msg.createReply();
            replay.setPerformative(ACLMessage.INFORM);
            replay.addUserDefinedParameter(Parameters.MESSAGE_TYPE, MessageTypes.GYM_RESPONSE);
            replay.addUserDefinedParameter(Parameters.DISTANCE, String.valueOf(distance));
            replay.addUserDefinedParameter(Parameters.LOAD, String.valueOf(gymAgentInfo.getLoad()));
            replay.addUserDefinedParameter(Parameters.LAT, String.valueOf(myAgent.getLocation().getLat()));
            replay.addUserDefinedParameter(Parameters.LON, String.valueOf(myAgent.getLocation().getLon()));
            replay.addUserDefinedParameter(Parameters.AGENT_NAME, myAgent.getName());
            replay.addUserDefinedParameter(Parameters.AGENT_ID, myAgent.getId());

            myAgent.send(replay);
        }
    }
}
