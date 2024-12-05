// Archivo ejecutar

package dbafinal;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class DBAFINAL {

    public static void main(String[] args) {
        try{
            Runtime rt = Runtime.instance();
        
            Profile p = new ProfileImpl();
            p.setParameter(Profile.MAIN_HOST, "localhost"); 
            p.setParameter(Profile.MAIN_PORT, "1099");     
            p.setParameter(Profile.CONTAINER_NAME, "DBAP3"); 

            ContainerController cc = rt.createMainContainer(p);

            String agentName1 = "Agente";
            AgentController agent1 = cc.createNewAgent(agentName1, Agente.class.getCanonicalName(), null);
            agent1.start();
            
            String agentName2 = "Elfo";
            AgentController agent2 = cc.createNewAgent(agentName2, Elfo.class.getCanonicalName(), null);
            agent2.start();
            
            String agentName3 = "Santa";
            AgentController agent3 = cc.createNewAgent(agentName3, Santa.class.getCanonicalName(), null);
            agent3.start();
            
            String agentName4 = "Rudolph";
            AgentController agent4 = cc.createNewAgent(agentName4, Rudolph.class.getCanonicalName(), null);
            agent4.start();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
