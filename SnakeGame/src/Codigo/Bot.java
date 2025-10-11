package Codigo;

import processing.core.*;

public class Bot extends PApplet{
	final int filas = 25;
	Dijkstra dij = new Dijkstra();
	int headNodo;
	int tailNodo;
	int snakeLength;
	boolean mapb[][];
	int contador = 0;
	int [] longestRoad;
	int[][] grafo;
	PVector vHead = new PVector (0, 0);
	PVector lastMove = new PVector (0, 0);
	String direction = "";
	
	public PVector getNewPosition(boolean map[][], PVector snakeHead, PVector snakeTail, PVector apple, int snakeLenght) {
		this.snakeLength = snakeLenght;
		mapb = map;
		grafo = createGraph(map);
		vHead.set(snakeHead);
		int appleNodo = transformVectorIntoNodo(apple);
		headNodo = transformVectorIntoNodo(snakeHead);
		tailNodo = transformVectorIntoNodo(snakeTail);
		
		return getSolution(dij.dijkstra(grafo,  appleNodo));
	}
	private PVector getSolution(int[] dist) {
		//CABEZA DE LA SERPIENTE
		int originNodo = headNodo;
		int i = (int) originNodo / filas;
		int j = originNodo % filas;
		
		int min = Integer.MAX_VALUE;
		int provisionalNodo = 0;
		//ARRIBA
		if (i > 0 && !direction.equals("abajo")) {
			if (checkMove((i - 1) * filas + j, tailNodo)) {
				if (dist[(i - 1) * filas + j] < min) {
					min = dist[(i - 1)*filas + j];
					provisionalNodo = (i-1)*filas + j;
				}
			}
		}
		//ABAJO
		if (i<filas - 1 && !direction.equals("arriba")) {
			if(checkMove((i+1)*filas + j, tailNodo)) {
				if (dist[(i+1) * filas + j] < min) {
					min = dist[(i + 1) * filas + j];
					provisionalNodo = (i+1) * filas + j;
				}
			}
		}
		// IZQUIERDA
		if (j < filas - 1 && !direction.equals("izquierda")) {
		    if (checkMove(i * filas + j + 1, tailNodo)) {
		        if (dist[i * filas + j + 1] < min) {
		            min = dist[i * filas + j + 1];
		            provisionalNodo = i * filas + j + 1;
		        }
		    }
		}
		// DERECHA
		if (j > 0 && !direction.equals("derecha")) {
		    if (checkMove(i * filas + j - 1, tailNodo)) {
		        if (dist[i * filas + j - 1] < min) {
		            min = dist[i * filas + j - 1];
		            provisionalNodo = i * filas + j - 1;
		        }
		    }
		}
		//CAMINO LARGO
		if (min == Integer.MAX_VALUE) {
			provisionalNodo = getLongestRoad();
		}
		else {
			contador = 0;
		}
	    // MOVIMIENTO
	    lastMove.set(
	        (provisionalNodo % filas) - vHead.x,
	        (provisionalNodo / filas) - vHead.y
	    );

	    // NUEVA DIRECCIÓN
	    if (lastMove.x == 1) {
	        direction = "derecha";
	    } else if (lastMove.x == -1) {
	        direction = "izquierda";
	    } else if (lastMove.y == 1) {
	        direction = "abajo";
	    } else if (lastMove.y == -1) {
	        direction = "arriba";
	    }

	    return lastMove;
	}
	//METODO PARA QUE NO SE ENCIERRA
	private boolean checkMove(int nextNodo, int tailNodo) {
		boolean map[][] = createFutureMap();
		int[] dist = dij.dijkstra(createGraph(map), nextNodo);
		
		if(dist[tailNodo] == Integer.MAX_VALUE && (numberOfPosibleMoves(dist)-5) < snakeLength) {
			return false;
		}else {
			return true;
		}
	}
	//BOOLEANO CON LA POSICION DE LA SERPIENTE
	private boolean[][] createFutureMap(){
		boolean[][] newMap = new boolean[filas][filas];
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < filas; j++) {
				newMap[i][j] = mapb[i][j];
			}
		}
		PVector tail = transformIntoVector (tailNodo);
		PVector head = transformIntoVector (headNodo);
		
		if (tail.x >= filas || tail.y >= filas || head.x >= filas || head.y >= filas) {
		    System.out.println("⚠️ Coordenadas fuera de rango:");
		    System.out.println("tail: " + tail + " | tailNodo: " + tailNodo);
		    System.out.println("head: " + head + " | headNodo: " + headNodo);
		}
		
		newMap[(int) tail.y][(int) tail.x] = true;
		
		if(tail.y > 0) {
			if (newMap[(int) tail.y - 1][(int) tail.x] == false) {
				newMap[(int) tail.y - 1][(int) tail.x] = true;
			}
		}
		if (tail.y < filas - 1) {
			if(newMap[(int) tail.y + 1][(int) tail.x] == false) {
				newMap[(int) tail.y + 1][(int) tail.x] = true;
			}
		}
		if (tail.x > 0) {
			if (newMap[(int) tail.y][(int) tail.x - 1] == false) {
				newMap[(int) tail.y][(int) tail.x - 1] = true;
			}
		}
		if(tail.x < filas - 1) {
			if (newMap[(int) tail.y][(int) tail.x + 1] == false) {
				newMap[(int) tail.y][(int) tail.x + 1] = true;
			}
		}
		//CABEZA A FALSE
		newMap[(int) head.y][(int) head.x] = false;
		return newMap;
	}
	//CUENTA CASILLAS A LAS QUE SE PUEDE ACCEDER
	private int numberOfPosibleMoves(int[] dist) {
		int cont = 0;
		for (int i = 0; i < dist.length; i++) {
			if(dist[i] != Integer.MAX_VALUE) {
				cont++;
			}
		}
		return cont;
	}
	public int [][] createGraph(boolean[][] matriz) {
		int matrizSize = matriz.length;
		int totalSize = matrizSize * matrizSize;
		int [][] graph = new int[totalSize][totalSize];
		
		for (int i = 0; i < totalSize; i++) {
			for (int j = 0; j < totalSize; j++) {
				graph[i][j] = 0;
			}
		}
		
		for (int i = 0; i <matrizSize; i++) {
			for (int j = 0; j < matrizSize; j++) {
				if (i > 0) {
					if (matriz[i - 1][j] == true) {
						graph[i * matrizSize + j][(i - 1) * matrizSize + j] = 1;
					}
				}
				if (i < matrizSize - 1) {
					if (matriz[i + 1][j] == true) {
						graph[ i * matrizSize + j][(i + 1) * matrizSize + j] = 1;
					}
				}
				if (j > 0) {
					if (matriz[i][j - 1] == true) {
						graph[i * matrizSize + j][i * matrizSize + (j - 1)] = 1;
					}
				}
				if (j < matrizSize - 1) {
					if (matriz[i][j+1]==true) {
						graph[i * matrizSize + j][i * matrizSize + (j + 1)] = 1;
					}
				}
			}
		}
		return graph;
	}
	
	private int getLongestRoad() {
		contador++;
		
		//CREAMOS UN MAPA BOOLEANO DONDE LA COLA SEA TRUE
		boolean[][] mapWithTail = new boolean [mapb.length][mapb.length];
		for (int i = 0; i<mapb.length; i++) {
			for (int j = 0; j<mapb.length; j++) {
				mapWithTail[i][j] = mapb[i][j];
			}
		}
		int x = tailNodo % filas;
		int y = tailNodo / filas;
		mapWithTail[y][x] = true;
		
		int[] distWithoutTail = dij.dijkstra(grafo, headNodo);
		int[] distWithTail = dij.dijkstra(createGraph(mapWithTail), headNodo);
		
		if (distWithTail[tailNodo] == Integer.MAX_VALUE) {
			if(contador == 1) {
				int nodoDestination = getFarestNodo(distWithoutTail);
				LongRoad longRoad = new LongRoad(mapb, headNodo, nodoDestination);
				longestRoad = longRoad.getLongestRoad();
			}
		}else {
			if (contador == 1) {
				LongRoad longRoad = new LongRoad (mapb, headNodo, tailNodo);
				longestRoad = longRoad.getLongestRoad();
			}
		}
		return longestRoad[Math.min(contador, longestRoad.length - 1)];
	}
	private int getFarestNodo(int dist[]){
        int nodo = 0;
        int min = 0;
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] != Integer.MAX_VALUE) {
                if (dist[i] >= min) {
                    nodo = i;
                    min = dist[i];
                }
            }
        }
        return nodo;
    }
    
	private int transformVectorIntoNodo(PVector v){
	    if (v.x < 0 || v.y < 0 || v.x >= filas || v.y >= filas) {
	        System.out.println("⚠️ Coordenada fuera de rango: " + v);
	        return 0; // valor por defecto seguro
	    }
	    // La fórmula correcta es: columna + fila * ancho
	    return (int)(v.x + v.y * filas);
	}
	private PVector transformIntoVector(int nodo) {
	    // Validación de seguridad
	    if (nodo < 0 || nodo >= filas * filas) {
	        System.out.println("⚠️ Nodo fuera de rango en transformIntoVector: " + nodo);
	        nodo = Math.max(0, Math.min(nodo, filas * filas - 1));
	    }
	    
	    int y = nodo / filas;
	    int x = nodo % filas;
	    
	    return new PVector(x, y);
	}
}
