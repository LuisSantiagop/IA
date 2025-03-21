import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TicTacToeBFS {
    public static char[][] inicializarTablero() {
        return new char[][]{
                {'O', ' ', 'X'},
                {'X', 'X', ' '},
                {'O', ' ', ' '}
        };
    }

    public static List<int[]> movimientosPosibles(char[][] tablero) {
        List<int[]> movimientos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == ' ') {
                    movimientos.add(new int[]{i, j});
                }
            }
        }
        return movimientos;
    }

    public static boolean verificarGanador(char[][] tablero, char jugador) {
        for (int i = 0; i < 3; i++) {
            if ((tablero[i][0] == jugador && tablero[i][1] == jugador && tablero[i][2] == jugador) ||
                (tablero[0][i] == jugador && tablero[1][i] == jugador && tablero[2][i] == jugador)) {
                return true;
            }
        }
        return (tablero[0][0] == jugador && tablero[1][1] == jugador && tablero[2][2] == jugador) ||
               (tablero[0][2] == jugador && tablero[1][1] == jugador && tablero[2][0] == jugador);
    }

    public static String dibujarTableroTxt(char[][] tablero) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : tablero) {
            for (int j = 0; j < row.length; j++) {
                sb.append(row[j] == ' ' ? '_' : row[j]);
                if (j < row.length - 1) sb.append(" | ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void bfs(char[][] tablero, FileWriter archivoTxt) throws IOException {
        Queue<String> cola = new LinkedList<>();
        Queue<char[][]> estados = new LinkedList<>();
        Queue<String> rutas = new LinkedList<>();
    
        cola.add("1");
        estados.add(tablero);
        rutas.add("Inicio");
    
        while (!cola.isEmpty()) {
            String nodo = cola.poll();
            char[][] estadoActual = estados.poll();
            String ruta = rutas.poll();
    
            archivoTxt.write("Nodo: " + nodo + "\n" + dibujarTableroTxt(estadoActual) + "\nRuta: " + ruta + "\n\n");
            archivoTxt.write("Cola actual: " + cola + "\n\n");
    
            if (verificarGanador(estadoActual, 'X') || verificarGanador(estadoActual, 'O')) {
                continue;
            }
    
            List<int[]> movimientos = movimientosPosibles(estadoActual);
            int contador = 1;
    
            // Determinar el turno basado en la cantidad de jugadas en el tablero
            int[] cuenta = contarMovimientos(estadoActual);
            char jugador = (cuenta[0] <= cuenta[1]) ? 'X' : 'O';
    
            for (int[] mov : movimientos) {
                int i = mov[0], j = mov[1];
                char[][] nuevoTablero = new char[3][3];
                for (int k = 0; k < 3; k++)
                    System.arraycopy(estadoActual[k], 0, nuevoTablero[k], 0, 3);
                nuevoTablero[i][j] = jugador;
    
                String nuevoNodo = nodo + "." + contador;
                cola.add(nuevoNodo);
                estados.add(nuevoTablero);
                rutas.add(ruta + " -> (" + i + ", " + j + ") [Jugador " + nuevoTablero[i][j] + "]");
                contador++;
            }
        }
    }
    
    public static int[] contarMovimientos(char[][] tablero) {
        int cuentaX = 0, cuentaO = 0;
        for (char[] fila : tablero) {
            for (char c : fila) {
                if (c == 'X') cuentaX++;
                else if (c == 'O') cuentaO++;
            }
        }
        return new int[]{cuentaX, cuentaO};
    }
    
    public static void main(String[] args) {
        char[][] tablero = inicializarTablero();
        
        try (FileWriter archivoTxt = new FileWriter("cola_movimientos.txt")) {
            archivoTxt.write("Exploración BFS con Cola:\n\n");
            archivoTxt.write("Estado Inicial\n" + dibujarTableroTxt(tablero) + "\n\n");
            bfs(tablero, archivoTxt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
