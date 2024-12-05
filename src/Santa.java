import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class Santa extends Agent{
    private static final double PROBABILIDAD_CONFIABLE = 0.8;
    private boolean esperandoRespuesta , renosCompletados , respuestaRecibida;
    int posX, posY, step;
    String textoMsg , respuesta;
    ACLMessage msgAgente;
    boolean esConfiable;
    private final int AGENTE = 0, ELFO = 1;
    private final String CODIGO_SECRETO = "1234";
    
    public Santa(int pX, int pY){
        posX = pX;
        posY = pY;
        msgAgente = new ACLMessage(ACLMessage.INFORM);
        msgAgente.addReceiver(new AID ("Agente", AID.ISLOCALNAME));
        respuestaRecibida = esperandoRespuesta = false;
        step = 0;
    }

    protected void comunicar(){
        switch (step) { 
            case AGENTE: {
                if (!renosCompletados){
                    //Si no tenemos la respuesta del elfo
                    if(!respuestaRecibida){
                        msgAgente = blockingReceive();
                        if (msgAgente.getPerformative() == ACLMessage.INFORM) {
                            
                            esConfiable = new Random().nextDouble() < PROBABILIDAD_CONFIABLE;
                            if (esConfiable) {
                                String codigoSecreto = CODIGO_SECRETO;
                                textoMsg = "Hyvää joulua, Eres digno. Código secreto: " + codigoSecreto + ", Nähdään pian";
                            } else {
                                textoMsg = "Hyvää joulua, Lo siento, no eres digno, Nähdään pian";
                            }

                            /*ACLMessage msgElfo = new ACLMessage(ACLMessage.REQUEST);
                            msgElfo.addReceiver(new AID("Elfo", AID.ISLOCALNAME));
                            msgElfo.setContent("Bro, me das el código secreto, en plan"); 
                            send(msgElfo);*/
                            //esperandoRespuesta = true;
                            step = ELFO;
                        }
                        else {
                            System.out.println("Santa: Error in the coversation protocol - step " + 0);
                            doDelete();
                        }
                    }
                    //Si no tenemos la respuesta del elfo
                    else{
                        ACLMessage replay;
                        //Si es confiable le mandamos el mensaje pero sino le madamos el fallo
                        if(esConfiable){
                            replay = msgAgente.createReply(ACLMessage.INFORM);
                            replay.setContent(respuesta);
                        }else{
                            replay = msgAgente.createReply(ACLMessage.FAILURE);
                            replay.setContent(respuesta);
                        }
                        
                        send(replay);
                    }
                }
                //Si se han encontrado los renos 
                else {
                    //Envía mensaje al Elfo
                    if (!respuestaRecibida) {
                        msgAgente = blockingReceive();
                        if (msgAgente.getPerformative() == ACLMessage.INFORM && msgAgente.getContent().contains("ya he encontrado a todos los renos, me pasas tu ubicación?")) {
                            textoMsg = "Hyvää joulua, Mi ubicación es : " + posX + " " + posY + ", Nähdään pian";
                            step = ELFO;
                        }
                        //Mensaje para el HOHOHO
                        else if(msgAgente.getPerformative() == ACLMessage.INFORM && msgAgente.getContent().contains("estoy contigo")){
                            textoMsg = "Hyvää joulua, HoHoHo! , Nähdään pian";
                            step = ELFO;
                        }
                        else {
                            System.out.println("Santa: Error in the coversation protocol - step " + 0);
                            doDelete();
                        }
                    }
                    //Recibe mensaje del Elfo
                    else{
                        ACLMessage replay = msgAgente.createReply(ACLMessage.INFORM);
                        replay.setContent(respuesta);
                        send(replay);
                    }
                }
            } 
            case ELFO: { 
                if (!renosCompletados){
                    //Envía mensaje al traductor
                    if (!esperandoRespuesta){
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.addReceiver(new AID("Elfo", AID.ISLOCALNAME));
                        msg.setContent(textoMsg); 
                        send(msg);
                        esperandoRespuesta = true;
                        respuestaRecibida = false;
                    }
                    //Recibe mensaje del traductor
                    else{
                        ACLMessage msg = blockingReceive();
                        if (msg.getPerformative() == ACLMessage.INFORM) {
                            respuesta = msg.getContent();
                            //step = text;
                            esperandoRespuesta = false;
                            respuestaRecibida = true;
                            step = AGENTE;
                        }
                        else {
                            System.out.println("Santa: Error in the coversation protocol - step " + 1);
                            doDelete();
                        }
                    }
                }
                //Si ya ha completado la búsqueda de renos, se inicia la comunicación con Santa Claus
                else{
                    //Envía mensaje al traductor
                    if (!esperandoRespuesta){
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.addReceiver(new AID("Elfo", AID.ISLOCALNAME));
                        msg.setContent(textoMsg); 
                        send(msg);
                        esperandoRespuesta = true;
                        respuestaRecibida = false;
                    }
                    //Recibe mensaje del traductor
                    else{
                        ACLMessage msg = blockingReceive();
                        if (msg.getPerformative() == ACLMessage.INFORM) {
                            respuesta = msg.getContent();
                            step = AGENTE;
                            esperandoRespuesta = false;
                            respuestaRecibida = true;
                        }
                        else {
                            System.out.println("Santa: Error in the coversation protocol - step " + 1);
                            doDelete();
                        }
                    }
                }
            }
            /*case RUDOLPH: {
                //Si es la primera vez que le hablamos, Rudolph tiene que darnos el visto bueno
                if (!vistoBuenoRudolph){
                    //Envía mensaje a Rudolph
                    if (!esperandoRespuesta) {
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.addReceiver(new AID("Rudolph", AID.ISLOCALNAME));
                        msg.setContent(codigoSecreto);
                        send(msg);
                        esperandoRespuesta = true;
                    }
                    //Recibe mensaje de Rudolph
                    else{
                        ACLMessage msg = blockingReceive();
                        if (msg.getPerformative() == ACLMessage.AGREE) {
                            esperandoRespuesta = false;
                            vistoBuenoRudolph = true;
                        }
                        else {
                            System.out.println("Error in the coversation protocol - step " + 3);
                            doDelete();
                        }
                    }
                }
                else {
                    //Envía mensaje a Rudolph
                    if (!esperandoRespuesta) {
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.addReceiver(new AID("Rudolph", AID.ISLOCALNAME));
                        msg.setContent("Deseo saber la posición de un reno");
                        send(msg);
                        esperandoRespuesta = true;
                    }
                    //Recibe mensaje de Rudolph
                    else{
                        ACLMessage msg = blockingReceive();
                        if (msg.getPerformative() == ACLMessage.INFORM) {
                            respuesta = msg.getContent();
                            if (respuesta == "No quedan renos que localizar"){
                                System.out.println("No quedan renos que localizar");
                                step = ELFO;
                                renosCompletados = true;
                            }
                            else{
                                String[] coordenadas = respuesta.split(" ");
                                metaX = Integer.parseInt(coordenadas[0]);
                                metaY = Integer.parseInt(coordenadas[1]);
                            }
                            esperandoRespuesta = false;
                        }
                        else {
                            System.out.println("Error in the coversation protocol - step " + 4);
                            doDelete();
                        }
                    }
                }
            }*/
            default:{
                System.out.println("Santa: Error in the coversation protocol - step " + 2);
                doDelete(); 
            } 
        }  
    }
}
