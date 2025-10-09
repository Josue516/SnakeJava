package Codigo;

import java.util.ArrayList;
import processing.core.PVector;

public class Snake {
	//ALMACENAN LAS POSICIONES DEL SNAKE
	public ArrayList<Integer> posX = new ArrayList<>();
	public ArrayList<Integer> posY = new ArrayList<>();
	
	public boolean alive = true;
	
	//POSICIONES POR DONDE EMPIEZAN
	private final PVector initialPosition1;
	private final PVector initialPosition2;
	private Bot movement = new Bot();
	
	public int r,g,b;
	
	public Snake(int r, int g, int b, PVector initial1, PVector initial2) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.initialPosition1 = initial1;
		this.initialPosition2 = initial2;
		
		restart();
	}
	
	//SUMA COORDENADAS A LA SERPIENTE PARA QUE SE MUEVA, DESPUES ELIMINA LA ULTIMA
	//PARA HACER PARECER EL MOVIMIENTO
	public void mover(int x, int y) {
		posX.add(0, posX.get(0)+x);
		posY.add(0, posY.get(0)+y);
		
		posX.remove(posX.size()-1);
		posY.remove(posY.size()-1);
	}
	//BOT MOVIMIENTO
	public void mover(PVector apple, boolean[][]map) {
		PVector headSnake = new PVector(posX.get(0), posY.get(0));
		PVector tailSnake = new PVector(posX.size()-1, posY.get(posY.size()-1));
		
		PVector nextMove = movement.getNewPosition(map, headSnake, tailSnake, apple, posX.size());
		System.out.println("Head: (" + posX.get(0) + ", " + posY.get(0) + 
			    ") NextMove: (" + nextMove.x + ", " + nextMove.y + ")");
		posX.add(0, posX.get(0) + (int)nextMove.x);
		posY.add(0, posY.get(0) + (int)nextMove.y);
		
		posX.remove(posX.size() - 1);
		posY.remove(posY.size() - 1);
	}
	//PARA HACER MAS GRANDE A LA SERPIENTE
	public void comer() {
		posX.add(posX.get(posX.size() - 1));
		posY.add(posY.get(posY.size() - 1));
	}
	//REINICIAR TODO AL COMIENZO
	public void restart() {
		posX.clear();
		posY.clear();
		alive=true;
		
		posX.add((int)initialPosition1.x);
		posY.add((int)initialPosition1.y);
		posX.add((int)initialPosition2.x);
		posY.add((int)initialPosition2.y);
	}
}
