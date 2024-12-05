package dbafinal;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import static jade.lang.acl.ACLParserConstants.CONVERSATION_ID;

public class Agente extends Agent{
    
    @Override 
    protected void setup(){
        int step = 0;
        System.out.println("Soy el agente");
        
        switch(step){
            case 0 -> {
                // Hablamos con el elfo para traducir
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.addReceiver(new AID("Elfo", AID.ISLOCALNAME));
                msg.setConversationId("conver-agente-elfo");
                send(msg);
                
                ACLMessage msg2 = blockingReceive();
                
                if(msg2.getConversationId().equals("conver-agente-elfo") && msg2.getPerformative() == ACLMessage.AGREE){
                    ACLMessage replay = msg2.createReply(ACLMessage.INFORM);
                    replay.setContent("Bro, me das el codigo secreto, en plan");
                    send(replay);
                }else{
                    System.out.println("Error en empezar conversacion de agente con elfo");
                    doDelete();
                }
                
                ACLMessage msg3 = blockingReceive();
                String traduccion = msg3.getContent();
                System.out.println(traduccion);
                // Aqui conecta con Santa
                
                if(msg3.getConversationId().equals("conver-agente-elfo") && msg3.getPerformative() == ACLMessage.INFORM){
                    ACLMessage msgSanta = new ACLMessage(ACLMessage.REQUEST);
                    msgSanta.addReceiver(new AID("Santa", AID.ISLOCALNAME));
                    msgSanta.setConversationId("conver-agente-santa");
                    send(msgSanta);
                }
                
                ACLMessage msg4 = blockingReceive();
                if(msg4.getConversationId().equals("conver-agente-santa") && msg4.getPerformative() == ACLMessage.AGREE){
                    System.out.println("Santa Claus confirma que podemos trabajar con él");
                }else if(msg4.getConversationId().equals("conver-agente-santa") && msg4.getPerformative() == ACLMessage.REFUSE){
                    System.out.println("Santa Claus rechaza nuestros servicios");
                }else{
                    System.out.println("Error en la conversacion agente-santa");
                }
                
            }
        }
    }
    
}
