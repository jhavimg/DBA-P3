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
    private final int ELFO = 0, SANTA = 1, RUDOLPH = 2;

    public Agente(Entorno env, int mX, int mY, int pX, int pY) {
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
    }

    
    protected void comunicar(){
        switch (step) { 
            case ELFO: { 
                //Si ya está con Santa Claus, se comunica con él para informarle
                if (conSanta){
                    //Envía mensaje al traductor
                    if (!esperandoRespuesta){
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.addReceiver(new AID("Elfo", AID.ISLOCALNAME));
                        msg.setContent("Bro, estoy contigo, en plan"); 
                        send(msg);
                        esperandoRespuesta = true;
                    }
                    //Recibe mensaje del traductor
                    else{
                        ACLMessage msg = blockingReceive();
                        if (msg.getPerformative() == ACLMessage.INFORM) {
                            respuesta = msg.getContent();
                            step = SANTA;
                            esperandoRespuesta = false;
                        }
                        else {
                            System.out.println("Agente: Error in the conversation protocol - step " + 1);
                            doDelete();
                        }
                    }
                }
                else if (finalizado){
                    //Envía mensaje al traductor
                    if (!esperandoRespuesta){
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.addReceiver(new AID("Elfo", AID.ISLOCALNAME));
                        msg.setContent("Bro, acabo de encontrar a un reno, en plan"); 
                        send(msg);
                        esperandoRespuesta = true;
                    }
                    //Recibe mensaje del traductor
                    else{
                        ACLMessage msg = blockingReceive();
                        if (msg.getPerformative() == ACLMessage.INFORM) {
                            respuesta = msg.getContent();
                            step = SANTA;
                            esperandoRespuesta = false;
                        }
                        else {
                            System.out.println("Agente: Error in the conversation protocol - step " + 1);
                            doDelete();
                        }
                    }
                }
                else if (!renosCompletados){
                    //Envía mensaje al traductor
                    if (!esperandoRespuesta){
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.addReceiver(new AID("Elfo", AID.ISLOCALNAME));
                        msg.setContent("Bro, me das el código secreto, en plan"); 
                        send(msg);
                        esperandoRespuesta = true;
                    }
                    //Recibe mensaje del traductor
                    else{
                        ACLMessage msg = blockingReceive();
                        if (msg.getPerformative() == ACLMessage.INFORM) {
                            respuesta = msg.getContent();
                            step = SANTA;
                            esperandoRespuesta = false;
                        }
                        else {
                            System.out.println("Agente: Error in the conversation protocol - step " + 1);
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
                        msg.setContent("Bro, ya he encontrado a todos los renos, me pasas tu ubicación? en plan"); 
                        send(msg);
                        esperandoRespuesta = true;
                    }
                    //Recibe mensaje del traductor
                    else{
                        ACLMessage msg = blockingReceive();
                        if (msg.getPerformative() == ACLMessage.INFORM) {
                            respuesta = msg.getContent();
                            step = SANTA;
                            esperandoRespuesta = false;
                        }
                        else {
                            System.out.println("Agente: Error in the conversation protocol - step " + 1);
                            doDelete();
                        }
                    }
                }
            }
            case SANTA: {
                //Si ya ha llegado a la meta, se comunica con Santa Claus para informarle
                if (conSanta){
                    //Envía mensaje a Santa Claus
                    if (!esperandoRespuesta) {
                        msgSanta.setContent(respuesta);
                        send(msgSanta);
                        esperandoRespuesta = true;
                    }
                    //Recibe mensaje de Santa Claus
                    else{
                        msgSanta = blockingReceive();
                        if (msgSanta.getPerformative() == ACLMessage.INFORM) {
                            respuesta = msgSanta.getContent();
                            System.out.println(respuesta);
                            esperandoRespuesta = false;
                            finalizadoFinal = true;
                        }
                        else {
                            System.out.println("Agente: Error in the conversation protocol - step " + 2);
                            doDelete();
                        }
                    }
                }
                // Si ha encontrado un reno se lo dice a Santa Claus
                else if (finalizado){
                    //Envía mensaje a Santa Claus
                    if (!esperandoRespuesta) {
                        msgSanta.setContent(respuesta);
                        send(msgSanta);
                        step = RUDOLPH;
                        finalizado = false;
                    }
                }
                else if (!renosCompletados){
                    //Envía mensaje a Santa Claus
                    if (!esperandoRespuesta) {
                        msgSanta.setContent(respuesta);
                        send(msgSanta);
                        esperandoRespuesta = true;
                    }
                    //Recibe mensaje de Santa Claus
                    else{
                        msgSanta = blockingReceive();
                        if (msgSanta.getPerformative() == ACLMessage.INFORM) {
                            codigoSecreto = msgSanta.getContent();
                            esperandoRespuesta = false;
                            step = RUDOLPH;
                        }
                        else if (msgSanta.getPerformative() == ACLMessage.FAILURE){
                            System.out.println("Santa Claus no ha querido confiar en ti");
                            doDelete();
                        }
                        else {
                            System.out.println("Agente: Error in the conversation protocol - step " + 2);
                            doDelete();
                        }
                    }
                }
                else {
                    //Envía mensaje a Santa Claus
                    if (!esperandoRespuesta) {
                        msgSanta.setContent(respuesta);
                        send(msgSanta);
                        esperandoRespuesta = true;
                    }
                    //Recibe mensaje de Santa Claus
                    else{
                        msgSanta = blockingReceive();
                        if (msgSanta.getPerformative() == ACLMessage.INFORM) {
                            respuesta = msgSanta.getContent();
                            String[] coordenadas = respuesta.split(" ");
                            metaX = santaX = Integer.parseInt(coordenadas[0]);
                            metaY = santaY = Integer.parseInt(coordenadas[1]);
                            esperandoRespuesta = false;
                            aMoverse = true;
                            step = ELFO;
                        }
                        else {
                            System.out.println("Agente: Error in the conversation protocol - step " + 2);
                            doDelete();
                        }
                    }
                }
            } 
            case RUDOLPH: {
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
                            System.out.println("Agente: Error in the conversation protocol - step " + 3);
                            doDelete();
                        }
                    }
                }
                else {
                    //Envía mensaje a Rudolph
                    if (!esperandoRespuesta) {
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.addReceiver(new AID("Rudolph", AID.ISLOCALNAME));
                        msg.setContent("Bro, deseo saber la posición de un reno, en plan");
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
                                aMoverse = true;
                                step = ELFO;
                            }
                            esperandoRespuesta = false;
                        }
                        else {
                            System.out.println("Agente: Error in the conversation protocol - step " + 4);
                            doDelete();
                        }
                    }
                }
            }
            default:{
                System.out.println("Agente: Error in the conversation protocol - step " + 2);
                doDelete(); 
            } 
        }  
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

    @Override
    protected void setup() {

        //Se mueve y comprueba si está en la meta
        addBehaviour(new SimpleBehaviour() {
            @Override
            public void action() {
                
                // mover();
                // mapaVisual.actualizarPosicionAgente(posX, posY);
                // energia++;
                // System.out.println("Nueva posicion: " + posX + " " + posY);

                // if (posX == metaX && posY == metaY) {
                //     finalizado = true;
                //     if (posX == santaX && posY == santaY){
                //         conSanta = true;
                //     }
                // }
                if(energia == 0){
                    mapaVisual.actualizarPosicionAgente(posX, posY);
                }

                if (aMoverse) mover();
                else comunicar();

                if (posX == metaX && posY == metaY && aMoverse) {
                    finalizado = true;
                    aMoverse = false;
                    if (posX == santaX && posY == santaY){
                        conSanta = true;
                    }
                }
                mapaVisual.actualizarPosicionAgente(posX, posY);
                energia++;
                try {
                    Thread.sleep(500); // Pausa de 500ms para visualizar el movimiento
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
        System.out.println("Terminating agent...");
    }

    public void setMapaVisual(MapaVisual mapaVisual) {
        this.mapaVisual = mapaVisual;
    }
}
