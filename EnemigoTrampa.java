package Practica_6;

import java.awt.Color;
import java.awt.Graphics;

public class EnemigoTrampa extends Enemigo {

    public EnemigoTrampa(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    @Override
    public void dibujar(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, ancho, alto);
    }
    
}
