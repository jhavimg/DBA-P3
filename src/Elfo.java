package dbafinal;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class Elfo extends Agent{
    @Override 
    protected void setup(){
        System.out.println("Soy el elfo");
        
        int step = 0;
        
        switch(step){
            case 0 -> {
                ACLMessage msg = blockingReceive();
                
                if(msg.getPerformative() == ACLMessage.REQUEST){
                    ACLMessage replay = msg.createReply(ACLMessage.AGREE);
                    send(replay);
                }else{
                    System.out.println("Error en la conversacion elfo-agente");
                    doDelete();
                }
                
                ACLMessage msg2 = blockingReceive();
                System.out.println(msg2.getContent());
                
                if(msg2.getPerformative() == ACLMessage.INFORM){
                    ACLMessage replay2 = msg2.createReply(ACLMessage.INFORM);
                    replay2.setContent("Rakas Joulupukki, me das el codigo secreto, Kiitos");
                    send(replay2);
                }else{
                    System.out.println("Error en la conversacion elfo-agente");
                    doDelete();
                }
                
                
            }
        }
        
        
    }
}
