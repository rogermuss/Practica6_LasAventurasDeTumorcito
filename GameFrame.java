package Practica_6;

import java.awt.Color;
import javax.swing.JFrame;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Juego de Plataforma 2D");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new GamePanel());
        setBackground(Color.LIGHT_GRAY);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
