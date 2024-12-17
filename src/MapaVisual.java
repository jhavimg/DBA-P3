import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class MapaVisual extends JFrame {

    private final int[][] mapa_interno;
    private JLabel[][] celdas;
    private int filas;
    private int columnas;
    private int lastX = -1, lastY = -1, metaX, metaY;
    JTextPane chatPane;
    StyledDocument chatDocument;
    int agente_ant = -1;
    ImageIcon rightIcon;
    Image rightImg;
    JLabel rightImage;

    public MapaVisual(Mapa mapa) {
        this.mapa_interno = mapa.getMapa();
        this.filas = mapa.getFilas();
        this.columnas = mapa.getColumnas();
        this.celdas = new JLabel[filas][columnas];

        setTitle("Práctica 3");
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(30, 30, 30));

        JPanel gridPanel = new JPanel(new GridLayout(filas, columnas));
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                celdas[i][j] = new JLabel();
                celdas[i][j].setOpaque(true);

                celdas[i][j].setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));

                if (mapa_interno[i][j] != 0) {
                    celdas[i][j].setBackground(new Color(0, 0, 0));;
                } /* else {
                    celdas[i][j].setIcon(new ImageIcon("Pr2-maps/muro.png"));
                } */

                gridPanel.add(celdas[i][j]);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(40, 40, 40));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setBackground(new Color(60, 60, 60));
        chatPane.setForeground(Color.WHITE);
        chatPane.setFont(new Font("Arial", Font.PLAIN, 14));
        chatDocument = chatPane.getStyledDocument();

        chatPane.setPreferredSize(new Dimension(300, 100)); 
        chatPane.setMinimumSize(new Dimension(300, 100));
        chatPane.setMaximumSize(new Dimension(300, 100));


        JScrollPane scrollPane = new JScrollPane(chatPane);
        scrollPane.setPreferredSize(new Dimension(300, 100));
        scrollPane.setMinimumSize(new Dimension(300, 100));
        scrollPane.setMaximumSize(new Dimension(300, 100));
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel leftImage = new JLabel();
        ImageIcon leftIcon = new ImageIcon("mapas-pr3/agente.jpg");
        Image leftImg = leftIcon.getImage().getScaledInstance(chatPane.getPreferredSize().height, chatPane.getPreferredSize().height, Image.SCALE_SMOOTH);
        leftImage.setIcon(new ImageIcon(leftImg));
        bottomPanel.add(leftImage, BorderLayout.WEST);

        rightImage = new JLabel();
        rightIcon = new ImageIcon("mapas-pr3/santa.jpg");
        rightImg = rightIcon.getImage().getScaledInstance(chatPane.getPreferredSize().height, chatPane.getPreferredSize().height, Image.SCALE_SMOOTH);
        rightImage.setIcon(new ImageIcon(rightImg));
        bottomPanel.add(rightImage, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        setSize(800, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actualizarPosicionAgente(int x, int y) {
        if (lastX != -1 && lastY != -1 && !(lastX == metaX && lastY == metaY)) {
            celdas[lastX][lastY].setIcon(new ImageIcon("mapas-pr3/tierra.png"));
        }

        
        celdas[x][y].setIcon(new ImageIcon(""));
        celdas[x][y].setBackground(new Color(0, 0, 255));

        
        lastX = x;
        lastY = y;

        repaint();
    }

    public void setMeta(int x, int y) {
        this.metaX = x;
        this.metaY = y;
        celdas[x][y].setIcon(null); 
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

    private ImageIcon resizeIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public void setMensaje(String mensaje, int agente, boolean comentarioAgente) {

        if (agente != agente_ant) {
            try {
                chatDocument.remove(0, chatDocument.getLength());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            agente_ant = agente;
        }
    
        switch (agente) {
            case 1:
                rightIcon = resizeIcon("mapas-pr3/santa.jpg");
                break;
            case 2:
                rightIcon = resizeIcon("mapas-pr3/elfo.png");
                break;
            case 3:
                rightIcon = resizeIcon("mapas-pr3/rudolph.jpeg");
                break;
        }
        rightImage.setIcon(rightIcon);
    
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        if (comentarioAgente) {
            StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
            StyleConstants.setForeground(attrs, Color.GREEN);
        } else {
            StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT);
            StyleConstants.setForeground(attrs, Color.CYAN);
        }
    
        try {
            int start = chatDocument.getLength(); 
            chatDocument.insertString(start, mensaje + "\n", null);
    
            chatDocument.setParagraphAttributes(start, mensaje.length() + 1, attrs, false);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    
        chatPane.setCaretPosition(chatDocument.getLength());
    
        repaint();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
