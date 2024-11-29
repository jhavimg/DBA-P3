

import java.io.BufferedReader;

import java.io.FileReader;
public class Mapa {
    private int filas;
    private int columnas;
    private int[][] mapa;

    public Mapa(String archivo) {
       try (FileReader fr = new FileReader(archivo)) {
            BufferedReader br = new BufferedReader(fr);
            
            String linea;
            
            filas = Integer.parseInt(br.readLine());
            columnas = Integer.parseInt(br.readLine());
            
            mapa = new int[filas][columnas];
            
            for(int i = 0; i < filas; i++){
                linea = br.readLine();
                String[] elementos = linea.split("\t");
                for(int j = 0; j < columnas; j++){
                    mapa[i][j] = Integer.parseInt(elementos[j]);
                    //System.out.println(elementos[j]);
                }
            }
        }catch(Exception e){
           e.printStackTrace();
        }
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int[][] getMapa() {
        return mapa;
    }
}
