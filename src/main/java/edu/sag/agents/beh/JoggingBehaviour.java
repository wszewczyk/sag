package edu.sag.agents.beh;

import edu.sag.agents.MobileAgent;
import jade.core.behaviours.Behaviour;

/**
 * Created by wmsze on 28.05.2017.
 */
public class JoggingBehaviour extends Behaviour {

    private MobileAgent agent;

    public JoggingBehaviour(MobileAgent agent)
    {
        this.agent = agent;
    }

    @Override
    public void action() {
        System.out.println("Want to run");
    }

    @Override
    public boolean done() {
        return false;
    }
}
