import matplotlib.pyplot as plt
import networkx as nx

def inicializar_tablero():
    # Tablero inicial con jugadas ya dadas
    return [
        ['O', ' ', ' '],
        [' ', 'X', ' '],
        [' ', ' ', ' ']
    ]

def movimientos_posibles(tablero):
    return [(i, j) for i in range(3) for j in range(3) if tablero[i][j] == ' ']

def verificar_ganador(tablero, jugador):
    for i in range(3):
        if all(tablero[i][j] == jugador for j in range(3)) or all(tablero[j][i] == jugador for j in range(3)):
            return True
    return all(tablero[i][i] == jugador for i in range(3)) or all(tablero[i][2 - i] == jugador for i in range(3))

def contar_movimientos(tablero):
    cuenta_x = sum(row.count('X') for row in tablero)
    cuenta_o = sum(row.count('O') for row in tablero)
    return cuenta_x, cuenta_o

def dibujar_tablero(tablero):
    return "\n".join([" | ".join([cell if cell != ' ' else '_' for cell in row]) for row in tablero])

def determinar_jugador_actual(cuenta_x, cuenta_o, jugador_inicial):
    if cuenta_o > cuenta_x:
        return 'X'  # Si O tiene más jugadas, le toca a X
    elif cuenta_x > cuenta_o:
        return 'O'  # Si X tiene más jugadas, le toca a O
    else:
        return jugador_inicial  # Si tienen las mismas jugadas, le toca al jugador inicial

def dfs(tablero, nodo, graph, jugador_inicial, parent=None, costo=0, nivel=0, max_nivel=1):
    if nivel >= max_nivel:
        return
    
    movimientos = movimientos_posibles(tablero)
    if not movimientos or verificar_ganador(tablero, 'X') or verificar_ganador(tablero, 'O'):
        return

    cuenta_x, cuenta_o = contar_movimientos(tablero)
    
    # Determinar el jugador actual basado en la lógica especificada
    jugador = determinar_jugador_actual(cuenta_x, cuenta_o, jugador_inicial)
    
    for idx, (i, j) in enumerate(movimientos, start=1):
        nuevo_tablero = [row.copy() for row in tablero]
        nuevo_tablero[i][j] = jugador
        nuevo_nodo = f"{nodo}-{idx}"
        nuevo_costo = costo + 1  # Se asigna un costo de 1 por movimiento
        
        label = f"({i}, {j})\n{dibujar_tablero(nuevo_tablero)}\nCosto: {nuevo_costo}\nJugador inicial: {jugador_inicial}"
        graph.add_node(nuevo_nodo, label=label)
        
        graph.add_edge(nodo, nuevo_nodo, label=f"\nCosto: {nuevo_costo}")  # Agregar flecha desde el nodo padre
        
        dfs(nuevo_tablero, nuevo_nodo, graph, jugador_inicial, parent=nuevo_nodo, costo=nuevo_costo, nivel=nivel+1, max_nivel=max_nivel)

def generar_arbol(jugador_inicial):
    tablero = inicializar_tablero()
    G = nx.DiGraph()
    G.add_node('0', label=f"Estado Inicial\n{dibujar_tablero(tablero)}\nCosto: 0\nJugador inicial: {jugador_inicial}")
    
    dfs(tablero, '0', G, jugador_inicial)
    
    pos = {}
    niveles = {}
    
    for node in G.nodes:
        nivel = node.count('-')
        if nivel not in niveles:
            niveles[nivel] = []
        niveles[nivel].append(node)
    
    for nivel, nodos in niveles.items():
        x_inicio = -len(nodos) / 2
        for i, nodo in enumerate(nodos):
            pos[nodo] = (x_inicio + i, -nivel)
    
    plt.figure(figsize=(30, 15))  # Aumentar el tamaño del gráfico
    labels = nx.get_node_attributes(G, 'label')
    
    nx.draw(G, pos, with_labels=True, labels=labels, node_size=4000, node_color="lightblue", font_size=8,
            font_weight="bold", arrows=True, edge_color="gray", width=2, alpha=0.9)
    
    edge_labels = nx.get_edge_attributes(G, 'label')
    nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels, font_color='red', font_size=10)
    
    plt.title(f"Árbol de Movimientos DFS Tic Tac Toe (Jugador inicial: {jugador_inicial})", size=15)
    plt.savefig(f'arbol_movimientos_{jugador_inicial}.png', format='png', dpi=300)
    plt.show()
    print(f"Árbol generado y guardado en 'arbol_movimientos_{jugador_inicial}.png'.")

if __name__ == "__main__":
    # Preguntar al usuario quién inicia la partida
    jugador_inicial = input("¿Quién inicia la partida? (X/O): ").strip().upper()
    while jugador_inicial not in ['X', 'O']:
        print("Entrada inválida. Introduce 'X' o 'O'.")
        jugador_inicial = input("¿Quién inicia la partida? (X/O): ").strip().upper()
    
    generar_arbol(jugador_inicial)