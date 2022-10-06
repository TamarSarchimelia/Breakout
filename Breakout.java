
/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/* Method: run() */
	/** Runs the Breakout program. */

	private GRect paddle;
	private boolean touch = false;
	private GOval BALL;
	private RandomGenerator rand = RandomGenerator.getInstance();
	private double MIN_VX = 1;
	private double MAX_VX = 3;
	private double PAUSE = 10;
	private double Vy = 3;

	int brickNumbers = NBRICK_ROWS * NBRICKS_PER_ROW;
	

	public void run() {
		/* You fill this in, along with any subsidiary methods */
		rectangles();
		paddle();
		addMouseListeners();
		// hear I wrote code for paint a heart
		

		for (int i = 0; i < NTURNS; i++) {
			ball();

			moveBall(i);
			if (brickNumbers == 0) {
				break;
			}
		}
	}

	
	// this code animations ball
	private void moveBall(int i) {
		// he waits for a click to start animation
		if (i == 0) {
			waitForClick();
		}
		// also rands angle
		double Vx = VX();
		if (rand.nextBoolean(0.5))
			Vx = -Vx;
		Vx = rand.nextDouble(1.0, 3.0);
		if (rand.nextBoolean(0.5))
			Vx = -Vx;
		hitBall(Vx);
	}

// this code listens if the ball hit walls, and if hits he chances an angle of velocity
	private void hitBall(double Vx) {

		while (true) {
			if (hitRight() || hitLeft()) {
				Vx = -Vx;
			}
			if (hitUp()) {
				Vy = -Vy;
			}
			// its for floor, and is ball will be at the floor game will be start aain, but
			// we have 3 heart.
			if (hitDown()) {
				remove(BALL);
				break;
			}
			// but also i wrote a code for breaks
			hitSomething(Vx);
			BALL.move(Vx, Vy);
			pause(PAUSE);
			// this is for this time, if ball hits every break. at this moments ball stops
			if (brickNumbers == 0) {
				break;
			}
		}

	}

// and this code is for breaks, but if ball hits paddle he does not removes
	private void hitSomething(double Vx) {
		for (int i = 0; i < 3; i++) {
			// code watches are in that point something or not
			GObject object = getElementAt(BALL.getX() + i * BALL_RADIUS + 1 * (1 - i), BALL.getY());
			if (object != null) {
				// hear i wrote a code that watch is is label or not
				if (object instanceof GLabel)
					break;
				if (object != paddle) {
					remove(object);
					
					// also i have Acceleration, at that time
					Vy = - Vy;
					// this counts how many breaks i have at that time for if i have zero one to
					// stop playing game
					brickNumbers--;
					break;
				}
			}
			GObject objectTwo = getElementAt(BALL.getX() + i * BALL_RADIUS + 1 * (1 - i),
					BALL.getY() + BALL_RADIUS * 2);
			if (objectTwo != null) {
				if (object instanceof GLabel)
					break;
				if (objectTwo != paddle) {
					remove(objectTwo);
					brickNumbers--;
				}
				Vy = - Vy;
				break;
			}
			GObject objectThree = getElementAt(BALL.getX(), BALL.getY() + i * BALL_RADIUS + 1 * (1 - i));
			if (objectThree != null) {
				if (object instanceof GLabel)
					break;
				if (objectThree != paddle) {
					remove(objectThree);
					brickNumbers--;
				}
				Vx = - Vx;
				break;
			}
			GObject objectFour = getElementAt(BALL.getX() + BALL_RADIUS * 2,
					BALL.getY() + i * BALL_RADIUS + 1 * (1 - i));
			if (objectFour != null) {
				
				if (objectFour != paddle) {
					remove(objectFour);
					brickNumbers--;
				}
				Vx = -Vx;
				break;
			}
		}
	}

// this code is for right wall 
	private boolean hitRight() {
		return BALL.getX() + BALL_RADIUS * 2 >= getWidth();
	}

// this code is for left wall
	private boolean hitLeft() {
		return BALL.getX() <= 0;
	}

// its for up wall
	private boolean hitUp() {
		return BALL.getY() < 0;
	}

// its for floor
	private boolean hitDown() {
		return BALL.getY() + BALL_RADIUS * 2 > getHeight();
	}

//this code is for red ball
	private void ball() {
		BALL = new GOval(getWidth() / 2 - BALL_RADIUS / 2, getHeight() / 2 - BALL_RADIUS / 2, BALL_RADIUS, BALL_RADIUS);
		BALL.setFilled(true);
		BALL.setColor(Color.red);
		add(BALL);
	}

// this is for random velocity
	private double VX() {
		return rand.nextDouble(MIN_VX, MAX_VX);
	}

// its for paddle, to changes his point on the canva
	public void mousePressed(MouseEvent e) {
		touch = paddle.contains(e.getX(), e.getY());
	}

// if we touch him and go to other point (sorry, I have bad English :( )
	public void mouseDragged(MouseEvent e) {
		if (touch) {
			if ((e.getX() - PADDLE_WIDTH / 2) < 0) {
				paddle.setLocation(0, getHeight() - PADDLE_Y_OFFSET);
			} else {
				if ((e.getX() + PADDLE_WIDTH) > getWidth()) {
					paddle.setLocation(getWidth() - PADDLE_WIDTH, getHeight() - PADDLE_Y_OFFSET);
				} else {
					paddle.setLocation(e.getX() - PADDLE_WIDTH / 2, getHeight() - PADDLE_Y_OFFSET);
				}
			}

		}
	}

// this code creats paddle
	private void paddle() {
		paddle = new GRect(getWidth() / 2 - PADDLE_WIDTH / 2, getHeight() - PADDLE_Y_OFFSET, PADDLE_WIDTH,
				PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setColor(Color.black);
		add(paddle);
	}

//  this code creacts rectangles rows
	private void rectangles() {
		for (int i = 0; i < NBRICK_ROWS; i++) {
			for (int j = 0; j < NBRICKS_PER_ROW; j++) {
				rect(i, j);
			}
		}
	}

// this code says what color will be rectangle
	private void rowColor(int i, GRect rect) {
		if (i < 2) {
			rect.setColor(Color.red);
		} else if (i < 4) {
			rect.setColor(Color.ORANGE);
		} else if (i < 6) {
			rect.setColor(Color.yellow);
		} else if (i < 8) {
			rect.setColor(Color.green);
		} else {
			rect.setColor(Color.cyan);
		}
	}

//this code creats one rectangle
	private GRect rect(int a, int b) {
		int x = (getWidth() - NBRICKS_PER_ROW * BRICK_WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / 2;
		GRect rect = new GRect(x + b * BRICK_WIDTH + b * BRICK_SEP, a * BRICK_HEIGHT + a * BRICK_SEP + BRICK_Y_OFFSET,
				BRICK_WIDTH, BRICK_HEIGHT);
		rect.setFilled(true);
		rowColor(a, rect);
		add(rect);
		return rect;

	}

}
