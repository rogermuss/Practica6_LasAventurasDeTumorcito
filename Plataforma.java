package Practica_6;

import java.awt.*;

public class Plataforma extends Entidad {
    boolean puedeEscalarse;
    public Plataforma(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, ancho, alto);
    }
    public void setPuedeEscalarse(boolean puedeEscalarse) {
        this.puedeEscalarse = puedeEscalarse;
    }
    public boolean getPuedeEscalarse(){
        return puedeEscalarse;
    }
}
