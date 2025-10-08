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
	
	boolean greenBox = false;
	boolean purpleBox = false;
	
	Manzana apple = new Manzana();
	Snake humanSnake = new Snake(100, 200, 100, new PVector(2, 2), new PVector(2, 1));
	Snake botSnake = new Snake(100, 100, 200, new PVector(18, 18), new PVector(18, 19));
	
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
		
		playHumanSnake();
		playBotSnake(botSnake);
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
		for (int i = 1; i < botSnake.posX.size(); i++) {
			map[botSnake.posY.get(i)][botSnake.posX.get(i)] = false;
		}
		for (int i = 1; i < humanSnake.posX.size(); i++) {
			map[humanSnake.posY.get(i)][humanSnake.posX.get(i)] = false;
		}
	}
	void drawMap() {
		  // Fondo gris de abajo
		  fill(100, 100, 100);
		  rect(0, 500, width, 40);
		  //CASILLAS DEL MARCADOR
		  fill(100, 100, 100);
		  rect(30, 510, 210, 20);
		  
		  fill(100, 100, 200);
		  rect(270, 510, 210, 20);
		  // --- Botón verde ---
		  if (!greenBox) {
		    if (mouseX >= 30 && mouseX <= 240 && mouseY >= 510 && mouseY <= 530) {
		      fill(200, 255, 200); // más claro cuando está encima
		    } else {
		      fill(100, 200, 100); // color normal
		    }
		  } else {
		    fill(250, 50, 50); // rojo cuando está desactivado
		  }
		  rect(30, 510, 210, 20);

		  // --- Botón morado ---
		  if (!purpleBox) {
		    if (mouseX >= 270 && mouseX <= 480 && mouseY >= 510 && mouseY <= 530) {
		      fill(200, 200, 255); // más claro cuando está encima
		    } else {
		      fill(100, 100, 200); // color normal
		    }
		  } else {
		    fill(250, 50, 50); // rojo cuando está desactivado
		  }
		  rect(270, 510, 210, 20);
		}

		void drawApple() {
		  fill(215, 0, 75);
		  rect(apple.position.x * bs, apple.position.y * bs, bs, bs);
		}
		
		void playHumanSnake() {
			if(humanSnake.alive == true) {
				moveHumanSnake();
				drawSnake(humanSnake);
				detectBorder(humanSnake);
				snakeCrash(humanSnake, botSnake);
			}
		}
		void moveHumanSnake() {
			humanSnake.mover((int) direction.x, (int) direction.y);
			eat(humanSnake);
		}
		void eat(Snake snake) {
			if (snake.posX.get(0) == apple.position.x && snake.posY.get(0) == apple.position.y) {
				apple.spawn(map);
				snake.comer();
			}
		}
		void drawSnake(Snake snake) {
			fill(snake.r, snake.g, snake.b);
			for(int i = 0; i < snake.posX.size(); i++) {
				rect(snake.posX.get(i) * bs, snake.posY.get(i) * bs, bs, bs);
			}
		}
		
		void detectBorder(Snake s) {
			if(s.posX.get(0) < 0 || s.posX.get(0) > (columnas - 1) || s.posY.get(0) < 0 || s.posY.get(0) > (filas - 1)) {
				s.restart();
			}
		}
		
		void snakeCrash(Snake s1, Snake s2) {
			if (s1.alive == true) {
				for (int i = 2; i < s1.posX.size(); i++) {
					if(s1.posX.get(0) == s1.posX.get(i) && s1.posY.get(0) == s1.posY.get(i)) {
						s1.restart();
					}
				}
			}
			if (s1.alive == true && s2.alive == true) {
				for (int i = 0; i < s2.posX.size(); i++) {
					if (s1.posX.get(0) == s2.posX.get(i) && s1.posY.get(0) == s2.posY.get(i)) {
						s1.restart();
					}
				}
			}
		}
		void playBotSnake(Snake bot) {
			if (bot.alive == true) {
				moveBotSnake(bot);
				drawSnake(bot);
				detectBorder(bot);
				snakeCrash(bot, humanSnake);
			}
		}
		void moveBotSnake(Snake s1) {
			s1.mover(apple.getPosition(), map);
			eat(s1);
		}
		void deleteSnake(Snake s) {
			s.posX.clear();
			s.posY.clear();
			s.alive = false;
		}
		
		void restartGame() {
			humanSnake.restart();
			initGame();
		}
		@Override
		public void keyPressed() {
			if (key == 'w' || keyCode == UP) {
				if (direction.y != 1) {
					direction.set(0, -1);
				}
			}
			if (key == 's' || keyCode == DOWN) {
				if (direction.y != -1) {
					direction.set(0, 1);
				}
			}
			if (key == 'a' || keyCode == LEFT) {
				if (direction.x != 1) {
				direction.set(-1, 0);			
				}
			}
			if (key == 'd' || keyCode == RIGHT) {
				if(direction.x != -1) {
				direction.set(1,0);
				}
			}
			if (key == 'r') {
				restartGame();
			}
			}
		@Override
		public void mouseClicked() {
		  // Botón verde
		  if (mouseX >= 30 && mouseX <= 240 && mouseY >= 510 && mouseY <= 530) {
		    greenBox = !greenBox;
		    if (humanSnake.alive == true) {
		    	deleteSnake(humanSnake);
		    }else {
		    	humanSnake.restart();
		    }
		  }
		  // Botón morado
		  if (mouseX >= 270 && mouseX <= 480 && mouseY >= 510 && mouseY <= 530) {
		    purpleBox = !purpleBox;
		    if (botSnake.alive == true) {
		    	deleteSnake(botSnake);
		    }else {
		    	botSnake.restart();
		    }
		  }
		}

}
