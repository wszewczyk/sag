package edu.sag;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.wrapper.ControllerException;

import java.util.Properties;

/**
 * Created by wmsze on 28.05.2017.
 */
public class MainPlatform {
    public static void main(String[] args) {
        try {
            Runtime runtime = Runtime.instance();
            Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "true");
            ProfileImpl profileImpl = new ProfileImpl((jade.util.leap.Properties) properties);
            jade.wrapper.AgentContainer agentContainer = runtime.createMainContainer(profileImpl);
            agentContainer.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}
