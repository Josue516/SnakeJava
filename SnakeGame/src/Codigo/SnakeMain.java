package Codigo;
import processing.core.*;
import ddf.minim.*;


public class SnakeMain extends PApplet{
	public static void main(String[] args) {
		PApplet.main(new String[] {Codigo.SnakeMain.class.getName()});
	}
	//SONIDO AL COMER LA MANZANA
	Minim minim;
	AudioPlayer eatSound;
	//MUSICA DE FONDO
	AudioPlayer music;
	//SISTEMA DE VIDAS PARA EL JUGADOR
	int vidasPlayer = 3;
	boolean gameOver = false;
	//PUNTAJE USUARIO/BOT
	int playerScore = 0;
	int botScore = 0;
	
	int filas = 25;
	int columnas = 25;
	int bs = 20;
	//SELECTOR DE DIFICULTAD:
	String dificultad = "medio";
	float velocidad = 25; // frameRate base
	String gameState = "menu";
	
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
		minim = new Minim(this);
		eatSound = minim.loadFile("eat.mp3"); // o .wav
		music = minim.loadFile("music.mp3");

		// Reproduce la música en bucle
		//music.loop();
		
		initGame();
	}
	@Override
	public void draw() {
		background(25);
		
		if (gameState.equals("menu")) {
		    drawMenu();
		  } else if (gameState.equals("jugando")) {
		    updateMap();
		    drawMap();
		    drawApple();

		    playHumanSnake();
		    playBotSnake(botSnake);
		  }
		if (gameOver) {
		    background(0);
		    textAlign(CENTER, CENTER);
		    textSize(32);
		    fill(255, 0, 0);
		    text("GAME OVER", width / 2, height / 2 - 40);
		    
		    textSize(20);
		    fill(255);
		    text("Presiona R para reiniciar", width / 2, height / 2 + 40);
		    noLoop(); // detiene el draw
		    return;}
	}
	//MENU DE SELECCION DE NIVEL:
	void drawMenu() {
		  background(30);

		  fill(255);
		  textAlign(CENTER);
		  textSize(28);
		  text("SNAKE GAME", width / 2, 150);

		  textSize(18);
		  text("Selecciona la dificultad \n Recomiendo la dificultad Facil.", width / 2, 220);
		  
		  // Botones
		  fill(100, 200, 100);
		  rect(width / 2 - 100, 260, 200, 40);
		  fill(255);
		  text("FÁCIL", width / 2, 285);

		  fill(230, 200, 100);
		  rect(width / 2 - 100, 320, 200, 40);
		  fill(255);
		  text("MEDIO", width / 2, 345);

		  fill(200, 80, 80);
		  rect(width / 2 - 100, 380, 200, 40);
		  fill(255);
		  text("DIFÍCIL", width / 2, 405);
		  
		  textSize(18);
		  text("Puede volver al menu con la tecla 'm' \n O cambiar en medio del juego con los numeros 1, 2 y 3.", width / 2, 450);
		  
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
		  //DIFICULTAD
		  fill(255);
		  textSize(12);
		  textAlign(LEFT);
		  text("Dificultad: " + dificultad, 30, 495);
		  
		  // ====== MOSTRAR VIDAS ======
		  textAlign(RIGHT);
		  textSize(12);
		  fill(255);
		  text("Vidas: ", 420, 495);
		  
		  // Dibujar corazones/vidas
		  for (int i = 0; i < vidasPlayer; i++) {
		    fill(255, 0, 0); // Rojo para corazones
		    // Dibuja un pequeño cuadrado por cada vida
		    rect(430 + (i * 20), 485, 15, 15);
		  }
		  
		  //PUNTAJES
		  textAlign(CENTER);
		  textSize(12);
		  fill(255);
		  text("Jugador: " + playerScore, 135, 524);
		  text("Bot: " + botScore, 375, 524);
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
				    //SONIDO AL COMER MANZANA
				    if (eatSound != null) {
				        eatSound.rewind();
				        eatSound.play();
				    }
				    
				    // Sumar puntaje según quién haya comido
				    if (snake == humanSnake) {
				      playerScore += 10;
				    } else if (snake == botSnake) {
				      botScore += 10;
				    }
				  }
		}
		void drawSnake(Snake snake) {
			fill(snake.r, snake.g, snake.b);
			for(int i = 0; i < snake.posX.size(); i++) {
				rect(snake.posX.get(i) * bs, snake.posY.get(i) * bs, bs, bs);
			}
		}
		
		void detectBorder(Snake s) {
			if(s.posX.get(0) < 0 || s.posX.get(0) > (columnas - 1) || 
				     s.posY.get(0) < 0 || s.posY.get(0) > (filas - 1)) {
				    
				    if (s == humanSnake) {
				      vidasPlayer--;
				      playerScore = 0;
				      direction.set(1, 0);
				      
				      if (vidasPlayer > 0) {
				        humanSnake.restart();
				      } else {
				        gameOver = true;
				        music.pause();
				      }
				    }
				    
				    if (s == botSnake) {
				      botScore = 0;
				      s.restart();
				    }
				  }
				}
		
		void snakeCrash(Snake s1, Snake s2) {
			 if (s1.alive == true) {
				    for (int i = 2; i < s1.posX.size(); i++) {
				      if(s1.posX.get(0) == s1.posX.get(i) && s1.posY.get(0) == s1.posY.get(i)) {
				        
				        if (s1 == humanSnake) {
				          vidasPlayer--;
				          playerScore = 0;
				          direction.set(1, 0);
				          if (vidasPlayer > 0) {
				            humanSnake.restart();
				          } else {
				            gameOver = true;
				            music.pause();
				          }
				        }
				        
				        if (s1 == botSnake) {
				          botScore = 0;
				          s1.restart();
				        }
				        return;
				      }
				    }
				  }
				  
				  if (s1.alive == true && s2.alive == true) {
				    for (int i = 0; i < s2.posX.size(); i++) {
				      if (s1.posX.get(0) == s2.posX.get(i) && s1.posY.get(0) == s2.posY.get(i)) {
				        
				        if (s1 == humanSnake) {
				          vidasPlayer--;
				          playerScore = 0;
				          direction.set(1, 0);
				          if (vidasPlayer > 0) {
				            humanSnake.restart();
				          } else {
				            gameOver = true;
				            music.pause();
				          }
				        }
				        
				        if (s1 == botSnake) {
				          botScore = 0;
				          s1.restart();
				        }
				        return;
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
			if (key == 'm') {
				  gameState = "menu";
				  humanSnake.restart();
				  botSnake.restart();
				  playerScore = 0;
				  botScore = 0;
				  music.pause();
				  vidasPlayer = 3;
			}
			 if (key == 'r') {
				    // Reiniciar completamente el juego
				    gameOver = false;
				    direction.set(1, 0);
				    music.loop();
				    vidasPlayer = 3;
				    playerScore = 0;
				    botScore = 0;
				    humanSnake.restart();
				    botSnake.restart();
				    apple.spawn(map);
				    loop(); // Reanudar el draw() si estaba detenido
				    
				    // Si estabas en el menú, volver a jugar
				    if (gameState.equals("menu")) {
				      gameState = "jugando";
				      if (!music.isPlaying()) {
				        music.loop();
				      }
				    }
				  }
			// --- Selector de dificultad ---
			  if (key == '1') seleccionarDificultad("fácil");
			  if (key == '2') seleccionarDificultad("medio");
			  if (key == '3') seleccionarDificultad("difícil");
		}
		@Override
		public void stop() {
		    eatSound.close();
		    minim.stop();
		    super.stop();
		}
		@Override
		public void mouseClicked() {
			//MENU PARA SELECCIONAR DIFICULTAD:
			if (gameState.equals("menu")) {
			    if (mouseX >= width / 2 - 100 && mouseX <= width / 2 + 100) {
			      if (mouseY >= 260 && mouseY <= 300) {
			        seleccionarDificultad("fácil");
			        gameState = "jugando";
			        music.loop();
			        initGame();
			      } else if (mouseY >= 320 && mouseY <= 360) {
			        seleccionarDificultad("medio");
			        gameState = "jugando";
			        music.loop();
			        initGame();
			      } else if (mouseY >= 380 && mouseY <= 420) {
			        seleccionarDificultad("difícil");
			        gameState = "jugando";
			        music.loop();
			        initGame();
			      }
			    }
			    return;
			  }
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
		//SELECTOR DE DIFICULTAD:
		void seleccionarDificultad(String nivel) {
			  dificultad = nivel;
			  switch (nivel) {
			    case "fácil":
			      velocidad = 15;
			      break;
			    case "medio":
			      velocidad = 25;
			      break;
			    case "difícil":
			      velocidad = 40;
			      break;
			  }
			  frameRate(velocidad);
			}
}
