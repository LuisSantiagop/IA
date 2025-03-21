import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TicTacToeDFS {
    public static char[][] inicializarTablero() {
        return new char[][]{
                {'O', ' ', 'X'},
                {'X', ' ', ' '},
                {'O', 'X', 'O'}
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

    public static void dfs(char[][] tablero, FileWriter archivoTxt, Stack<String> pila, String nodo) throws IOException {
        List<int[]> movimientos = movimientosPosibles(tablero);
        if (movimientos.isEmpty() || verificarGanador(tablero, 'X') || verificarGanador(tablero, 'O')) {
            //archivoTxt.write("Estado Final (" + nodo + "):\n" + dibujarTableroTxt(tablero) + "\nRuta: " + pila + "\n\n");
            return;
        }

        int[] cuentas = contarMovimientos(tablero);
        char jugador = (cuentas[0] <= cuentas[1]) ? 'X' : 'O';
        int contador = 1;
        
        for (int[] mov : movimientos) {
            int i = mov[0], j = mov[1];
            char[][] nuevoTablero = new char[3][3];
            for (int k = 0; k < 3; k++)
                System.arraycopy(tablero[k], 0, nuevoTablero[k], 0, 3);
            nuevoTablero[i][j] = jugador;
            
            String nuevoNodo = nodo + "." + contador;
            pila.push("(" + i + ", " + j + ") [Jugador " + jugador + "] Nodo: " + nuevoNodo);
            archivoTxt.write("Movimiento " + nuevoNodo + ": " + pila + "\n" + dibujarTableroTxt(nuevoTablero) + "\n\n");
            
            dfs(nuevoTablero, archivoTxt, pila, nuevoNodo);
            pila.pop();
            contador++;
        }
    }

    public static int[] contarMovimientos(char[][] tablero) {
        int cuentaX = 0, cuentaO = 0;
        for (char[] row : tablero) {
            for (char c : row) {
                if (c == 'X') cuentaX++;
                else if (c == 'O') cuentaO++;
            }
        }
        return new int[]{cuentaX, cuentaO};
    }

    public static void main(String[] args) {
        char[][] tablero = inicializarTablero();
        Stack<String> pila = new Stack<>();
        
        try (FileWriter archivoTxt = new FileWriter("pila_movimientos.txt")) {
            archivoTxt.write("Exploración DFS con Pila:\n\n");
            archivoTxt.write("Estado Inicial\n" + dibujarTableroTxt(tablero) + "\n\n");
            dfs(tablero, archivoTxt, pila, "1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
