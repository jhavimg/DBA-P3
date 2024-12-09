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
        setLayout(new BorderLayout()); // Cambiamos el layout principal a BorderLayout

        getContentPane().setBackground(new Color(30, 30, 30));

        // Panel para el grid
        JPanel gridPanel = new JPanel(new GridLayout(filas, columnas));
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                celdas[i][j] = new JLabel();
                celdas[i][j].setOpaque(true);

                celdas[i][j].setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));

                if (mapa_interno[i][j] == 0) {
                    // Espacios libres
                    celdas[i][j].setIcon(new ImageIcon("Pr2-maps/hierba.png"));
                } else {
                    // Obstáculos
                    celdas[i][j].setIcon(new ImageIcon("Pr2-maps/muro.png"));
                }

                gridPanel.add(celdas[i][j]);
            }
        }
        add(gridPanel, BorderLayout.CENTER); // Añadimos el grid al centro del BorderLayout

        // Panel para el apartado inferior
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(40, 40, 40));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Chat en el centro
        JTextArea chatArea = new JTextArea(5, 20);
        chatArea.setEditable(false); // Hacemos que el área de chat no sea editable
        chatArea.setBackground(new Color(60, 60, 60));
        chatArea.setForeground(Color.WHITE);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea); // Agregamos un scroll
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        // Imagen izquierda
        JLabel leftImage = new JLabel();
        ImageIcon leftIcon = new ImageIcon("mapas-pr3/santa.jpg");
        Image leftImg = leftIcon.getImage().getScaledInstance(chatArea.getPreferredSize().height, chatArea.getPreferredSize().height, Image.SCALE_SMOOTH);
        leftImage.setIcon(new ImageIcon(leftImg));
        bottomPanel.add(leftImage, BorderLayout.WEST);

        // Imagen derecha
        JLabel rightImage = new JLabel();
        ImageIcon rightIcon = new ImageIcon("mapas-pr3/santa.jpg");
        Image rightImg = rightIcon.getImage().getScaledInstance(chatArea.getPreferredSize().height, chatArea.getPreferredSize().height, Image.SCALE_SMOOTH);
        rightImage.setIcon(new ImageIcon(rightImg));
        bottomPanel.add(rightImage, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH); // Añadimos el panel inferior al sur del BorderLayout

        setSize(800, 900); // Ajustamos el tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actualizarPosicionAgente(int x, int y) {
        if (lastX != -1 && lastY != -1 && !(lastX == metaX && lastY == metaY)) {
            // Marca la celda por la que ha pasado el agente
            celdas[lastX][lastY].setIcon(new ImageIcon("mapas-pr3/tierra.png"));
        }

        // Actualiza a la nueva posición del agente con una imagen específica
        celdas[x][y].setIcon(new ImageIcon(""));
        celdas[x][y].setBackground(new Color(0, 0, 255));

        // Actualiza la posición previa
        lastX = x;
        lastY = y;

        repaint();
    }

    public void setMeta(int x, int y) {
        this.metaX = x;
        this.metaY = y;
        celdas[x][y].setIcon(null); // Meta con imagen específica
        celdas[x][y].setBackground(new Color(255, 187, 51));
        repaint();
    }

    public void setReno(int x, int y) {
        celdas[x][y].setBackground(new Color(116, 78, 59));
        repaint();
    }

    public void setSanta(int x, int y) {
        celdas[x][y].setBackground(new Color(255, 0, 0));
        repaint();
    }
}
