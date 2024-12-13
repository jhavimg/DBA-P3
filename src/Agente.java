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
    private String comienzo = "Bro," , fin = ",en plan";


    public Agente(Entorno env, int mX, int mY, int pX, int pY , MapaVisual mapavisual) {
        this.env = env;
        metaX = mX;
        metaY = mY;
        posX = pX;
        posY = pY;
        esperandoRespuesta = vistoBuenoRudolph = renosCompletados = conSanta = aMoverse = finalizadoFinal = false;
        energia = 0;
        finalizado = false;
        santaX = santaY = -1;
        this.mapaVisual = mapavisual;
        step = 0;
    }

    protected void mover() {
        // Obtiene la información de las celdas adyacentes en forma de Pair (isFree, weight)
        Pair[] infoAdyacente = env.isFree(posX, posY);
        comprobarDiagonales(infoAdyacente);

        // Lista de movimientos posibles (x, y, índice en posiciones[])
        int[][] movimientos = {
            {-1, -1, 0}, // arriba-izquierda
            {-1, 0, 1}, // arriba
            {-1, 1, 2}, // arriba-derecha
            {0, -1, 3}, // izquierda
            {0, 1, 4}, // derecha
            {1, -1, 5}, // abajo-izquierda
            {1, 0, 6}, // abajo
            {1, 1, 7} // abajo-derecha
        };

        int mejorX = posX;
        int mejorY = posY;
        int menorEvaluacion = Integer.MAX_VALUE;

        // Buscar la celda adyacente libre con el menor valor de (peso + distancia a la meta)
        for (int[] mov : movimientos) {
            int nuevaX = posX + mov[0];
            int nuevaY = posY + mov[1];
            int indice = mov[2];

            // Verificar si la celda es libre
            if (infoAdyacente[indice].libre) {
                int peso = infoAdyacente[indice].peso;
                int distancia = calcularDistanciaManhattan(nuevaX, nuevaY, metaX, metaY);
                int evaluacion = peso + distancia; // Función de evaluación

                // Seleccionar la celda con la menor evaluación
                if (evaluacion < menorEvaluacion) {
                    mejorX = nuevaX;
                    mejorY = nuevaY;
                    menorEvaluacion = evaluacion;
                }
                else if (evaluacion == menorEvaluacion){
                    if (calcularDistanciaEuclidiana(nuevaX, nuevaY, metaX, metaY) < calcularDistanciaEuclidiana(mejorX, mejorY, metaX, metaY)){
                        mejorX = nuevaX;
                        mejorY = nuevaY;
                    }
                }
            }
        }

        // Actualizar posición del agente a la mejor opción encontrada
        posX = mejorX;
        posY = mejorY;
        env.incrementarPeso(posX, posY); // Incrementa el peso de la celda visitada en el entorno
    }

    // Método auxiliar para calcular la distancia de Manhattan entre dos puntos
    private int calcularDistanciaManhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private double calcularDistanciaEuclidiana(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private void comprobarDiagonales(Pair[] infoAdyacente){
        if (!infoAdyacente[1].libre && !infoAdyacente[3].libre){
            infoAdyacente[0].libre = false;
        }
        if (!infoAdyacente[1].libre && !infoAdyacente[4].libre){
            infoAdyacente[2].libre = false;
        }
        if (!infoAdyacente[6].libre && !infoAdyacente[3].libre){
            infoAdyacente[5].libre = false;
        }
        if (!infoAdyacente[6].libre && !infoAdyacente[4].libre){
            infoAdyacente[7].libre = false;
        }
    }

    private String traducir(String mensaje){
        String final_msg = "";
        
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Elfo", AID.ISLOCALNAME));
        msg.setContent(mensaje); 
        send(msg);
        
        msg = blockingReceive();
        if (msg.getPerformative() == ACLMessage.INFORM) {
            final_msg = msg.getContent();
            System.out.print("Agente: " + final_msg + "\n");
        }else{
            System.out.print("Agente: El protocolo entre el Elfo y yo no es correcto\n ");
            doDelete();
        }
        
        return final_msg;
    }

    protected void comunicar(){
        switch (step) { 
            case 0: { 
                String mensaje = comienzo + "me das el codigo" + fin;
                mapaVisual.setMensaje(mensaje , 2, true);
                /* try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } */

                mensaje = traducir(mensaje);
                mapaVisual.setMensaje(mensaje , 2, false);
                /* try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } */

                msgSanta = new ACLMessage(ACLMessage.REQUEST);
                msgSanta.addReceiver(new AID("SantaClaus", AID.ISLOCALNAME));
                msgSanta.setContent(mensaje); 
                send(msgSanta);
                mapaVisual.setMensaje(mensaje , 1, true);
                /* try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } */
                
                msgSanta = blockingReceive();
                if (msgSanta.getPerformative() == ACLMessage.AGREE) {
                    codigoSecreto = msgSanta.getContent();
                    mapaVisual.setMensaje(codigoSecreto , 1, false);
                    /* try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } */
                    codigoSecreto = codigoSecreto.split(",")[1];
                    System.out.println("Agente: Me ha dado el codigo secreto");
                    System.out.println("Agente: El codigo secreto es: " + codigoSecreto);
                    mapaVisual.setMensaje("Me ha dado el codigo secreto" , 1 , true );
                    step++;
                }else{
                    System.out.println("Agente: No ha confiado en mi");
                    mensaje = msgSanta.getContent();
                    mapaVisual.setMensaje(codigoSecreto , 1, false);
                    finalizadoFinal = true;
                }
                break;
            }
            case 1: {
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.addReceiver(new AID("Rudolph", AID.ISLOCALNAME));
                String mensaje = comienzo + "dame la posición de un reno" + fin;
                msg.setContent(mensaje);
                msg.setConversationId(codigoSecreto);
                send(msg);
                mapaVisual.setMensaje(mensaje , 3, true);
                /* try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } */

                msg = blockingReceive();
                String coordenadas = msg.getContent();
                if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                    //String coordenadas = msg.getContent();
                    System.out.println("Agente: " + coordenadas);
                    mapaVisual.setMensaje(coordenadas , 3, false);
                    /* try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } */
                    String[] partes = coordenadas.split(" ");
                    if (partes.length > 1) {
                        metaX = Integer.parseInt(partes[0]);
                        metaY = Integer.parseInt(partes[1]);
                        mapaVisual.setReno(metaX, metaY);
                        aMoverse = true;
                    } else {
                        System.out.println("No se encontró un mensaje entre comillas.");
                    }
                }
                else if (msg.getPerformative() == ACLMessage.INFORM) {
                    System.out.println("Agente: No quedan renos");
                    mapaVisual.setMensaje(coordenadas, 3, false);
                    step = 3;
                }
                else if (msg.getPerformative() == ACLMessage.REFUSE) {
                    System.out.println("Agente: Codigo incorrecto");
                    finalizadoFinal = true;
                }
                else{
                    System.out.println("Agente: No se ha podido localizar al reno");
                    finalizadoFinal = true;
                }
                break;
            }
            case 2:{
                String mensaje = comienzo + "he encontrado un reno" + fin;
                mensaje = traducir(mensaje);
                msgSanta.clearAllReceiver();
                msgSanta.addReceiver(new AID("SantaClaus", AID.ISLOCALNAME));
                msgSanta.setPerformative(ACLMessage.INFORM);
                msgSanta.setContent(mensaje);
                send(msgSanta);
                step--;
                break;
            } 
            case 3:{
                String mensaje = comienzo + "dónde estás, Santa?" + fin;
                mapaVisual.setMensaje(mensaje, 2, true);
                mensaje = traducir(mensaje);
                mapaVisual.setMensaje(mensaje, 2, false);
                /*msgSanta.clearAllReceiver();
                msgSanta.addReceiver(new AID("SantaClaus", AID.ISLOCALNAME));
                msgSanta.setPerformative(ACLMessage.REQUEST);
                msgSanta.setContent(mensaje);
                send(msgSanta);*/
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.addReceiver(new AID("SantaClaus", AID.ISLOCALNAME));
                msg.setContent(mensaje);
                send(msg);
                mapaVisual.setMensaje(mensaje, 1, true);

                System.out.println("Agente: Esperando respuesta de Santa");
                msg = blockingReceive();
                if (msg.getPerformative() == ACLMessage.INFORM) {
                    String coordenadas = msg.getContent();
                    mapaVisual.setMensaje(coordenadas, 1, false);
                    System.out.println("Agente: " + coordenadas);
                    coordenadas = coordenadas.split(",")[1];
                    String[] partes = coordenadas.split(" ");
                    if (partes.length > 1) {
                        santaX = metaX = Integer.parseInt(partes[0]);
                        santaY = metaY = Integer.parseInt(partes[1]);
                        mapaVisual.setSanta(santaX, santaY);
                        aMoverse = true;
                    } else {
                        System.out.println("No se encontró un mensaje entre comillas.");
                        finalizadoFinal = true;
                    }
                }
                else{
                    System.out.println("Agente: No se ha podido localizar a Santa");
                    finalizadoFinal = true;
                }
                break;
            } 
            case 4:{
                String mensaje = comienzo + "te he encontrado" + fin;
                mapaVisual.setMensaje(mensaje, 1, true);
                mensaje = traducir(mensaje);
                mapaVisual.setMensaje(mensaje, 1 , false);
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("SantaClaus", AID.ISLOCALNAME));
                msg.setContent(mensaje);
                send(msg);
                mapaVisual.setMensaje(mensaje, 1 , true);
                finalizadoFinal = true;
                
                msg = blockingReceive();
                if (msg.getPerformative() == ACLMessage.INFORM) {
                    mapaVisual.setMensaje(msg.getContent(), 1 , false);
                    System.out.println("Agente: " + msg.getContent());
                }
                else{
                    System.out.println("Agente: No se ha podido localizar a Santa");
                }
                break;
            }
        }
    }

    
    @Override
    protected void setup() {

        //Se mueve y comprueba si está en la meta
        addBehaviour(new SimpleBehaviour() {
            @Override
            public void action() {
                if(energia == 0){
                    mapaVisual.actualizarPosicionAgente(posX, posY);
                }

                if (aMoverse) {
                    mover();
                    mapaVisual.actualizarPosicionAgente(posX, posY);
                    energia++;
                }
                else comunicar();

                if (posX == metaX && posY == metaY && aMoverse) {
                    finalizado = true;
                    aMoverse = false;
                    if (posX == santaX && posY == santaY){
                        step++;
                    }
                    else step = 2;
                }
                try {
                    Thread.sleep(5); // Pausa de 500ms para visualizar el movimiento
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        System.out.println("Cerrando agente...");
    }

    public void setMapaVisual(MapaVisual mapaVisual) {
        this.mapaVisual = mapaVisual;
    }
}
