package Practica_6;

import java.awt.*;

public class PlataformaMovil extends Plataforma {
    private int limiteIzquierdo;
    private int limiteDerecho;
    private int velocidad;
    private boolean movimientoDerecha;
    private boolean esMovimientoVertical;
    private int xAnterior;
    private int yAnterior;

    // Constructor para plataforma móvil horizontal
    public PlataformaMovil(int x, int y, int ancho, int alto,
                           int limiteIzquierdo, int limiteDerecho, int velocidad) {
        super(x, y, ancho, alto, false);
        this.limiteIzquierdo = limiteIzquierdo;
        this.limiteDerecho = limiteDerecho;
        this.velocidad = velocidad;
        this.movimientoDerecha = true;
        this.esMovimientoVertical = false;
    }

    // Constructor para plataforma móvil con opción de movimiento vertical
    public PlataformaMovil(int x, int y, int ancho, int alto,
                           int limite1, int limite2, int velocidad, boolean esVertical) {
        super(x, y, ancho, alto, false);
        this.limiteIzquierdo = limite1;
        this.limiteDerecho = limite2;
        this.velocidad = velocidad;
        this.movimientoDerecha = true;
        this.esMovimientoVertical = esVertical;
    }

    public void actualizar() {
        // Guardar posición anterior
        xAnterior = x;
        yAnterior = y;

        if (esMovimientoVertical) {
            // Movimiento vertical
            if (movimientoDerecha) { // Movimiento hacia abajo
                y += velocidad;

                // Si alcanza el límite inferior, cambia de dirección
                if (y >= limiteDerecho - alto) {
                    movimientoDerecha = false;
                }
            } else { // Movimiento hacia arriba
                y -= velocidad;

                // Si alcanza el límite superior, cambia de dirección
                if (y <= limiteIzquierdo) {
                    movimientoDerecha = true;
                }
            }
        } else {
            // Movimiento horizontal
            if (movimientoDerecha) {
                x += velocidad;

                // Si alcanza el límite derecho, cambia de dirección
                if (x >= limiteDerecho - ancho) {
                    movimientoDerecha = false;
                }
            } else {
                x -= velocidad;

                // Si alcanza el límite izquierdo, cambia de dirección
                if (x <= limiteIzquierdo) {
                    movimientoDerecha = true;
                }
            }
        }
    }

    public int getDeltaX() {
        return x - xAnterior;
    }

    public int getDeltaY() {
        return y - yAnterior;
    }

    public boolean esMovimientoVertical() {
        return esMovimientoVertical;
    }
}
//para el commit