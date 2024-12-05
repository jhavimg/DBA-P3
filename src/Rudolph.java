import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.List;

public class Rudolph extends Agent {

    private List<String> coordenadasRenos; 
    private int indiceActual; // Índice del reno actual
    private boolean todosRenosEncontrados;
    private final String CODIGO_SECRETO = "1234";

    @Override
    protected void setup() {
        coordenadasRenos = new ArrayList<>();
        // Añadimos las coordenadas de los renos
        coordenadasRenos.add("10 44");
        coordenadasRenos.add("1 10");
        coordenadasRenos.add("53 15");
        coordenadasRenos.add("45 51");
        coordenadasRenos.add("94 69");
        coordenadasRenos.add("42 19");
        coordenadasRenos.add("85 14");
        coordenadasRenos.add("96 7");

        indiceActual = 0;
        todosRenosEncontrados = false;

        // Comportamiento para manejar mensajes
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage mensaje = blockingReceive();
                if (mensaje != null) {
                    manejarMensaje(mensaje);
                }
            }
        });
    }

    private void manejarMensaje(ACLMessage mensaje) {
        String contenido = mensaje.getContent();
        ACLMessage respuesta = mensaje.createReply();

        switch (mensaje.getPerformative()) {
            case ACLMessage.REQUEST: // Validar el código secreto
                if (contenido.equals(CODIGO_SECRETO)) {
                    respuesta.setPerformative(ACLMessage.AGREE);
                    respuesta.setContent("Código aceptado");
                } else {
                    respuesta.setPerformative(ACLMessage.REFUSE);
                    respuesta.setContent("Código incorrecto");
                }
                send(respuesta);
                break;

            case ACLMessage.INFORM: // Enviar la posición del siguiente reno
                if (!todosRenosEncontrados) {
                    if (indiceActual < coordenadasRenos.size()) {
                        respuesta.setPerformative(ACLMessage.INFORM);
                        respuesta.setContent(coordenadasRenos.get(indiceActual));
                        indiceActual++;
                        if (indiceActual == coordenadasRenos.size()) {
                            todosRenosEncontrados = true;
                        }
                    } else {
                        respuesta.setPerformative(ACLMessage.INFORM);
                        respuesta.setContent("Rudolph: Error in the coversation protocol - step" + 1);
                    }
                    send(respuesta);
                }
                break;

            default:
                System.out.println("Mensaje no reconocido");
                break;
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("Rudolph finalizando...");
    }
}
