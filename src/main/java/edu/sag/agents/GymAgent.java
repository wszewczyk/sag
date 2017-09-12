package edu.sag.agents;

import edu.sag.agents.beh.GymRequestServer;
import edu.sag.models.GymAgentInfo;
import edu.sag.utils.GymInfoLoader;

/**
 * Created by wmsze on 28.08.2017.
 */
public class GymAgent extends LocationAgent {

    private String id;
    private GymAgentInfo gymAgentInfo;
    private GymInfoLoader gymInfoLoader;

    public void setup(){
        this.gymInfoLoader = GymInfoLoader.getInstance();
        Object[] args = getArguments();
        if(args != null && args.length != 0)
        {
            try{
                this.id = (String) args[0];
                System.out.println("Agent id: " + this.id);
                this.gymAgentInfo = this.gymInfoLoader.getByAgentId(this.id);
                System.out.println("Load: " + this.gymAgentInfo.getLoad());

                this.location = gymAgentInfo.getLocation();
            }catch (Exception e){
                System.out.println("Incorrect gym agent data");
            }
        }else{
            System.out.println("No argument passed");
            this.doDelete();
        }

        addBehaviour(new GymRequestServer(this));
    }

    public GymAgentInfo getGymAgentInfo(){
        return this.gymAgentInfo;
    }

    public String getId(){
        return this.id;
    }

}
