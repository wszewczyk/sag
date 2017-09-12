package edu.sag.agents;

import edu.sag.agents.beh.ReportMobileAgentHandler;
import edu.sag.utils.MessageTypes;
import edu.sag.utils.Parameters;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
/**
 * Created by wmsze on 03.09.2017.
 */
public class BrokerAgent extends Agent{
    private List<String> mobileAgents;

    public BrokerAgent()
    {
        mobileAgents = new ArrayList<>();
    }

    protected void setup()
    {
        addBehaviour(new ReportMobileAgentHandler(this));
    }

    public void addMobileAgent(String agentName)
    {
        boolean exists = false;
        for(String ma : this.mobileAgents)
        {
            if(ma == agentName)
            {
                exists = true;
            }
        }

        if(!exists)
        {
            this.mobileAgents.add(agentName);
        }
    }

    public void removeMobileAgent(String agentName)
    {
        this.mobileAgents.remove(agentName);
    }

    public List<String> getMobileAgents()
    {
        return this.mobileAgents;
    }

    public void sendRequestedList(String agentName) {
        ACLMessage informMessage = new ACLMessage(ACLMessage.INFORM);
        AID agentAid = new AID(agentName, AID.ISLOCALNAME);
        String requestedList = StringUtils.join(mobileAgents, ',');
        informMessage.addUserDefinedParameter(Parameters.AGENT_LIST_RESPONSE, requestedList);
        informMessage.addReceiver(agentAid);
        informMessage.addUserDefinedParameter(Parameters.MESSAGE_TYPE, MessageTypes.MOBILE_AGENT_LIST_REPONSE);
        this.send(informMessage);
    }
}
