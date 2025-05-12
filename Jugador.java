package Practica_6;

import java.awt.*;
import java.util.List;

public class Jugador extends Entidad {
    private int dy = 0;
    private boolean izquierda = false, derecha = false, enSuelo = false, usoDobleSalto = false;

    public Jugador(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    public void actualizar() {
        if (izquierda) x -= 4;
        if (derecha) x += 4;
        dy += 1;
        y += dy;
    }

    public void verificarColisiones(List<Entidad> entidades) {
        enSuelo = false;
        for (Entidad e : entidades) {
            if (e instanceof Plataforma && getRect().intersects(e.getRect())) {
                y = e.getRect().y - alto;
                dy = 0;
                enSuelo = true;
            }
            if (e instanceof Enemigo && getRect().intersects(e.getRect())) {
                x = 50; y = 500; dy = 0;
            }
        }
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, ancho, alto);
    }

    public void saltar() {
        if (enSuelo) dy = -15;
        usoDobleSalto = false;
    }

    //Corregir doble salto
    /* 
    public void dobleSalto(){
        if(dy >= -15 && dy <= -12 && usoDobleSalto == false ){
            dy = -30;
            usoDobleSalto = true;
        }
    }
        */

    public void setIzquierda(boolean b) { izquierda = b; }
    public void setDerecha(boolean b) { derecha = b; }

    public int getX() { return x; }
    public int getY() { return y; }
}
