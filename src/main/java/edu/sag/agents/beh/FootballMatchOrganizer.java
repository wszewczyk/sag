package edu.sag.agents.beh;

import edu.sag.agents.MobileAgent;
import edu.sag.utils.Agents;
import edu.sag.utils.MessageTypes;
import edu.sag.utils.Ontonologies;
import edu.sag.utils.Parameters;
import edu.sag.utils.enums.MobileAgentManagerAction;
import edu.sag.utils.enums.ProposeResponse;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import javax.naming.OperationNotSupportedException;
import java.util.*;

/**
 * Created by wmsze on 03.09.2017.
 */
public class FootballMatchOrganizer extends Behaviour{
    private long actionStart;
    private boolean isBrokerResponseSend = false;
    private boolean isBrokerResponseReceived = false;
    private boolean isMobileAgentsProposeSent = false;
    private int positiveCount = 0;
    private int answers = 0;
    boolean isMobileAgentsResponsesReceived = false;
    boolean errorOccurred = false;
    boolean isMatchInfoSent = false;
    List<String> receivedMobileAgents = null;
    List<String> receivedPlayers = null;
    private MobileAgent agent;

    public FootballMatchOrganizer(MobileAgent agent){
        this.agent = agent;
        receivedMobileAgents = new ArrayList<String>();
        receivedPlayers = new ArrayList<String>();
    }

    @Override
    public void action() {
        actionStart = System.currentTimeMillis();
        if(!isBrokerResponseSend){
            ACLMessage cfp = new ACLMessage(ACLMessage.PROPOSE);
            AID brokerAid = new AID(Agents.BROKER_AGENT, AID.ISLOCALNAME);
            cfp.addUserDefinedParameter(Parameters.AGENT_NAME, agent.getLocalName());
            cfp.addUserDefinedParameter(Parameters.ACTION, String.valueOf(MobileAgentManagerAction.AGENT_LIST_REQUEST));
            cfp.addReceiver(brokerAid);
            myAgent.send(cfp);
            isBrokerResponseSend = true;
        }
        if(isBrokerResponseSend && !isBrokerResponseReceived)
        {
            ACLMessage replay = myAgent.receive();
            if( replay != null && replay.getUserDefinedParameter(Parameters.MESSAGE_TYPE) == (MessageTypes.MOBILE_AGENT_LIST_REPONSE)){
                try{
                    String mobileAgents = replay.getUserDefinedParameter(Parameters.AGENT_LIST_RESPONSE);
                    System.out.println("Recived agents:" + mobileAgents);
                    receivedMobileAgents = Arrays.asList(mobileAgents.split(","));
                    isBrokerResponseReceived = true;
                }
                catch (Exception e){
                    errorOccurred = true;
                }
            }
        }
        if(isBrokerResponseReceived && !isMobileAgentsProposeSent){
            if(isMinAgents()){
                ACLMessage cfp = new ACLMessage(ACLMessage.PROPOSE);
                cfp.setOntology(Ontonologies.FOOTBALL_PROPOSE_ONTONOLOGY);
                cfp.addUserDefinedParameter(Parameters.FOOTBALL_PROPOSE, "Football propose info.");
                cfp.addUserDefinedParameter(Parameters.MESSAGE_TYPE, MessageTypes.FOOTBALL_PROPOSE);
                for(int i = 0; i <  receivedMobileAgents.size(); i++) {
                    String nn = agent.getLocalName().trim();
                    String mobileAgent = receivedMobileAgents.get(i).trim();
                    if (!mobileAgent.equals(nn)) {
                        cfp.addReceiver(new AID(mobileAgent, AID.ISLOCALNAME));
                    }
                }
                agent.send(cfp);
                isMobileAgentsProposeSent = true;
            }
        }
        if(isMobileAgentsProposeSent)
        {
            MessageTemplate messageTemplate = MessageTemplate.MatchOntology(Ontonologies.FOOTBALL_RESPONSE_ONTONOLOGY);
            ACLMessage response = agent.receive(messageTemplate);
            if(response != null && response.getUserDefinedParameter(Parameters.MESSAGE_TYPE) == (MessageTypes.RESPONSE_FOR_FOOTBALL_PROPOSE)){
                String x = response.getUserDefinedParameter(Parameters.FOOTBALL_RESPONSE);
                ProposeResponse proposeResponse = ProposeResponse.valueOf(x);
                String playerName = response.getUserDefinedParameter(Parameters.AGENT_NAME);
                answers++;
                if(proposeResponse == ProposeResponse.YES)
                {
                    receivedPlayers.add(playerName);
                    positiveCount++;
                }


                isMobileAgentsResponsesReceived = answers == receivedMobileAgents.size() - 1;
            }
        }
    }

    @Override
    public boolean done() {
        if(errorOccurred){
            System.out.println("Error in match organizer.");
        }

        boolean condition = errorOccurred ||
                System.currentTimeMillis() - (this.actionStart + 10000) > 0 ||
                (isMobileAgentsResponsesReceived && !isEnoughPlayers());
//        System.out.println("isEnoughPlayers: " + isEnoughPlayers());
//        System.out.println("isMobileAgentsResponsesReceived: " + isMobileAgentsResponsesReceived);

        if(isMobileAgentsResponsesReceived && !isMatchInfoSent)
        {
            if(isEnoughPlayers()){

                String info = getFootballMatchInfo();

                ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                message.addUserDefinedParameter(Parameters.FOOTBALL_MATCH_INFO, info);
                message.addUserDefinedParameter(Parameters.MESSAGE_TYPE, MessageTypes.MATCH_INFO);
                message.setOntology(Ontonologies.FOOTBALL_INFO_ONTONOLOGY);
                for(int i = 0; i <  receivedPlayers.size(); i++) {
                    String player = receivedPlayers.get(i).trim();
                    if (!player.equals(agent.getLocalName().trim())){
                        message.addReceiver(new AID(player, AID.ISLOCALNAME));
                    }
                }
                agent.send(message);
                isMatchInfoSent = true;
            }
        }
        if(!isMinAgents() && isBrokerResponseReceived){
            condition = true;
        }
//        System.out.println("Condition: " + condition);

        return condition;
    }

    private boolean isEnoughPlayers()
    {
        return positiveCount > 0;
    }

    private boolean isMinAgents(){
        return receivedMobileAgents.size() > 1;
    }

    private String getFootballMatchInfo()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 1);
        String info = "Localization: " + String.valueOf(agent.getLocation().getLat()) + ", " + String.valueOf(agent.getLocation().getLon()) + " Time: " + cal.getTime().toString();
        return info;
    }
}
