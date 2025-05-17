package Practica_6;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Timer;


public class EnemigoTerrestre extends Enemigo {


    private int velocidad = 2;
    private int limiteIzquierdo;  // Límite horizontal izquierdo
    private int limiteDerecho;    // Límite horizontal derecho
    private int limiteSuperior;   // Límite vertical superior
    private int limiteInferior;   // Límite vertical inferior
    private int dy = 0;


    private boolean moverHorizontal = false;  // Movimiento horizontal
    private boolean moverVertical = false;    // Movimiento vertical
    private boolean enSuelo = false;
    private int conSalto = 1;

    public static final  int CON_SALTO = 1;

    public EnemigoTerrestre(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    // Constructor para movimiento horizontal con salto
    public EnemigoTerrestre(int x, int y, int ancho, int alto, int limiteIzquierdo, int limiteDerecho, int conSalto) {
        super(x, y, ancho, alto);
        this.limiteIzquierdo = limiteIzquierdo;
        this.limiteDerecho = limiteDerecho;
        this.moverHorizontal = true;
        if(conSalto == CON_SALTO){
            saltar();
        } 
    }



    public void saltar() {
        int delay = 2000; // 3 segundos

        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (enSuelo) {
                    dy = -15;
                }
            }
        });
        timer.start();
    }

    

    // Método para actualizar la posición del enemigo
    public void actualizar() {
        if (moverHorizontal) {
            x += velocidad;

            // Cambiar dirección si alcanza los límites horizontales
            if (x <= limiteIzquierdo || x + ancho >= limiteDerecho) {
                velocidad = -velocidad;
            }
        }

        if (moverVertical) {
            y += velocidad;

            // Cambiar dirección si alcanza los límites verticales
            if (y <= limiteSuperior || y + alto >= limiteInferior) {
                velocidad = -velocidad;
            }
        }


        //Saltar
        dy += 1;
        y += dy;

    }

    // Nuevo método adaptado para el nivel de la imagen
    public void verificarColisionesPersonalizado(List<Entidad> entidades) {
        enSuelo = false;

        for (Entidad e : entidades) {
            // Colisiones con plataformas
            if (e instanceof Plataforma && getRect().intersects(e.getRect())) {
                Plataforma plataforma = (Plataforma) e;
                Rectangle rectEnemigo = getRect();
                Rectangle rectPlataforma = e.getRect();

                Rectangle interseccion = rectEnemigo.intersection(rectPlataforma);

                // Colisión desde arriba
                if (dy > 0 && rectEnemigo.y + rectEnemigo.height - dy <= rectPlataforma.y) {
                    y = rectPlataforma.y - alto;
                    dy = 0;
                    enSuelo = true;
                }
                // Colisión desde abajo
                else if (dy < 0 && rectEnemigo.y - dy >= rectPlataforma.y + rectPlataforma.height) {
                    y = rectPlataforma.y + rectPlataforma.height;
                    dy = 0;
                }
                // Colisión desde la izquierda
                else if (interseccion.height > interseccion.width &&
                        rectEnemigo.x + rectEnemigo.width - interseccion.width <= rectPlataforma.x) {
                    x = rectPlataforma.x - ancho;
                }
                // Colisión desde la derecha
                else if (interseccion.height > interseccion.width &&
                        rectEnemigo.x + interseccion.width >= rectPlataforma.x + rectPlataforma.width) {
                    x = rectPlataforma.x + rectPlataforma.width;
                }
            }

        }
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, ancho, alto);
    }
}
