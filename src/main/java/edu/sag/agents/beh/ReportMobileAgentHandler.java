package edu.sag.agents.beh;

import edu.sag.agents.BrokerAgent;
import edu.sag.agents.MobileAgent;
import edu.sag.utils.Agents;
import edu.sag.utils.Parameters;
import edu.sag.utils.enums.MobileAgentManagerAction;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Created by wmsze on 05.09.2017.
 */
public class ReportMobileAgentHandler extends CyclicBehaviour{

    private BrokerAgent brokerAgent;

    public ReportMobileAgentHandler(BrokerAgent brokerAgent)
    {
        this.brokerAgent = brokerAgent;
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null)
        {
            try
            {
                String agentName = msg.getUserDefinedParameter(Parameters.AGENT_NAME);
                MobileAgentManagerAction action = MobileAgentManagerAction.valueOf(msg.getUserDefinedParameter(Parameters.ACTION));
//                ACLMessage replay = msg.createReply();

                switch (action)
                {
                    case ADD:
                        brokerAgent.addMobileAgent(agentName);
//                        replay.addUserDefinedParameter(Parameters.ACTION, String.valueOf(MobileAgentManagerAction.ADDED));
                        break;
                    case REMOVE:
                        brokerAgent.removeMobileAgent(agentName);
//                        replay.addUserDefinedParameter(Parameters.ACTION, String.valueOf(MobileAgentManagerAction.REMOVED));
                        break;
                    case AGENT_LIST_REQUEST:
                        brokerAgent.sendRequestedList(agentName);
                    default:
                        break;
                }

            }
            catch (Exception e){
                System.out.println("Error in broker agent beh.");
            }
        }
    }
}
