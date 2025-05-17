package Practica_6;

import java.awt.*;

// Clase para el objeto final del juego (cuadrado amarillo)
public class ObjetivoFinal extends Entidad {

    private boolean alcanzado = false;

    public ObjetivoFinal(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto, false);
    }

    @Override
    public void dibujar(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, ancho, alto);
    }

    public boolean isAlcanzado() {
        return alcanzado;
    }

    public void setAlcanzado(boolean alcanzado) {
        this.alcanzado = alcanzado;
    }
}
