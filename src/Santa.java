import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class Santa extends Agent{
    private static final double PROBABILIDAD_CONFIABLE = 0.8;
    private boolean renosCompletados ;
    private int posX, posY, step, contadorRenos;
    private String respuesta , mensaje;
    private ACLMessage msgAgente;
    private final String CODIGO_SECRETO = "1234";
    boolean finalizadoFinal = false;
    private String comienzoR = "Rakas Joulupukki " , finR = " Kiitos" , comienzoE = "Hyvää joulua,"  , finE = ",Nähdään pian";
    
    public Santa(int pX, int pY){
        posX = pX;
        posY = pY;
        msgAgente = new ACLMessage(ACLMessage.INFORM);
        step = contadorRenos = 0;
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
            System.out.println("Santa Claus: " + final_msg);
        }else{
            System.out.println("Santa Claus: El protocolo entre el Elfo y yo no es correcto");
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
                    ACLMessage reply;
                    if (esConfiable) {
                        mensaje = comienzoE + CODIGO_SECRETO + finE;
                        System.out.println("Santa Claus: Si confio en el agente");
                        
                        reply = msg.createReply(ACLMessage.AGREE);
                        
                    } else {
                        mensaje = comienzoE + "Lo siento, no eres digno" + finE;
                        System.out.println("Santa Claus: No confio en el agente");
                        reply = msg.createReply(ACLMessage.FAILURE);
                    }
                    
                    respuesta = traducir(mensaje);
                    reply.setContent(respuesta);
                    send(reply);
                    step++;
                   
                }else{
                    System.out.print("Pregunta invalida");
                }
                break;
            } 
            case 1:{
                ACLMessage msg = blockingReceive();
                if (msg.getPerformative() == ACLMessage.INFORM){
                    contadorRenos++;
                    System.out.println("Santa Claus: " + msg.getContent());
                    if (contadorRenos == 8){
                        System.out.println("Santa Claus: Todos los renos han sido encontrados");
                        renosCompletados = true;
                        step++;
                    }
                }
                else{
                    System.out.println("Santa Claus: Faltan renos por encontrar");
                }
                break;
            }
            case 2:{
                ACLMessage msg = blockingReceive();
                if (msg.getPerformative() == ACLMessage.REQUEST){
                    mensaje = comienzoE + posX + " " + posY + finE;
                    System.out.println("Santa Claus: Enviando mis coordenadas");
                    ACLMessage reply = msg.createReply(ACLMessage.INFORM);
                    mensaje = traducir(mensaje);
                    reply.setContent(mensaje);
                    send(reply);
                    step++;
                }
                else{
                    System.out.println("Santa Claus: Pregunta invalida");
                    ACLMessage reply = msg.createReply(ACLMessage.FAILURE);
                    reply.setContent("Pregunta invalida");
                    send(reply);
                }
                break;
            }
            case 3:{
                ACLMessage msg = blockingReceive();
                if (msg.getPerformative() == ACLMessage.INFORM){
                    mensaje = comienzoE + "HoHoHo!" + finE;
                    System.out.println("Santa Claus: Agradeciendo al agente");
                    ACLMessage reply = msg.createReply(ACLMessage.INFORM);
                    mensaje = traducir(mensaje);
                    reply.setContent(mensaje);
                    send(reply);
                    step++;
                }
                else{
                    System.out.println("Santa Claus: Pregunta invalida");
                    ACLMessage reply = msg.createReply(ACLMessage.FAILURE);
                    reply.setContent("Pregunta invalida");
                    send(reply);
                }
                break;
            }
        }  
    }
    
    @Override
    protected void setup() {
        msgAgente.addReceiver(new AID ("Agente", AID.ISLOCALNAME));

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
    }

    @Override
    public void takeDown() {
        System.out.println("Cerrando Santa...");
    }
}
