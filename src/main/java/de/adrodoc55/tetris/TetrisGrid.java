package de.adrodoc55.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.Timer;

import de.adrodoc55.common.util.Coordinate;

public class TetrisGrid extends JComponent implements ActionListener {

	private static final long serialVersionUID = 1L;

	public static final int TOTAL_X = 11;
	public static final int TOTAL_Y = 20;
	public static int blockSize = 25;
	private Baustein baustein;
	private final TetrisQueue queue;

	private Color[][] grid = new Color[TOTAL_X][TOTAL_Y];

	private Timer gameTimer = new Timer(200, this);
	private Timer endTimer;

	private int score = 0;
	private JLabel scoreLabel;

	public TetrisGrid(TetrisQueue queue, JLabel scoreLabel) {
		this.queue = queue;
		this.scoreLabel = scoreLabel;
	}

	public void start() {
		gameTimer.stop();
		if (endTimer != null) {
			endTimer.stop();
		}
		clear();
		score = 0;
		nextBaustein();
		gameTimer.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (!baustein.moveTowards(Coordinate.DOWN)) {
			checkRows(baustein.getAllY());
			if (!gameOver()) {
				nextBaustein();
			}
		}
		repaint();
	}

	public int getScore() {
		return score;
	}

	public Baustein getBaustein() {
		return baustein;
	}

	public void nextBaustein() {
		this.baustein = queue.getNextBaustein();
		baustein.setTetris(this);
		baustein.setCenter(new Coordinate(TOTAL_X / 2, 1));
		if (!baustein.canMoveTowards(Coordinate.SELF)) {
			endGame();
		} else {
			incrementScore();
			baustein.setVisible(true);
		}
	}

	private void incrementScore() {
		score++;
		scoreLabel.setText("Score: " + score);
	}

	private void clear() {
		for (int x = 0; x < TOTAL_X; x++) {
			for (int y = 0; y < TOTAL_Y; y++) {
				grid[x][y] = null;
			}
		}
	}

	private void checkRows(Iterable<Integer> rows) {
		for (int y : rows) {
			checkRow(y);
		}
	}

	private void checkRow(int y) {
		boolean rowFull = true;
		for (Color[] x : grid) {
			if (x[y] == null) {
				rowFull = false;
				break;
			}
		}
		if (rowFull) {
			for (Color[] x : grid) {
				for (int yTemp = y; yTemp >= 0; yTemp--) {
					if (yTemp == 0) {
						x[yTemp] = null;
					} else {
						x[yTemp] = x[yTemp - 1];
					}
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		int xBlockSize = getWidth() / TOTAL_X;
		int yBlockSize = getHeight() / TOTAL_Y;
		blockSize = Math.min(xBlockSize, yBlockSize);
		g.setColor(Color.BLACK);
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[x].length; y++) {
				Color color = grid[x][y];
				if (color == null) {
					color = Color.BLACK;
				}
				g.setColor(color);
				g.fillRect(x * blockSize, y * blockSize, blockSize, blockSize);
			}
		}
		getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(TOTAL_X * blockSize, TOTAL_Y * blockSize);
	}

	public Color[][] getGrid() {
		return grid;
	}

	private boolean gameOver() {
		for (Color[] x : grid) {
			if (x[0] != null) {
				endGame();
				return true;
			}
		}
		return false;
	}

	private void endGame() {
		gameTimer.stop();
		baustein = null;
		if (endTimer != null) {
			endTimer.stop();
		}
		endTimer = new Timer(10, new ActionListener() {
			private int x = 0;
			private int y = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				// if (x == 0 && y >= 3 && y <= 7) {
				// grid[x][y] = Color.RED;
				// } else if (x == 1 && (y == 3 || y == 7)) {
				// grid[x][y] = Color.RED;
				// } else if (x == 2 && (y == 3 || y == 5 || y == 7)) {
				// grid[x][y] = Color.RED;
				// } else if (x == 3 && (y == 3 || (y >= 5 && y <= 7))) {
				// grid[x][y] = Color.RED;
				// } else if (x == 5 && y >= 3 && y <= 7) {
				// grid[x][y] = Color.RED;
				// } else if (x == 6 && (y == 3 || y == 5)) {
				// grid[x][y] = Color.RED;
				// } else if (x == 7 && y >= 3 && y <= 7) {
				// grid[x][y] = Color.RED;
				// } else {
				grid[x][y] = Color.GRAY;
				// }
				repaint();

				if (y % 2 == 0) {
					x++;
					if (x >= TOTAL_X) {
						x--;
						y++;
					}
				} else {
					x--;
					if (x < 0) {
						x++;
						y++;
					}
				}

				if (y >= TOTAL_Y) {
					endTimer.stop();
				}
			}
		});
		endTimer.start();
	}

}