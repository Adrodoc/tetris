package de.adrodoc55.tetris;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import de.adrodoc55.common.util.Coordinate;

public class TetrisQueue extends JComponent {

	private static final long serialVersionUID = 1L;
	private static final int length = 4;
	private List<Baustein> queue = new ArrayList<Baustein>(3);

	public TetrisQueue() {
		while (queue.size() <= length) {
			queue.add(new Baustein());
		}
	}

	public Baustein getNextBaustein() {
		Baustein baustein = queue.get(0);
		queue.remove(0);
		queue.add(new Baustein());
		repaint();
		return baustein;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		for (int x = 0; x < length; x++) {
			Baustein baustein = queue.get(x);
			baustein.setCenter(new Coordinate(2, x * 5 + 2));
			g.setColor(baustein.getColor());
			for (Coordinate part : baustein.getLayout()) {
				g.fillRect(part.getX() * TetrisGrid.blockSize, part.getY()
						* TetrisGrid.blockSize, TetrisGrid.blockSize, TetrisGrid.blockSize);
			}
		}
		getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(5 * TetrisGrid.blockSize, length * 5
				* TetrisGrid.blockSize);
	}

}
