package Practica_6;

import java.awt.*;

public abstract class Enemigo extends Entidad {
    public Enemigo(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto, false);
    }

    //TODO: Terminar metodo de colision
//    public void verificarColisiones(List<Entidad> entidades) {
//        enSuelo = false;
//        for (Entidad e : entidades) {
//            if (e instanceof Plataforma && getRect().intersects(e.getRect())) {
//                y = e.getRect().y - alto;
//                dy = 0;
//                enSuelo = true;
//            }
//            if (e instanceof Enemigo && getRect().intersects(e.getRect())) {
//                x = 50; y = 500; dy = 0;
//            }
//        }
//    }
}
