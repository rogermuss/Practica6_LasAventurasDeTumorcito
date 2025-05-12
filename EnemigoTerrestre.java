package Practica_6;

import java.awt.*;

public class EnemigoTerrestre extends Enemigo {
    public EnemigoTerrestre(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, ancho, alto);
    }
}
