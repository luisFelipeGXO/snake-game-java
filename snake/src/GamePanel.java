//Tela do Jogo

import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener { //Jpanel desenha a tela, actionListener manipula informações sobre a tela 

	static final int SCREEN_WIDTH = 600; // largura da tela
	static final int SCREEN_HEIGHT = 600; // altura da tela 
	static final int UNIT_SIZE = 25; // tamanho da tela 
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; // unidades de objetos na tela
	static final int DELAY = 75; // velocidade de movimentação da cobra
	final int x[] = new int[GAME_UNITS]; // eixo X
	final int y[] = new int[GAME_UNITS]; // eixo Y
	int bodyParts = 6; //tamanho inicial da cobra
	int applesEaten; // blocos comidos 
	int appleX; // identificador do bloco X
	int appleY; // bloco y
	char direction = 'R'; // começando com a mov direita
	boolean running = false; // está rodando? falso. variavel que controla se o jogo está sendo executaod ou nao
	Timer timer; // renderizar a tela de tempos em tempo
	Random random; // gerador aleatorio de bloquinhos 
	
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	} 
	public void draw(Graphics g) {
		
		if(running) {
			/*
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			*/
			g.setColor(Color.white);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i = 0; i< bodyParts;i++) {
				if(i == 0) {
					g.setColor(Color.white);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(250,250,250));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.white);
			g.setFont(new Font("Arial",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move() {
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
			
		}
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i]&& y[0] == y[i])) {
				running = false;
			}
		}
		if(x[0] < 0) {
			running = false;
		}
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		if(y[0] < 0) {
			running = false;
		}
		if(y[0] > SCREEN_WIDTH) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		//
		g.setColor(Color.white);
		g.setFont(new Font("Arial",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		//
		g.setColor(Color.white);
		g.setFont(new Font("Arial",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
			
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		 
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

}
