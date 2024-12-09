import javax.swing.*;
import java.awt.*;

public class MapaVisual extends JFrame {

    private final int[][] mapa_interno;
    private JLabel[][] celdas;  
    private int filas;
    private int columnas;
    private int lastX = -1, lastY = -1, metaX, metaY;

    public MapaVisual(Mapa mapa) {
        this.mapa_interno = mapa.getMapa();
        this.filas = mapa.getFilas();
        this.columnas = mapa.getColumnas();
        this.celdas = new JLabel[filas][columnas];

        setTitle("Práctica 2");
        setLayout(new GridLayout(filas, columnas));

        getContentPane().setBackground(new Color(30, 30, 30)); 

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                celdas[i][j] = new JLabel();
                celdas[i][j].setOpaque(true); 
                
                celdas[i][j].setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50))); 

                if (mapa_interno[i][j] == 0) {
                    // Espacios libre
                    celdas[i][j].setIcon(new ImageIcon("Pr2-maps/hierba.png")); 
                } else {
                    // Obstáculos 
                    celdas[i][j].setIcon(new ImageIcon("Pr2-maps/muro.png")); 
                }

                add(celdas[i][j]);
            }
        }

        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actualizarPosicionAgente(int x, int y) {
        if (lastX != -1 && lastY != -1 && !(lastX == metaX && lastY == metaY)) {
            // Marca la celda por la que ha pasdo el agente
            celdas[lastX][lastY].setIcon(new ImageIcon("mapas-pr3/tierra.png")); 
        }

        // Actualiza a la nueva posición del agente con una imagen específica
        celdas[x][y].setIcon(new ImageIcon("")); 
        celdas[x][y].setBackground(new Color(0,0,255));

        // Actualiza la posición previa
        lastX = x;
        lastY = y;

        repaint();
    }

    public void setMeta(int x, int y) {
        this.metaX = x;
        this.metaY = y;
        celdas[x][y].setIcon(null);  // Meta con imagen específica
        celdas[x][y].setBackground(new Color(255, 187, 51));
        repaint();
    }

    public void setReno(int x , int y){
        celdas[x][y].setBackground(new Color(116,78,59));
        repaint();
    }

    public void setSanta(int x , int y){
        celdas[x][y].setBackground(new Color(255,0,0));
        repaint();
    }
}