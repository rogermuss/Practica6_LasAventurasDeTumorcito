package Practica_6;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Plataforma extends Entidad {
    boolean puedeEscalarse;
    private BufferedImage spriteActual;
    private boolean intentoCargaImagen = false;

    public Plataforma(int x, int y, int ancho, int alto, boolean z) {
        super(x, y, ancho, alto, z);
        cargarImagen();
    }

    private void cargarImagen() {
        try {
            File archivo = null;

            // Selección de imagen basada en las dimensiones
            if (alto >= 100) {
                archivo = new File("textures/muroLimite.png");
            } else if (alto == 120) {
                archivo = new File("textures/murolimite.png");
            } else if (ancho <= 50 && alto <= 40) {
                archivo = new File("textures/plataformaChica.png");
            }else if (ancho == 120) {
                archivo = new File("textures/plataformaMediana.png");
            }else if (ancho >= 300){
                archivo = new File("textures/plataformaLarga.png");
            }
            else {
                archivo = new File("textures/plataformaChica.png");
            }

            // Fallback si no se encuentra
            if (!archivo.exists()) {
                archivo = new File("C:\\Users\\dlara\\Desktop\\projects\\chapter01\\Juego 2Dv2\\textures\\muroLimite.png");
                if (!archivo.exists()) archivo = new File("Plataforma.png");
                if (!archivo.exists()) archivo = new File("src/Practica_6/Plataforma.png");
            }

            if (archivo.exists()) {
                spriteActual = ImageIO.read(archivo);
                System.out.println("Imagen de plataforma cargada desde: " + archivo.getAbsolutePath());
            } else {
                System.out.println("No se pudo encontrar la imagen de plataforma en ninguna ubicación");
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen de plataforma: " + e.getMessage());
            e.printStackTrace();
        }

        intentoCargaImagen = true;
    }

    public void dibujar(Graphics g) {
        // Si es la primera vez y no se ha intentado cargar la imagen, intentarlo
        if (!intentoCargaImagen) {
            cargarImagen();
        }

        if (spriteActual != null) {
            g.drawImage(spriteActual, x, y, ancho, alto, null);
        } else {
            // Si no hay sprite, dibujamos un rectángulo gris oscuro
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x, y, ancho, alto);
        }
    }

    public void setPuedeEscalarse(boolean puedeEscalarse) {
        this.puedeEscalarse = puedeEscalarse;
    }

    public boolean getPuedeEscalarse(){
        return puedeEscalarse;
    }
}
