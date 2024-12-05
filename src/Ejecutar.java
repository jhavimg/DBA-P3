import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime; 
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Ejecutar {
    public static void main(String[] args) {
        
        
            /*Runtime rt = Runtime.instance();
        
            Profile p = new ProfileImpl();
            p.setParameter(Profile.MAIN_HOST, "localhost"); 
            //p.setParameter(Profile.MAIN_PORT, "109");     
            p.setParameter(Profile.CONTAINER_NAME, "Container"); 

            ContainerController cc = rt.createMainContainer(p);
            
            //Scanner sc = new Scanner(System.in);

            //Mapa mapa = new Mapa(menu(sc));
            //Entorno env = new Entorno(mapa);

            
            int metaX = 0, metaY = 0, posX = 0, posY = 0;

            /* do {
                System.out.print("Introduce la posición inicial del agente(posX posY): ");
                posX = sc.nextInt();
                posY = sc.nextInt();
            } while ((posX > 0 || posX >= mapa.getColumnas() || posY > 0 || posY >= mapa.getFilas()) && mapa.getMapa()[posX][posY] != 0 );

            System.out.println("\n");

            do {
                System.out.print("Introduce la posición de la meta(posX posY): ");
                metaX = sc.nextInt();
                metaY = sc.nextInt();
            } while ((metaX > 0 || metaX >= mapa.getColumnas() || metaY > 0 || metaY >= mapa.getFilas()) && mapa.getMapa()[metaX][metaY] != 0 ); */
            
            //sc.close();

            //Agente agente = new Agente(env, metaX, metaY, posX, posY);
            
            //MapaVisual mapaVisual = new MapaVisual(mapa);
            //mapaVisual.setMeta(metaX, metaY);
            //agente.setMapaVisual(mapaVisual);*/
        
        Runtime rt = Runtime.instance();
        
        String host = "localhost";            
        String containerName = "MainContainer"; 
        String agentName = "Agente";
        
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, host);
        p.setParameter(Profile.CONTAINER_NAME, containerName);
        ContainerController cc = rt.createAgentContainer(p);
        
        Scanner sc = new Scanner(System.in);

        Mapa mapa = new Mapa(menu(sc));
        Entorno env = new Entorno(mapa);
        
        int metaX = 0, metaY = 0, posX = 2, posY = 2;
        
        sc.close();
        
        MapaVisual mapaVisual = new MapaVisual(mapa);
        mapaVisual.setMeta(metaX, metaY);
        
        try {
            Agente agente = new Agente(env, metaX, metaY, posX, posY, mapaVisual);
            AgentController agent1 = cc.acceptNewAgent(agentName, agente);
            agent1.start();
            
            Santa santa = new Santa( 2, 2);
            AgentController agent2 = cc.acceptNewAgent("SantaClaus", santa);
            agent2.start();
            
            Elfo elfo = new Elfo();
            AgentController agent3 = cc.acceptNewAgent("Elfo", elfo);
            agent3.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
        
    }

    static String menu(Scanner sc){
        int numMapa;
        
        System.out.println("Para elegir el mapa introduzca uno de los siguientes números:");
        System.out.println("1. Mapa sin obstáculos");
        System.out.println("2. Mapa con obstáculos");
        System.out.print("Introduce un numero en 1-2: ");
        numMapa = sc.nextInt();
        
        while(numMapa < 1 || numMapa > 2){
            System.out.println("Numero invalido: Introduce un numero en 1-2: ");
            numMapa = sc.nextInt();
        }
        System.out.print("\n");
        
        Path projectDir = Paths.get("").toAbsolutePath();
        switch(numMapa){
            case 1:
                return projectDir.toString() + "/mapas-pr3/100x100-sinObstaculos.txt";
            case 2:
                return projectDir.toString() + "/mapas-pr3/100x100-conObstaculos.txt";
            default:
                System.err.println("Opción no válida.");
                return "Opción no válida.";
        }
    }
}
