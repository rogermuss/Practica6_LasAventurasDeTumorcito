package Practica_6;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class EnemigoVolador extends Enemigo {
    private int velocidad = 2;
    private int limiteIzquierdo;  // Límite movimiento
    private int limiteDerecho;    // Límite movimiento
    private int limiteSuperior;
    private int limiteInferior;
    private boolean moverHorizontal = false;
    private boolean moverVertical = false;

    private BufferedImage spriteActual;
    private boolean intentoCargaImagen = false;

    public EnemigoVolador(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    // Constructor con límites para el movimiento
    public EnemigoVolador(int x, int y, int ancho, int alto, int limiteIzquierdo, int limiteDerecho) {
        super(x, y, ancho, alto);
        this.limiteIzquierdo = limiteIzquierdo;
        this.limiteDerecho = limiteDerecho;
        this.moverHorizontal = true;
    }

    // Constructor con límites para el movimiento
    public EnemigoVolador(int x, int y, int ancho, int alto, int limiteSuperior, int limiteInferior, boolean moverVertical) {
        super(x, y, ancho, alto);
        this.limiteSuperior = limiteSuperior;
        this.limiteInferior = limiteInferior;
        this.moverVertical = moverVertical;
    }

    // Método para actualizar la posición del enemigo
    public void actualizar() {
        if (moverHorizontal) {
            x += velocidad;
            if (x <= limiteIzquierdo || x + ancho >= limiteDerecho) {
                velocidad = -velocidad;
            }
        }
        if (moverVertical) {
            y += velocidad;
            if (y <= limiteSuperior || y + alto >= limiteInferior) {
                velocidad = -velocidad;
            }
        }
    }

    private void cargarImagen() {
        try {
            File archivo = new File("textures/volador.png"); // Ruta a tu imagen

            if (!archivo.exists()) {
                archivo = new File("textures/volador.png");
            }

            if (archivo.exists()) {
                spriteActual = ImageIO.read(archivo);
                System.out.println("Imagen del enemigo cargada desde: " + archivo.getAbsolutePath());
            } else {
                System.out.println("No se pudo encontrar la imagen del enemigo en ninguna ubicación");
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen del enemigo: " + e.getMessage());
            e.printStackTrace();
        }

        intentoCargaImagen = true;
    }

    @Override
    public void dibujar(Graphics g) {
        if (!intentoCargaImagen) {
            cargarImagen();
        }

        if (spriteActual != null) {
            g.drawImage(spriteActual, x, y, ancho, alto, null);
        } else {
            g.setColor(java.awt.Color.RED);
            g.fillOval(x, y, ancho, alto); // Fallback si no se carga la imagen
        }
    }
}
