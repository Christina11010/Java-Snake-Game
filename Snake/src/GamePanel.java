// This is my first ever Java project --- "Snake Game"
// Tutorial by Bro Code

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener{
	// declare
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75; // higher the delay, slower the game is
	final int x[] = new int[GAME_UNITS]; // x units of the body parts of the snake
	final int y[] = new int[GAME_UNITS]; // y units of the body parts of the snake
	// x[0] and y[0] represents the coordinates of the head of the snake
	// x[1] and y[1] represents the first coordinates of the first body part, etc.
	int bodyParts = 6;
	int applesEaten; // initially be 0
	int appleX; // appear randomly
	int appleY;
	char direction = 'R'; // snake begin by going right
	boolean running = false;
	Timer timer;
	Random random;
	
	// constructor
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.white);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame(); // call the startGame method after constructing the game panel
	}
	
	public void startGame() {
		newApple(); // call the newApple method first to create an apple on the screen
		running = true;
		timer = new Timer(DELAY,this); // determine how fast the game is running
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    draw(g);
	}
	// the 'g' after 'Graphics' refers to the parameter name of the 'paintComponent' method and the 'draw' method
	
	public void draw(Graphics g) {
		
		if(running) {
			/*
			// draw a grid to see the positions easier (optional)
			// the default color for the grid line is black, so if the background color is set to black, the grid lines will be invisible
			for(int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // draw the vertical lines
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); // draw the horizontal lines
			}
			*/
			
			// draw the apple
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // this is how big the apple is (fit into one of the item spot)
		
			// draw the head of the snake
			for(int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				// draw the body of the snake
				else {
					// g.setColor(new Color(45, 180, 0));
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))); // give each body part a random color
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			// display the scores at the top of the screen
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score:" + applesEaten)) / 2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}

	// a new apple is generated (the position of the apple) every time when a previous apple is eaten, or begin a new game
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; // generate the x axis for the apple
		appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE; // generate the y axis for the apple 
		// we want the apple to be distributed evenly in the item spot within the grid lines, rather than on the grid lines
	}
	
	public void move() {
		// iterate through all the body parts of the snake
		for(int i = bodyParts; i > 0; i--)  {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		// change the direction of where the snake is headed
		// top left corner is [0, 0]
		switch(direction) {
		case 'U': // 'U' for "up"
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D': // 'D' for "down"
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L': // 'L' for "left"
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R': // 'R' for "right"
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkApple() {
		// examine the coordinates of the snake and the coordinates of the apple
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++; // increase a body part
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollisions() {
		// iterate through all body parts of the snake, starting from the tail and moving towards the head
		
		// check if the head collides with the body
		for(int i = bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false; // then we end the game
			}
		}
		// check if the head touches the left border
		if(x[0] < 0) {
			running = false;
		}
		// check if the head touches the right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		// check if the head touches the top border
		if(y[0] < 0) {
			running = false;
		}
		// check if the head touches the bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		// stop the timer when the game stops running
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		// display the scores at the top center of the screen
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score:" + applesEaten)) / 2, g.getFont().getSize());
		
		//game over text in the center of the screen
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move(); // if the game is running, we want to move the snake
			checkApple();
			checkCollisions();
		}
		repaint(); // if the game is no longer running, call the repaint method 
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			// actually control the movement of the snake
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				// prevent the snake from turning 180 degree (it'll be a game over right away)
				// ensure the snake can only change its direction to left if not currently moving towards the right (if the snake is currently moving towards up or down)
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
