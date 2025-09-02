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
	boolean greenBox = true;
	boolean purpleBox = true;
	
	@Override
	public void settings() {
		size(500, 540);
	}
	@Override
	public void setup() {
		frameRate(25);
	}
	@Override
	public void draw() {
		background(25);
		drawMap();
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
