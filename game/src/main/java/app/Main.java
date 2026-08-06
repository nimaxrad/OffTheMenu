package app;


import javax.swing.JFrame;

import core.GamePanel;

/**
 * Entry point for Off the Menu.
 * Creates the window and starts the game.
 */
public class Main {

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Off the Menu™");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack(); // sizes the window to fit the panel

        window.setLocationRelativeTo(null); // center on screen
        window.setVisible(true);
        gamePanel.startGame();
    }
}