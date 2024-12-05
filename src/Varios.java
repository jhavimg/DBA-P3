/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import java.util.Scanner;
/**
 *
 * @author jezuz
 */

public class Varios extends Agent {
    Scanner scanner = new Scanner(System.in);
    private int numeroElementos;
    private double suma = 0;
    
    @Override
    protected void setup(){
        System.out.println("\nHola soy el agente " + getAID().getLocalName());

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                System.out.println("Introduce cuantos numeros quieres sumar: ");
                numeroElementos = scanner.nextInt();
                System.out.println("Introduce los elementos: ");
            }
        });
        
        addBehaviour(new SimpleBehaviour() {
            protected int repeticiones = 0;
            
            @Override
            public void action() {
                suma += (double) scanner.nextInt();
                repeticiones++;
                
                if(numeroElementos == repeticiones ){
                    addBehaviour(new OneShotBehaviour() {
                        @Override
                        public void action() {
                            double media = suma / (double) numeroElementos;
                            System.out.println("La media es: " + media + "\n");
                        }
                    });
                }
            }

            @Override
            public boolean done() {
                return repeticiones == numeroElementos;
            }
        }); 
      
    }
    
    @Override
    public void takeDown(){
        System.out.println("Terminando agente ");
    }
    
   
}
