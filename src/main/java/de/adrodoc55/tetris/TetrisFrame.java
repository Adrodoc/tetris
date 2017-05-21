package de.adrodoc55.tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import de.adrodoc55.common.util.Coordinate;

public class TetrisFrame extends JFrame {

  private static final long serialVersionUID = 1L;

  private int highScore = 0;

  public TetrisFrame() {
    super("Tetris");
    init();
  }

  private void init() {
    JPanel contentPane = new JPanel(new BorderLayout());
    setContentPane(contentPane);

    JLabel scoreLabel = new JLabel("Score: 0");
    scoreLabel.setHorizontalAlignment(JLabel.CENTER);
    JLabel highScoreLabel = new JLabel("Highscore: 0");
    highScoreLabel.setHorizontalAlignment(JLabel.CENTER);
    JPanel scorePanel = new JPanel();
    scorePanel.add(scoreLabel);
    scorePanel.add(highScoreLabel);
    scorePanel.setBorder(new BevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, Color.GRAY));
    contentPane.add(scorePanel, BorderLayout.NORTH);

    TetrisQueue queue = new TetrisQueue();
    queue.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.GRAY));
    queue.setBackground(Color.DARK_GRAY);
    contentPane.add(queue, BorderLayout.EAST);

    TetrisGrid tetris = new TetrisGrid(queue, scoreLabel);
    tetris.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.GRAY));
    contentPane.add(tetris, BorderLayout.CENTER);

    JButton startButton = new JButton("Start");
    startButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        if (tetris.getScore() > highScore) {
          highScore = tetris.getScore();
          highScoreLabel.setText("Highscore: " + highScore);
        }
        tetris.start();
      }
    });
    contentPane.add(startButton, BorderLayout.SOUTH);

    KeyEventDispatcher dispatcher = new KeyEventDispatcher() {

      @Override
      public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
          Baustein baustein = tetris.getBaustein();
          if (baustein == null) {
            return false;
          }
          switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
              baustein.moveTowards(Coordinate.LEFT);
              return true;
            case KeyEvent.VK_D:
              baustein.moveTowards(Coordinate.RIGHT);
              return true;
            case KeyEvent.VK_S:
              baustein.moveRecursivelyTowards(Coordinate.DOWN);
              return true;
            case KeyEvent.VK_Q:
              baustein.turnCounterClockwise();
              return true;
            case KeyEvent.VK_E:
              baustein.turnClockwise();
              return true;
          }
        }
        return false;
      }
    };
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);

    pack();
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

}
