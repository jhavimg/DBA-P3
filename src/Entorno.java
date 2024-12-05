public class Entorno {

    private final int[][] map_interno;
    private final int[][] pesos;
    private static final int INCREMENTO_PESO = 10;

    public Entorno(Mapa mapa) {
        map_interno = mapa.getMapa();
        this.pesos = new int[map_interno.length][map_interno[0].length];
    }
    
    public void incrementarPeso(int x, int y) {
        pesos[x][y] += INCREMENTO_PESO;
    }
    
    public Pair[] isFree(int x, int y){
        Pair[] infoAdyacente = new Pair[8];
        
        // Arriba-izquierda
        if (x > 0 && y > 0) {
            infoAdyacente[0] = new Pair(map_interno[x - 1][y - 1] == 0, pesos[x - 1][y - 1]);
        } else {
            infoAdyacente[0] = new Pair(false, Integer.MAX_VALUE);
        }

        // Arriba
        if (x > 0) {
            infoAdyacente[1] = new Pair(map_interno[x - 1][y] == 0, pesos[x - 1][y]);
        } else {
            infoAdyacente[1] = new Pair(false, Integer.MAX_VALUE);
        }

        // Arriba-derecha
        if (x > 0 && y < map_interno[x].length - 1) {
            infoAdyacente[2] = new Pair(map_interno[x - 1][y + 1] == 0, pesos[x - 1][y + 1]);
        } else {
            infoAdyacente[2] = new Pair(false, Integer.MAX_VALUE);
        }

        // Izquierda
        if (y > 0) {
            infoAdyacente[3] = new Pair(map_interno[x][y - 1] == 0, pesos[x][y - 1]);
        } else {
            infoAdyacente[3] = new Pair(false, Integer.MAX_VALUE);
        }

        // Derecha
        if (y < map_interno[x].length - 1) {
            infoAdyacente[4] = new Pair(map_interno[x][y + 1] == 0, pesos[x][y + 1]);
        } else {
            infoAdyacente[4] = new Pair(false, Integer.MAX_VALUE);
        }

        // Abajo-izquierda
        if (x < map_interno.length - 1 && y > 0) {
            infoAdyacente[5] = new Pair(map_interno[x + 1][y - 1] == 0, pesos[x + 1][y - 1]);
        } else {
            infoAdyacente[5] = new Pair(false, Integer.MAX_VALUE);
        }

        // Abajo
        if (x < map_interno.length - 1) {
            infoAdyacente[6] = new Pair(map_interno[x + 1][y] == 0, pesos[x + 1][y]);
        } else {
            infoAdyacente[6] = new Pair(false, Integer.MAX_VALUE);
        }

        // Abajo-derecha
        if (x < map_interno.length - 1 && y < map_interno[x].length - 1) {
            infoAdyacente[7] = new Pair(map_interno[x + 1][y + 1] == 0, pesos[x + 1][y + 1]);
        } else {
            infoAdyacente[7] = new Pair(false, Integer.MAX_VALUE);
        }

        return infoAdyacente;
    }
}
