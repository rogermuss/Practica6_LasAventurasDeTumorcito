package Practica_6;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Clase para el objeto final del juego (cuadrado amarillo)
public class ObjetivoFinal extends Entidad {

    private boolean alcanzado = false;
    private BufferedImage spriteActual;
    private boolean intentoCargaImagen = false;

    public ObjetivoFinal(int x, int y, int ancho, int alto) {
        super(x, y, ancho, alto, false);
    }


    public boolean isAlcanzado() {
        return alcanzado;
    }

    public void setAlcanzado(boolean alcanzado) {
        this.alcanzado = alcanzado;
    }

    private void cargarImagen() {
        try {
            File archivo = new File("textures/salida.png"); // Ruta a tu imagen

            if (!archivo.exists()) {
                archivo = new File("textures/salida.png");
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
            g.fillOval(x, y, ancho, alto);
        }
    }
}
