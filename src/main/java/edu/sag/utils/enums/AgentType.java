package edu.sag.utils.enums;

import edu.sag.agents.MobileAgent;

/**
 * Created by wmsze on 28.05.2017.
 */
public enum AgentType {
    APP_AGENT(MobileAgent.class.getName());

    private String agentClass;

    AgentType(String agentClass) {
        this.agentClass = agentClass;
    }

    public String getAgentClass() {
        return agentClass;
    }
    }
