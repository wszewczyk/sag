package edu.sag.agents.beh;

import edu.sag.agents.MobileAgent;
import edu.sag.utils.Agents;
import edu.sag.utils.MessageTypes;
import edu.sag.utils.Parameters;
import edu.sag.utils.enums.ActivityType;
import edu.sag.utils.enums.MobileAgentManagerAction;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Created by wmsze on 05.09.2017.
 */
public class ReportMobileAgent extends OneShotBehaviour{

    MobileAgent agent;

    public ReportMobileAgent(MobileAgent agent){
        this.agent = agent;
    }

    @Override
    public void action() {
        ACLMessage cfp = new ACLMessage(ACLMessage.INFORM);
        AID brokerAgentAid = new AID(Agents.BROKER_AGENT, AID.ISLOCALNAME);
        cfp.addUserDefinedParameter(Parameters.AGENT_NAME, agent.getLocalName());
        cfp.addUserDefinedParameter(Parameters.ACTION, String.valueOf(MobileAgentManagerAction.ADD));
        cfp.addReceiver(brokerAgentAid);
        cfp.addUserDefinedParameter(Parameters.MESSAGE_TYPE, MessageTypes.REGISTER_WITH_BROKER);
        myAgent.send(cfp);
    }
}
