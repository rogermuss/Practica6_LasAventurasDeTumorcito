package Practica_6;

import java.awt.*;

public class EnemigoVolador extends Enemigo {
    private int velocidad = 2;

    // Movimiento horizontal
    private int limiteIzquierdo;
    private int limiteDerecho;
    private boolean moverHorizontal = false;

    // Movimiento vertical
    private boolean movimientoVertical = false;
    private int limiteSuperior;
    private int limiteInferior;

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
    public EnemigoVolador(int x, int y, int ancho, int alto, int limiteSuperior, int limiteInferior, boolean movimientoVertical) {
        super(x, y, ancho, alto);
        this.limiteSuperior = limiteSuperior;
        this.limiteInferior = limiteInferior;
        this.movimientoVertical = movimientoVertical;
    }

    public void actualizar() {
        if (movimientoVertical) {
            y += velocidad;
            if (y <= limiteSuperior || y + alto >= limiteInferior) {
                velocidad = -velocidad;
            }
        } else if (moverHorizontal) {
            x += velocidad;
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
