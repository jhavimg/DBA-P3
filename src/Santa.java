import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class Santa extends Agent{
    private static final double PROBABILIDAD_CONFIABLE = 0.8;
    private boolean esperandoRespuesta , renosCompletados , respuestaRecibida;
    int posX, posY, step;
    String respuesta , mensaje;
    ACLMessage msgAgente;
    private final int AGENTE = 0, ELFO = 1;
    private final String CODIGO_SECRETO = "1234";
    boolean finalizadoFinal = false;
    String comienzoR = "Rakas Joulupukki " , finR = " Kiitos" , comienzoE = "Hyvää joulua,"  , finE = ",Nähdään pian";
    
    public Santa(int pX, int pY){
        posX = pX;
        posY = pY;
        msgAgente = new ACLMessage(ACLMessage.INFORM);
        msgAgente.addReceiver(new AID ("Agente", AID.ISLOCALNAME));
        respuestaRecibida = esperandoRespuesta = false;
        step = 0;
    }
    
    //Función para enviar un mensaje al Elfo para que lo traduzca
    private String traducir(String mensaje){
        String final_msg = "";
        
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Elfo", AID.ISLOCALNAME));
        msg.setContent(mensaje); 
        send(msg);
        
        msg = blockingReceive();
        if (msg.getPerformative() == ACLMessage.INFORM) {
            final_msg = msg.getContent();
            System.out.print("Santa Claus: " + final_msg + "\n");
        }else{
            System.out.print("Santa Claus: El protocolo entre el Elfo y yo no es correcto\n ");
            doDelete();
        }
        
        return final_msg;
    }
    
    protected void comunicar(){
        switch (step) { 
            case 0: {
                ACLMessage msg = blockingReceive();
                if (msg.getPerformative() == ACLMessage.REQUEST) {
                    boolean esConfiable = new Random().nextDouble() < PROBABILIDAD_CONFIABLE;
                    ACLMessage replay;
                    if (esConfiable) {
                        mensaje = comienzoE + CODIGO_SECRETO + finE;
                        System.out.print("Santa Claus: Si confio en el agente\n");
                        
                        replay = msg.createReply(ACLMessage.AGREE);
                        
                    } else {
                        mensaje = comienzoE + "Lo siento, no eres digno" + finE;
                        System.out.print("Santa Claus: No confio en el agente\n");
                        replay = msg.createReply(ACLMessage.FAILURE);
                    }
                    
                    respuesta = this.traducir(mensaje);
                    replay.setContent(respuesta);
                    send(replay);
                        
                   
                    this.finalizadoFinal = true;
                }else{
                    System.out.print("Pregunta invalida");
                }
            } 
            
            /*default:{
                System.out.println("Error in the coversation protocol - step " + 2);
                doDelete(); 
            } */
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
