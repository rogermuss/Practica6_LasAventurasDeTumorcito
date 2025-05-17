package Practica_6;

import java.awt.Color;
import javax.swing.JFrame;
public class GameFrame extends JFrame {
    public GameFrame(boolean opcionJugar) {
        setTitle("Juego de Plataforma 2D");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        if(opcionJugar) {
            setContentPane(new GamePanel(2));  
        }
        else {
            setContentPane(new GamePanel());
        }
        setBackground(Color.LIGHT_GRAY);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GameFrame(true);
    }
}
