package Practica_6;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EnemigoEstaticoTriangulo extends Enemigo {

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;
    public static final int LEFT = 4;
    private int direccion;
    private BufferedImage sprite;
    private boolean intentoCargaImagen = false;

    public EnemigoEstaticoTriangulo(int x, int y, int ancho, int alto, int direccion) {
        super(x, y, ancho, alto);
        this.direccion = direccion;
        cargarImagen();
    }

    private void cargarImagen() {
        try {
            File archivo = new File("textures/picos.png");

            if (!archivo.exists()) {
                archivo = new File("textures/picos.png");
            }

            if (archivo.exists()) {
                sprite = ImageIO.read(archivo);
                System.out.println("Imagen de picos cargada desde: " + archivo.getAbsolutePath());
            } else {
                System.out.println("No se encontró la imagen de picos.");
            }
        } catch (IOException e) {
            System.out.println("Error al cargar imagen: " + e.getMessage());
        }

        intentoCargaImagen = true;
    }

    @Override
    public void dibujar(Graphics g) {
        if (!intentoCargaImagen) {
            cargarImagen();
        }

        if (sprite != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            AffineTransform transform = new AffineTransform();

            // Centra la imagen en x, y
            transform.translate(x + ancho / 2.0, y + alto / 2.0);

            // Aplica rotación según dirección
            switch (direccion) {
                case UP:
                    transform.rotate(Math.toRadians(0));
                    break;
                case DOWN:
                    transform.rotate(Math.toRadians(180));
                    break;
                case LEFT:
                    transform.rotate(Math.toRadians(270));
                    break;
                case RIGHT:
                    transform.rotate(Math.toRadians(90));
                    break;
            }

            // Ajusta escala y ubicación de la imagen
            transform.translate(-ancho / 2.0, -alto / 2.0);
            transform.scale(ancho / (double) sprite.getWidth(), alto / (double) sprite.getHeight());

            g2d.drawImage(sprite, transform, null);
            g2d.dispose();
        } else {
            // Fallback si no hay imagen: dibuja triángulo
            g.setColor(Color.RED);
            if (direccion == UP) {
                int[] xPoints = {x, x + ancho / 2, x + ancho};
                int[] yPoints = {y + alto, y, y + alto};
                g.fillPolygon(xPoints, yPoints, 3);
            } else if (direccion == LEFT) {
                int[] xPoints = {x + ancho, x, x + ancho};
                int[] yPoints = {y, y + alto / 2, y + alto};
                g.fillPolygon(xPoints, yPoints, 3);
            } else if (direccion == RIGHT) {
                int[] xPoints = {x, x + ancho, x};
                int[] yPoints = {y, y + alto / 2, y + alto};
                g.fillPolygon(xPoints, yPoints, 3);
            } else if (direccion == DOWN) {
                int[] xPoints = {x, x + ancho / 2, x + ancho};
                int[] yPoints = {y, y + alto, y};
                g.fillPolygon(xPoints, yPoints, 3);
            }
        }
    }
}
