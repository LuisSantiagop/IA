import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TicTacToeTree {

    // Inicializa el tablero
    public static char[][] inicializarTablero() {
        return new char[][]{
            {'O', ' ', ' '},
            {' ', 'X', ' '},
            {' ', ' ', ' '}
        };
    }

    // Obtiene los movimientos posibles
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

    // Verifica si hay un ganador
    public static boolean verificarGanador(char[][] tablero, char jugador) {
        // Verificar filas y columnas
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0] == jugador && tablero[i][1] == jugador && tablero[i][2] == jugador) {
                return true;
            }
            if (tablero[0][i] == jugador && tablero[1][i] == jugador && tablero[2][i] == jugador) {
                return true;
            }
        }
        // Verificar diagonales
        if (tablero[0][0] == jugador && tablero[1][1] == jugador && tablero[2][2] == jugador) {
            return true;
        }
        if (tablero[0][2] == jugador && tablero[1][1] == jugador && tablero[2][0] == jugador) {
            return true;
        }
        return false;
    }

    // Dibuja el tablero en formato de texto
    public static String dibujarTablero(char[][] tablero) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(" ").append(tablero[i][0]).append(" | ").append(tablero[i][1]).append(" | ").append(tablero[i][2]).append("\n");
            if (i < 2) {
                sb.append("-----------\n");
            }
        }
        return sb.toString();
    }

    // Función recursiva para generar el árbol
    public static void dfs(char[][] tablero, int nivel, FileWriter writer) throws IOException {
        // Verificar si el juego ha terminado
        if (verificarGanador(tablero, 'X') || verificarGanador(tablero, 'O') || movimientosPosibles(tablero).isEmpty()) {
            return;
        }

        // Determinar el jugador actual
        int cuentaX = 0, cuentaO = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == 'X') cuentaX++;
                if (tablero[i][j] == 'O') cuentaO++;
            }
        }
        char jugador = (cuentaX <= cuentaO) ? 'X' : 'O';

        // Generar movimientos posibles
        List<int[]> movimientos = movimientosPosibles(tablero);
        for (int[] movimiento : movimientos) {
            int i = movimiento[0];
            int j = movimiento[1];

            // Crear un nuevo tablero con el movimiento
            char[][] nuevoTablero = new char[3][3];
            for (int x = 0; x < 3; x++) {
                System.arraycopy(tablero[x], 0, nuevoTablero[x], 0, 3);
            }
            nuevoTablero[i][j] = jugador;

            // Escribir el tablero en el archivo
            writer.write("Nivel: " + nivel + "\n");
            writer.write(dibujarTablero(nuevoTablero));
            writer.write("\n");

            // Llamada recursiva
            dfs(nuevoTablero, nivel + 1, writer);
        }
    }

    // Función principal para generar el árbol
    public static void generarArbol() {
        char[][] tablero = inicializarTablero();
        try (FileWriter writer = new FileWriter("arbol_movimientos.txt")) {
            writer.write("Árbol de Movimientos Tic Tac Toe\n\n");
            writer.write("Estado Inicial:\n");
            writer.write(dibujarTablero(tablero));
            writer.write("\n");
            dfs(tablero, 1, writer);
            System.out.println("Árbol generado y guardado en 'arbol_movimientos.txt'.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        generarArbol();
    }
}