package edu.sag.agents.beh;

import edu.sag.agents.MobileAgent;
import edu.sag.utils.MessageTypes;
import edu.sag.utils.Ontonologies;
import edu.sag.utils.Parameters;
import edu.sag.utils.enums.ActivityType;
import edu.sag.utils.enums.ProposeResponse;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by wmsze on 06.09.2017.
 */
public class FootballPlayer extends CyclicBehaviour {

    private MobileAgent agent;
    private boolean alreadySign = false;

    public FootballPlayer(MobileAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchOntology(Ontonologies.FOOTBALL_PROPOSE_ONTONOLOGY);
        ACLMessage message = agent.receive(messageTemplate);

        if (message != null &&
                message.getUserDefinedParameter(Parameters.MESSAGE_TYPE) == (MessageTypes.FOOTBALL_PROPOSE) &&
                (agent.getActivityType() != ActivityType.FOOTBALL) || alreadySign) {
            ACLMessage replay = message.createReply();
            replay.setPerformative(ACLMessage.REJECT_PROPOSAL);
            replay.addUserDefinedParameter(Parameters.AGENT_NAME, agent.getLocalName());
            replay.addUserDefinedParameter(Parameters.MESSAGE_TYPE, MessageTypes.RESPONSE_FOR_FOOTBALL_PROPOSE);
            replay.addUserDefinedParameter(Parameters.FOOTBALL_RESPONSE, String.valueOf(ProposeResponse.NO));
            agent.send(replay);
            return;
        } else if (message != null &&
                message.getUserDefinedParameter(Parameters.MESSAGE_TYPE) == (MessageTypes.FOOTBALL_PROPOSE)) {
            System.out.println("Response for propose");
            String propose = message.getUserDefinedParameter(Parameters.FOOTBALL_PROPOSE);
            ProposeResponse proposeResponse = isProposeOk(propose);
            ACLMessage replay = message.createReply();
        replay.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            replay.setOntology(Ontonologies.FOOTBALL_RESPONSE_ONTONOLOGY);
            replay.addUserDefinedParameter(Parameters.AGENT_NAME, agent.getLocalName());
            replay.addUserDefinedParameter(Parameters.MESSAGE_TYPE, MessageTypes.RESPONSE_FOR_FOOTBALL_PROPOSE);
            replay.addUserDefinedParameter(Parameters.FOOTBALL_RESPONSE, String.valueOf(proposeResponse));
            agent.send(replay);
            return;
        }

        MessageTemplate matchInfoTempatee = MessageTemplate.MatchOntology(Ontonologies.FOOTBALL_INFO_ONTONOLOGY);
        ACLMessage matchInfo = agent.receive(matchInfoTempatee);
        if (matchInfo != null && (matchInfo.getUserDefinedParameter(Parameters.MESSAGE_TYPE) == (MessageTypes.MATCH_INFO))) {
            String info = matchInfo.getUserDefinedParameter(Parameters.FOOTBALL_MATCH_INFO);
            System.out.println("Match info received by " + this.agent.getLocalName() + " is: " + info);
            alreadySign = true;
        }
    }

    private ProposeResponse isProposeOk(String propose) {
        return ProposeResponse.YES;
    }
}
