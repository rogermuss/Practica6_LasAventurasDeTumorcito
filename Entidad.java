package Practica_6;

import java.awt.*;

public abstract class Entidad {
    protected int x, y, ancho, alto;
    protected boolean activo;

    public Entidad(int x, int y, int ancho, int alto, boolean z) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.activo = z;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, ancho, alto);
    }

    public abstract void dibujar(Graphics g);
}
