package Codigo;
import processing.core.*;
public class SnakeMain extends PApplet{
	public static void main(String[] args) {
		PApplet.main(new String[] {Codigo.SnakeMain.class.getName()});
	}
	int filas = 25;
	int columnas = 25;
	int bs = 20;
	
	boolean map[][] = new boolean [filas][columnas];
	PVector direction = new PVector(1,0);
	
	boolean greenBox = true;
	boolean purpleBox = true;
	
	Manzana apple = new Manzana();
	//Snake humanSnake = new Snake(100, 200, 100, new PVector(2, 2), new PVector(2, 1));
	//Snake botSnake = new Snake()(100, 100, 200, new PVector(18, 18), new PVector(18, 19));
	
	@Override
	public void settings() {
		size(500, 540);
	}
	@Override
	public void setup() {
		frameRate(25);
		initGame();
	}
	@Override
	public void draw() {
		background(25);
		updateMap();
		drawMap();
		drawApple();
		
		//playHumanSnake();
		//playBotSnake(botSnake);
	}
	void initGame() {
		updateMap();
		apple.spawn(map);
	}
	void updateMap() {
		for (int i=0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				map[i][j]=true;
		}	
	}
		//for (int i = 1; i < botSnake.posX.size(); i++) {
		//	map[botSnake.posY.get(i)][botSnake.posX.get(i)] = false;
		//}
		//for (int i = 1; i < humanSnake.posX.size(); i++) {
		//	map[humanSnake.posY.get(i)][humanSnake.posX.get(i)] = false;
		//}
	}
	void drawMap() {
		//DIBUJAMOS EL CUADRADO GRIS DE ABAJO
		fill(100, 100, 100);
		rect(0, 500, width, 40);
		//DIBUJAMOS LAS DOS CASILLAS DEL MARCADOR
		fill(100, 200,100);
		rect(30, 510,210,20);
		
		fill(100,100,200);
		rect(270, 510, 210, 20);
		
		if(greenBox == false) {
			fill(250,50,50);
			rect(30, 510, 210, 20);
		}
		if(purpleBox == false) {
			fill(250,50,50);
			rect(270,510,210,20);
		}
	}
	void drawApple() {
		fill(215, 0, 75);
		rect(apple.position.x * bs, apple.position.y * bs, bs, bs);
	}
	
	@Override
	public void mouseClicked() {
		//COMPRUEBA SI ESTA EN EL CUADRADO VERDE EL CLICK. SI ES ASI CAMBIARA EL ESTADO DEL SNAKE MUERTO-VIVO
		if (mouseX >= 30 && mouseX <= 240 && mouseY >= 510 && mouseY <= 530) {
			greenBox = !greenBox;
		}
		//COMPRUEBA SI ESTA EN EL CUADRADO MORADO AL HACER CLICK
		if (mouseX >= 270 && mouseX <= 480 && mouseY >= 510 && mouseY <= 530) {
			purpleBox = !purpleBox;
		}
	}
}
