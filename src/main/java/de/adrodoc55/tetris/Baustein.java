package de.adrodoc55.tetris;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.adrodoc55.common.util.Coordinate;

public class Baustein {

	private static final Random RANDOM = new Random();
	private static final int anzahlLayouts = 7;

	private TetrisGrid tetris;
	private Coordinate center;
	private Color color;
	private List<Coordinate> layout;

	public Baustein() {
		this(null);
	}

	Baustein(TetrisGrid tetris) {
		this.tetris = tetris;
		// float r = RANDOM.nextFloat();
		// float g = RANDOM.nextFloat();
		// float b = RANDOM.nextFloat();
		// color = new Color(r, g, b);
		int random = RANDOM.nextInt(anzahlLayouts);
		color = getColor(random);
		layout = getLayout(random);
	}

	public void turnClockwise() {
		setVisible(false);
		List<Coordinate> oldLayout = new ArrayList<Coordinate>(layout);
		layout.clear();
		for (Coordinate part : oldLayout) {
			layout.add(new Coordinate(part.getRelativeY() * -1, part
					.getRelativeX(), center));
		}
		if (!canMoveTowards(Coordinate.SELF)) {
			layout = oldLayout;
		}
		setVisible(true);
	}

	public void turnCounterClockwise() {
		setVisible(false);
		List<Coordinate> oldLayout = new ArrayList<Coordinate>(layout);
		layout.clear();
		for (Coordinate part : oldLayout) {
			layout.add(new Coordinate(part.getRelativeY(), part.getRelativeX()
					* -1, center));
		}
		if (!canMoveTowards(Coordinate.SELF)) {
			layout = oldLayout;
		}
		setVisible(true);
	}

	private boolean visible;

	public void setVisible(boolean b) {
		if (tetris == null) {
			return;
		}
		if (visible == b) {
			return;
		}
		visible = b;
		for (Coordinate part : layout) {
			if (part.getX() < 0 || part.getX() >= TetrisGrid.TOTAL_X) {
				continue;
			}
			if (part.getY() < 0 || part.getY() >= TetrisGrid.TOTAL_Y) {
				continue;
			}
			if (b)
				tetris.getGrid()[part.getX()][part.getY()] = color;
			else
				tetris.getGrid()[part.getX()][part.getY()] = null;
		}
		tetris.repaint();
	}

	public boolean canMoveTowards(Coordinate relative) {
		if (tetris == null) {
			return false;
		}
		boolean wasVisible = visible;
		setVisible(false);
		boolean canMove = true;
		for (Coordinate part : layout) {
			int toX = part.getX() + relative.getRelativeX();
			int toY = part.getY() + relative.getRelativeY();
			if (toX < 0 || toX >= TetrisGrid.TOTAL_X) {
				canMove = false;
				break;
			}
			if (toY < 0 || toY >= TetrisGrid.TOTAL_Y) {
				canMove = false;
				break;
			}
			if (tetris.getGrid()[toX][toY] != null) {
				canMove = false;
				break;
			}
		}
		setVisible(wasVisible);
		return canMove;
	}

	public boolean moveTowards(Coordinate direction) {
		if (canMoveTowards(direction)) {
			setVisible(false);
			center.addX(direction.getRelativeX());
			center.addY(direction.getRelativeY());
			setVisible(true);
			return true;
		}
		return false;
	}

	public void moveRecursivelyTowards(Coordinate direction) {
		while (moveTowards(direction))
			;
	}

	public Set<Integer> getAllY() {
		Set<Integer> ys = new HashSet<Integer>(4);
		ys.add(center.getY());
		for (Coordinate part : layout) {
			ys.add(part.getY());
		}
		return ys;
	}

	public void setTetris(TetrisGrid tetris) {
		this.tetris = tetris;
	}

	public void setCenter(Coordinate center) {
		this.center = center;
		for (Coordinate part : layout) {
			part.setBase(center);
		}
	}

	public Color getColor() {
		return color;
	}

	public List<Coordinate> getLayout() {
		return layout;
	}

	private static Color getColor(int c) {
		c %= anzahlLayouts;
		if (c < 0) {
			c += anzahlLayouts;
		}
		switch (c) {
		case 0:
			return Color.RED;
		case 1:
			return Color.GREEN;
		case 2:
			return Color.YELLOW;
		case 3:
			return Color.BLUE;
		case 4:
			return Color.CYAN;
		case 5:
			return Color.ORANGE;
		case 6:
			return Color.PINK;
		default:
			throw new RuntimeException("This Exception cannot be thrown");
		}
	}

	private static List<Coordinate> getLayout(int l) {
		l %= anzahlLayouts;
		List<Coordinate> layout = new ArrayList<Coordinate>(4);
		switch (l) {
		case 0:
			layout.add(new Coordinate(0, 0));
			layout.add(new Coordinate(0, 1));
			layout.add(new Coordinate(1, 0));
			layout.add(new Coordinate(-1, 0));
			break;
		case 1:
			layout.add(new Coordinate(0, 0));
			layout.add(new Coordinate(0, 1));
			layout.add(new Coordinate(1, 0));
			layout.add(new Coordinate(1, 1));
			break;
		case 2:
			layout.add(new Coordinate(-1, 0));
			layout.add(new Coordinate(0, 0));
			layout.add(new Coordinate(0, 1));
			layout.add(new Coordinate(1, 1));
			break;
		case 3:
			layout.add(new Coordinate(1, 0));
			layout.add(new Coordinate(0, 0));
			layout.add(new Coordinate(0, 1));
			layout.add(new Coordinate(-1, 1));
			break;
		case 4:
			layout.add(new Coordinate(0, -1));
			layout.add(new Coordinate(0, 0));
			layout.add(new Coordinate(0, 1));
			layout.add(new Coordinate(-1, 1));
			break;
		case 5:
			layout.add(new Coordinate(0, -1));
			layout.add(new Coordinate(0, 0));
			layout.add(new Coordinate(0, 1));
			layout.add(new Coordinate(1, 1));
			break;
		case 6:
			layout.add(new Coordinate(0, -1));
			layout.add(new Coordinate(0, 0));
			layout.add(new Coordinate(0, 1));
			layout.add(new Coordinate(0, 2));
			break;
		}
		return layout;
	}

}
