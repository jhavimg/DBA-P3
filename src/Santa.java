package dbafinal;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.Random;

public class Santa extends Agent{
    private static final double PROBABILIDAD_CONFIABLE = 0.8;
    boolean esConfiable;
    
    @Override 
    protected void setup(){
        System.out.println("Soy Santa");
        
        int step = 0;
        switch(step){
            case 0 -> {
                ACLMessage msg = blockingReceive();
                
                esConfiable = new Random().nextDouble() < PROBABILIDAD_CONFIABLE;
                
                if(esConfiable){
                    ACLMessage replay = msg.createReply(ACLMessage.AGREE);
                    send(replay);
                }else{
                    ACLMessage replay = msg.createReply(ACLMessage.REFUSE);
                    send(replay);
                }
                
            }
        }
    }
}
