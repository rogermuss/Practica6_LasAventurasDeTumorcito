package Practica_6;

import java.awt.*;

public class EnemigoVolador extends Enemigo {
    private int velocidad = 2;  
    private int limiteIzquierdo;  // Límite movimiento
    private int limiteDerecho;    // Límite movimiento
    private boolean moverHorizontal = false;  

    public EnemigoVolador(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    // Constructor con límites para el movimiento
    public EnemigoVolador(int x, int y, int ancho, int alto, int limiteIzquierdo, int limiteDerecho, boolean moverHorizontal) {
        super(x, y, ancho, alto);
        this.limiteIzquierdo = limiteIzquierdo;
        this.limiteDerecho = limiteDerecho;
        this.moverHorizontal = true;  
    }

    // Método para actualizar la posición del enemigo
    public void actualizar() {
        if (moverHorizontal) {
            x += velocidad;

            // Cambiar dirección si alcanza los límites
            if (x <= limiteIzquierdo || x + ancho >= limiteDerecho) {
                velocidad = -velocidad;
            }
        }
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillOval(x, y, ancho, alto);
    }
}
