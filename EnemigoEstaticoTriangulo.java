package Practica_6;

import java.awt.*;

// Clase para enemigos estáticos en forma de pico
public class EnemigoEstaticoTriangulo extends Enemigo {

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;
    public static final int LEFT = 4;
    private int direccion;

    public EnemigoEstaticoTriangulo(int x, int y, int ancho, int alto, int direccion) {
        super(x, y, ancho, alto);
        this.direccion = direccion;
    }

    @Override
    public void dibujar(Graphics g) {
        
        g.setColor(Color.RED);
        if(direccion == UP){
        // Dibujamos picos rojos triangulares
            // Crear un triángulo
            int[] xPoints = {x, x + ancho/2, x + ancho};
            int[] yPoints = {y + alto, y, y + alto};
            g.fillPolygon(xPoints, yPoints, 3);

        }
        else if(direccion == LEFT){

            // Crear triángulo con la punta hacia la izquierda
            int[] xPoints = {x + ancho, x, x + ancho};
            int[] yPoints = {y, y + alto / 2, y + alto};
            g.fillPolygon(xPoints, yPoints, 3);
        }
        else if(direccion == RIGHT){
            int[] xPoints = {x, x + ancho, x};
            int[] yPoints = {y, y + alto / 2, y + alto};
            g.fillPolygon(xPoints, yPoints, 3);

        }
        else if(direccion == DOWN){
            int[] xPoints = {x, x + ancho / 2, x + ancho};
            int[] yPoints = {y, y + alto, y};
            g.fillPolygon(xPoints, yPoints, 3);


        }

    }
}
