import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;


public class Elfo extends Agent{

    private String mensaje_recibido, mensaje_enviar;
    private int step;
    private final int AGENTE = 0, SANTA = 1;
    private boolean finalizadoFinal = false;

    public Elfo(){
        mensaje_recibido = "";
        mensaje_enviar = "";
    }

    protected String traducirMensaje(String mensaje, String agente){
        String aux = "", final_msg = "";
        if(agente.equals("Agente")){
             aux = mensaje.replace("Bro", "Rakas Joulupukki");
             final_msg = aux.replace("en plan", "Kiitos");
        }
        else if(agente.equals("SantaClaus")){
             aux = mensaje.replace("Hyvää joulua", "Bro");
             final_msg = aux.replace("Nähdään pian", "en plan"); 
        }
        return final_msg;
    }

    protected void comunicar(){
        switch (step) { 
            case 0: {
                ACLMessage msg = blockingReceive();

                if (msg.getPerformative() == ACLMessage.REQUEST) {
                    mensaje_recibido = msg.getContent();
                    mensaje_enviar = traducirMensaje(mensaje_recibido, msg.getSender().getLocalName());
                    System.out.print("Elfo: " + mensaje_enviar + "\n");
                    ACLMessage reply = msg.createReply(ACLMessage.INFORM);
                    reply.setContent(mensaje_enviar);
                    send(reply);
                    
                    finalizadoFinal = true;
                }
                else {
                    System.out.println("Error in the coversation protocol - step " + 1);
                    doDelete();
                }
                
            }
            
            
            /*default: {

                System.out.println("Error in the coversation protocol - step " + 2);
                doDelete(); 

            }*/
        } 
    } 
    
    @Override
    protected void setup() {

        //Se mueve y comprueba si está en la meta
        addBehaviour(new SimpleBehaviour() {
            @Override
            public void action() {
                comunicar();
            }

            @Override
            public boolean done() {
                return finalizadoFinal;
            }
        });

        //Imprime la energía consumida al final
        /*addBehaviour(new SimpleBehaviour() {
            @Override
            public void action() {
                if (finalizadoFinal) {
                    System.out.println("Energía consumida: " + energia);
                }
            }

            @Override
            public boolean done() {
                return finalizadoFinal;
            }
        });*/
    }

    @Override
    public void takeDown() {
        System.out.println("Terminating agent...");
    }
}
