package Practica_6;

import java.awt.*;

public class EnemigoVolador extends Enemigo {
    private int velocidad = 2;
    private int limiteIzquierdo;  // Límite horizontal izquierdo
    private int limiteDerecho;    // Límite horizontal derecho
    private int limiteSuperior;   // Límite vertical superior
    private int limiteInferior;   // Límite vertical inferior

    private boolean moverHorizontal = false;  // Movimiento horizontal
    private boolean moverVertical = false;    // Movimiento vertical

    // Constructor básico
    public EnemigoVolador(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    // Constructor para movimiento horizontal
    public EnemigoVolador(int x, int y, int ancho, int alto, int limiteIzquierdo, int limiteDerecho) {
        super(x, y, ancho, alto);
        this.limiteIzquierdo = limiteIzquierdo;
        this.limiteDerecho = limiteDerecho;
        this.moverHorizontal = true;
    }

    // Constructor para movimiento vertical
    public EnemigoVolador(int x, int y, int ancho, int alto, int limiteSuperior, int limiteInferior, boolean vertical) {
        super(x, y, ancho, alto);
        this.limiteSuperior = limiteSuperior;
        this.limiteInferior = limiteInferior;
        this.moverVertical = true;
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
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillOval(x, y, ancho, alto);
    }
}
