import jade.core.Agent;
import jade.lang.acl.ACLMessage;


public class Elfo extends Agent{

    private String mensaje_recibido, mensaje_enviar;
    private int step;
    private final int AGENTE = 0, SANTA = 1;

    public Elfo(){
        mensaje_recibido = "";
        mensaje_enviar = "";
    }

    protected String traducirMensaje(String mensaje, boolean agente){
        String aux = "", final_msg = "";
        if(agente){
             aux = mensaje.replace("Bro", "Rakas Joulupukki");
             final_msg = aux.replace("en plan", "Kiitos");
        }
        else{
             aux = mensaje.replace("Hyvää joulua", "Bro");
             final_msg = aux.replace("Nähdään pian", "en plan"); 
        }
        return final_msg;
    }

    protected void comunicar(){
        switch (step) { 
            case AGENTE: {
                ACLMessage msg = blockingReceive();

                if (msg.getPerformative() == ACLMessage.REQUEST) {
                    mensaje_recibido = msg.getContent();
                    mensaje_enviar = traducirMensaje(mensaje_recibido, true);
                    
                    ACLMessage reply = msg.createReply(ACLMessage.INFORM);
                    msg.setContent(mensaje_enviar);
                    send(reply);
                    step++;
                }
                else {
                    System.out.println("Elfo: Error in the coversation protocol - step " + 1);
                    doDelete();
                }
                
            }
            
            case SANTA: {

                ACLMessage msg = blockingReceive();

                if (msg.getPerformative() == ACLMessage.REQUEST) {
                    mensaje_recibido = msg.getContent();
                    mensaje_enviar = traducirMensaje(mensaje_recibido, false);
                    
                    ACLMessage reply = msg.createReply(ACLMessage.INFORM);
                    msg.setContent(mensaje_enviar);
                    send(reply);
                    step--;
                }
                else {
                    System.out.println("Elfo: Error in the coversation protocol - step " + 1);
                    doDelete();
                }

            }

            default: {

                System.out.println("Elfo: Error in the coversation protocol - step " + 2);
                doDelete(); 

            }
        } 
    }   
}
