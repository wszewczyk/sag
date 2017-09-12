package edu.sag;

import jade.core.ProfileImpl;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

/**
 * Created by wmsze on 16.08.2017.
 */
public class MainContainer {
    public static void main(String[] args) {
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        ProfileImpl profile = new ProfileImpl(false);
        profile.setParameter(ProfileImpl.MAIN_HOST,"localhost");
        jade.wrapper.AgentContainer agentContainer = runtime.createAgentContainer(profile);
        try {
            AgentFactory.createAgents(agentContainer);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        try {
            agentContainer.start();
        } catch (ControllerException e) {

        }
    }
}
