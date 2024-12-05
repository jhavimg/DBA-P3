import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class Agente extends Agent {

    private int energia, posX, posY, metaX, metaY, step, santaX, santaY;
    private boolean finalizado, esperandoRespuesta, vistoBuenoRudolph, renosCompletados, conSanta, aMoverse, finalizadoFinal;
    private Entorno env;
    private MapaVisual mapaVisual;
    private ACLMessage msgSanta;
    private String codigoSecreto, respuesta;
    private String comienzo = "Bro " , fin = " en plan";


    public Agente(Entorno env, int mX, int mY, int pX, int pY , MapaVisual mapavisual) {
        this.env = env;
        metaX = mX;
        metaY = mY;
        posX = pX;
        posY = pY;
        msgSanta = new ACLMessage(ACLMessage.INFORM);
        msgSanta.addReceiver(new AID ("SantaClaus", AID.ISLOCALNAME));
        esperandoRespuesta = vistoBuenoRudolph = renosCompletados = conSanta = aMoverse = finalizadoFinal = false;
        energia = 0;
        finalizado = false;
        santaX = santaY = -1;
        this.mapaVisual = mapavisual;
        step = 0;
    }

    
    protected void comunicar(){
        switch (step) { 
            case 0: { 
                String mensaje = comienzo + "me das el codigo" + fin;
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.addReceiver(new AID("SantaClaus", AID.ISLOCALNAME));
                msg.setContent(mensaje); 
                send(msg);
                
                msg = blockingReceive();
                if (msg.getPerformative() == ACLMessage.AGREE) {
                    respuesta = msg.getContent();
                    System.out.print("Agente1: Me ha dado el codigo secreto\n");
                    finalizadoFinal = true;
                    String[] partes = respuesta.split(",");
                    if (partes.length > 1) {
                        this.codigoSecreto = partes[1]; // Esto obtiene "mensaje que quiero"
                        System.out.println(mensaje);
                    } else {
                        System.out.println("No se encontró un mensaje entre comillas.");
                    }
                    
                    System.out.print("Agente: El codigo secreto es: " + this.codigoSecreto + "\n");
                }else{
                    System.out.print("Agente: No ha confiado en mi\n ");
                }
                
                
            }
            
            /*default:{
                System.out.println("Error in the coversation protocol - step " + 0);
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
        addBehaviour(new SimpleBehaviour() {
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
        });
    }

    @Override
    public void takeDown() {
        System.out.println("Terminating agent...");
    }

    public void setMapaVisual(MapaVisual mapaVisual) {
        this.mapaVisual = mapaVisual;
    }
}
