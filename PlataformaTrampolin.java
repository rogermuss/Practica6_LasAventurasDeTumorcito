package Practica_6;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PlataformaTrampolin extends Entidad {

    private BufferedImage spriteActual;
    private boolean intentoCargaImagen = false;

    public PlataformaTrampolin(int x, int y, int ancho, int alto, boolean z) {
        super(x, y, ancho, alto, z);
        //TODO Auto-generated constructor stub
    }

    private void cargarImagen() {
        try {
            // Intentar cargar desde la ruta absoluta primero
            File archivo = new File("C:\\Users\\yojua\\OneDrive\\Escritorio\\Practica6_LasAventurasDeTumorcito-master\\Plataforma.png");

            if (!archivo.exists()) {
                // Si no existe en la ruta absoluta, intentar rutas relativas
                archivo = new File("log.jpg");

                if (!archivo.exists()) {
                    archivo = new File("src/Practica_6/Plataforma.png");
                }
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

    @Override
    public void dibujar(Graphics g) {
        g.setColor(Color.orange);
        g.fillRect(x, y, ancho, alto);
    }


}